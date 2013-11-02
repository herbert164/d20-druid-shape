package com.tempestsoul.dnd.d20;

import java.util.Map;

public class SpecialAttack {
	boolean savingThrowReq;
	Ability baseSave;
	String saveName;
	String description;
	
	int getSavingThrowDC(int iNumHitDice, Map<Ability, Integer> allScores) throws Exception {
		if(!savingThrowReq)
			throw new Exception("Cannot calculate saving throw for ability without saving throw!");
		// TODO verify algorithm, add in extra bonuses
		return 10 + iNumHitDice /2 + allScores.get(baseSave);
	}
}
