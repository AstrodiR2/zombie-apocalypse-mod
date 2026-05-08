package com.astrodir2.zombieapoc.entity;

import com.astrodir2.zombieapoc.ZombieApocMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<TopHatZombieEntity> TOP_HAT_ZOMBIE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(ZombieApocMod.MOD_ID, "top_hat_zombie"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, TopHatZombieEntity::new)
                    // x2 розмір від звичайного зомбі (0.6 * 2 = 1.2, 1.95 * 2 = 3.9)
                    .dimensions(EntityDimensions.fixed(1.2f, 3.9f))
                    .build()
    );

    public static void register() {
        // Реєструємо спавн тільки вночі
        net.fabricmc.fabric.api.biome.v1.BiomeModifications.addSpawn(
                net.fabricmc.fabric.api.biome.v1.BiomeSelectors.foundInOverworld(),
                SpawnGroup.MONSTER,
                TOP_HAT_ZOMBIE,
                5,  // вага (рідкість) - менше ніж звичайний зомбі
                1,  // мін група
                3   // макс група
        );
    }
}
