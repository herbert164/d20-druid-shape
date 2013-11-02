package com.tempestsoul.dnd.service;

import java.io.File;
import java.net.URL;
import com.tempestsoul.dnd.d20.Creature;

public class HeroForgeLoaderTest {
	HeroForgeLoadService service = new HeroForgeLoadService();
	
	public void test() {
		URL testFileUrl = this.getClass().getResource("Nimue  (Lvl 8)_v6.1.0.1.b2.14(D&D 3.5).hfg");
		File testFile = new File(testFileUrl.getFile());
		Creature nimue = service.loadCharacter(testFile);
		// TODO create tests
	}
}
