package com.assignsecurities.bean.surepass;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChequeDetailsExtended {
	@JsonProperty("CENTRE") 
    public String CENTRE;
    @JsonProperty("CONTACT") 
    public String CONTACT;
    @JsonProperty("SWIFT") 
    public String SWIFT;
    @JsonProperty("IMPS") 
    public boolean IMPS;
    @JsonProperty("ADDRESS") 
    public String ADDRESS;
    @JsonProperty("UPI") 
    public boolean UPI;
    @JsonProperty("MICR") 
    public String MICR;
    @JsonProperty("RTGS") 
    public boolean RTGS;
    @JsonProperty("DISTRICT") 
    public String DISTRICT;
    @JsonProperty("NEFT") 
    public boolean NEFT;
    @JsonProperty("STATE") 
    public String STATE;
    @JsonProperty("CITY") 
    public String CITY;
    @JsonProperty("BRANCH") 
    public String BRANCH;
    @JsonProperty("BANK") 
    public String BANK;
    @JsonProperty("BANKCODE") 
    public String BANKCODE;
    @JsonProperty("IFSC") 
    public String IFSC;

    
    public String getCENTRE() {
        return CENTRE;
    }
    public void setCENTRE(String cENTRE) {
        this.CENTRE = cENTRE;
    }
    public String getCONTACT() {
        return CONTACT;
    }
    public void setCONTACT(String cONTACT) {
        this.CONTACT = cONTACT;
    }
    public String getSWIFT() {
        return SWIFT;
    }
    public void setSWIFT(String sWIFT) {
        this.SWIFT = sWIFT;
    }
    public Boolean getIMPS() {
        return IMPS;
    }
    public void setIMPS(Boolean iMPS) {
        this.IMPS = iMPS;
    }
    public String getADDRESS() {
        return ADDRESS;
    }
    public void setADDRESS(String aDDRESS) {
        this.ADDRESS = aDDRESS;
    }
    public Boolean getUPI() {
        return UPI;
    }
    public void setUPI(Boolean uPI) {
        this.UPI = uPI;
    }
    public String getMICR() {
        return MICR;
    }
    public void setMICR(String mICR) {
        this.MICR = mICR;
    }
    public Boolean getRTGS() {
        return RTGS;
    }
    public void setRTGS(Boolean rTGS) {
        this.RTGS = rTGS;
    }
    public String getDISTRICT() {
        return DISTRICT;
    }
    public void setDISTRICT(String dISTRICT) {
        this.DISTRICT = dISTRICT;
    }
    public Boolean getNEFT() {
        return NEFT;
    }
    public void setNEFT(Boolean nEFT) {
        this.NEFT = nEFT;
    }
    public String getSTATE() {
        return STATE;
    }
    public void setSTATE(String sTATE) {
        this.STATE = sTATE;
    }
    public String getCITY() {
        return CITY;
    }
    public void setCITY(String cITY) {
        this.CITY = cITY;
    }
    public String getBRANCH() {
        return BRANCH;
    }
    public void setBRANCH(String bRANCH) {
        this.BRANCH = bRANCH;
    }
    public String getBANK() {
        return BANK;
    }
    public void setBANK(String bANK) {
        this.BANK = bANK;
    }
    public String getBANKCODE() {
        return BANKCODE;
    }
    public void setBANKCODE(String bANKCODE) {
        this.BANKCODE = bANKCODE;
    }
    public String getIFSC() {
        return IFSC;
    }
    public void setIFSC(String iFSC) {
        this.IFSC = iFSC;
    }
    
}
