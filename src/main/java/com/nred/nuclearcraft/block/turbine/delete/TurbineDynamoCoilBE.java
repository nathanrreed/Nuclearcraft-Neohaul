//package com.nred.nuclearcraft.block.turbine;
//
//import com.nred.nuclearcraft.multiblock.turbine.TurbineDynamoCoilType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//
//public class TurbineDynamoCoilBE extends TileTurbineDynamoPart {
//
//	/**
//	 * Don't use this constructor!
//	 */
//	public TurbineDynamoCoilBE() {
//		super();
//	}
//
//	public TurbineDynamoCoilBE(String coilName, Double conductivity, String ruleID) {
//		super(coilName, conductivity, ruleID);
//	}
//
//	public static class Meta extends TurbineDynamoCoilBE {
//
//		protected Meta(TurbineDynamoCoilType coilType) {
//			super(coilType.getSerializedName(), coilType.getConductivity(), coilType.getSerializedName() + "_coil");
//		}
//
//		@Override
//		public boolean shouldRefresh(Level worldIn, BlockPos posIn, BlockState oldState, BlockState newState) {
//			return oldState != newState;
//		}
//	}
//
//	public static class Magnesium extends Meta {
//
//		public Magnesium() {
//			super(TurbineDynamoCoilType.MAGNESIUM);
//		}
//	}
//
//	public static class Beryllium extends Meta {
//
//		public Beryllium() {
//			super(TurbineDynamoCoilType.BERYLLIUM);
//		}
//	}
//
//	public static class Aluminum extends Meta {
//
//		public Aluminum() {
//			super(TurbineDynamoCoilType.ALUMINUM);
//		}
//	}
//
//	public static class Gold extends Meta {
//
//		public Gold() {
//			super(TurbineDynamoCoilType.GOLD);
//		}
//	}
//
//	public static class Copper extends Meta {
//
//		public Copper() {
//			super(TurbineDynamoCoilType.COPPER);
//		}
//	}
//
//	public static class Silver extends Meta {
//
//		public Silver() {
//			super(TurbineDynamoCoilType.SILVER);
//		}
//	}
//}
