package net.notion.avatar_pack.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.notion.avatar_pack.client.renderer.BoomerangEntityRenderer;
import net.notion.avatar_pack.entity.ModEntityTypes;

public class Avatar_packClient implements ClientModInitializer {
    
    @Override
    public void onInitializeClient() {
        // Реєструємо рендерер для бумеранга
        EntityRendererRegistry.register(ModEntityTypes.BOOMERANG_ENTITY, BoomerangEntityRenderer::new);
    }
}