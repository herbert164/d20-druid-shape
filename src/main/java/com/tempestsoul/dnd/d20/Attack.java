package com.tempestsoul.dnd.d20;

import java.util.EnumSet;

public class Attack {
	boolean primaryAtk;
	String sWeapon;
	int iNumber;	// can only use one, unless full attacking
	EnumSet<DamageType> damageTypes;
	String dmgDie;
	Critical critData;
	
	enum DamageType {
		Slashing, Bludgeoning, Piercing;
	}
	class Critical {
		short multiplier;
		short minRange;
		short maxRange;
		
		public Critical() {
			multiplier = 2;
			minRange = 20;
			maxRange = 20;
		}
		
		public Critical(short minRange, short maxRange, short multiplier) {
			this.minRange = minRange;
			this.maxRange = maxRange;
			this.multiplier = multiplier;
		}
		
		@Override
		public String toString() {
			String str = "";
			if(minRange == maxRange)
				str += minRange;
			else
				str += minRange + "-" + maxRange;
			str += "/x" + multiplier;
			return str;
		}
	}
}
