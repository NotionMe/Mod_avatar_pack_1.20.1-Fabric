package net.notion.avatar_pack;

import net.fabricmc.api.ModInitializer;
import net.notion.avatar_pack.entity.ModEntityTypes;
import net.notion.avatar_pack.item.ModItems;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Avatar_pack implements ModInitializer {
	public static final String MOD_ID = "avatar_pack";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModEntityTypes.registerEntityTypes();
	}
}