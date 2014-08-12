package com.genuineminecraft.screensize.key;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;

public class ScreenSizeKeyBinding extends KeyBinding {

	public ScreenSizeKeyBinding() {
		super("Screen Size", Keyboard.KEY_ADD, "Opens the screen size gui");
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
