package net.notion.avatar_pack.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class StaffStormSettings extends Item {

    public StaffStormSettings(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        if (!world.isClient) {
            // 10 Block
            player.addVelocity(0, 2.0, 0);
            player.velocityModified = true;

            // Effect SLOW_FALLING 
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOW_FALLING, 100, 0));

            // Effect player
            if (world instanceof ServerWorld serverWorld) {

                // Wind particles around the player
                serverWorld.spawnParticles(ParticleTypes.CLOUD,
                        player.getX(), player.getY(), player.getZ(),
                        30, 2.0, 1.0, 2.0, 0.1);

                // Dust particles swirling
                serverWorld.spawnParticles(ParticleTypes.SWEEP_ATTACK,
                        player.getX(), player.getY(), player.getZ(),
                        15, 1.5, 0.5, 1.5, 0.2);

                // Wind effect
                serverWorld.spawnParticles(ParticleTypes.POOF,
                        player.getX(), player.getY(), player.getZ(),
                        20, 1.0, 0.2, 1.0, 0.15);
            }

            // Wind sound
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ITEM_ELYTRA_FLYING, SoundCategory.PLAYERS, 1.0F, 0.8F);

            // Magic sound
            world.playSound(null, player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.PLAYERS, 0.5F, 1.5F);

            player.getItemCooldownManager().set(this, 100); // 5 секунд кулдауну
        }

        return TypedActionResult.success(player.getStackInHand(hand));
    }
}
