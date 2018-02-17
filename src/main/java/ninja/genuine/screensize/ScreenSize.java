package ninja.genuine.screensize;

import org.lwjgl.input.Keyboard;

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
import ninja.genuine.screensize.gui.GuiScreenSize;

@Mod(modid = Constants.MODID, name = Constants.NAME, version = Constants.VERSION, clientSideOnly = true, updateJSON = Constants.URL
		+ "update.json", canBeDeactivated = true, useMetadata = true)
public class ScreenSize {

	@Instance(Constants.MODID)
	public static ScreenSize instance;
	public static KeyBinding configKey = new KeyBinding("Screen Size", Keyboard.KEY_ADD, "Screen size gui");
	public static boolean enabled = true;

	@EventHandler
	public void pre(final FMLPreInitializationEvent event) {
		ClientRegistry.registerKeyBinding(configKey);
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
		if (configKey.isPressed())
			Minecraft.getMinecraft().displayGuiScreen(new GuiScreenSize(Minecraft.getMinecraft().currentScreen));
	}
}
