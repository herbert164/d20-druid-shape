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

import com.tempestsoul.dnd.d20.Ability;
import com.tempestsoul.dnd.d20.AbilityScore;
import com.tempestsoul.dnd.d20.Creature;
import com.tempestsoul.dnd.d20.CreatureSubType;
import com.tempestsoul.dnd.d20.CreatureType;
import com.tempestsoul.dnd.d20.Size;

public class HeroForgeLoadService implements CharacterLoadService {

/* HeroForge notes
[Only support up to lv20!!]
A 	B 	C 	D 	E 	F 	G 	H 	I 	J 	K 	L 	M 	N 	O 	P 	Q 	R 	S 	T 	U 	V 	W 	X 	Y 	Z
1 	2 	3 	4 	5 	6 	7 	8 	9 	10 	11 	12 	13 	14 	15 	16 	17 	18 	19 	20 	21 	22 	23 	24 	25 	26
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
	// HeroForge constants
	private static final int DRUID_CLASS_ID = 5;
	private static final Ability[] abilities = new Ability[] {Ability.STR, Ability.DEX, Ability.CON, Ability.INT, Ability.WIS, Ability.CHA};
	
	@Override
	public Creature loadCharacter(File file) {
		Creature character = new Creature();
		Map<Ability, AbilityScore> stats = new HashMap<Ability, AbilityScore>();
		character.setStats(stats);
		
		try {
			Workbook workbook = WorkbookFactory.create(file);
			Sheet charSheet = workbook.getSheet("ExportSheet");
			
			String name = getCellText(charSheet, 11, 4);
			character.setName(name);

			// parse ability scores
			for(int i = 0; i < abilities.length; i++) {
				Double score = getNumericCellValue(charSheet, i+1, 4);
				stats.put(abilities[i], new AbilityScore(score.intValue()));
			}
			
			// Type/Subtype, Size: by race, template
			Double raceNum = getNumericCellValue(charSheet, 7, 4);
			setCharacterTypeInfo(character, raceNum);
			
			// TODO implement
			// Parse classes (gets druid level, BAB, # hit dice, base saves, etc)
			int hp = 0;
			int druidLvl = 0;
			for(int i = 8; i < 68; i++) {
				Double classId = getNumericCellValue(charSheet, i, 16);
				if(classId == DRUID_CLASS_ID) {
					druidLvl++;
				} else if(classId == 1) {
					break;	// 1 = no class
				}
				
				if(i == 8) {
					// TODO get max hp of hit die for classId
					hp += 8;	// default to druid for now
				} else {
					Double hpRolled = getNumericCellValue(charSheet, i, 17);
					hp += hpRolled;
				}
				hp += stats.get(Ability.CON).getModifier();
			}
			character.setHitPoints(hp);
			character.setDruidLvl(druidLvl);
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
	 * Figures out type and subtypes based on race id, template, etc.
	 * ... is what it should do, but I'm just using race id for now.
	 * @param character
	 * @param raceId
	 */
	private void setCharacterTypeInfo(Creature character, Double raceId) {
		// TODO Auto-generated method stub
		if(raceId == 3) {
			// elf
			character.setType(CreatureType.HUMANOID);
			character.setSubTypes(Arrays.asList(CreatureSubType.Elf));
			character.setSize(Size.Medium);
			Map<Ability, Integer> modifiers = new HashMap<Ability, Integer>();
			modifiers.put(Ability.DEX, 2);
			modifiers.put(Ability.CON, -2);
			mergeAbilities(character.getStats(), modifiers);
		}
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

}
