package com.nred.nuclearcraft.util;


import com.nred.nuclearcraft.util.PrimitiveFunction.ByteSupplier;
import com.nred.nuclearcraft.util.PrimitiveFunction.CharSupplier;
import com.nred.nuclearcraft.util.PrimitiveFunction.FloatSupplier;
import com.nred.nuclearcraft.util.PrimitiveFunction.ShortSupplier;

import java.util.function.*;

public class Lazy<T> {
	
	protected boolean initialized = false;
	protected T value;
	
	protected Supplier<T> supplier;
	
	public Lazy(Supplier<T> supplier) {
		this.supplier = supplier;
	}
	
	public T get() {
		if (!initialized) {
			initialized = true;
			value = supplier.get();
		}
		return value;
	}
	
	public static class LazyByte {
		
		protected boolean initialized = false;
		protected byte value;
		
		protected ByteSupplier supplier;
		
		public LazyByte(ByteSupplier supplier) {
			this.supplier = supplier;
		}
		
		public byte get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsByte();
			}
			return value;
		}
	}
	
	public static class LazyShort {
		
		protected boolean initialized = false;
		protected short value;
		
		protected ShortSupplier supplier;
		
		public LazyShort(ShortSupplier supplier) {
			this.supplier = supplier;
		}
		
		public short get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsShort();
			}
			return value;
		}
	}
	
	public static class LazyInt {
		
		protected boolean initialized = false;
		protected int value;
		
		protected IntSupplier supplier;
		
		public LazyInt(IntSupplier supplier) {
			this.supplier = supplier;
		}
		
		public int get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsInt();
			}
			return value;
		}
	}
	
	public static class LazyLong {
		
		protected boolean initialized = false;
		protected long value;
		
		protected LongSupplier supplier;
		
		public LazyLong(LongSupplier supplier) {
			this.supplier = supplier;
		}
		
		public long get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsLong();
			}
			return value;
		}
	}
	
	public static class LazyFloat {
		
		protected boolean initialized = false;
		protected float value;
		
		protected FloatSupplier supplier;
		
		public LazyFloat(FloatSupplier supplier) {
			this.supplier = supplier;
		}
		
		public float get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsFloat();
			}
			return value;
		}
	}
	
	public static class LazyDouble {
		
		protected boolean initialized = false;
		protected double value;
		
		protected DoubleSupplier supplier;
		
		public LazyDouble(DoubleSupplier supplier) {
			this.supplier = supplier;
		}
		
		public double get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsDouble();
			}
			return value;
		}
	}
	
	public static class LazyBoolean {
		
		protected boolean initialized = false;
		protected boolean value;
		
		protected BooleanSupplier supplier;
		
		public LazyBoolean(BooleanSupplier supplier) {
			this.supplier = supplier;
		}
		
		public boolean get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsBoolean();
			}
			return value;
		}
	}
	
	public static class LazyChar {
		
		protected boolean initialized = false;
		protected char value;
		
		protected CharSupplier supplier;
		
		public LazyChar(CharSupplier supplier) {
			this.supplier = supplier;
		}
		
		public char get() {
			if (!initialized) {
				initialized = true;
				value = supplier.getAsChar();
			}
			return value;
		}
	}
}
