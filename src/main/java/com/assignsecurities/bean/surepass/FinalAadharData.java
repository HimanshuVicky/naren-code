package com.assignsecurities.bean.surepass;


import com.google.gson.GsonBuilder;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@lombok.Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FinalAadharData {

	public class Address {
		public String country;
		public String dist;
		public String state;
		public String po;
		public String loc;
		public String vtc;
		public String subdist;
		public String street;
		public String house;
		public String landmark;
		public String getCountry() {
			return country;
		}
		public void setCountry(String country) {
			this.country = country;
		}
		public String getDist() {
			return dist;
		}
		public void setDist(String dist) {
			this.dist = dist;
		}
		public String getState() {
			return state;
		}
		public void setState(String state) {
			this.state = state;
		}
		public String getPo() {
			return po;
		}
		public void setPo(String po) {
			this.po = po;
		}
		public String getLoc() {
			return loc;
		}
		public void setLoc(String loc) {
			this.loc = loc;
		}
		public String getVtc() {
			return vtc;
		}
		public void setVtc(String vtc) {
			this.vtc = vtc;
		}
		public String getSubdist() {
			return subdist;
		}
		public void setSubdist(String subdist) {
			this.subdist = subdist;
		}
		public String getStreet() {
			return street;
		}
		public void setStreet(String street) {
			this.street = street;
		}
		public String getHouse() {
			return house;
		}
		public void setHouse(String house) {
			this.house = house;
		}
		public String getLandmark() {
			return landmark;
		}
		public void setLandmark(String landmark) {
			this.landmark = landmark;
		}
		
	}

	public class Data {
		public String client_id;
		public String full_name;
		public String aadhaar_number;
		public String dob;
		public String gender;
		public Address address;
		public boolean face_status;
		public int face_score;
		public String zip;
		public String profile_image;
		public boolean has_image;
		public String raw_xml;
		public String zip_data;
		public String care_of;
		public String share_code;
		public boolean mobile_verified;
		public String reference_id;
		public Object aadhaar_pdf;
		public String getClient_id() {
			return client_id;
		}
		public void setClient_id(String client_id) {
			this.client_id = client_id;
		}
		public String getFull_name() {
			return full_name;
		}
		public void setFull_name(String full_name) {
			this.full_name = full_name;
		}
		public String getAadhaar_number() {
			return aadhaar_number;
		}
		public void setAadhaar_number(String aadhaar_number) {
			this.aadhaar_number = aadhaar_number;
		}
		public String getDob() {
			return dob;
		}
		public void setDob(String dob) {
			this.dob = dob;
		}
		public String getGender() {
			return gender;
		}
		public void setGender(String gender) {
			this.gender = gender;
		}
		public Address getAddress() {
			return address;
		}
		public void setAddress(Address address) {
			this.address = address;
		}
		public boolean isFace_status() {
			return face_status;
		}
		public void setFace_status(boolean face_status) {
			this.face_status = face_status;
		}
		public int getFace_score() {
			return face_score;
		}
		public void setFace_score(int face_score) {
			this.face_score = face_score;
		}
		public String getZip() {
			return zip;
		}
		public void setZip(String zip) {
			this.zip = zip;
		}
		public String getProfile_image() {
			return profile_image;
		}
		public void setProfile_image(String profile_image) {
			this.profile_image = profile_image;
		}
		public boolean isHas_image() {
			return has_image;
		}
		public void setHas_image(boolean has_image) {
			this.has_image = has_image;
		}
		public String getRaw_xml() {
			return raw_xml;
		}
		public void setRaw_xml(String raw_xml) {
			this.raw_xml = raw_xml;
		}
		public String getZip_data() {
			return zip_data;
		}
		public void setZip_data(String zip_data) {
			this.zip_data = zip_data;
		}
		public String getCare_of() {
			return care_of;
		}
		public void setCare_of(String care_of) {
			this.care_of = care_of;
		}
		public String getShare_code() {
			return share_code;
		}
		public void setShare_code(String share_code) {
			this.share_code = share_code;
		}
		public boolean isMobile_verified() {
			return mobile_verified;
		}
		public void setMobile_verified(boolean mobile_verified) {
			this.mobile_verified = mobile_verified;
		}
		public String getReference_id() {
			return reference_id;
		}
		public void setReference_id(String reference_id) {
			this.reference_id = reference_id;
		}
		public Object getAadhaar_pdf() {
			return aadhaar_pdf;
		}
		public void setAadhaar_pdf(Object aadhaar_pdf) {
			this.aadhaar_pdf = aadhaar_pdf;
		}
	}

	public Data data;
	public int status_code;
	public boolean success;
	public Object message;
	public String message_code;
	
	public String toString() {
	    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
	}

}
