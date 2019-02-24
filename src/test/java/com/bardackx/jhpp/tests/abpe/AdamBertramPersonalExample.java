package com.bardackx.jhpp.tests.abpe;

import java.util.Arrays;

public class AdamBertramPersonalExample {

	private String firstName;
	private String lastName;
	private String hairColor;
	private boolean married;

	private Spouse spouse;

	private int dogCount;

	private Dog[] dogs;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getHairColor() {
		return hairColor;
	}

	public void setHairColor(String hariColor) {
		this.hairColor = hariColor;
	}

	public boolean isMarried() {
		return married;
	}

	public void setMarried(boolean married) {
		this.married = married;
	}

	public Spouse getSpouse() {
		return spouse;
	}

	public void setSpouse(Spouse spouse) {
		this.spouse = spouse;
	}

	public int getDogCount() {
		return dogCount;
	}

	public void setDogCount(int dogCount) {
		this.dogCount = dogCount;
	}

	public Dog[] getDogs() {
		return dogs;
	}

	public void setDogs(Dog[] dogs) {
		this.dogs = dogs;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + dogCount;
		result = prime * result + Arrays.hashCode(dogs);
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((hairColor == null) ? 0 : hairColor.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + (married ? 1231 : 1237);
		result = prime * result + ((spouse == null) ? 0 : spouse.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		AdamBertramPersonalExample other = (AdamBertramPersonalExample) obj;
		if (dogCount != other.dogCount) return false;
		if (!Arrays.equals(dogs, other.dogs)) return false;
		if (firstName == null) {
			if (other.firstName != null) return false;
		} else if (!firstName.equals(other.firstName)) return false;
		if (hairColor == null) {
			if (other.hairColor != null) return false;
		} else if (!hairColor.equals(other.hairColor)) return false;
		if (lastName == null) {
			if (other.lastName != null) return false;
		} else if (!lastName.equals(other.lastName)) return false;
		if (married != other.married) return false;
		if (spouse == null) {
			if (other.spouse != null) return false;
		} else if (!spouse.equals(other.spouse)) return false;
		return true;
	}

}
