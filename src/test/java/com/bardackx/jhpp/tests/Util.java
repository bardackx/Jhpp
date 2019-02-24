package com.bardackx.jhpp.tests;

import static org.junit.Assert.assertNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;

import org.junit.Assert;

public class Util {

	public static String asString(InputStream is) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int read;
			while ((read = is.read(buffer)) != -1)
				os.write(buffer, 0, read);
			return new String(buffer);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null) is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void assertEqualsVerbose(Object expected, Object actual) {

		Assert.assertNotNull("El actual no debe ser null", actual);

		fix(expected);
		fix(actual);

		ArrayList<Method> sortedMethods = new ArrayList<Method>();
		for (Method method : expected.getClass().getMethods())
			sortedMethods.add(method);
		Collections.sort(sortedMethods, (a, b) -> a.getName().compareTo(b.getName()));

		for (Method method : sortedMethods) {
			if (!method.getName().startsWith("get")) continue;
			if (method.getParameterCount() > 0) continue;
			try {
				Object expectedValue = method.invoke(expected);
				Object actualValue = method.invoke(actual);

				if (expectedValue == null && actualValue == null) continue;

				if (expectedValue == null) assertNull(method.getName().substring(3), actualValue);

				if (expectedValue instanceof BigDecimal) expectedValue = simplifyBigDecimal((BigDecimal) expectedValue);
				if (actualValue instanceof BigDecimal) actualValue = simplifyBigDecimal((BigDecimal) actualValue);

				if (expectedValue.getClass().isArray()) {
					Assert.assertEquals(expectedValue.getClass(), actualValue.getClass());
					int length = Array.getLength(expectedValue);
					for (int i = 0; i < length; i++) {
						Object expectedElement = Array.get(expectedValue, i);
						Object actualElement = Array.get(actualValue, i);
						assertEqualsVerbose(expectedElement, actualElement);
					}
				} else {
					Assert.assertEquals(method.getName().substring(3), expectedValue, actualValue);
				}

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
			}
		}

		Assert.assertEquals(expected, actual);
	}

	public static void fix(Object e) {

		Method[] methods = e.getClass().getMethods();

		for (Method method : methods) {

			if (method.getParameterCount() != 1) continue;
			if (method.getParameters()[0].getType() != BigDecimal.class) continue;
			if (!method.getName().startsWith("set")) continue;

			String VARNAME = method.getName().substring(3);

			Method setter = method;
			Method getter = null;

			try {
				getter = e.getClass().getMethod("get" + VARNAME);
			} catch (NoSuchMethodException ex) {
				continue;
			} catch (SecurityException ex) {
				ex.printStackTrace();
			}

			try {

				BigDecimal current = (BigDecimal) getter.invoke(e);

				if (current == null) continue;

				BigDecimal fixed = fixBigDecimal(current);

				setter.invoke(e, fixed);

			} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
				e1.printStackTrace();
			}

		}

	}

	public static BigDecimal fixBigDecimal(BigDecimal current) {
		int precision = current.precision();
		int scale = current.scale();
		int integerDigits = precision - scale;
		MathContext mc = new MathContext(integerDigits + 10, RoundingMode.HALF_UP);
		return new BigDecimal(current.round(mc).stripTrailingZeros().toPlainString());
	}

	public static BigDecimal simplifyBigDecimal(BigDecimal e) {
		return e.stripTrailingZeros();
	}
}
