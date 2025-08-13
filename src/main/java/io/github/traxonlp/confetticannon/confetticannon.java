package io.github.traxonlp.confetticannon;

import io.github.traxonlp.confetticannon.Item.ModItems;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class confetticannon implements ModInitializer {
	public static final String ID = "confetti_cannon";
	public static final Logger LOGGER = LoggerFactory.getLogger(ID);

	@Override
	public void onInitialize() {
		//LOGGER.info("[Mod ID] pretty pink princess ponies prancing perpendicular");
		ModItems.registerModItems();
	}
}
