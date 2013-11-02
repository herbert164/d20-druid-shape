package com.tempestsoul.dnd.service;

import java.io.File;
import com.tempestsoul.dnd.d20.Creature;

public interface CharacterLoadService {
	
	public Creature loadCharacter(File file);
}
