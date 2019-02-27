package com.bardackx.jhpp.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.junit.Test;

import com.bardackx.jhpp.Jhpp;
import com.bardackx.jhpp.JhppException;
import com.bardackx.jhpp.tests.abpe.AdamBertramPersonalExample;
import com.bardackx.jhpp.tests.abpe.Dog;
import com.bardackx.jhpp.tests.abpe.Spouse;
import com.bardackx.jhpp.tests.collections.ExchangeRates;
import com.bardackx.jhpp.tests.collections.ListSetMapArray;
import com.bardackx.jhpp.tests.collections.Measure;
import com.bardackx.jhpp.tests.collections.TodoList;
import com.bardackx.jhpp.tests.collections.TodoListItem;
import com.bardackx.jhpp.tests.exceptions.NoDefaultConstructor;
import com.bardackx.jhpp.tests.exceptions.NoDefaultConstructorWrapper;
import com.bardackx.jhpp.tests.exceptions.NoGetter;
import com.bardackx.jhpp.tests.exceptions.NoGetterSetterWrapper;
import com.bardackx.jhpp.tests.exceptions.NoSetter;
import com.bardackx.jhpp.tests.glossary.GlossDef;
import com.bardackx.jhpp.tests.glossary.GlossDiv;
import com.bardackx.jhpp.tests.glossary.GlossEntry;
import com.bardackx.jhpp.tests.glossary.GlossList;
import com.bardackx.jhpp.tests.glossary.Glossary;
import com.bardackx.jhpp.tests.menu.Menu;
import com.bardackx.jhpp.tests.menu.MenuItem;
import com.bardackx.jhpp.tests.menu.Popup;
import com.bardackx.jhpp.tests.tutorial.Account;
import com.bardackx.jhpp.tests.tutorial.Environment;
import com.bardackx.jhpp.tests.tutorial.Environments;

public class JhppTests {

	/**
	 * Inspired by
	 * https://searchitoperations.techtarget.com/tip/Learn-YAML-through-a-personal-example
	 */
	@Test
	public void adamBertramPersonalExampleTest() {

		AdamBertramPersonalExample actual = new Jhpp().fromProperties(
				getClass().getResourceAsStream("abpe/scenario.properties"), AdamBertramPersonalExample.class);

		assertNotNull(actual);

		AdamBertramPersonalExample expected = new AdamBertramPersonalExample();
		expected.setFirstName("Adam");
		expected.setLastName("Bertram");
		expected.setHairColor("Brown");
		expected.setMarried(true);
		expected.setDogCount(2);

		Spouse spouse = new Spouse();
		spouse.setName("Miranda");
		spouse.setOccupation("Mom");
		spouse.setInterests(new String[] { "Instagram", "Facebook", "Keeping the Bertram family in check" });
		expected.setSpouse(spouse);

		Dog dog1 = new Dog();
		dog1.setName("Elliot");
		dog1.setBreed("Shih-Tzu");
		dog1.setColor("black/white");

		Dog dog2 = new Dog();
		dog2.setName("Brody");
		dog2.setBreed("Shih-Tzu");
		dog2.setColor("black/white");

		Dog[] dogs = new Dog[] { dog1, dog2 };

		expected.setDogs(dogs);

		Util.assertEqualsVerbose(expected, actual);
	}

