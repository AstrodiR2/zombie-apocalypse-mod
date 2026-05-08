package com.astrodir2.zombieapoc.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BandageItem extends Item {
    public BandageItem(Settings settings) { super(settings); }

    @Override
    public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.INSTANT_HEALTH, 1, 1));
            stack.decrement(1);
        }
        return stack;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, net.minecraft.world.World world) { return 32; }

    @Override
    public net.minecraft.util.UseAction getUseAction(ItemStack stack) {
        return net.minecraft.util.UseAction.EAT;
    }

    @Override
    public net.minecraft.util.TypedActionResult<ItemStack> use(World world, PlayerEntity user, net.minecraft.util.Hand hand) {
        user.setCurrentHand(hand);
        return net.minecraft.util.TypedActionResult.consume(user.getStackInHand(hand));
    }
}
