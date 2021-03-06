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
package cn.academy.core.registry;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import cn.academy.core.AcademyCraft;
import cn.annoreg.core.AnnotationData;
import cn.annoreg.core.LoadStage;
import cn.annoreg.core.RegistryType;
import cn.annoreg.core.RegistryTypeDecl;

/**
 * @author WeAthFolD
 */
@RegistryTypeDecl
public class ACRecipeNamesRegistration extends RegistryType {
	
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface RegACRecipeNames {}

	public ACRecipeNamesRegistration() {
		super(RegACRecipeNames.class, "ac_recipeNames");
		setLoadStage(LoadStage.POST_INIT);
	}

	@Override
	public boolean registerClass(AnnotationData data) throws Exception {
		AcademyCraft.addToRecipe(data.getTheClass());
		return true;
	}

	@Override
	public boolean registerField(AnnotationData data) throws Exception {
		return false;
	}

}
