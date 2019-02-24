package com.bardackx.jhpp;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

public class Jhpp {

	private Map<String, Map<String, Object>> maps;

	public <E> E fromProperties(Properties p, Class<E> t) {
		try {
			maps = new HashMap<>();
			E e = t.newInstance();
			for (Entry<Object, Object> entry : p.entrySet())
				handleProperty(e, (String) entry.getKey(), (String) entry.getKey(), (String) entry.getValue());
			for (Entry<String, Map<String, Object>> entry : maps.entrySet())
				handleProperty(e, entry.getKey(), entry.getKey(), entry.getValue());
			return e;
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e1) {
			e1.printStackTrace();
			return null;
		} finally {
			maps = null;
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private <E> void handleProperty(E entity, String fullKey, String key, Object value)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

		int dotIndex = key.indexOf(".");
		if (dotIndex == -1) {

			System.out.println(" simple " + key);

			Method getter = getPropertyGetterMethod(entity.getClass(), key);
			if (getter == null) {
				System.out.println("no salió el getter");
				return; // deberíamos decir algo?
			}

			Method setter = getPropertySetterMethod(entity.getClass(), key, getter.getReturnType());
			if (setter == null) {
				System.out.println("no salió el setter");
				return; // deberíamos decir algo?
			}

			if (value instanceof String) setter.invoke(entity, deserialize(getter.getReturnType(), (String) value));
			else {
				if (getter.getReturnType().isArray() && value instanceof Map) {
					setter.invoke(entity, mapToObjectArray(getter.getReturnType().getComponentType(), (Map) value));
				} else {
					setter.invoke(entity, value);
				}
			}

		} else {
			System.out.println("complex " + key);

			String head = key.substring(0, dotIndex);
			String tail = key.substring(dotIndex + 1);
			System.out.println("        " + tail + " from " + head);

			Method getter = getPropertyGetterMethod(entity.getClass(), head);
			if (getter == null) {
				System.out.println("no salió el getter");
				return; // deberíamos decir algo?
			}

			Object headEntity = getter.invoke(entity);
			if (headEntity == null) {

				Method setter = getPropertySetterMethod(entity.getClass(), head, getter.getReturnType());
				if (setter == null) {
					System.out.println("no salió el setter");
					return; // deberíamos decir algo?
				}

				if (getter.getReturnType().isArray()) {

					int j = tail.indexOf(".");
					if (j == -1) {
						System.out.println("la entrada no tiene llave de elemento (o valor de elemento)");
						return;
					}

					String mapKey = fullKey.substring(0, fullKey.length() - tail.length() - 1);
					String elementKey = tail.substring(0, j);
					String valueKey = tail.substring(j + 1);

					Map<String, Object> map = getTemporalMap(mapKey);

					Object element = null;
					if (map.containsKey(elementKey)) {
						element = map.get(elementKey);
					} else {
						try {
							element = getter.getReturnType().getComponentType().newInstance();
						} catch (InstantiationException e) {
							System.out.println("no salió el constructor sin parametros del elemento");
							return;
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
						System.out.println("no salió el constructor sin parametros");
						return;
					}
				}
			}

			handleProperty(headEntity, fullKey, tail, value);
		}

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

	@SuppressWarnings("unchecked")
	private <E> E deserialize(Class<E> type, String s) {

		// System.out.println("DESERIALIZE " + s + " as " + type);

		if (type == byte.class) return (E) (Byte) Byte.parseByte(s);
		else if (type == short.class) return (E) (Short) Short.parseShort(s);
		else if (type == int.class) return (E) (Integer) Integer.parseInt(s);
		else if (type == long.class) return (E) (Long) Long.parseLong(s);
		else if (type == boolean.class) return (E) (Boolean) Boolean.parseBoolean(s);
		else if (type == float.class) return (E) (Float) Float.parseFloat(s);
		else if (type == double.class) return (E) (Double) Double.parseDouble(s);
		else if (type == String.class) return (E) s;
		else if (type.isArray()) {

			String[] ss = csvRowSplit(s);
			Class<?> elementsType = type.getComponentType();
			Object d = Array.newInstance(elementsType, ss.length);

			if (elementsType == byte.class) for (int i = 0; i < ss.length; i++)
				Array.setByte(d, i, (Byte) deserialize(elementsType, ss[i]));
			else if (elementsType == short.class) for (int i = 0; i < ss.length; i++)
				Array.setShort(d, i, (Short) deserialize(elementsType, ss[i]));
			else if (elementsType == int.class) for (int i = 0; i < ss.length; i++)
				Array.setInt(d, i, (Integer) deserialize(elementsType, ss[i]));
			else if (elementsType == long.class) for (int i = 0; i < ss.length; i++)
				Array.setLong(d, i, (Long) deserialize(elementsType, ss[i]));
			else if (elementsType == boolean.class) for (int i = 0; i < ss.length; i++)
				Array.setBoolean(d, i, (Boolean) deserialize(elementsType, ss[i]));
			else if (elementsType == float.class) for (int i = 0; i < ss.length; i++)
				Array.setFloat(d, i, (Float) deserialize(elementsType, ss[i]));
			else if (elementsType == double.class) for (int i = 0; i < ss.length; i++)
				Array.setDouble(d, i, (Double) deserialize(elementsType, ss[i]));
			else for (int i = 0; i < ss.length; i++)
				Array.set(d, i, deserialize(elementsType, ss[i]));

			return (E) d;
		}

		System.out.println("UNKNOWN DESERIALIZABLE TYPE " + type);

		return null;
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

	public <E> E fromProperties(InputStream is, Class<E> t) {
		try {
			Properties p = new Properties();
			p.load(is);
			return fromProperties(p, t);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public <E> E fromProperties(Reader r, Class<E> t) {
		try {
			Properties p = new Properties();
			p.load(r);
			return fromProperties(p, t);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
