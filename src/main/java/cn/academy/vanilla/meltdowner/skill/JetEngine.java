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
package cn.academy.vanilla.meltdowner.skill;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import cn.academy.ability.api.Skill;
import cn.academy.ability.api.ctrl.ActionManager;
import cn.academy.ability.api.ctrl.SkillInstance;
import cn.academy.ability.api.ctrl.action.SkillSyncAction;
import cn.academy.ability.api.data.AbilityData;
import cn.academy.vanilla.generic.entity.EntityRippleMark;
import cn.academy.vanilla.meltdowner.client.render.MdParticleFactory;
import cn.annoreg.core.Registrant;
import cn.annoreg.mc.network.RegNetworkCall;
import cn.annoreg.mc.s11n.StorageOption.Data;
import cn.annoreg.mc.s11n.StorageOption.Target;
import cn.liutils.render.particle.Particle;
import cn.liutils.util.generic.RandUtils;
import cn.liutils.util.generic.VecUtils;
import cn.liutils.util.helper.Motion3D;
import cn.liutils.util.mc.EntitySelectors;
import cn.liutils.util.raytrace.Raytrace;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * @author WeAthFolD
 */
@Registrant
public class JetEngine extends Skill {
	
	static JetEngine instance;

	public JetEngine() {
		super("jet_engine", 4);
		instance = this;
	}
	
	@Override
	public SkillInstance createSkillInstance(EntityPlayer player) {
		SkillInstance ret = new SkillInstance().addChild(new JEAction());
		ret.estimatedCP = getConsumption(AbilityData.get(player));
		return ret;
	}
	
	public static class JEAction extends SkillSyncAction {

		public JEAction() {
			super(-1);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			
			if(isRemote)
				startEffects();
		}
		
		@Override
		public void onTick() {
			if(isRemote)
				updateEffects();
			
			if(cpData.getCP() < instance.getConsumption(aData))
				ActionManager.abortAction(this);
		}
		
		@Override
		public void onEnd() {
			if(!isRemote && cpData.perform(instance.getConsumption(aData), instance.getOverload(aData))) {
				startTriggerAction(player, getDest().addVector(0, 1.65, 0));
			}
		}
		
		@Override
		public void onFinalize() {
			if(isRemote)
				endEffects();
		}
		
		Vec3 getDest() {
			double dist = 20.0;
			MovingObjectPosition result = Raytrace.traceLiving(player, dist, EntitySelectors.nothing);
			return (result == null ? new Motion3D(player, true).move(dist).getPosVec() : result.hitVec);
		}
		
		// CLIENT
		@SideOnly(Side.CLIENT)
		EntityRippleMark mark;
		
		@SideOnly(Side.CLIENT)
		void startEffects() {
			if(isLocal()) {
				world.spawnEntityInWorld(mark = new EntityRippleMark(world));
				mark.color.setColor4d(0.2, 1.0, 0.2, 0.7);
			}
		}
		
		@SideOnly(Side.CLIENT)
		void updateEffects() {
			if(isLocal()) {
				Vec3 dest = getDest();
				mark.setPosition(dest.xCoord, dest.yCoord, dest.zCoord);
			}
		}
		
		@SideOnly(Side.CLIENT)
		void endEffects() {
			if(isLocal()) {
				mark.setDead();
			}
		}
		
	}
	
	public static class JETriggerAction extends SkillSyncAction {
		
		static final float TIME = 8, LIFETIME = 20;
		
		Vec3 target;
		Vec3 start;
		Vec3 velocity;
		int ticks;

		public JETriggerAction(Vec3 _target) {
			super(-1);
			target = _target;
		}
		
		public JETriggerAction() {
			super(-1);
		}
		
		@Override
		public void onStart() {
			super.onStart();
			if(isRemote) {
				start = VecUtils.vec(player.posX, player.posY, player.posZ);
				velocity = VecUtils.multiply(VecUtils.subtract(target, start), 1.0 / TIME);
			}
		}
		
		@Override
		public void onTick() {
			++ticks;
			
			if(isRemote) {
				if(ticks >= LIFETIME) {
					ActionManager.endAction(this);
				} else if(ticks <= TIME){
					Vec3 pos = VecUtils.lerp(start, target, ticks / TIME);
					player.setPosition(pos.xCoord, pos.yCoord, pos.zCoord);
					player.motionX = velocity.xCoord;
					player.motionY = velocity.yCoord;
					player.motionZ = velocity.zCoord;
					player.fallDistance = 0.0f;
				}
				
				updateEffects();
			}
			
			
		}
		
		@Override
		public void onFinalize() {
			player.capabilities.setPlayerWalkSpeed(0.1f);
		}
		
		@SideOnly(Side.CLIENT)
		private void updateEffects() {
			if(isLocal()) {
				player.capabilities.setPlayerWalkSpeed(0.07f);
			}
			
			{
				for(int i = 0; i < 10; ++i) {
					Vec3 pos2 = VecUtils.lerp(start, target, 3 * ticks / TIME);
					Particle p = MdParticleFactory.INSTANCE.next(world,
						VecUtils.add(VecUtils.vec(player.posX, player.posY, player.posZ), 
						VecUtils.vec(
							RandUtils.ranged(-.3, .3), 
							RandUtils.ranged(-.3, .3),
							RandUtils.ranged(-.3, .3))), 
						VecUtils.vec(
							RandUtils.ranged(-.02, .02), 
							RandUtils.ranged(-.02, .02),
							RandUtils.ranged(-.02, .02)));
					world.spawnEntityInWorld(p);
				}
			}
		}
		
	}
	
	@RegNetworkCall(side = Side.CLIENT)
	private static void startTriggerAction(@Target EntityPlayer player, @Data Vec3 vec) {
		ActionManager.startAction(new JETriggerAction(vec));
	}

}