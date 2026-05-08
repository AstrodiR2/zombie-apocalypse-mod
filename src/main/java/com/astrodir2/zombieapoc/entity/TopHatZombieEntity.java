package com.astrodir2.zombieapoc.entity;

import com.astrodir2.zombieapoc.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.explosion.ExplosionBehavior;

import java.util.List;

public class TopHatZombieEntity extends ZombieEntity {

    // Таймер призову зомбі (в тіках, 20 тіків = 1 сек)
    private int summonCooldown = 0;
    private int totalSummoned = 0;
    private static final int SUMMON_COOLDOWN = 20 * 60 * 3; // 3 хвилини
    private static final int MAX_SUMMONS = 5;

    // Чи хоче він призвати зомбі (рандомно вирішується)
    private boolean wantsToSummon = false;

    public TopHatZombieEntity(EntityType<? extends ZombieEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return ZombieEntity.createZombieAttributes()
                // 1.5x здоров'я (20 * 1.5 = 30)
                .add(EntityAttributes.MAX_HEALTH, 30.0)
                // 1.3x швидкість
                .add(EntityAttributes.MOVEMENT_SPEED, 0.26f)
                // Трохи сильніший удар
                .add(EntityAttributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        // Додаємо навігацію навколо перешкод - вже є в базовому ZombieEntity
    }

    @Override
    public void tick() {
        super.tick();

        if (!this.getWorld().isClient && this.isAlive()) {
            // Зменшуємо кулдаун
            if (summonCooldown > 0) summonCooldown--;

            // Рандомно вирішує чи хоче призвати
            if (summonCooldown == 0 && totalSummoned < MAX_SUMMONS) {
                // 30% шанс що він "захоче" призвати
                if (this.getRandom().nextInt(100) < 30) {
                    wantsToSummon = true;
                }
            }

            // Якщо хоче призвати — чекає поки гравець відвернеться
            if (wantsToSummon && summonCooldown == 0) {
                PlayerEntity target = this.getWorld().getClosestPlayer(this, 32);
                if (target != null && isPlayerLookingAway(target)) {
                    summonZombieHorde();
                    wantsToSummon = false;
                    summonCooldown = SUMMON_COOLDOWN;
                }
            }
        }
    }

    // Перевіряємо чи гравець відвернувся від зомбі
    private boolean isPlayerLookingAway(PlayerEntity player) {
        Vec3d playerLook = player.getRotationVec(1.0f);
        Vec3d toZombie = this.getPos().subtract(player.getPos()).normalize();
        // Скалярний добуток: якщо < 0, гравець дивиться в інший бік
        double dot = playerLook.dotProduct(toZombie);
        return dot < -0.3;
    }

    // Призиваємо зомбі
    private void summonZombieHorde() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;

        int toSummon = Math.min(
                this.getRandom().nextInt(3) + 1, // 1-3 зомбі за раз
                MAX_SUMMONS - totalSummoned
        );

        for (int i = 0; i < toSummon; i++) {
            ZombieEntity zombie = EntityType.ZOMBIE.create(serverWorld);
            if (zombie == null) continue;

            // Спавнимо навколо зомбі зі шляпою
            double offsetX = (this.getRandom().nextDouble() - 0.5) * 6;
            double offsetZ = (this.getRandom().nextDouble() - 0.5) * 6;
            BlockPos spawnPos = this.getBlockPos().add((int)offsetX, 0, (int)offsetZ);

            zombie.refreshPositionAndAngles(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ(), 0, 0);

            // Спрямовуємо на поточну ціль
            if (this.getTarget() != null) {
                zombie.setTarget((LivingEntity) this.getTarget());
            }

            serverWorld.spawnEntity(zombie);
            totalSummoned++;
        }
    }

    // Вибух при смерті + дроп шляпи
    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);

        if (!this.getWorld().isClient) {
            // Вибух (сила 2, не руйнує блоки)
            this.getWorld().createExplosion(
                    this,
                    this.getX(), this.getY(), this.getZ(),
                    2.0f,
                    false,
                    World.ExplosionSourceType.NONE
            );
        }
    }

    // Дроп шляпи
    @Override
    protected void dropLoot(ServerWorld world, DamageSource damageSource, boolean causedByPlayer) {
        super.dropLoot(world, damageSource, causedByPlayer);
        this.dropItem(world, ModItems.TOP_HAT);
    }
}
