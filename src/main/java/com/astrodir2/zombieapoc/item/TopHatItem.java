package com.astrodir2.zombieapoc.item;

import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TopHatItem extends Item {

    public TopHatItem(Settings settings) {
        super(settings);
    }

    // Кожен тік перевіряємо чи шляпа в руці гравця
    @Override
    public void inventoryTick(ItemStack stack, World world, net.minecraft.entity.Entity entity, int slot, boolean selected) {
        if (!world.isClient && entity instanceof PlayerEntity player) {
            // Перевіряємо чи шляпа одягнута в слот голови
            ItemStack helmet = player.getInventory().getArmorStack(3);
            if (helmet.getItem() == this) {
                // Speed I
                if (!player.hasStatusEffect(StatusEffects.SPEED)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, 40, 0, false, false, true));
                }
                // Regeneration I
                if (!player.hasStatusEffect(StatusEffects.REGENERATION)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 40, 0, false, false, true));
                }
                // +4 серця (Max Health boost)
                if (!player.hasStatusEffect(StatusEffects.HEALTH_BOOST)) {
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.HEALTH_BOOST, 40, 1, false, false, true));
                }
            }
        }
    }

    // Дозволяємо одягати як шолом
    @Override
    public net.minecraft.item.equipment.EquipmentType getEquipmentType() {
        return net.minecraft.item.equipment.EquipmentType.HELMET;
    }
}
