package com.bardackx.jhpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.bardackx.jhpp.JhppException.Code;

public class Jhpp {

	private Map<String, Map<String, Object>> maps;

	/**
	 * This method parses the given properties object into an object of the
	 * specified type.
	 * 
	 * @param   <E> the type of the desired object
	 * @param p the parsed properties
	 * @param t the class of E
	 * @return the parsed oject
	 */
	public <E> E fromProperties(Properties p, Class<E> t) {
		try {
			maps = new HashMap<>();
			E e = t.newInstance();
			for (Entry<Object, Object> entry : p.entrySet())
				handleProperty(e, (String) entry.getKey(), (String) entry.getKey(), (String) entry.getValue());
			for (Entry<String, Map<String, Object>> entry : maps.entrySet())
				handleProperty(e, entry.getKey(), entry.getKey(), entry.getValue());
			return e;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
			e1.printStackTrace();
			return null;
		} catch (InstantiationException e1) {
			throw new JhppException(Code.DEFAULT_CONSTRUCTOR_IS_MISSING,
					"There's no default constructor in " + t.getCanonicalName());
		} finally {
			maps = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E> void handleProperty(E entity, String fullKey, String key, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		int dotIndex = key.indexOf(".");
		if (dotIndex == -1) {

			Method getter = getPropertyGetterMethod(entity.getClass(), key);
			if (getter == null)
				throw new JhppException(JhppException.Code.GETTER_IS_MISSING, "There is no getter method for the '"
						+ key + "' property in the '" + entity.getClass() + "' class");

			Method setter = getPropertySetterMethod(entity.getClass(), key, getter.getReturnType());
			if (setter == null)
				throw new JhppException(JhppException.Code.SETTER_IS_MISSING, "There is no setter method for the '"
						+ key + "' property in the '" + entity.getClass() + "' class");

			if (value instanceof String) {
				Class<?> getterType = getter.getReturnType();
				Class<?> elementType = null;
				if (isCollectionType(getterType)) elementType = getCollectionComponentType(setter);
				setter.invoke(entity, deserialize(getterType, (String) value, elementType));
			} else {

				if (isCollectionType(getter.getReturnType()) && value instanceof Map) {

					Class<?> ct = getter.getReturnType();
					Class<?> componentType = getCollectionComponentType(setter);
					if (ct.isArray()) setter.invoke(entity, mapToObjectArray(componentType, (Map) value));
					else if (ct == List.class) setter.invoke(entity, mapToObjectList(componentType, (Map) value));
					else if (ct == Map.class) setter.invoke(entity, value);
					else if (ct == Set.class) setter.invoke(entity, mapToObjectSet(componentType, (Map) value));
					else throw new JhppException(Code.UNEXPECTED,
							"Unhandled collection type " + getter.getReturnType());

				} else {

					setter.invoke(entity, value);
				}

			}

		} else {

			String head = key.substring(0, dotIndex);
			String tail = key.substring(dotIndex + 1);

			Method getter = getPropertyGetterMethod(entity.getClass(), head);
			if (getter == null)
				throw new JhppException(JhppException.Code.GETTER_IS_MISSING, "There is no getter method for the '"
						+ key + "' property in the '" + entity.getClass() + "' class");

			Object headEntity = getter.invoke(entity);
			if (headEntity == null) {

				Method setter = getPropertySetterMethod(entity.getClass(), head, getter.getReturnType());
				if (setter == null)
					throw new JhppException(JhppException.Code.SETTER_IS_MISSING, "There is no setter method for the '"
							+ key + "' property in the '" + entity.getClass() + "' class");

				if (isCollectionType(getter.getReturnType())) {

					int j = tail.indexOf(".");
					if (j == -1) throw new JhppException(JhppException.Code.ELEMENT_KEY_IS_MISSING,
							"A key was expected for the element of the '" + fullKey + "' array");

					String mapKey = fullKey.substring(0, fullKey.length() - tail.length() - 1);
					String elementKey = tail.substring(0, j);
					String valueKey = tail.substring(j + 1);

					Map<String, Object> map = getTemporalMap(mapKey);

					Object element = null;
					if (map.containsKey(elementKey)) {
						element = map.get(elementKey);
					} else {
						try {
							element = getCollectionComponentType(setter).newInstance();
						} catch (InstantiationException e) {
							throw new JhppException(Code.DEFAULT_CONSTRUCTOR_IS_MISSING,
									"There's no default constructor in "
											+ getter.getReturnType().getComponentType().getCanonicalName());
						}
						map.put(elementKey, element);
					}

					handleProperty(element, fullKey, valueKey, value);

					return;

				} else {
					try {
						headEntity = getter.getReturnType().newInstance();
						setter.invoke(entity, headEntity);
					} catch (InstantiationException e) {
						throw new JhppException(Code.DEFAULT_CONSTRUCTOR_IS_MISSING,
								"There's no default constructor in " + getter.getReturnType().getCanonicalName());
					}
				}
			}

			handleProperty(headEntity, fullKey, tail, value);
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List mapToObjectList(Class<?> componentType, Map map) {
		Object array = mapToObjectArray(componentType, map);
		List list = new ArrayList<>();
		int length = Array.getLength(array);
		for (int i = 0; i < length; i++)
			list.add(Array.get(array, i));
		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Set mapToObjectSet(Class<?> componentType, Map map) {
		Object array = mapToObjectArray(componentType, map);
		Set list = new HashSet<>();
		int length = Array.getLength(array);
		for (int i = 0; i < length; i++)
			list.add(Array.get(array, i));
		return list;
	}

	private boolean isCollectionType(Class<?> type) {
		if (type.isArray()) return true;
		if (type == List.class) return true;
		if (type == Set.class) return true;
		if (type == Map.class) return true;
		return false;
	}

	private Class<?> getCollectionComponentType(Method setterMethod) {

		try {

			Class<?>[] parameterTypes = setterMethod.getParameterTypes();
			Class<?> parameterType = parameterTypes[0];

			if (parameterType.isArray()) return parameterType.getComponentType();

			Type[] genericParameterTypes = setterMethod.getGenericParameterTypes();
			Type genericParameterType = genericParameterTypes[0];

			ParameterizedType parameterizedType = (ParameterizedType) genericParameterType;
			Type[] typeArguments = parameterizedType.getActualTypeArguments();

			if (parameterType == List.class || parameterType == Set.class) {
				return Class.forName(typeArguments[0].getTypeName());
			} else if (parameterType == Map.class) {
				return Class.forName(typeArguments[1].getTypeName());
			} else {
				throw new JhppException(Code.UNEXPECTED, "Unsupported collection type " + parameterType);
			}

		} catch (SecurityException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		// System.out.println(type);
		// System.out.println(type.getGenericInterfaces());
		// System.out.println(type.getGenericInterfaces().length);
		// System.out.println(type.getGenericSuperclass());

		return null;
	}

	private Map<String, Object> getTemporalMap(String key) {
		if (maps.containsKey(key)) return maps.get(key);
		else {
			Map<String, Object> map = new HashMap<String, Object>();
			maps.put(key, map);
			return map;
		}
	}

	@SuppressWarnings("unchecked")
	private <E> E mapToObjectArray(Class<E> componentType, Map<String, E> map) {

		List<String> orderedKeys = new ArrayList<String>();
		orderedKeys.addAll(map.keySet());
		Collections.sort(orderedKeys);

		int length = map.size();
		Object array = Array.newInstance(componentType, length);
		for (int i = 0; i < length; i++)
			Array.set(array, i, map.get(orderedKeys.get(i)));

		return (E) array;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E> E deserialize(Class<E> type, String s, Class elementType) {

		if (type == byte.class) return (E) (Byte) Byte.parseByte(s);
		else if (type == short.class) return (E) (Short) Short.parseShort(s);
		else if (type == int.class) return (E) (Integer) Integer.parseInt(s);
		else if (type == long.class) return (E) (Long) Long.parseLong(s);
		else if (type == boolean.class) return (E) (Boolean) Boolean.parseBoolean(s);
		else if (type == float.class) return (E) (Float) Float.parseFloat(s);
		else if (type == double.class) return (E) (Double) Double.parseDouble(s);
		else if (type == String.class) return (E) s;
		else if (type.isArray()) return (E) deserializeCsvArray(type, s, elementType);
		else if (type == Set.class) {
			Set set = new HashSet<>();
			Object array = deserializeCsvArray(type, s, elementType);
			int length = Array.getLength(array);
			for (int i = 0; i < length; i++)
				set.add(Array.get(array, i));
			return (E) set;
		}
		;

		throw new JhppException(Code.UNEXPECTED, "Deserialization not supported for '" + type + "' type");
	}

	private Object deserializeCsvArray(Class<?> type, String serialized, Class<?> elementsType) {

		String[] ss = csvRowSplit(serialized);
		Object d = Array.newInstance(elementsType, ss.length);

		if (elementsType == byte.class) for (int i = 0; i < ss.length; i++)
			Array.setByte(d, i, (Byte) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == short.class) for (int i = 0; i < ss.length; i++)
			Array.setShort(d, i, (Short) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == int.class) for (int i = 0; i < ss.length; i++)
			Array.setInt(d, i, (Integer) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == long.class) for (int i = 0; i < ss.length; i++)
			Array.setLong(d, i, (Long) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == boolean.class) for (int i = 0; i < ss.length; i++)
			Array.setBoolean(d, i, (Boolean) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == float.class) for (int i = 0; i < ss.length; i++)
			Array.setFloat(d, i, (Float) deserialize(elementsType, ss[i], elementsType));
		else if (elementsType == double.class) for (int i = 0; i < ss.length; i++)
			Array.setDouble(d, i, (Double) deserialize(elementsType, ss[i], elementsType));
		else for (int i = 0; i < ss.length; i++)
			Array.set(d, i, deserialize(elementsType, ss[i], elementsType));

		return d;
	}

	private static String[] csvRowSplit(String row) {

		List<String> list = new ArrayList<>();

		final int SIMPLE = 1;
		final int QUOTES = 2;

		int mode = SIMPLE;

		StringBuilder builder = new StringBuilder();

		for (int i = 0; i < row.length(); i++) {

			Character c = row.charAt(i);
			boolean isLastCharacter = i == row.length() - 1;
			Character d = !isLastCharacter ? row.charAt(i + 1) : null;

			if (mode == SIMPLE) {
				if (c == ',') {
					list.add(builder.toString().trim());
					builder.setLength(0);
				} else if (c == '"') {
					mode = QUOTES;
				} else {
					builder.append(c);
				}
			} else {
				if (c == '"' && d == ',') {
					list.add(builder.toString().trim());
					builder.setLength(0);
					mode = SIMPLE;
					i++; // skip comma
				} else {
					builder.append(c);
				}
			}

			if (isLastCharacter) list.add(builder.toString().trim());

		}

		return list.toArray(new String[list.size()]);
	}

	private Method getPropertySetterMethod(Class<?> type, String property, Class<?> parameterType) {
		String p = property.substring(0, 1).toUpperCase() + property.substring(1);
		return getMethod(type, "set" + p, parameterType);
	}

	private Method getPropertyGetterMethod(Class<?> type, String property) {
		String p = property.substring(0, 1).toUpperCase() + property.substring(1);
		Method getter = getMethod(type, "get" + p);
		if (getter != null) return getter;
		return getMethod(type, "is" + p);
	}

	private Method getMethod(Class<?> type, String methodName, Class<?>... parameterTypes) {
		try {
			return type.getMethod(methodName, parameterTypes);
		} catch (NoSuchMethodException | SecurityException e) {
			return null;
		}
	}

	/**
	 * This is equivalent to creating a properties object and load the given input
	 * stream before using the properties as a parameter for the
	 * <code>fromProperties(Properties, Class)</code> method.
	 * 
	 * <pre>
	 * Properties p = new Properties();
	 * p.load(is);
	 * new Jhpp().fromProperties(p);
	 * </pre>
	 * 
	 * @param    <E> the type of the desired object
	 * @param is the input stream
	 * @param t  the class of E
	 * @return the parsed object
	 */
	public <E> E fromProperties(InputStream is, Class<E> t) {
		try {
			Properties p = new Properties();
			p.load(is);
			return fromProperties(p, t);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is equivalent to creating a properties object and load the given input
	 * stream before using the properties as a parameter for the
	 * <code>fromProperties(Properties, Class)</code> method.
	 * 
	 * <pre>
	 * Properties p = new Properties();
	 * p.load(r);
	 * new Jhpp().fromProperties(p);
	 * </pre>
	 * 
	 * @param   <E> the type of the desired object
	 * @param r the reader
	 * @param t the class of E
	 * @return the parsed object
	 */
	public <E> E fromProperties(Reader r, Class<E> t) {
		try {
			Properties p = new Properties();
			p.load(r);
			return fromProperties(p, t);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Writes the source object values as entries of a properties object
	 * 
	 * @param e source object
	 * @return the properties object
	 */
	public Properties toProperties(Object e) {
		try {
			Properties p = new Properties();
			serializeIntoProperty(p, "", e);
			return p;
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			throw new JhppException(ex);
		}
	}

	/**
	 * Write each propertie as a line in a string and sort the fields in
	 * alphabetical order
	 * 
	 * @param e source object
	 * @return properties file as string
	 */
	public String toPropertiesString(Object e) {

		Properties p = toProperties(e);
		StringBuilder b = new StringBuilder();

		List<String> immediateKeys = new ArrayList<>();
		List<String> nestedKeys = new ArrayList<>();

		for (Object keyObject : p.keySet()) {
			String key = (String) keyObject;
			if (key.contains(".")) nestedKeys.add(key);
			else immediateKeys.add(key);
		}

		Collections.sort(immediateKeys);
		Collections.sort(nestedKeys);

		for (Object k : immediateKeys) {
			b.append(k);
			b.append(" : ");
			b.append(p.get(k));
			b.append("\r\n");
		}
		for (Object k : nestedKeys) {
			b.append(k);
			b.append(" : ");
			b.append(p.get(k));
			b.append("\r\n");
		}

		return b.toString();
	}

	public void toProperties(Object e, OutputStream os) throws IOException {
		toProperties(e, os, Charset.defaultCharset());
	}

	public void toProperties(Object e, OutputStream os, Charset charset) throws IOException {
		os.write(toPropertiesString(e).getBytes(charset));
	}

	private void serializeIntoProperty(Properties p, String keyPrefix, Object e)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		// System.out.println(keyPrefix + " <- " + e);

		for (Method method : e.getClass().getMethods()) {

			if (method.getParameterCount() != 0) continue;

			String methodName = method.getName();

			String propertyName = null;
			if (methodName.startsWith("get")) propertyName = methodName.substring(3);
			else if (methodName.startsWith("is")) propertyName = methodName.substring(2);
			else continue;

			if ("Class".equals(propertyName)) continue;

			String entryKey = keyPrefix + propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);
			Object entryValue = method.invoke(e);

			if (isSerializable(entryValue)) p.put(entryKey, serialize(entryValue));
			else if (isCollection(entryValue)) {

				Class<?> collectionType = entryValue.getClass();
				if (collectionType.isArray()) {
					int length = Array.getLength(entryValue);
					for (int index = 0; index < length; index++) {
						Object element = Array.get(entryValue, index);
						serializeIntoProperty(p, entryKey + "." + index + ".", element);
					}
				} else {
					throw new JhppException(Code.UNEXPECTED, "Unsupported collection type '" + collectionType + "'.");
				}

			} else serializeIntoProperty(p, entryKey + ".", entryValue);
		}
	}

	private boolean isCollection(Object entryValue) {
		if (entryValue == null) return true;
		Class<?> type = entryValue.getClass();
		return isCollectionType(type);
	}

	private static final Map<Class<?>, Serializer> serializers = new HashMap<>();

	static {

		serializers.put(int.class, e -> Integer.toString((Integer) e));
		serializers.put(char.class, e -> Character.toString((Character) e));
		serializers.put(byte.class, e -> Byte.toString((Byte) e));
		serializers.put(long.class, e -> Long.toString((Long) e));
		serializers.put(short.class, e -> Short.toString((Short) e));
		serializers.put(float.class, e -> Float.toString((Float) e));
		serializers.put(double.class, e -> Double.toString((Double) e));
		serializers.put(boolean.class, e -> Boolean.toString((Boolean) e));

		serializers.put(Integer.class, e -> Integer.toString((Integer) e));
		serializers.put(Character.class, e -> Character.toString((Character) e));
		serializers.put(Byte.class, e -> Byte.toString((Byte) e));
		serializers.put(Long.class, e -> Long.toString((Long) e));
		serializers.put(Short.class, e -> Short.toString((Short) e));
		serializers.put(Float.class, e -> Float.toString((Float) e));
		serializers.put(Double.class, e -> Double.toString((Double) e));
		serializers.put(Boolean.class, e -> Boolean.toString((Boolean) e));

		serializers.put(String.class, e -> "" + e);
	}

	private boolean isSerializable(Object entryValue) {
		if (entryValue == null) return true;
		Class<?> type = entryValue.getClass();
		if (type.isArray()) return serializers.containsKey(type.getComponentType());
		return serializers.containsKey(type);
	}

	private Object serialize(Object entryValue) {
		if (entryValue == null) return "null";
		Class<?> type = entryValue.getClass();
		if (type.isArray()) {
			CsvRow row = new CsvRow();
			int length = Array.getLength(entryValue);
			for (int i = 0; i < length; i++)
				row.add(serialize(Array.get(entryValue, i)));
			return row.toString();
		}
		return serializers.get(type).serialize(entryValue);
	}

}
