package com.bardackx.jhpp.tests;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.bardackx.jhpp.Jhpp;
import com.bardackx.jhpp.tests.abpe.AdamBertramPersonalExample;
import com.bardackx.jhpp.tests.abpe.Dog;
import com.bardackx.jhpp.tests.abpe.Spouse;
import com.bardackx.jhpp.tests.glossary.GlossDef;
import com.bardackx.jhpp.tests.glossary.GlossDiv;
import com.bardackx.jhpp.tests.glossary.GlossEntry;
import com.bardackx.jhpp.tests.glossary.GlossList;
import com.bardackx.jhpp.tests.glossary.Glossary;
import com.bardackx.jhpp.tests.menu.Menu;
import com.bardackx.jhpp.tests.menu.MenuItem;
import com.bardackx.jhpp.tests.menu.Popup;

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

		Dog[] dogs = new Dog[2];
		dogs[0] = new Dog();
		dogs[0].setName("Elliot");
		dogs[0].setBreed("Shih-Tzu");
		dogs[0].setColor("black/white");
		dogs[1] = new Dog();
		dogs[1].setName("Brody");
		dogs[1].setBreed("Shih-Tzu");
		dogs[1].setColor("black/white");
		expected.setDogs(dogs);

		Util.assertEqualsVerbose(expected, actual);
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
}
