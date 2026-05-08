package com.astrodir2.zombieapoc.item;

import com.astrodir2.zombieapoc.ZombieApocMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item CANNED_FOOD = new Item(new Item.Settings()
            .food(new FoodComponent.Builder()
                    .nutrition(10)
                    .saturationModifier(10.0f)
                    .build()));

    public static final Item MEDKIT = new MedkitItem(new Item.Settings().maxCount(1));

    public static final Item BANDAGE = new BandageItem(new Item.Settings().maxCount(16));

    public static final Item TOP_HAT = new TopHatItem(new Item.Settings().maxCount(1));

    public static final RegistryKey<ItemGroup> ZOMBIE_APOC_GROUP = RegistryKey.of(
            RegistryKeys.ITEM_GROUP,
            Identifier.of(ZombieApocMod.MOD_ID, "main")
    );

    public static void register() {
        Registry.register(Registries.ITEM, Identifier.of(ZombieApocMod.MOD_ID, "canned_food"), CANNED_FOOD);
        Registry.register(Registries.ITEM, Identifier.of(ZombieApocMod.MOD_ID, "medkit"), MEDKIT);
        Registry.register(Registries.ITEM, Identifier.of(ZombieApocMod.MOD_ID, "bandage"), BANDAGE);
        Registry.register(Registries.ITEM, Identifier.of(ZombieApocMod.MOD_ID, "top_hat"), TOP_HAT);

        Registry.register(Registries.ITEM_GROUP, ZOMBIE_APOC_GROUP, FabricItemGroup.builder()
                .icon(() -> new ItemStack(TOP_HAT))
                .displayName(Text.translatable("itemGroup.zombieapoc.main"))
                .entries((context, entries) -> {
                    entries.add(CANNED_FOOD);
                    entries.add(MEDKIT);
                    entries.add(BANDAGE);
                    entries.add(TOP_HAT);
                })
                .build());
    }
}
