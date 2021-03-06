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
package cn.academy.vanilla.electromaster.event;

import cn.academy.vanilla.electromaster.entity.EntityCoinThrowing;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;

/**
 * @author WeAthFolD
 */
public class CoinThrowEvent extends PlayerEvent {
	
	public final EntityCoinThrowing coin;

	public CoinThrowEvent(EntityPlayer _player, EntityCoinThrowing _coin) {
		super(_player);
		coin  = _coin;
	}

}
