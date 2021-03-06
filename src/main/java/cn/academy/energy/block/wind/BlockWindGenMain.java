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
package cn.academy.energy.block.wind;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cn.academy.core.block.ACBlockMulti;
import cn.academy.energy.ModuleEnergy;
import cn.academy.energy.client.gui.wind.GuiWindGenMain;
import cn.annoreg.core.Registrant;
import cn.annoreg.mc.gui.GuiHandlerBase;
import cn.annoreg.mc.gui.RegGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@Registrant
public class BlockWindGenMain extends ACBlockMulti {
	
	@RegGuiHandler
	public static GuiHandlerBase guiHandler = new GuiHandlerBase() {
		@SideOnly(Side.CLIENT)
		@Override
		protected Object getClientContainer(EntityPlayer player, World world, int x, int y, int z) {
			ContainerWindGenMain container = (ContainerWindGenMain) getServerContainer(player, world, x, y, z);
			return container == null ? null : new GuiWindGenMain(container);
		}
		
		@Override
		protected Object getServerContainer(EntityPlayer player, World world, int x, int y, int z) {
			TileWindGenMain gen = locate(world, x, y, z);
			return gen == null ? null : new ContainerWindGenMain(player, gen);
		}
		
		TileWindGenMain locate(World world, int x, int y, int z) {
			Block block = world.getBlock(x, y, z);
			if(block != ModuleEnergy.windgenMain)
				return null;
			TileEntity te = ModuleEnergy.windgenMain.getOriginTile(world, x, y, z);
			return (TileWindGenMain) ((te instanceof TileWindGenMain) ? te : null);
		}
	};

	public BlockWindGenMain() {
		super("windgen_main", Material.rock);
		this.addSubBlock(new int[][] {
			{ 0, 0, -1 },
			{ 0, 0, 1 }
		});
		finishInit();
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TileWindGenMain();
	}

	@Override
	public double[] getRotCenter() {
		return new double[] { 0.5, 0, 0.4 };
	}

	@Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, 
            float tx, float ty, float tz) {
        if(!player.isSneaking()) {
        	guiHandler.openGuiContainer(player, world, x, y, z);
            return true;
        }
        return false;
    }
	
}
