package com.stebars.armoreffects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
		fetchItemEffectRules();
		fetchSetEffectRules();
	}

	public static ItemEffectRule getItemEffectRule(Item item) {
		return itemEffectRules.get(item); // TODO get rid of this function, if it still works with modded armor
	}

	private static void fetchItemEffectRules() {
		// TODO if modded items don't work, maybe set item effect rules to null, then fetch here if it's null
		itemEffectRules = new HashMap<Item, ItemEffectRule>();
		List<? extends String> ruleStrings = ConfigDefinition.COMMON.itemEffects.get();
		for (Object ruleStringObj : ruleStrings) {
			try {
				String ruleString = (String) ruleStringObj;
				String[] parts = ruleString.split("=");
				if (parts.length != 2)
					throw new RuntimeException("Need 2 parts separated by = sign: " + ruleString);

				String[] itemParts = parts[0].split(":");
				if (itemParts.length != 2)
					throw new RuntimeException("Item ID does not make sense: " + parts[0]);
				Item armorItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemParts[0], itemParts[1]));
				// Forge docs for 1.16 say to use this, which does not work:
				// RegistryObject.of(new ResourceLocation(itemParts[0], itemParts[1]), ForgeRegistries.ITEMS).get();

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
					hiddens.add(effectParts.length >= 4 ? (Integer.parseInt(effectParts[2]) != 0) : true);
				}

				if (effects.size() == 0) continue;
				if (itemEffectRules.containsKey(armorItem)) {
					Log.warn("Armor Effects: multiple effect lists for this armor item, will use both: ", armorItem);
					ItemEffectRule rule = itemEffectRules.get(armorItem);
					rule.effects.addAll(effects);
					rule.intensities.addAll(intensities);
					rule.hiddens.addAll(hiddens);
				} else
					itemEffectRules.put(armorItem, new ItemEffectRule(armorItem, effects, intensities, durations, hiddens));
			} catch (Exception e) {
				Log.error("Armor Effects mod could not understand your item rule, skipping: ", ruleStringObj);
				e.printStackTrace();
			}

			// TODO test all this with some modded armor, since the registration happens at a different time
		}
	}

	private static void fetchSetEffectRules() {
		// TODO if modded items don't work, maybe set the set effect rules to null, then fetch here if it's null
		setEffectRules = new ArrayList<SetEffectRule>();
		List<? extends String> ruleStrings = ConfigDefinition.COMMON.setEffects.get();
		for (Object ruleStringObj : ruleStrings) {
			try {
				String ruleString = (String) ruleStringObj;
				String[] parts = ruleString.split("=");
				if (parts.length != 2)
					throw new RuntimeException("Need 2 parts separated by = sign: " + ruleString);

				Set<Item> items = new HashSet<Item>();
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
					durations.add(effectParts.length >= 3 ? Integer.parseInt(effectParts[2]) : 1);
					hiddens.add(effectParts.length >= 4 ? (Integer.parseInt(effectParts[3]) != 0) : true);
				}

				if (effects.size() != 0)
					setEffectRules.add(new SetEffectRule(items, effects, intensities, durations, hiddens));
			} catch (Exception e) {
				Log.error("Armor Effects mod could not understand your armor-set rule, skipping: ", ruleStringObj);
				e.printStackTrace();
			}

			// TODO test all this with some modded armor, since the registration happens at a different time
		}
	}

}