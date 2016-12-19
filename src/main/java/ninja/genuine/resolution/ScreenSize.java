package ninja.genuine.resolution;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import ninja.genuine.resolution.gui.GuiScreenSize;
import ninja.genuine.resolution.key.KeyBindingScreenSize;

@Mod(modid = ScreenSize.MODID, name = ScreenSize.NAME, version = ScreenSize.VERSION, clientSideOnly = true, updateJSON = ScreenSize.URL + "update.json", canBeDeactivated = true, useMetadata = true)
public class ScreenSize {

	@Instance(ScreenSize.MODID)
	public static ScreenSize instance;
	public static final String MODID = "screen-size";
	public static final String NAME = "Screen-Size";
	public static final String URL = "http://genuine.ninja/screen-size/";
	public static final String VERSION = "1.2.0";
	public static KeyBinding screenSizeKey;
	public static boolean enabled = true;

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		ScreenSize.screenSizeKey = new KeyBindingScreenSize();
		ClientRegistry.registerKeyBinding(ScreenSize.screenSizeKey);
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void deactivated(FMLModDisabledEvent event) {
		enabled = false;
	}

	@SubscribeEvent
	public void keyPress(final KeyInputEvent keyInput) {
		if (ScreenSize.screenSizeKey.isPressed())
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSize(Minecraft.getMinecraft().currentScreen));
	}
}
