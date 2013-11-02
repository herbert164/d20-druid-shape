package com.tempestsoul.dnd.service;

import java.io.File;
import java.net.URL;
import com.tempestsoul.dnd.d20.D20Character;

public class HeroForgeLoaderTest {
	HeroForgeLoadService service = new HeroForgeLoadService();
	
	public void test() {
		URL testFileUrl = this.getClass().getResource("Nimue  (Lvl 8)_v6.1.0.1.b2.14(D&D 3.5).hfg");
		File testFile = new File(testFileUrl.getFile());
		D20Character nimue = service.loadCharacter(testFile);
		// TODO create tests
	}
}
