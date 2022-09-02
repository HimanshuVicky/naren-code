package com.assignsecurities.app.util;

import java.util.Objects;

import com.assignsecurities.bean.PersonName;

public class PersonNameUtil {
	public static PersonName splitFullName(String fullName) {
		PersonName personName = PersonName.builder().build();

		String[] splitName = fullName.split(" ");
		if (splitName.length > 0) {
			switch (splitName.length) {
			case 1:
				personName.setFirstName(splitName[0]);
				break;
			case 2:
				personName.setFirstName(splitName[0]);
				personName.setLastName(splitName[1]);
				break;
			case 3:
				personName.setFirstName(splitName[0]);
				personName.setMiddleName(splitName[1]);
				personName.setLastName(splitName[2]);
				break;
			}
		}
		if (Objects.isNull(personName.getFirstName())) {
			int idx = fullName.lastIndexOf(' ');
			if (idx == -1)
			    throw new IllegalArgumentException("Only a single name: " + fullName);
			personName.setFirstName(fullName.substring(0, idx));
			personName.setLastName(fullName.substring(idx + 1));
		}
		return personName;
	}
	
	public static String getFullName(String firstName, String middleName, String lastName) {
		String fullName =firstName;
		if(Objects.nonNull(middleName)) {
			fullName = fullName + " " + middleName;
		}
		
		if(Objects.nonNull(lastName)) {
			fullName = fullName + " " + lastName;
		}
		return fullName;
	}
}
