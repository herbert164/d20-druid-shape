package com.tempestsoul.dnd.d20;

import java.util.ArrayList;
import java.util.HashMap;
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
	List<Skill> skills;
	List<String> feats;
	// could be calculated...
	int iBaseAtkBonus; // 3/4 druid level (if only druid); should be set by class level instead?
	int iBaseFort; // good
	int iBaseRef; // poor
	int iBaseWill; // good
	
	// Creature-specific
	List<Attack> attacks;
	List<SpecialAttack> specialAtks;
	String movement;
	int iNaturalArmor;
	
	public Creature() { }
	public Creature(Creature c) {
		sName = c.sName;
		iNumHitDice = c.iNumHitDice;
		iHitPoints = c.iHitPoints;
		iDruidLvl = c.iDruidLvl;
		type = c.type;
		subTypes = new ArrayList<CreatureSubType>(c.subTypes);
		size = c.size;
		stats = new HashMap<Ability, AbilityScore>(c.stats);
		skills = new ArrayList<Skill>(c.skills);
		feats = new ArrayList<String>(c.feats);
		iBaseAtkBonus = c.iBaseAtkBonus;
		iBaseFort = c.iBaseFort;
		iBaseRef = c.iBaseRef;
		iBaseWill = c.iBaseWill;
		attacks = new ArrayList<Attack>(c.attacks);
		specialAtks = new ArrayList<SpecialAttack>(c.specialAtks);
		movement = c.movement;
		iNaturalArmor = c.iNaturalArmor;
	}
	
	// move to separate class? really a class ability, not something a character does...
	public Creature wildShape(Creature animal) {
		if(animal.iNumHitDice > this.iNumHitDice) {
			throw new IllegalArgumentException("A druid cannot wild shape into creatures with more hit dice");
		}
		// TODO finish implementing
		Creature shape = new Creature(this);
		// size: wild shape
		shape.size = animal.size;
		// physical ability scores: shape
		// mental ability scores: druid
		shape.setPhysicalScores(animal.stats);
		shape.setMentalScores(this.stats);	// redundant, but oh well
		// natural weapons: shape
		shape.attacks = animal.attacks;
		// if old creature has aquatic, add aquatic
		if(animal.isAquatic() && !shape.isAquatic())
			shape.subTypes.add(CreatureSubType.Aquatic);
		// natural armor: shape
		shape.iNaturalArmor = animal.iNaturalArmor;
		// movement: shape
		shape.movement = animal.movement;
		// lose Ex special attacks
		for(SpecialAttack atk : this.specialAtks) {
			shape.specialAtks.remove(atk);
		}
		// special attacks(Ex): shape
		for(SpecialAttack atk : animal.specialAtks) {
			SpecialAbilityType atkType = atk.type;
			if(atkType.equals(SpecialAbilityType.EXTRAORDINARY)
					|| atkType.equals(SpecialAbilityType.NATURAL))
				shape.specialAtks.add(atk);
		}
		return shape;
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
		// TODO implement
		return 10 + stats.get(Ability.DEX).getModifier() + iNaturalArmor + size.getSizeMod();
	}
	public int getTouchArmorCount() {
		// TODO implement
		return 10 + stats.get(Ability.DEX).getModifier() + size.getSizeMod();
	}
	public int getFlatArmorCount() {
		// TODO implement
		return 10 + iNaturalArmor + size.getSizeMod();
	}
	
	public boolean isAquatic() {
		return subTypes.contains(CreatureSubType.Aquatic);
	}
	
	public String getName() {
		return sName;
	}
	public void setName(String name) {
		this.sName = name;
	}
	public int getNumHitDice() {
		return iNumHitDice;
	}
	public void setNumHitDice(int numHitDice) {
		this.iNumHitDice = numHitDice;
	}
	public int getHitPoints() {
		return iHitPoints;
	}
	public void setHitPoints(int hitPoints) {
		this.iHitPoints = hitPoints;
	}
	public int getDruidLvl() {
		return iDruidLvl;
	}
	public void setDruidLvl(int druidLvl) {
		this.iDruidLvl = druidLvl;
	}
	public CreatureType getType() {
		return type;
	}
	public void setType(CreatureType type) {
		this.type = type;
	}
	public List<CreatureSubType> getSubTypes() {
		return subTypes;
	}
	public void setSubTypes(List<CreatureSubType> subTypes) {
		this.subTypes = subTypes;
	}
	public Size getSize() {
		return size;
	}
	public void setSize(Size size) {
		this.size = size;
	}
	public Map<Ability, AbilityScore> getStats() {
		return stats;
	}
	public void setStats(Map<Ability, AbilityScore> stats) {
		this.stats = stats;
	}
	
	public List<Skill> getSkills() {
		return skills;
	}
	/**
	 * 
	 * @param skillRanks
	 */
	public void setSkillRanks(Map<String, Integer> skillRanks) {
		for(String skillName : skillRanks.keySet()) {
			Skill skill = getSkillByName(skillName);
			if(skill != null) {
				skill.setRanks(skillRanks.get(skillName));
			} else {
				throw new IllegalArgumentException("No skill called " + skillName);
			}
		}
	}
	
	// should really think about some kind of skill wrapper or way to access the map...
	public Skill getSkillByName(String skillName) {
		if(skills == null)
			return null;
		for(Skill skill : skills) {
			if(skill.getName().equalsIgnoreCase(skillName))
				return skill;
		}
		return null;
	}
	
	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}
	
	public List<String> getFeats() {
		return feats;
	}
	public void setFeats(List<String> feats) {
		this.feats = feats;
	}
	
	public int getBaseAtkBonus() {
		return iBaseAtkBonus;
	}
	public void setBaseAtkBonus(int baseAtkBonus) {
		this.iBaseAtkBonus = baseAtkBonus;
	}
	public int getBaseFort() {
		return iBaseFort;
	}
	public void setBaseFort(int iBaseFort) {
		this.iBaseFort = iBaseFort;
	}
	public int getBaseRef() {
		return iBaseRef;
	}
	public void setBaseRef(int iBaseRef) {
		this.iBaseRef = iBaseRef;
	}
	public int getBaseWill() {
		return iBaseWill;
	}
	public void setBaseWill(int iBaseWill) {
		this.iBaseWill = iBaseWill;
	}
	public List<Attack> getAttacks() {
		return attacks;
	}
	public void setAttacks(List<Attack> attacks) {
		this.attacks = attacks;
	}
	public List<SpecialAttack> getSpecialAtks() {
		return specialAtks;
	}
	public void setSpecialAtks(List<SpecialAttack> specialAtks) {
		this.specialAtks = specialAtks;
	}
	public String getMovement() {
		return movement;
	}
	public void setMovement(String movement) {
		this.movement = movement;
	}
	public int getNaturalArmor() {
		return iNaturalArmor;
	}
	public void setNaturalArmor(int naturalArmor) {
		this.iNaturalArmor = naturalArmor;
	}
}
