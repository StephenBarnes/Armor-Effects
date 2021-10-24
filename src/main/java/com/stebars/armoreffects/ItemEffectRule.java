package com.stebars.armoreffects;

import java.util.List;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

public class ItemEffectRule {

	public Item item;
	public List<Effect> effects;
	public List<Integer> intensities;
	public List<Integer> durations;
	public List<Boolean> hiddens;

	public ItemEffectRule(Item itemArg, List<Effect> effectsArg, List<Integer> intensitiesArg, List<Integer> durationsArg,
			List<Boolean> hiddensArg) {
		item = itemArg;
		effects = effectsArg;
		intensities = intensitiesArg;
		durations = durationsArg;
		hiddens = hiddensArg;
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