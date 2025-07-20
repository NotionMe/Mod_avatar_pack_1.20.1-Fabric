package net.notion.avatar_pack.entity;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.notion.avatar_pack.Avatar_pack;

public class ModEntityTypes {
    
    public static final EntityType<BoomerangEntity> BOOMERANG_ENTITY = Registry.register(
        Registries.ENTITY_TYPE,
        new Identifier(Avatar_pack.MOD_ID, "boomerang"),
        FabricEntityTypeBuilder.<BoomerangEntity>create(SpawnGroup.MISC, BoomerangEntity::new)
            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
            .trackRangeBlocks(64)
            .trackedUpdateRate(10)
            .build()
    );
    
    public static void registerEntityTypes() {
        // Метод для ініціалізації класу
    }
}