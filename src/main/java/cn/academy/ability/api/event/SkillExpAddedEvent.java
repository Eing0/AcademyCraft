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
package cn.academy.ability.api.event;

import cn.academy.ability.api.Skill;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Fired in SERVER only, when the specified skill's exp is being ADDED.
 * When change category or sth else the event won't get fired.
 * @author WeAthFolD
 */
public class SkillExpAddedEvent extends AbilityEvent {
	
	public final Skill skill;

	public SkillExpAddedEvent(EntityPlayer _player, Skill _skill) {
		super(_player);
		skill = _skill;
	}

}
