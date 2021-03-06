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
package cn.academy.terminal.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.academy.terminal.App;
import cn.academy.terminal.AppRegistry;
import cn.academy.terminal.registry.AppRegistration.RegApp;
import cn.annoreg.base.RegistrationFieldSimple;
import cn.annoreg.core.LoadStage;
import cn.annoreg.core.RegistryTypeDecl;

@RegistryTypeDecl
public class AppRegistration extends RegistrationFieldSimple<RegApp, App> {

	public AppRegistration() {
		super(RegApp.class, "ac_App");
		setLoadStage(LoadStage.PRE_INIT);
	}

	/**
	 * Register an App field into AppRegistry.
	 * @author WeAthFolD
	 */
	@Target(ElementType.FIELD)
	@Retention(RetentionPolicy.RUNTIME)
	public static @interface RegApp {}

	@Override
	protected void register(App value, RegApp anno, String field)
			throws Exception {
		AppRegistry.INSTANCE.register(value);
	}
	
}
