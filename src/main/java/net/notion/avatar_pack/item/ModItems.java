package net.notion.avatar_pack.item;

import java.util.function.Function;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.notion.avatar_pack.Avatar_pack;

public class ModItems {

    // Реєстрація айтемів
    public static final Item STAFF_STORM = register("staff_storm_3d", 
    StaffStormSettings::new, new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        Identifier itemId = Identifier.of(Avatar_pack.MOD_ID, name);

        Item item = itemFactory.apply(settings);

        return Registry.register(Registries.ITEM, itemId, item);
    }

    public static void registerModItems() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((entries) -> {
                    entries.add(ModItems.STAFF_STORM);
                });
    }
}