	/**
	 * Inspired by
	 * https://searchitoperations.techtarget.com/tip/Learn-YAML-through-a-personal-example
	 */
	@Test
	public void adamBertramPersonalExample2Test() {

		AdamBertramPersonalExample source = new AdamBertramPersonalExample();
		source.setFirstName("Adam");
		source.setLastName("Bertram");
		source.setHairColor("Brown");
		source.setMarried(true);
		source.setDogCount(2);

		Spouse spouse = new Spouse();
		spouse.setName("Miranda");
		spouse.setOccupation("Mom");
		spouse.setInterests(new String[] { "Instagram", "Facebook", "Keeping the Bertram family in check" });
		source.setSpouse(spouse);

		Dog dog1 = new Dog();
		dog1.setName("Elliot");
		dog1.setBreed("Shih-Tzu");
		dog1.setColor("black/white");

		Dog dog2 = new Dog();
		dog2.setName("Brody");
		dog2.setBreed("Shih-Tzu");
		dog2.setColor("black/white");

		Dog[] dogs = new Dog[] { dog1, dog2 };

		source.setDogs(dogs);

		Properties actual = new Jhpp().toProperties(source);

		Properties expected = new Properties();
		try {
			expected.load(getClass().getResourceAsStream("abpe/scenario.to.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("============================ EXPECTED");
		for (Entry<Object, Object> e : expected.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
		System.out.println("============================ ACTUAL");
		for (Entry<Object, Object> e : actual.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
		System.out.println("============================ FALTANTES");
		Properties faltantes = new Properties();
		for (Object keyObject : expected.keySet()) {
			String key = (String) keyObject;
			String expectedValue = expected.getProperty(key);
			if (!actual.containsKey(key)) {
				faltantes.setProperty(key, expectedValue);
			} else {
				String actualValue = actual.getProperty(key);
				if (!expectedValue.equals(actualValue)) {
					System.out.println(actualValue + " != " + expectedValue);
					faltantes.setProperty(key, expectedValue);
				}
			}
		}
		for (Entry<Object, Object> e : faltantes.entrySet()) {
			System.out.println(e.getKey() + " : " + e.getValue());
		}
		System.out.println("============================");

		assertEquals(expected, actual);
	}

	/**
	 * Inspired by https://json.org/example.html
	 */
	@Test
	public void glossaryExampleTest() {

		Glossary actual = new Jhpp().fromProperties(getClass().getResourceAsStream("glossary/scenario.properties"),
				Glossary.class);

		Glossary expected = new Glossary();
		expected.setTitle("example glossary");

		GlossDiv glossDiv = new GlossDiv();
		glossDiv.setTitle("S");

		GlossList glossList = new GlossList();

		GlossEntry glossEntry = new GlossEntry();
		glossEntry.setID("SGML");
		glossEntry.setSortAs("SGML");
		glossEntry.setGlossTerm("Standard Generalized Markup Language");
		glossEntry.setAcronym("SGML");
		glossEntry.setAbbrev("ISO 8879:1986");
		glossEntry.setGlossSee("markup");

		GlossDef glossDef = new GlossDef();
		glossDef.setPara("A meta-markup language, used to create markup languages such as DocBook.");
		glossDef.setGlossSeeAlso(new String[] { "GML", "XML" });

		glossEntry.setGlossDef(glossDef);

		glossList.setGlossEntry(glossEntry);

		glossDiv.setGlossList(glossList);

		expected.setGlossDiv(glossDiv);

		Util.assertEqualsVerbose(expected, actual);
	}

	/**
	 * Inspired by https://json.org/example.html
	 */
	@Test
	public void menuExampleTest() {

		Menu actual = new Jhpp().fromProperties(getClass().getResourceAsStream("menu/scenario.properties"), Menu.class);

		Menu expected = new Menu();
		expected.setId("file");
		expected.setValue("File");
		Popup popup = new Popup();
		MenuItem[] menuitem = new MenuItem[] { //
				new MenuItem("New", "CreateNewDoc()"), //
				new MenuItem("Open", "OpenDoc()"), //
				new MenuItem("Close", "CloseDoc()"),//
		};
		popup.setMenuitem(menuitem);
		expected.setPopup(popup);

		Util.assertEqualsVerbose(expected, actual);
	}

	@Test(expected = JhppException.class)
	public void noDefaultConstructorInMainObjectClassTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/wrapper.properties"),
					NoDefaultConstructor.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.DEFAULT_CONSTRUCTOR_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test(expected = JhppException.class)
	public void noDefaultConstructorInNestedObjectClassTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/wrapper.properties"),
					NoDefaultConstructorWrapper.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.DEFAULT_CONSTRUCTOR_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test(expected = JhppException.class)
	public void noGetterMethodTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/nogetset.properties"), NoGetter.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.GETTER_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test(expected = JhppException.class)
	public void noSetterMethodTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/nogetset.properties"), NoSetter.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.SETTER_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test(expected = JhppException.class)
	public void noGetterMethodInNestedObjectTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/nested-noget.properties"),
					NoGetterSetterWrapper.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.GETTER_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test(expected = JhppException.class)
	public void noSetterMethodInNestedObjectTest() {
		try {
			new Jhpp().fromProperties(getClass().getResourceAsStream("exceptions/nested-noset.properties"),
					NoGetterSetterWrapper.class);
		} catch (JhppException ex) {
			assertEquals(JhppException.Code.SETTER_IS_MISSING, ex.getType());
			throw ex;
		}
	}

	@Test
	public void mapTest() {

		ExchangeRates actual = new Jhpp().fromProperties(
				getClass().getResourceAsStream("collections/exchange-rates.properties"), ExchangeRates.class);

		ExchangeRates expected = new ExchangeRates();
		expected.setCurrency("mxn");

		Map<String, Measure> measures = new HashMap<>();
		measures.put("usd", new Measure(19.15, "Google"));
		measures.put("eur", new Measure(21.71, "Yahoo"));
		measures.put("cad", new Measure(14.57, "Google"));

		expected.setMeasures(measures);

		Util.assertEqualsVerbose(expected, actual);
	}

	@Test
	public void listOfObjectsTest() {

		TodoList actual = new Jhpp().fromProperties(getClass().getResourceAsStream("collections/todolist.properties"),
				TodoList.class);

		TodoList expected = new TodoList();
		expected.setTitle("title");

		List<TodoListItem> items = new ArrayList<>();
		items.add(new TodoListItem("test 1", "desc 1"));
		items.add(new TodoListItem("test 2", "desc 2"));
		items.add(new TodoListItem("test 3", "desc 3"));

		expected.setItems(items);

		Set<String> tags = new HashSet<>();
		tags.add("test");
		tags.add("useless");
		tags.add("funny");
		expected.setTags(tags);

		Util.assertEqualsVerbose(expected, actual);

	}

	@Test
	public void exampleTest() {

		InputStream is = getClass().getResourceAsStream("tutorial/env.props");

		Environments actual = new Jhpp().fromProperties(is, Environments.class);

		Environments expected = new Environments();

		Environment pro = new Environment();
		pro.setUrl("pro.example.com");
		pro.setEmail(new Account("pro", "complex$Password123"));
		pro.setDb(new Account("myprosql", "different$Password123"));
		expected.setPro(pro);

		Environment qas = new Environment();
		qas.setUrl("qas.example.com");
		qas.setEmail(new Account("qas", "1234<-easyPassword"));
		qas.setDb(new Account("myqassql", "1234<-samePassword"));
		expected.setQas(qas);

		Util.assertEqualsVerbose(expected, actual);

	}

	@Test
	public void listSetMapArrayTest() {

		ListSetMapArray actual = new Jhpp().fromProperties(
				getClass().getResourceAsStream("collections/list-set-map-array.properties"), ListSetMapArray.class);

		List<Measure> list = new ArrayList<>();
		Set<Measure> set = new HashSet<>();
		Map<String, Measure> map = new HashMap<>();
		Measure[] array = { new Measure(1.0, null), new Measure(2.0, null), new Measure(3.0, null), };

		for (int i = 0; i < array.length; i++) {
			Measure e = array[i];
			list.add(e);
			set.add(e);
			map.put("" + (i + 1), e);
		}

		ListSetMapArray expected = new ListSetMapArray(list, set, map, array);

		Util.assertEqualsVerbose(expected, actual);
	}
}
