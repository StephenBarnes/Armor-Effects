package com.stebars.armoreffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jline.utils.Log;

import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

public class ConfigParser {
	// Fetches config values the first time they're needed. This is so we don't re-parse strings etc. every time.

	public static Map<Item, ItemEffectRule> itemEffectRules = null;
	public static List<SetEffectRule> setEffectRules = null;
	public static Integer applyEveryNTicks = null;

	public static int DEFAULT_DURATION = 200;

	public static void loadAll() {
		applyEveryNTicks = ConfigDefinition.COMMON.applyEveryNTicks.get();
		fetchEffectRules();
	}

	public static ItemEffectRule getItemEffectRule(Item item) {
		return itemEffectRules.get(item); // TODO get rid of this function, if it still works with modded armor
	}

	private static void fetchEffectRules() {
		// TODO if modded items don't work, maybe set the set effect rules to null, then fetch here if it's null
		setEffectRules = new ArrayList<SetEffectRule>();
		itemEffectRules = new HashMap<Item, ItemEffectRule>();
		List<? extends String> ruleStrings = ConfigDefinition.COMMON.effectRules.get();
		for (Object ruleStringObj : ruleStrings) {
			try {
				String ruleString = (String) ruleStringObj;
				String[] parts = ruleString.split("=");
				if (parts.length != 2)
					throw new RuntimeException("Need 2 parts separated by = sign: " + ruleString);

				List<Item> items = new ArrayList<Item>();
				for (String itemString: parts[0].split("\\+")) {
					String[] itemParts = itemString.split(":");
					if (itemParts.length != 2)
						throw new RuntimeException("Item ID does not make sense: " + parts[0]);
					items.add(ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemParts[0], itemParts[1])));
				}

				List<Effect> effects = new ArrayList<Effect>();
				List<Integer> intensities = new ArrayList<Integer>();
				List<Integer> durations = new ArrayList<Integer>();
				List<Boolean> hiddens = new ArrayList<Boolean>();
				for (String effectString: parts[1].split("\\+")) {
					String[] effectParts = effectString.split("/");
					if (effectParts.length == 0 || effectParts.length > 4)
						throw new RuntimeException("Effect does not make sense, must be 1-4 parts separated by '/': " + parts[1]);
					String[] effectNameParts = effectParts[0].split(":");
					if (effectNameParts.length != 2)
						throw new RuntimeException("Effect ID does not make sense: " + effectParts[0]);
					effects.add(ForgeRegistries.POTIONS.getValue(new ResourceLocation(effectNameParts[0], effectNameParts[1])));
					// TODO check this works for effects without potions, e.g. dolphin's grace.
					// TODO check this works for modded effects
					intensities.add(effectParts.length >= 2 ? Integer.parseInt(effectParts[1]) : 1);
					durations.add(effectParts.length >= 3 ? Integer.parseInt(effectParts[2]) : DEFAULT_DURATION);
					hiddens.add(effectParts.length >= 4 ? (Integer.parseInt(effectParts[3]) != 0) : true);
				}

				if (effects.size() == 0)
					continue;
				if (effects.size() == 1) {
					Item armorItem = items.get(0);
					if (itemEffectRules.containsKey(armorItem)) {
						Log.warn("Armor Effects: multiple effect lists for this armor item, will use both: ", armorItem);
						ItemEffectRule rule = itemEffectRules.get(armorItem);
						rule.effects.addAll(effects);
						rule.intensities.addAll(intensities);
						rule.hiddens.addAll(hiddens);
					} else
						itemEffectRules.put(armorItem, new ItemEffectRule(armorItem, effects, intensities, durations, hiddens));
				} else
					setEffectRules.add(new SetEffectRule(new HashSet<Item>(items), effects, intensities, durations, hiddens));
			} catch (Exception e) {
				Log.error("Armor Effects mod could not understand your armor-set rule, skipping: ", ruleStringObj);
				e.printStackTrace();
			}

			// TODO test all this with some modded armor, since the registration happens at a different time
		}
	}

}