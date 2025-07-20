package net.notion.avatar_pack.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import net.notion.avatar_pack.entity.BoomerangEntity;

public class BoomerangSokkaSettings extends Item {

    public BoomerangSokkaSettings(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);

        if (!world.isClient) {
            // Шукаємо вже кинутий бумеранг цього гравця
            BoomerangEntity existingBoomerang = null;

            // Спочатку шукаємо всі бумеранги в світі
            for (BoomerangEntity boomerang : world.getEntitiesByClass(BoomerangEntity.class,
                    user.getBoundingBox().expand(100), entity -> true)) {
                System.out.println("Found boomerang entity, checking owner...");
                System.out.println("Boomerang owner: " + boomerang.getOwner());
                System.out.println("Current user: " + user);

                // Перевіряємо власника через thrower замість getOwner()
                if (boomerang.thrower == user) {
                    existingBoomerang = boomerang;
                    System.out.println("Match found via thrower!");
                    break;
                }

                // Також перевіряємо через getOwner()
                if (boomerang.getOwner() == user) {
                    existingBoomerang = boomerang;
                    System.out.println("Match found via getOwner()!");
                    break;
                }
            }

            if (existingBoomerang != null) {
                // Якщо бумеранг вже існує - змінюємо його стан
                System.out.println("Found existing boomerang, changing state");
                existingBoomerang.changeState();
            } else {
                // Якщо бумеранга немає - кидаємо новий
                System.out.println("No existing boomerang found, throwing new one");
                BoomerangEntity boomerang = new BoomerangEntity(world, user);
                boomerang.setPosition(user.getX(), user.getEyeY() - 0.1, user.getZ());
                boomerang.setVelocity(user, user.getPitch(), user.getYaw(), 0.0f, 1.5f, 1.0f);
                world.spawnEntity(boomerang);

                // Звук кидання
                world.playSound(null, user.getX(), user.getY(), user.getZ(),
                        SoundEvents.ITEM_TRIDENT_THROW, SoundCategory.NEUTRAL, 0.5f, 0.4f);

                // Зменшуємо кількість предметів тільки при кидку нового бумеранга
                if (!user.getAbilities().creativeMode) {
                    itemStack.decrement(1);
                }
            }
        }

        // Статистика
        user.incrementStat(Stats.USED.getOrCreateStat(this));

        return TypedActionResult.success(itemStack, world.isClient());
    }
}
