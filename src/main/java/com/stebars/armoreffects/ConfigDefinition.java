package com.stebars.armoreffects;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;


public class ConfigDefinition {

	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ConfigValue<List<? extends String>> effectRules;
	public static final ConfigValue<Integer> applyEveryNTicks;

	static {
		effectRules = BUILDER.comment("Sets of armor items that have effects when all are worn at the same time. "
				+ "Format is armorid+armorid+armorid=effectid/level/ticks/hidden+effectid/level/ticks/hidden. You can leave out any "
				+ "of the last 3 values for effects. By default level will be 1, ticks will be 200 (i.e. 10 seconds), and hidden will "
				+ "be true (1). Hidden should be 0 to show and 1 to hide. Hidden effects have no bubbles and don't show on top-right, "
				+ "but they do still show on the inventory screen.")
				.defineList("effectRules", Arrays.asList(
						"minecraft:diamond_helmet+minecraft:diamond_chestplate+minecraft:diamond_leggings+minecraft:diamond_boots=minecraft:dolphins_grace/1/400/0",
						"minecraft:golden_helmet=minecraft:haste/1+minecraft:speed/2/100/0",
						"minecraft:iron_helmet=minecraft:poison/2/800"
						),
						o -> o instanceof String);
		applyEveryNTicks = BUILDER.comment("Armor effects will be checked and re-applied every [this number] ticks. "
				+ "They will not be applied instantly when the armor is put on.") // TODO apply instantly when armor is put on
				.define("applyEveryNTicks", 20);

		SPEC = BUILDER.build();
	}
}