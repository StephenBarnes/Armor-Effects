package com.stebars.armoreffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;


@EventBusSubscriber
public class ModEventHandler {

	@SubscribeEvent
	public static void checkArmor(final PlayerTickEvent event) {		
		PlayerEntity player = event.player;
		if (player.tickCount % ConfigParser.applyEveryNTicks != 0) return; // TODO use config value
		
		applyAllArmorEffects(player);
	}
	
	public static void applyAllArmorEffects(PlayerEntity player) {
		Iterable<ItemStack> slots = player.getArmorSlots();
		for (ItemStack slot : slots) {
			if (slot.isEmpty()) continue;
			ItemEffectRule itemRule = ConfigParser.getItemEffectRule(slot.getItem());
			if (itemRule != null)
				itemRule.apply(player);
		}
		
		for (SetEffectRule setRule: ConfigParser.setEffectRules) {
			if (setRule.appliesTo(slots))
				setRule.apply(player);
		}
	}

}