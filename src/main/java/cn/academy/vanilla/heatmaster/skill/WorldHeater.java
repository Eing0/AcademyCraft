package cn.academy.vanilla.heatmaster.skill;

import java.util.HashMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import cn.academy.ability.api.Skill;
import cn.academy.ability.api.ctrl.SkillInstance;
import cn.academy.ability.api.ctrl.action.SyncActionInstant;
import cn.academy.ability.api.ctrl.instance.SkillInstanceInstant;
import cn.academy.ability.api.data.AbilityData;
import cn.academy.ability.api.data.CPData;
import cn.academy.core.client.sound.ACSounds;
import cn.academy.vanilla.electromaster.client.effect.ArcPatterns;
import cn.academy.vanilla.electromaster.entity.EntityArc;
import cn.academy.vanilla.electromaster.skill.ArcGen;
import cn.academy.vanilla.electromaster.skill.ArcGen.ArcGenAction;
import cn.academy.vanilla.heatmaster.entity.EntityWorldHeater;
import cn.liutils.entityx.handlers.Life;
import cn.liutils.util.mc.BlockFilters;
import cn.liutils.util.mc.EntitySelectors;
import cn.liutils.util.mc.EntitySelectors.SelectorOfType;
import cn.liutils.util.raytrace.Raytrace;

public class WorldHeater extends Skill
{
	static WorldHeater instance;
	public static HashMap<Block,Block> heatmap = new HashMap();
	static
	{
		heatmap.put(Block.getBlockFromName("sand"),Block.getBlockFromName("glass"));
		heatmap.put(Block.getBlockFromName("cobblestone"),Block.getBlockFromName("stone"));
		heatmap.put(Block.getBlockFromName("clay"),Block.getBlockFromName("brick_block"));
		heatmap.put(Block.getBlockFromName("snow_layer"),Block.getBlockFromName("air"));
		heatmap.put(Block.getBlockFromName("snow"),Block.getBlockFromName("water"));
		heatmap.put(Block.getBlockFromName("ice"),Block.getBlockFromName("water"));
		//TODO add more
		// Suggestion: Maybe you can access minecraft's melting data? Weathfold
		// Reply: Yiya~ exp will be ignored. ShieLian
	}
	public WorldHeater()
	{
		super("world_heater", 1);
		instance=this;
	}

	@Override
    public SkillInstance createSkillInstance(EntityPlayer player)
	{
		return new SkillInstanceInstant().addExecution(new WorldHeaterAction());
	}


	// class

	public static class WorldHeaterAction extends SyncActionInstant
	{

		@Override
		public boolean validate()
		{
			AbilityData aData= AbilityData.get(player);
			CPData cpData= CPData.get(player);

			boolean flag= false;

			// Perform ray trace
			// TODO add ripple support --8(distance)
			MovingObjectPosition result=Raytrace.traceLiving(player, 8,null, BlockFilters.filNothing);
			if (result!=null&&result.typeOfHit ==MovingObjectPosition.MovingObjectType.BLOCK)
				flag= true;
			if(!flag) return false;
			
			Block block=player.worldObj.getBlock(result.blockX,result.blockY,result.blockZ);
			flag=heatmap.keySet().contains(block);
			if(!flag) return false;
			
			return cpData.perform(getOverload(aData),getConsumption(aData));
		}

		@Override
		public void execute()
		{
			AbilityData aData= AbilityData.get(player);
			boolean flag= false;

			// Perform ray trace
			// TODO add ripple support --8(distance)
			//MovingObjectPosition result= player.rayTrace(8,1.0F);
			MovingObjectPosition result=Raytrace.traceLiving(player, 8,null, BlockFilters.filNothing);
			if (result!=null&&result.typeOfHit ==MovingObjectPosition.MovingObjectType.BLOCK)
				flag= true;
			if(!flag) return;
			
			Block block=player.worldObj.getBlock(result.blockX,result.blockY,result.blockZ);
			flag=heatmap.keySet().contains(block);
			if(!flag) return;
			
			if (!isRemote)
			{
				player.worldObj.setBlock(result.blockX,result.blockY,result.blockZ,heatmap.get(block));
				System.out.println("Block changed");
			}
			else
			{
				spawnEffects();
			}
		}
		
		private static float getOverload(AbilityData data) {
			/*return instance.pipeFloat("overload", 
				instance.getFunc("overload")
				.callFloat(data.getSkillExp(instance)));*/
			//TODO ripple support
			return 1.0F;
		}
		
		private static float getConsumption(AbilityData data) {
			/*return instance.pipeFloat("cp", 
				instance.getFunc("consumption")
				.callFloat(data.getSkillExp(instance)));*/
			//TODO ripple support
			return 10.0F;
		}

		@SideOnly(Side.CLIENT)
		private void spawnEffects()
		{
			System.out.println("Effects Spawned");
			//TODO
			/*
			EntityWorldHeater worldheater= new EntityWorldHeater(player.worldObj);
//			worldheater.texWiggle= 0.7;
//			worldheater.showWiggle= 0.1;
//			worldheater.hideWiggle= 0.4;
			worldheater.addMotionHandler(new Life(10));

			player.worldObj.spawnEntityInWorld(worldheater);
			ACSounds.playClient(player,"em.arc_weak",0.5f,1f);
			*/
		}
	}
}