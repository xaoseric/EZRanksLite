package me.clip.ezrankslite.multipliers;

public class Discount {
	
	private String identifier;
	
	private int priority;
	
	private String permission;
	
	private double multiplier;
	
	public Discount(String identifier) {
		this.setIdentifier(identifier);
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public double getMultiplier() {
		return multiplier;
	}

	public void setMultiplier(double multiplier) {
		this.multiplier = multiplier;
	}
	
	

}
