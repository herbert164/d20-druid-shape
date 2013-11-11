package com.tempestsoul.dnd.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import com.tempestsoul.dnd.d20.Creature;
import com.tempestsoul.dnd.d20.CreatureType;
import com.tempestsoul.dnd.d20.Size;

public class HeroForgeLoaderTest {

	HeroForgeLoadService service = new HeroForgeLoadService();

	@Test
	public void testLoadCharacter() {
		File testFile = new File("src/test/resources/Nimue  (Lvl 8)_v6.1.0.1.b2.14(D&D 3.5).hfg");
		assertTrue("Test file does not exist", testFile.exists());
		Creature nimue = service.loadCharacter(testFile);
		
		assertNotNull("Did not build character", nimue);
		assertEquals("Nimue ", nimue.getName());	// no, I don't know why she put an extra space in there
		assertEquals(8, nimue.getDruidLvl());
		assertEquals(Size.Medium, nimue.getSize());
		assertEquals(CreatureType.HUMANOID, nimue.getType());
		assertNotNull(nimue.getStats());
		//assertEquals(nimue.getHitPoints());
		assertEquals(6, nimue.getBaseAtkBonus());
		// TODO create tests (expected, actual)
	}
}
