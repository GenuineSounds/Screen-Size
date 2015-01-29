package com.genuineflix.resolution;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

import com.genuineflix.resolution.gui.GuiScreenSize;
import com.genuineflix.resolution.key.KeyBindingScreenSize;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@Mod(modid = ScreenSize.MODID, name = ScreenSize.NAME, version = ScreenSize.VERSION)
public class ScreenSize {

	@Instance(ScreenSize.MODID)
	public static ScreenSize instance;
	public static final String MODID = "ScreenSize";
	public static final String NAME = "ScreenSize";
	public static final String VERSION = "1.0.11";
	public static KeyBinding screenSizeKey;

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(this);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void keyPress(final KeyInputEvent keyInput) {
		if (ScreenSize.screenSizeKey.getIsKeyPressed())
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSize(Minecraft.getMinecraft().currentScreen));
	}

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		ScreenSize.screenSizeKey = new KeyBindingScreenSize();
		ClientRegistry.registerKeyBinding(ScreenSize.screenSizeKey);
	}
}
