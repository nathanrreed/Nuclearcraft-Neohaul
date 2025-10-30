package com.nred.nuclearcraft.property;

import com.google.common.base.Predicate;
import com.nred.nuclearcraft.util.StreamHelper;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.Arrays;
import java.util.Collection;

public class SidedEnumProperty<T extends Enum<T> & StringRepresentable> extends EnumProperty<T> {
	public Direction facing;
	
	public SidedEnumProperty(String name, Class<T> valueClass, Collection<T> allowedValues, Direction facing) {
		super(name, valueClass, allowedValues);
		this.facing = facing;
	}
	
	public static <T extends Enum<T> & StringRepresentable> SidedEnumProperty<T> create(String name, Class<T> clazz, Direction facing) {
		return create(name, clazz, x -> true, facing);
	}
	
	public static <T extends Enum<T> & StringRepresentable> SidedEnumProperty<T> create(String name, Class<T> clazz, Predicate<T> filter, Direction facing) {
		return create(name, clazz, StreamHelper.filter(clazz.getEnumConstants(), filter), facing);
	}
	
	public static <T extends Enum<T> & StringRepresentable> SidedEnumProperty<T> create(String name, Class<T> clazz, T[] values, Direction facing) {
		return create(name, clazz, Arrays.asList(values), facing);
	}
	
	public static <T extends Enum<T> & StringRepresentable> SidedEnumProperty<T> create(String name, Class<T> clazz, Collection<T> values, Direction facing) {
		return new SidedEnumProperty<>(name, clazz, values, facing);
	}
}
