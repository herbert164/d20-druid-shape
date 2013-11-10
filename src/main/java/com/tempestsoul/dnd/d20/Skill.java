package com.tempestsoul.dnd.d20;

public class Skill {
	private String name;
	private Ability baseAbility;
	private Integer ranks;
	private Integer miscBonusTotal;
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Ability getBaseAbility() {
		return baseAbility;
	}
	
	public void setBaseAbility(Ability baseAbility) {
		this.baseAbility = baseAbility;
	}
	
	public Integer getRanks() {
		return ranks;
	}
	
	public void setRanks(Integer ranks) {
		this.ranks = ranks;
	}

	public void setMiscBonusTotal(Integer miscBonusTotal) {
		this.miscBonusTotal = miscBonusTotal;
	}

	public Integer getMiscBonusTotal() {
		return miscBonusTotal;
	}
	
	@Override
	public boolean equals(Object o) {
		// a skill is equal to another if it has the same name.
		if(o instanceof Skill) {
			Skill s = (Skill) o;
			return this.name.equals(s.name);
		}
		return false;
	}
}
