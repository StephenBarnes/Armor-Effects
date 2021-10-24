package com.stebars.armoreffects;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;


public class ConfigDefinition {
	public static class Common {	

		public ConfigValue<List<? extends String>> setEffects;
		public ConfigValue<List<? extends String>> itemEffects;
		public ConfigValue<Integer> applyEveryNTicks;

		public Common(ForgeConfigSpec.Builder builder) {
			setEffects = builder.comment("Sets of armor items that have effects when all are worn at the same time.") // TODO explain format
					.defineList("setEffects", Arrays.asList(
							"minecraft:diamond_helmet+minecraft:diamond_chestplate+minecraft:diamond_leggings+minecraft:diamond_boots=minecraft:night_vision/1/400/0"
							),
							o -> o instanceof String);
			itemEffects = builder.comment("Individual armor items that have effects when worn.") // TODO explain format
					.defineList("itemEffects", Arrays.asList(
							"minecraft:golden_helmet=minecraft:haste/1+minecraft:speed/2/100/0",
							"minecraft:iron_helmet=minecraft:poison/2/800"
							),
							o -> o instanceof String);
			applyEveryNTicks = builder.comment("Armor effects will be checked and re-applied every [this number] ticks. "
					+ "They will also be applied instantly when the armor is put on.") // TODO apply instantly when armor is put on
					.define("applyEveryNTicks", 20);
		}
	}

	public static final Common COMMON;
	public static final ForgeConfigSpec COMMON_SPEC;

	static { //constructor
		Pair<Common, ForgeConfigSpec> commonSpecPair = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON = commonSpecPair.getLeft();
		COMMON_SPEC = commonSpecPair.getRight();
	}
}