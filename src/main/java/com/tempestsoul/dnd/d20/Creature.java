package com.tempestsoul.dnd.d20;

import java.util.List;
import java.util.Map;

/*
 * type/subtype: druid
gain aquatic subtype of assumed
size: wild shape
natural weapons: shape
natural armor: shape
movement: shape
special attacks(Ex): shape
special qualities: druid
abilities/attacks(Sp,Su): druid
physical ability scores: shape
mental ability scores: druid
base save: druid
skills: druid
feats: druid
BAB: druid
hp: druid
spellcasting: (Natural Spell) ? true : false
spoken language: shape

druid items: disappear
shape items: fall off
+10 disguise as form
shape HD <= druid HD

class Animal {
    int iHitDice;
    String sName;
    boolean isAquatic;
    Size size;
    int iStrength;
    int iDexterity;
    int iConstitution;
    int iNaturalArmor;
    List<Attack> attacks;
    String sMovement;
    List<SpecialAttack> specialAttacks;
    int iSpace; int iReach;
}
class SpecialAttack {
    name
    text
    savingThrow
    savingThrowDc
}
    Enum Size {
        0=M (medium)
        1=S (small)
        -1=L
        -2=H
        2=T
        
        getGrappleBonus() {return this.intValue() * -4;}
        getSizeBonus() { return this.intValue(); }
        getHideBonus() {return this.intValue() * 4; }
    }
 */
public class Creature {
	String sName;
	int iNumHitDice;
	int iHitPoints;
	int iDruidLvl;
	CreatureType type;
	List<CreatureSubType> subTypes;
	Size size;
	Map<Ability, AbilityScore> stats;
	//TODO implement
	Map<String, Integer> skillRanks;
	List<String> feats;
	// could be calculated...
	int iBaseAtkBonus; // 3/4 druid level (if only druid)
	int iBaseFort; // good
	int iBaseRef; // poor
	int iBaseWill; // good
	
	// Creature-specific
	List<Attack> attacks;
	List<SpecialAttack> specialAtks;
	String sMovement;
	int iNaturalArmor;
	
	public Creature() { }
	public Creature(Creature c) {/* TODO implement */}
	
	// TODO move to separate class? really a class ability, not something a character does...
	public Creature wildShape(Creature creature) {
		if(creature.iNumHitDice > this.iNumHitDice) {
			throw new IllegalArgumentException("A druid cannot wild shape into creatures with more hit dice");
		}
		// TODO finish implementing
		Creature shape = new Creature(this);
		shape.size = creature.size;
		shape.setPhysicalScores(creature.stats);
		shape.attacks = creature.attacks;
		return null;
	}
	
	private void setPhysicalScores(Map<Ability, AbilityScore> src) {
		for(Ability stat : Ability.physicalScores) {
			stats.put(stat, src.get(stat));
		}
	}
	private void setMentalScores(Map<Ability, AbilityScore> src) {
		for(Ability stat : Ability.mentalScores) {
			stats.put(stat, src.get(stat));
		}
	}
	
	public Integer getFortSave() {
		AbilityScore score = stats.get(Ability.CON);
		if(score == null) 
			return null;
		return iBaseFort + score.getModifier(); // + miscellaneous
	}
	public Integer getRefSave() {
		AbilityScore score = stats.get(Ability.DEX);
		if(score == null) 
			return null;
		return iBaseRef + score.getModifier(); // + miscellaneous
	}
	public Integer getWillSave() {
		AbilityScore score = stats.get(Ability.WIS);
		if(score == null) 
			return null;
		return iBaseWill + score.getModifier(); // + miscellaneous
	}
	public int getArmorCount() {
		return 10 + stats.get(Ability.DEX).getModifier() + iNaturalArmor + size.getSizeMod();	// TODO implement
	}
	public int getTouchArmorCount() {
		return 10 + stats.get(Ability.DEX).getModifier() + size.getSizeMod();	// TODO implement
	}
	public int getFlatArmorCount() {
		return 10 + iNaturalArmor + size.getSizeMod();	// TODO implement
	}
}
