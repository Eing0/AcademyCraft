/**
 * Copyright (c) Lambda Innovation, 2013-2015
 * 本作品版权由Lambda Innovation所有。
 * http://www.li-dev.cn/
 *
 * This project is open-source, and it is distributed under
 * the terms of GNU General Public License. You can modify
 * and distribute freely as long as you follow the license.
 * 本项目是一个开源项目，且遵循GNU通用公共授权协议。
 * 在遵照该协议的情况下，您可以自由传播和修改。
 * http://www.gnu.org/licenses/gpl.html
 */
package cn.academy.energy.client.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;
import cn.academy.energy.block.ContainerPhaseGen;
import cn.academy.energy.block.TilePhaseGen;
import cn.liutils.cgui.gui.LIGui;
import cn.liutils.cgui.gui.LIGuiContainer;
import cn.liutils.cgui.gui.Widget;
import cn.liutils.cgui.gui.annotations.GuiCallback;
import cn.liutils.cgui.gui.component.ProgressBar;
import cn.liutils.cgui.gui.event.FrameEvent;
import cn.liutils.cgui.loader.EventLoader;
import cn.liutils.cgui.loader.xml.CGUIDocLoader;

/**
 * @author WeAthFolD
 */
public class GuiPhaseGen extends LIGuiContainer {
	
	static LIGui loaded;
	static {
		loaded = CGUIDocLoader.load(new ResourceLocation("academy:guis/phase_gen.xml"));
	}

	public final TilePhaseGen tile;
	
	Widget main;
	
	public GuiPhaseGen(ContainerPhaseGen c) {
		super(c);
		tile = c.tile;
		init();
	}
	
	void init() {
		main = loaded.getWidget("main").copy();
		EventLoader.load(main, this);
		EnergyUIHelper.initNodeLinkButton(tile, main.getWidget("btn_link"));
		
		gui.addWidget(main);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		GL11.glPushMatrix();
		GL11.glTranslated(-guiLeft, -guiTop, 0);
		
		Widget w = gui.getTopWidget(x, y);
		if(w != null) {
			String text = null;
			switch(w.getName()) {
			case "prog_liquid":
				text = tile.getLiquidAmount() + "/" + tile.getTankSize() + "mB";
				break;
			case "prog_buffer":
				text = String.format("%.1f/%.1fIF", tile.getEnergy(), tile.bufferSize);
				break;
			}
			
			if(text != null) {
				//int offsetX = -160, offsetY = -45;
				GL11.glEnable(GL11.GL_BLEND);
				EnergyUIHelper.drawTextBox(text, x + 5, y + 5, 10);
			}
		}
		
		GL11.glPopMatrix();
	}
	
	@GuiCallback("prog_liquid")
	public void updateLiquid(Widget w, FrameEvent event) {
		ProgressBar.get(w).progress = (double) tile.getLiquidAmount() / tile.getTankSize();
	}
	
	@GuiCallback("prog_buffer")
	public void updateBuffer(Widget w, FrameEvent event) {
		ProgressBar.get(w).progress = tile.getEnergy() / tile.bufferSize;
	}

}
