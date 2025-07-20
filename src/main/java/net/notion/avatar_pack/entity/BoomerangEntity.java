package net.notion.avatar_pack.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.notion.avatar_pack.item.ModItems;

public class BoomerangEntity extends ThrownItemEntity {
    private static final int MAX_FLIGHT_TIME = 200; // 10 секунд при 20 TPS
    private int flightTime = 0;
    private int state = 0; // 0 = летить вперед, 1 = крутиться на місці, 2 = повертається
    public PlayerEntity thrower;

    public BoomerangEntity(EntityType<? extends ThrownItemEntity> entityType, World world) {
        super(entityType, world);
    }

    public BoomerangEntity(World world, LivingEntity owner) {
        super(ModEntityTypes.BOOMERANG_ENTITY, owner, world);
        if (owner instanceof PlayerEntity) {
            this.thrower = (PlayerEntity) owner;
        }
        // Встановлюємо гравітацію для правильної траєкторії
        this.setNoGravity(false);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.BOOMERANG_SOKKA_ITEM;
    }

    @Override
    public void tick() {
        flightTime++;

        // Дебаг повідомлення кожні 20 тіків (1 секунда)
        if (flightTime % 20 == 0) {
            System.out.println("Boomerang tick - State: " + state + ", FlightTime: " + flightTime);
        }

        switch (state) {
            case 0: // Летить вперед
                super.tick();
                break;
                
            case 1: // Крутиться на місці
                this.setNoGravity(true);
                this.setVelocity(0, 0, 0); // Зупиняємо рух
                damageNearbyEntities(); // Дамажимо навколо
                break;
                
            case 2: // Повертається до гравця
                if (thrower != null && !thrower.isRemoved()) {
                    this.setNoGravity(true);
                    Vec3d playerPos = thrower.getPos().add(0, 1.0, 0);
                    Vec3d direction = playerPos.subtract(this.getPos());
                    double distance = direction.length();
                    
                    if (distance < 1.5) {
                        returnToPlayer();
                        return;
                    }
                    
                    Vec3d velocity = direction.normalize().multiply(0.5);
                    this.setVelocity(velocity);
                } else {
                    this.discard();
                }
                break;
        }

        // Автоматичне повернення через час
        if (flightTime > MAX_FLIGHT_TIME) {
            returnToPlayer();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();

        // Якщо вдарив власника при поверненні - повертаємо предмет
        if (entity == this.getOwner() && state == 2) {
            returnToPlayer();
            return;
        }

        // Не вражаємо власника в інших станах
        if (entity == this.getOwner()) {
            return;
        }

        // Завдаємо шкоди іншим сутностям
        if (entity instanceof LivingEntity) {
            DamageSource damageSource = this.getDamageSources().thrown(this, this.getOwner());
            entity.damage(damageSource, 6.0f);
        }
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        // При зіткненні з блоком не робимо нічого особливого
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            super.onCollision(hitResult);
        }
    }

    // Метод для зміни стану бумеранга (викликається при ПКМ)
    public void changeState() {
        if (state < 2) {
            state++;
            System.out.println("Boomerang state changed to: " + state);
            
            // Додаємо звук зміни стану
            this.getWorld().playSound(null, this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ITEM_TRIDENT_RIPTIDE_1, SoundCategory.NEUTRAL, 0.5f, 1.2f);
        }
    }
    
    // Дамажимо сутності навколо бумеранга
    private void damageNearbyEntities() {
        if (this.getWorld().getTime() % 10 == 0) { // Кожні 0.5 секунди
            for (Entity entity : this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(2.0))) {
                if (entity instanceof LivingEntity && entity != this.getOwner()) {
                    DamageSource damageSource = this.getDamageSources().thrown(this, this.getOwner());
                    entity.damage(damageSource, 4.0f);
                }
            }
        }
    }

    private void returnToPlayer() {
        if (thrower != null && !thrower.isRemoved()) {
            // Повертаємо предмет в інвентар
            ItemStack boomerang = new ItemStack(ModItems.BOOMERANG_SOKKA_ITEM);
            if (!thrower.getInventory().insertStack(boomerang)) {
                // Якщо інвентар повний, кидаємо на землю біля гравця
                thrower.dropItem(boomerang, false);
            }

            // Звук повернення
            this.getWorld().playSound(null, thrower.getX(), thrower.getY(), thrower.getZ(),
                    SoundEvents.ITEM_TRIDENT_RETURN, SoundCategory.NEUTRAL, 1.0f, 1.0f);
        }

        this.discard();
    }
}