package me.manaki.plugin.crafts.craft;

public class CraftContent {
	
	private String recipeID;
	private long timeExpired;
	
	public CraftContent(String recipeID, long timeExpired) {
		this.recipeID = recipeID;
		this.timeExpired = timeExpired;
	}
	
	public String getRecipeID() {
		return this.recipeID;
	}
	
	public long getTimeExpired() {
		return this.timeExpired;
	}
	
	public String toString() {
		return this.recipeID + ":" + this.timeExpired;
	}
	
	@Override
	public boolean equals(Object o) {
		CraftContent cc = (CraftContent) o;
		return this.recipeID.equals(cc.getRecipeID()) && this.timeExpired == cc.getTimeExpired();
	}
	
	public static CraftContent fromString(String s) {
		return new CraftContent(s.split(":")[0], Long.valueOf(s.split(":")[1]));
	}
	
	
}
