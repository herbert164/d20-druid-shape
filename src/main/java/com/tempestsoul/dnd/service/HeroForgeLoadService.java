package com.tempestsoul.dnd.service;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.tempestsoul.MutableInt;
import com.tempestsoul.dnd.d20.Ability;
import com.tempestsoul.dnd.d20.AbilityScore;
import com.tempestsoul.dnd.d20.Creature;
import com.tempestsoul.dnd.d20.CreatureSubType;
import com.tempestsoul.dnd.d20.CreatureType;
import com.tempestsoul.dnd.d20.Size;
import com.tempestsoul.dnd.d20.Skill;
import com.tempestsoul.dnd.service.util.D20ClassData;

public class HeroForgeLoadService implements CharacterLoadService {

	// HeroForge constants
	private static final EnumSet<Ability> abilities = EnumSet.of(Ability.STR, Ability.DEX, Ability.CON, Ability.INT, Ability.WIS, Ability.CHA);
	
	@Override
	public Creature loadCharacter(File file) {
		Creature character = new Creature();
		
		try {
			Workbook workbook = WorkbookFactory.create(file);
			Sheet charSheet = workbook.getSheet("ExportSheet");
			
			String name = getCellText(charSheet, 11, 4);
			character.setName(name);

			// parse ability scores
			Map<Ability, AbilityScore> stats = parseAbilityScores(charSheet);
			character.setStats(stats);
			
			// TODO Parse skills (available & base stat)
			
			// Parse race to get type/subtype, ability adjustments, etc.
			parseRacialInfo(character, charSheet);
			
			// Parse classes (gets druid level, BAB, # hit dice, base saves, etc)
			parseCharacterClasses(character, charSheet);
			
			// TODO feats
		} catch (InvalidFormatException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return character;
	}
	
	/**
	 * Parses the ability scores for a character from the sheet, including inherent level-up bonus
	 * (but not racial modifiers, magical items, magic buffs, etc)
	 * @param charSheet
	 * @return
	 */
	protected Map<Ability, AbilityScore> parseAbilityScores(Sheet charSheet) {
		Map<Ability, AbilityScore> stats = new HashMap<Ability, AbilityScore>();
		// make list of boosts first: <ability row #, count>
		Map<Double, MutableInt> abilityBoosts = new HashMap<Double, MutableInt>();
		for(int row = 8; row < 13; row++) {
			Double boosted = getNumericCellValue(charSheet, row, 11) - 1;
			if(boosted > 0) {
				MutableInt count = abilityBoosts.get(boosted);
				if(count != null) {
					count.inc();
				} else {
					abilityBoosts.put(boosted, new MutableInt());
				}
			}
		}
		int i = 1;
		for(Ability ability : abilities) {
			Double score = getNumericCellValue(charSheet, i, 4);
			MutableInt count = abilityBoosts.get(new Double(i));
			if(count != null)
				score += abilityBoosts.get(new Double(i)).getValue();
			stats.put(ability, new AbilityScore(score.intValue()));
			i++;
		}
		return stats;
	}
	
	/**
	 * Parses the character's class levels from the provided character sheet,
	 * and sets any relevant information obtained from the class level in the character object
	 * @param character
	 * @param charSheet
	 */
	protected void parseCharacterClasses(Creature character, Sheet charSheet) {
		/* should just stick these into the Creature class instead, so we can figure out what abilities 
		 * the class has in future version */
		int hp = 0;
		int iNumHitDice = 0;
		Map<Ability, AbilityScore> stats = character.getStats();// stats must be done first!
		int conMod = stats.get(Ability.CON).getModifier();
		//int druidLvl = 0;
		Map<D20ClassData, MutableInt> classes = new HashMap<D20ClassData, MutableInt>();
		for(int i = 8; i < 68; i++) {
			// row 16 is first class, row 17 is gestalt secondary class, row 18 is hp rolled
			Double classId = getNumericCellValue(charSheet, i, 16);
			D20ClassData classData = D20ClassData.getClassDataById(classId);
			switch(classData) {
			case NONE:
				// do nothing
				break;
			case DRUID:
			default:
				iNumHitDice++;
				if(i == 8) {
					hp += classData.getHitDie();
				} else {
					Double hpRolled = getNumericCellValue(charSheet, i, 18);
					hp += hpRolled;
				}
				hp += conMod;
				MutableInt count = classes.get(classData);
				if(count != null) {
					count.inc();
				} else {
					classes.put(classData, new MutableInt());
				}
				break;
			}
		}
		character.setHitPoints(hp);
		character.setNumHitDice(iNumHitDice);
		if(iNumHitDice > 20)
			throw new UnsupportedOperationException("Cannot accurately load characters with more than 20 HD");
		
		MutableInt druidLvl = classes.get(D20ClassData.DRUID);
		character.setDruidLvl((druidLvl == null ? 0 : druidLvl.getValue()));
		
		int iBab = 0, iFort = 0, iRef = 0, iWill = 0;
		for(D20ClassData charClass : classes.keySet()) {
			MutableInt classLvl = classes.get(charClass);
			if(classLvl != null) {	// since we're using the keyset, this is just a paranoid safety check
				int iClassLvl = classLvl.getValue();
				iBab += charClass.getBAB(iClassLvl);
				iFort += charClass.getFortSave(iClassLvl);
				iRef += charClass.getRefSave(iClassLvl);
				iWill += charClass.getWillSave(iClassLvl);
			}
		}
		character.setBaseAtkBonus(iBab);
		character.setBaseFort(iFort);
		character.setBaseRef(iRef);
		character.setBaseWill(iWill);
	}
	
	/**
	 * Figures out type and subtypes based on race id, template, etc.
	 * ... is what it should do, but I'm just using race id for now.
	 * @param character
	 * @param raceId
	 */
	protected void parseRacialInfo(Creature character, Sheet charSheet) {
		Double raceId = getNumericCellValue(charSheet, 7, 4);
		// hack, such a hack. you didn't even add the elf's skill modifiers!
		if(raceId == 3) {
			// elf
			character.setType(CreatureType.HUMANOID);
			character.setSubTypes(Arrays.asList(CreatureSubType.Elf));
			character.setSize(Size.Medium);
			Map<Ability, Integer> modifiers = new HashMap<Ability, Integer>();
			modifiers.put(Ability.DEX, 2);
			modifiers.put(Ability.CON, -2);
			mergeAbilities(character.getStats(), modifiers);
			// +2 Listen,Search,Spot
			String [] racialSkills = {"Listen", "Search", "Spot"}; 
			for(String racialName : racialSkills) {
				Skill racialSkill = character.getSkillByName(racialName);
				if(racialSkill != null) {
					racialSkill.setMiscBonusTotal(racialSkill.getMiscBonusTotal() + 2);
				}
			}
		}
	}
	
	/**
	 * Parses the character's skills using the character sheet.
	 * Handles cross-class and class skill point conversion.
	 * @param character
	 * @param charSheet
	 */
	void parseSkills(Creature character, Sheet charSheet) {
		// uhoh. Need to get what class skills are for each level so we can calculate the ranks
		// cross-class: 2 pts = 1 rank; class: 1 pt = 1 rank
		// also need to get the base ability for each skill somehow (HF save sheet doesn't have it)
	}
	
	private void mergeAbilities(Map<Ability, AbilityScore> baseScores, Map<Ability, Integer> modifiers) {
		for(Ability ability : modifiers.keySet()) {
			AbilityScore baseScore = baseScores.get(ability);
			Integer modifier = modifiers.get(ability);
			Integer finalScore = baseScore.getScore() + modifier;
			baseScores.put(ability, new AbilityScore(finalScore));
		}
	}

	private Double getNumericCellValue(Sheet sheet, int rowNum, int colNum) {
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		Double value = null;
		if(cell.getCellType() == Cell.CELL_TYPE_NUMERIC)
			value = cell.getNumericCellValue();
		return value;
	}

	private String getCellText(Sheet sheet, int rowNum, int colNum) {
		Row row = sheet.getRow(rowNum);
		Cell cell = row.getCell(colNum);
		String text = null;
		switch (cell.getCellType()) {
        case Cell.CELL_TYPE_STRING:
            text = cell.getStringCellValue();
            break;
        case Cell.CELL_TYPE_NUMERIC:
            if (DateUtil.isCellDateFormatted(cell)) {
                text = cell.getDateCellValue().toString();
            } else {
                text = Double.toString(cell.getNumericCellValue());
            }
            break;
        case Cell.CELL_TYPE_BOOLEAN:
            text = Boolean.toString(cell.getBooleanCellValue());
            break;
        case Cell.CELL_TYPE_FORMULA:
            text = cell.getCellFormula();
            break;
        default:
        	break;
		}
		return text;
	}
	
	/* HeroForge notes
	[Only support up to lv20!!]
	A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M 	N 	O 	P 	Q 	R 	S 	T 	U 	V 	W 	X 	Y 	Z
	0 	1 	2 	3 	4 	5 	6 	7 	8 	9 	10 	11 	12 	13 	14 	15 	16 	17 	18 	19 	20 	21 	22 	23 	24 	25
	Q9:Q68 class (primary)
	R9:R68 class (secondary for gestalt)
	S9:S68 # hp rolled
	E2:E7 ability scores
	E8 Race
	E9 Gender (female=2)
	E10 Alignment
	E11 Deity
	E12 Name

	L9:N13 Stat Bumps (L9-13 = 4,8,12,16,20)
	BY3:CB3322 Feats
	    BY: Feat Name
	    BZ: Selected (True = picked, False = not picked but available,
	        #N/A = not available)
	    CA: Bonus (same as selected?)
	    CB: List (for feats like Weapon Prof/Focus, Spell Focus, etc)
	D101:BN179 Skills/Points

	CD2:CE169 Languages
	    CD: Language Name
	    CE: Selected (True/False)
	T20: Animal Companion
	T22: Familiar [skill bonuses!!]
	AM9:AP66 Templates
	*/
}
