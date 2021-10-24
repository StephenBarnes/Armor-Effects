package com.stebars.armoreffects;

import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class SetEffectRule {

	public Set<Item> items;
	public List<Effect> effects;
	public List<Integer> intensities;
	public List<Integer> durations;
	public List<Boolean> hiddens;

	public SetEffectRule(Set<Item> itemsArg, List<Effect> effectsArg, List<Integer> intensitiesArg, List<Integer> durationsArg,
			List<Boolean> hiddensArg) {
		items = itemsArg;
		effects = effectsArg;
		intensities = intensitiesArg;
		durations = durationsArg;
		hiddens = hiddensArg;
	}
	
	public boolean appliesTo(Iterable<ItemStack> slots) {
		int count = 0;
		for (ItemStack stack: slots) {
			if (items.contains(stack.getItem()))
				count++;
		}
		return (count == items.size());
	}

	public void apply(PlayerEntity player) {
		for (int i = 0; i < effects.size(); i++) {
			boolean show = !hiddens.get(i);
			player.addEffect(new EffectInstance(effects.get(i), durations.get(i), intensities.get(i) - 1,
					false, show, show));
			// args: effect, duration, amplifier, ambient, visible, showIcon
		}
	}
}