package com.genuineminecraft.screensize.gui;

import java.lang.reflect.Method;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class GuiScreenSize extends GuiScreen {

	private Method method;
	private GuiTextField theGuiTextField;
	private String errorMessage1 = "";
	private String errorMessage2 = "";
	private String screenSize;
	private final GuiScreen parent;
	private GuiButton done;
	private GuiButton cancel;
	private GuiButton max;
	private boolean changed = false;
	String[] presets = new String[] { "2560x1440", "1920x1080", "1680x1050", "1600x900", "1280x720", "1024x768", "800x600" };

	public GuiScreenSize(GuiScreen parent) {
		this.parent = parent;
		screenSize = Integer.toString(Display.getWidth()) + "x" + Integer.toString(Display.getHeight());
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
			// Cancel
			case 0:
				mc.displayGuiScreen(parent);
				break;
			// Done
			case 1:
				String[] size = theGuiTextField.getText().split("[ -/xX]");
				if (size.length == 2) {
					try {
						int width = Integer.valueOf(size[0]);
						int height = Integer.valueOf(size[1]);
						if (width >= 400 && height >= 300) {
							setInGameScreenSize(width, height);
							mc.displayGuiScreen(parent);
						} else {
							errorMessage1 = "\2474Error:\247f Must be > or = 400x300";
							errorMessage2 = "(x can be anything on the numpad)";
						}
					} catch (NumberFormatException e) {
						errorMessage1 = "\2474Error:\247f Input must be numbers";
						errorMessage2 = "";
					}
				} else {
					errorMessage1 = "\2474Error:\247f Input must look like 800x600";
					errorMessage2 = "(x can be anything on the numpad)";
				}
				break;
			// Max
			case 2:
				DisplayMode mode;
				try {
					mode = getMaxDisplayMode();
					theGuiTextField.setText(Integer.toString(mode.getWidth()) + "x" + Integer.toString(mode.getHeight()));
				} catch (LWJGLException e) {
					errorMessage1 = "\2474Error:\247f Something bad happened with LWJGL";
					errorMessage2 = "Reverting for your protection";
				}
				break;
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		screenSize = Integer.toString(Display.getWidth()) + "x" + Integer.toString(Display.getHeight());
		drawDefaultBackground();
		drawCenteredString(fontRendererObj, "Set Screen Size", width / 2, height / 4 - 60 + 20, 16777215);
		String string = "Enter new size - Like: 1280x720 or 1920*1080";
		drawString(fontRendererObj, string, width / 2 - fontRendererObj.getStringWidth(string) / 2, height / 4 - 15, 10526880);
		drawString(fontRendererObj, errorMessage1, width / 2 - 100, height / 4 + 27, 10526880);
		drawString(fontRendererObj, errorMessage2, width / 2 - 100, height / 4 + 47, 10526880);
		theGuiTextField.drawTextBox();
		super.drawScreen(par1, par2, par3);
	}

	@Override
	public void initGui() {
		cancel = new GuiButton(0, width / 2 + 60 - 25, height / 4 + 132, 50, 20, I18n.format("gui.cancel"));
		done = new GuiButton(1, width / 2 - 60 - 25, height / 4 + 132, 50, 20, I18n.format("gui.done"));
		done.enabled = false;
		max = new GuiButton(2, width / 2 - 25, height / 4 + 132, 50, 20, "Max");
		Keyboard.enableRepeatEvents(true);
		buttonList.clear();
		buttonList.add(cancel);
		buttonList.add(done);
		buttonList.add(max);
		theGuiTextField = new GuiTextField(fontRendererObj, width / 2 - 100, height / 4, 200, 20);
		theGuiTextField.setText(screenSize);
		theGuiTextField.setTextColor(0x555555);
	}

	@Override
	public void keyTyped(char par1, int par2) {
		if (par1 == 13 /* Enter */)
			actionPerformed(done);
		if (theGuiTextField.isFocused()) {
			if (par1 == 27 /* Escape */)
				theGuiTextField.setFocused(false);
			theGuiTextField.textboxKeyTyped(par1, par2);
			theGuiTextField.setTextColor(0xFFFFFF);
			changed = true;
		} else {
			if (par1 == 27 /* Escape */)
				actionPerformed(cancel);
			theGuiTextField.setText(presets[par1 % presets.length]);
			theGuiTextField.setTextColor(0xFFFFFF);
			changed = true;
		}
		handle();
	}

	@Override
	public void mouseClicked(int par1, int par2, int button) {
		super.mouseClicked(par1, par2, button);
		theGuiTextField.mouseClicked(par1, par2, button);
		if (theGuiTextField.isFocused() && !changed) {
			theGuiTextField.setTextColor(0xFFFFFF);
			theGuiTextField.setText("");
			changed = true;
		}
		handle();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	@Override
	public void updateScreen() {
		theGuiTextField.updateCursorCounter();
	}

	private void handle() {
		if (changed && !theGuiTextField.getText().trim().isEmpty())
			done.enabled = true;
		else
			done.enabled = false;
	}

	public void setInGameScreenSize(int width, int height) {
		try {
			DisplayMode mode = getDisplayMode(width, height);
			Display.setDisplayMode(mode);
			mc.displayWidth = Display.getWidth();
			mc.displayHeight = Display.getHeight();
			resizeMinecraft();
			Display.update();
		} catch (LWJGLException e) {
		}
	}

	private DisplayMode getMaxDisplayMode() throws LWJGLException {
		return getDisplayMode(1000000, 1000000);
	}

	private DisplayMode getDisplayMode(int width, int height) throws LWJGLException {
		DisplayMode mode = null;
		if (Display.isFullscreen()) {
			int x = 10000000;
			int y = 10000000;
			for (DisplayMode dm : Display.getAvailableDisplayModes()) {
				if ((Math.abs(dm.getWidth() - width) <= x) && (Math.abs(dm.getHeight() - height) <= y)) {
					x = Math.abs(dm.getWidth() - width);
					y = Math.abs(dm.getHeight() - height);
					mode = dm;
				}
			}
			for (DisplayMode dm : Display.getAvailableDisplayModes())
				if ((Math.abs(dm.getWidth() - width) == x) && (Math.abs(dm.getHeight() - height) == y) && mode.getBitsPerPixel() <= dm.getBitsPerPixel() && mode.getFrequency() <= dm.getFrequency())
					mode = dm;
		} else {
			if (width > Display.getDesktopDisplayMode().getWidth() || height > Display.getDesktopDisplayMode().getHeight()) {
				mode = Display.getDesktopDisplayMode();
			} else
				mode = new DisplayMode(width, height);
		}
		return mode;
	}

	private boolean resizeMinecraft() {
		if (method == null) {
			try {
				method = Minecraft.class.getDeclaredMethod("func_71370_a", int.class, int.class);
			} catch (Exception e) {
				try {
					method = Minecraft.class.getDeclaredMethod("resize", int.class, int.class);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		if (method != null) {
			method.setAccessible(true);
			try {
				method.invoke(mc, mc.displayWidth, mc.displayHeight);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
}
