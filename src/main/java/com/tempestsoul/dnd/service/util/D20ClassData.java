package com.tempestsoul.dnd.service.util;

public enum D20ClassData {
	// class name (hero forge id, BAB, good Fort?, good Ref?, good Will?, HD)
	NONE (1.0, 0.0, false, false, false, 0),
	BARBARIAN (2.0, 1.0, true, false, false, 12),
	BARD (3.0, 0.75, false, true, true, 6),
	CLERIC (4.0, 0.75, true, false, true, 8),
	DRUID (5.0, 0.75, true, false, true, 8),
	FIGHTER (6.0, 1.0, true, false, false, 10),
	MONK (7.0, 0.75, true, true, true, 8),
	PALADIN (8.0, 1.0, true, false, false, 10),
	RANGER (9.0, 1.0, true, true, false, 8),
	ROGUE (10.0, 0.75, false, true, false, 6),
	SORCERER (11.0, 0.5, false, false, true, 4),
	WIZARD (12.0, 0.5, false, true, false, 4);
	
	D20ClassData(double id, double babRate, boolean goodFort, boolean refRate, boolean willRate, int dieSize) {
		heroForgeId = id;
		this.babRate = babRate;
		this.goodFort = goodFort;
		this.goodRef = refRate;
		this.goodWill = willRate;
		this.dieSize = dieSize;
	}
	double heroForgeId;
	double babRate;
	boolean goodFort, goodRef, goodWill;
	int dieSize;
	
	public int getHitDie() { return dieSize; }
	public int getBAB(int numLvl) {
		return (int) (babRate * numLvl);
	}
	public int getFortSave(int numLvl) {
		return getSave(goodFort, numLvl);
	}
	public int getRefSave(int numLvl) {
		return getSave(goodRef, numLvl);
	}
	public int getWillSave(int numLvl) {
		return getSave(goodWill, numLvl);
	}
	
	private int getSave(boolean goodSave, int numLvl) {
		if(goodSave)
			return numLvl/2 + 2;
		else
			return numLvl/3;
	}
	
	public static D20ClassData getClassDataById(Double heroForgeId) {
		if(heroForgeId == null)
			return null;
		for(D20ClassData classData : D20ClassData.values()) {
			if(classData.heroForgeId == heroForgeId) {
				return classData;
			}
		}
		return null;
	}
}