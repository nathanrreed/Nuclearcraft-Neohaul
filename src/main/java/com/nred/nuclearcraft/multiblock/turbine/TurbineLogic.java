//package com.nred.nuclearcraft.multiblock.turbine;
//
//import com.nred.nuclearcraft.multiblock.MultiblockLogic;
//
//public class TurbineLogic extends MultiblockLogic<Turbine> {
//    public boolean searchFlag = false;
//
////    public final ObjectSet<TileTurbineDynamoPart> dynamoPartCache = new ObjectOpenHashSet<>(), dynamoPartCacheOpposite = new ObjectOpenHashSet<>();
////    public final Long2ObjectMap<TileTurbineDynamoPart> componentFailCache = new Long2ObjectOpenHashMap<>(), assumedValidCache = new Long2ObjectOpenHashMap<>();
//
//    public float prevAngVel = 0F;
//
//    public TurbineLogic(Turbine turbine) {
//        super(turbine);
//    }
//
//    public TurbineLogic(TurbineLogic oldLogic) {
//        super(oldLogic);
//        searchFlag = oldLogic.searchFlag;
//        prevAngVel = oldLogic.prevAngVel;
//    }
//
//    @Override
//    public String getID() {
//        return "turbine";
//    }
//
//    // Multiblock Size Limits
//
//    @Override
//    public int getMinimumInteriorLength() {
//        return turbine_min_size;
//    }
//
//    @Override
//    public int getMaximumInteriorLength() {
//        return turbine_max_size;
//    }
//
//}
