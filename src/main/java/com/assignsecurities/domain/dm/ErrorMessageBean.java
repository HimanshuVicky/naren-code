package com.assignsecurities.domain.dm;


public class ErrorMessageBean 
{
	long lineItemID;
	String lineItemName;
	String message;
    boolean tenurePlacementRuleError;
    long candidateOverrideStatus;
    long teamId;

	public static String ID_FIELD_KEY =  "lineItemID";
    public static String NAME_FIELD_KEY =  "lineItemName";
    public static String MESSAGE_FIELD_KEY =  "message";
    public static String TENURE_PLACEMENT_FIELD_KEY =  "tenurePlacementRuleError";
    public static String CND_OVERRIDE_STATUS =  "candidateOverrideStatus";
    public static String TEAM_ID_KEY =  "teamId";
	
	public void setMessage(String msg)
	{
	    this.message = msg;
	}
	
	public String getMessage()
	{
	    return message;
	}
	
	public void setLineItemID(long id)
	{
	    this.lineItemID = id;
	}
	public long getLineItemID()
	{
	    return lineItemID;
	}
	
	public void setLineItemName(String name)
	{
	    this.lineItemName = name;
	}
	public String getLineItemName()
	{
	    return lineItemName;
	}
	
    public String toString() 
    {
        return "ErrorMessageModel{" + "[ lineItemId:" + lineItemID+ "]"
                     + "[lineItemName:" + lineItemName + "]"
                     + "[message:" + message + "]"
                     + "}";
    }
	
	public ErrorMessageBean(long id, String name, String message)
	{
	    this.lineItemID = id;
	    this.lineItemName = name;
	    this.message=message;
	}   
	
    public ErrorMessageBean(String message)
    {
        this.lineItemID = 0;
        this.lineItemName = "";
        this.message=message;
    }   

	
	public ErrorMessageBean()
	{
	    super();
	}     
        
    /**
     * @return Returns the tenurePlacementRuleError.
     */
    public boolean isTenurePlacementRuleError() {
        return tenurePlacementRuleError;
    }
    /**
     * @param tenurePlacementRuleError The tenurePlacementRuleError to set.
     */
    public void setTenurePlacementRuleError(boolean tenurePlacementRuleError) {
        this.tenurePlacementRuleError = tenurePlacementRuleError;
    }
    /**
     * @return Returns the candidateOverrideStatus.
     */
    public long getCandidateOverrideStatus() {
        return candidateOverrideStatus;
    }
    /**
     * @param candidateOverrideStatus The candidateOverrideStatus to set.
     */
    public void setCandidateOverrideStatus(long candidateOverrideStatus) {
        this.candidateOverrideStatus = candidateOverrideStatus;
    }
    /**
     * @return Returns the teamId.
     */
    public long getTeamId() {
        return teamId;
    }
    /**
     * @param teamId The teamId to set.
     */
    public void setTeamId(long teamId) {
        this.teamId = teamId;
    }
}

/*
 * $Log:
 * 
 */
