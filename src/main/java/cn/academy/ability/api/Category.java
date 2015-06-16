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
package cn.academy.ability.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import cn.academy.ability.api.ctrl.Controllable;
import cn.academy.core.client.Resources;

import com.google.common.collect.ImmutableList;

/**
 * @author WeAthFolD
 */
public class Category {

	private List<Skill> skillList = new ArrayList();
	private List<Controllable> ctrlList = new ArrayList();
	
	private Map<String, List<Skill> > typeMap;
	
	private final String name;
	
	int catID = -1;
	
	protected ResourceLocation icon;
	
	public Category(String _name) {
		name = _name;
		icon = Resources.getTexture("abilities/" + name + "/icon");
	}
	
	public void defineTypes(String ...types) {
		if(typeMap != null)
			throw new RuntimeException("Can't define twice!");
		
		typeMap = new HashMap();
		for(String s : types)
			typeMap.put(s, new ArrayList());
	}
	
	public void addSkill(String type, Skill skill) {
		// TODO Can remove when release
		if(getSkill(skill.getName()) != null)
			throw new RuntimeException("Duplicating skill " + skill.getName() + "!!");
		if(typeMap == null)
			throw new RuntimeException("Type not defined");
		
		List<Skill> mapList = typeMap.get(type);
		if(mapList == null)
			throw new RuntimeException("Type " + type + " does not exist");
		
		skillList.add(skill);
		mapList.add(skill);
		addControllable(skill);
		
		skill.addedIntoCategory(this, skillList.size() - 1);
	}
	
	public int getSkillID(Skill s) {
		return skillList.indexOf(s);
	}
	
	public int getSkillCount() {
		return skillList.size();
	}
	
	public Skill getSkill(int id) {
		return id >= skillList.size() ? null : skillList.get(id);
	}
	
	public boolean containsSkill(Skill skill) {
		return skill == getSkill(skill.getID());
	}
	
	public Skill getSkill(String name) {
		for(Skill s : skillList)
			if(s.getName().equals(name))
				return s;
		return null;
	}
	
	/**
	 * Get an <b>immutable</b> list of skills in this category.
	 */
	public List<Skill> getSkillList() {
		return ImmutableList.copyOf(skillList);
	}
	
	public List<Skill> getSkillsOfType(String type) {
		return ImmutableList.copyOf(typeMap.get(type));
	}
	
	public List<Skill> getSkillsOfLevel(int level) {
		return skillList.stream().filter((Skill s) -> s.getLevel() == level)
				.collect(Collectors.toList());
	}
	
	public int getCategoryID() {
		return catID;
	}
	
	public void addControllable(Controllable c) {
		ctrlList.add(c);
	}
	
	/**
	 * Internal call used majorly by Preset system. DO NOT CALL THIS!
	 */
	public int getControlID(Skill skill) {
		return ctrlList.indexOf(skill);
	}
	
	/**
	 * Internal call used majorly by Preset system. DO NOT CALL THIS!
	 */
	public Controllable getControllable(int id) {
		if(id < 0)
			return null;
		if(ctrlList.size() > id)
			return ctrlList.get(id);
		return null;
	}
	
	/**
	 * Internal call used majorly by Preset system. DO NOT CALL THIS!
	 */
	public List<Controllable> getControllableList() {
		return ImmutableList.copyOf(ctrlList);
	}
	
	public ResourceLocation getIcon() {
		return icon;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDisplayName() {
		return StatCollector.translateToLocal("ac.ability." + name + ".name");
	}
	
}