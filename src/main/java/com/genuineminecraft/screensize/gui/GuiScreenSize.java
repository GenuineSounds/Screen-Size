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
		this.screenSize = Integer.toString(Display.getWidth()) + "x" + Integer.toString(Display.getHeight());
	}

	@Override
	public void actionPerformed(GuiButton button) {
		if (button.enabled) {
			switch (button.id) {
				case 0:
					this.mc.displayGuiScreen(this.parent);
					break;
				case 1:
					String[] size = this.theGuiTextField.getText().split("[ -/xX]");
					if (size.length == 2) {
						try {
							int width = Integer.valueOf(size[0]);
							int height = Integer.valueOf(size[1]);
							if (width >= 400 && height >= 300) {
								this.setInGameScreenSize(width, height);
								this.mc.displayGuiScreen(this.parent);
							} else {
								this.errorMessage1 = "\2474Error:\247f Must be > or = 400x300";
								this.errorMessage2 = "(x can be anything on the numpad)";
							}
						}
						catch (NumberFormatException e) {
							this.errorMessage1 = "\2474Error:\247f Input must be numbers";
							this.errorMessage2 = "";
						}
					} else {
						this.errorMessage1 = "\2474Error:\247f Input must look like 800x600";
						this.errorMessage2 = "(x can be anything on the numpad)";
					}
					break;
				case 2:
					DisplayMode mode;
					try {
						mode = this.getMaxDisplayMode();
						this.theGuiTextField.setText(Integer.toString(mode.getWidth()) + "x" + Integer.toString(mode.getHeight()));
						this.theGuiTextField.setTextColor(0xFFFFFF);
						changed = true;
					}
					catch (LWJGLException e) {
						this.errorMessage1 = "\2474Error:\247f Something bad happened with LWJGL";
						this.errorMessage2 = "Reverting for your protection";
					}
					break;
			}
		}
	}

	@Override
	public void drawScreen(int par1, int par2, float par3) {
		this.screenSize = Integer.toString(Display.getWidth()) + "x" + Integer.toString(Display.getHeight());
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRendererObj, "Set Screen Size", this.width / 2, this.height / 4 - 60 + 20, 16777215);
		String string = "Enter new size - Like: 1280x720 or 1920*1080";
		this.drawString(this.fontRendererObj, string, this.width / 2 - this.fontRendererObj.getStringWidth(string) / 2, this.height / 4 - 15, 10526880);
		this.drawString(this.fontRendererObj, this.errorMessage1, this.width / 2 - 100, this.height / 4 + 27, 10526880);
		this.drawString(this.fontRendererObj, this.errorMessage2, this.width / 2 - 100, this.height / 4 + 47, 10526880);
		this.theGuiTextField.drawTextBox();
		super.drawScreen(par1, par2, par3);
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

	private DisplayMode getMaxDisplayMode() throws LWJGLException {
		return this.getDisplayMode(1000000, 1000000);
	}

	private void handle() {
		if (this.changed && !this.theGuiTextField.getText().trim().isEmpty())
			this.done.enabled = true;
		else
			this.done.enabled = false;
	}

	@Override
	public void initGui() {
		this.cancel = new GuiButton(0, this.width / 2 + 60 - 25, this.height / 4 + 132, 50, 20, I18n.format("gui.cancel"));
		this.done = new GuiButton(1, this.width / 2 - 60 - 25, this.height / 4 + 132, 50, 20, I18n.format("gui.done"));
		this.done.enabled = false;
		this.max = new GuiButton(2, this.width / 2 - 25, this.height / 4 + 132, 50, 20, "Max");
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(this.cancel);
		this.buttonList.add(this.done);
		this.buttonList.add(this.max);
		this.theGuiTextField = new GuiTextField(this.fontRendererObj, this.width / 2 - 100, this.height / 4, 200, 20);
		this.theGuiTextField.setText(this.screenSize);
		this.theGuiTextField.setTextColor(0x555555);
	}

	@Override
	public void keyTyped(char par1, int par2) {
		if (par1 == 13) // Enter
			this.actionPerformed(this.done);
		if (this.theGuiTextField.isFocused()) {
			if (par1 == 27) // Escape
				this.theGuiTextField.setFocused(false);
			this.theGuiTextField.textboxKeyTyped(par1, par2);
			this.theGuiTextField.setTextColor(0xFFFFFF);
			this.changed = true;
		} else {
			if (par1 == 27) // Escape
				this.actionPerformed(this.cancel);
			this.theGuiTextField.setText(this.presets[par1 % this.presets.length]);
			this.theGuiTextField.setTextColor(0xFFFFFF);
			this.changed = true;
		}
		this.handle();
	}

	@Override
	public void mouseClicked(int par1, int par2, int button) {
		super.mouseClicked(par1, par2, button);
		this.theGuiTextField.mouseClicked(par1, par2, button);
		if (this.theGuiTextField.isFocused() && !this.changed) {
			this.theGuiTextField.setTextColor(0xFFFFFF);
			this.theGuiTextField.setText("");
			this.changed = true;
		}
		this.handle();
	}

	@Override
	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	private boolean resizeMinecraft() {
		if (this.method == null) {
			try {
				this.method = Minecraft.class.getDeclaredMethod("func_71370_a", int.class, int.class);
			}
			catch (Exception e) {
				try {
					this.method = Minecraft.class.getDeclaredMethod("resize", int.class, int.class);
				}
				catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
		if (this.method != null) {
			this.method.setAccessible(true);
			try {
				this.method.invoke(this.mc, this.mc.displayWidth, this.mc.displayHeight);
				return true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public void setInGameScreenSize(int width, int height) {
		try {
			DisplayMode mode = this.getDisplayMode(width, height);
			Display.setDisplayMode(mode);
			this.mc.displayWidth = Display.getWidth();
			this.mc.displayHeight = Display.getHeight();
			this.resizeMinecraft();
			Display.update();
		}
		catch (LWJGLException e) {}
	}

	@Override
	public void updateScreen() {
		this.theGuiTextField.updateCursorCounter();
	}
}
