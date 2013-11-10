package com.tempestsoul.dnd.d20;

public interface HitDieLevel {
	
	Integer getHitDieSize();
	
	Integer getBaseAttackBonus(Integer numHitDice);
	
	Integer getBaseFortitudeSave(Integer numHitDice);
	Integer getBaseReflexSave(Integer numHitDice);
	Integer getBaseWillSave(Integer numHitDice);
	
	Integer getSkillPoints(AbilityScore intelligenceScore, Integer numHitDice);
}
