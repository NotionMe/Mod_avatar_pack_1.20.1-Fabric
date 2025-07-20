package net.notion.avatar_pack.client.renderer;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.notion.avatar_pack.entity.BoomerangEntity;
import net.notion.avatar_pack.item.ModItems;

public class BoomerangEntityRenderer extends EntityRenderer<BoomerangEntity> {
    private final ItemRenderer itemRenderer;

    public BoomerangEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(BoomerangEntity entity, float yaw, float tickDelta, MatrixStack matrices,
            VertexConsumerProvider vertexConsumers, int light) {
        matrices.push();

        // Обертання бумеранга по Z осі (як справжній бумеранг)
        float rotation = (entity.age + tickDelta) * 20.0f;
        matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(rotation));

        // Масштабування
        matrices.scale(0.5f, 0.5f, 0.5f);

        // Рендеримо як предмет
        ItemStack itemStack = new ItemStack(ModItems.BOOMERANG_SOKKA_ITEM);
        this.itemRenderer.renderItem(itemStack, ModelTransformationMode.GROUND, light,
                OverlayTexture.DEFAULT_UV, matrices, vertexConsumers, entity.getWorld(), entity.getId());

        matrices.pop();
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);
    }

    @Override
    public Identifier getTexture(BoomerangEntity entity) {
        return new Identifier("minecraft", "textures/item/stick.png"); // Тимчасова текстура
    }
}