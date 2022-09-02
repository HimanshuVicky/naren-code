package com.assignsecurities.domain.dm;

import java.util.List;

public class SSTestDataModel {
   private String name;
   private int age;
   private SSTestDataModel tempSSTestDataModel;
   private List<SSTestDataModel> tempSSTestDataModels;
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public int getAge() {
	return age;
}
public void setAge(int age) {
	this.age = age;
}
public SSTestDataModel getTempSSTestDataModel() {
	return tempSSTestDataModel;
}
public void setTempSSTestDataModel(SSTestDataModel tempSSTestDataModel) {
	this.tempSSTestDataModel = tempSSTestDataModel;
}
public List<SSTestDataModel> getTempSSTestDataModels() {
	return tempSSTestDataModels;
}
public void setTempSSTestDataModels(List<SSTestDataModel> tempSSTestDataModels) {
	this.tempSSTestDataModels = tempSSTestDataModels;
}
}
