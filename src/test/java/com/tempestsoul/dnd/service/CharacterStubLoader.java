package com.tempestsoul.dnd.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.tempestsoul.dnd.d20.Ability;
import com.tempestsoul.dnd.d20.AbilityScore;
import com.tempestsoul.dnd.d20.Creature;
import com.tempestsoul.dnd.d20.CreatureSubType;
import com.tempestsoul.dnd.d20.CreatureType;
import com.tempestsoul.dnd.d20.Size;

public class CharacterStubLoader implements CharacterLoadService {

	@Override
	public Creature loadCharacter(File file) {
		Creature druid = new Creature();
		druid.setDruidLvl(8);
		druid.setBaseAtkBonus(6);
		druid.setBaseFort(6);
		druid.setBaseRef(2);
		druid.setBaseWill(6);
		druid.setFeats(Arrays.asList("Barf", "Combat Expertise", "Superior Two-Weapon Fighting"));
		druid.setHitPoints(5);	// somebody's suffering
		druid.setMovement("30 ft, fly 2000000000 mi");
		druid.setName("Spawny McCamperson <br/> someone isn't checking injected strings");
		druid.setNaturalArmor(0);
		druid.setNumHitDice(10);
		druid.setSize(Size.Large);
		druid.setType(CreatureType.DRAGON);
		druid.setSubTypes(Arrays.asList(CreatureSubType.Fire, CreatureSubType.Evil));
		Map<Ability, AbilityScore> stats = new HashMap<Ability, AbilityScore>();
		stats.put(Ability.STR, new AbilityScore(10));
		stats.put(Ability.DEX, new AbilityScore(11));
		stats.put(Ability.CON, new AbilityScore(20));
		stats.put(Ability.INT, new AbilityScore(8));
		stats.put(Ability.WIS, new AbilityScore(32));
		stats.put(Ability.CHA, new AbilityScore(5));
		druid.setStats(stats);
		// TODO attacks, special attacks
		return druid;
	}

}
