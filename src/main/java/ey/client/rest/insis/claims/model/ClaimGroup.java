package ey.client.rest.insis.claims.model;

public class ClaimGroup {
	
	private String policyNo;
	private String claimStarted;
	private Integer causeId;
	private String claimCategory;
	private Integer eventType;
	private String eventDate;
	private String eventCountry;
	
	public ClaimGroup() {}
	
	public String getPolicyNo() {
		return this.policyNo;
	}
	
	public void setPolicyNo(String policyNo) {
		this.policyNo = policyNo;
	}
	
	public String getClaimStarted() {
		return this.claimStarted;
	}
	
	public void setClaimStarted(String claimStarted) {
		this.claimStarted = claimStarted;
	}
	
	public Integer getCauseId() {
		return this.causeId;
	}
	
	public void setCauseId(Integer causeId) {
		this.causeId = causeId;
	}
	
	public String getClaimCategory() {
		return this.claimCategory;
	}
	
	public void setClaimCategory(String claimCategory) {
		this.claimCategory = claimCategory;
	}
	
	public Integer getEventType() {
		return this.eventType;
	}
	
	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}
	
	public String getEventDate() {
		return this.eventDate;
	}
	
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	
	public String getEventCountry() {
		return this.eventCountry;
	}
	
	public void setEventCountry(String eventCountry) {
		this.eventCountry = eventCountry;
	}

}
