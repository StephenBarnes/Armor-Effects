package com.stebars.armoreffects;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;


@Mod(ArmorEffectsMod.MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArmorEffectsMod {
	public final static String MOD_ID = "armoreffects";

	public ArmorEffectsMod() {
		MinecraftForge.EVENT_BUS.register(this);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigDefinition.COMMON_SPEC);
		ConfigParser.loadAll();
	}

}