package com.tempestsoul.dnd.service;

import java.io.File;
import com.tempestsoul.dnd.d20.D20Character;

public interface CharacterLoadService {
	
	public D20Character loadCharacter(File file);
}
