package ninja.genuine.resolution.key;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingConfig extends KeyBinding {

	public KeyBindingConfig() {
		super("Screen Size", Keyboard.KEY_ADD, "Screen size gui");
	}

	@Override
	public String getKeyCategory() {
		return "Screen Size";
	}

	@Override
	public int getKeyCodeDefault() {
		return Keyboard.KEY_ADD;
	}

	@Override
	public String getKeyDescription() {
		return "Open Screen Size Window";
	}
}
