//package com.nred.nuclearcraft.multiblock.turbine;
//
//import com.nred.nuclearcraft.block.turbine.TurbineDynamoCoilBE;
//import net.minecraft.com.nred.nuclearcraft.util.StringRepresentable;
//
//import static com.nred.nuclearcraft.config.Config.turbine_coil_conductivity;
//
//public enum TurbineDynamoCoilType implements StringRepresentable, IMetaEnum, ITileEnum<TurbineDynamoCoilBE.Meta> {
//
//    MAGNESIUM("magnesium", 0, turbine_coil_conductivity.get(0), TurbineDynamoCoilBE.Magnesium.class),
//    BERYLLIUM("beryllium", 1, turbine_coil_conductivity.get(1), TurbineDynamoCoilBE.Beryllium.class),
//    ALUMINUM("aluminum", 2, turbine_coil_conductivity.get(2), TurbineDynamoCoilBE.Aluminum.class),
//    GOLD("gold", 3, turbine_coil_conductivity.get(3), TurbineDynamoCoilBE.Gold.class),
//    COPPER("copper", 4, turbine_coil_conductivity.get(4), TurbineDynamoCoilBE.Copper.class),
//    SILVER("silver", 5, turbine_coil_conductivity.get(5), TurbineDynamoCoilBE.Silver.class);
//
//    private final String name;
//    private final int id;
//    private final double conductivity;
//    private final Class<? extends TurbineDynamoCoilBE.Meta> tileClass;
//
//    TurbineDynamoCoilType(String name, int id, double conductivity, Class<? extends TurbineDynamoCoilBE.Meta> tileClass) {
//        this.name = name;
//        this.id = id;
//        this.conductivity = conductivity;
//        this.tileClass = tileClass;
//    }
//
//    @Override
//    public String toString() {
//        return getSerializedName();
//    }
//
//    @Override
//    public int getID() {
//        return id;
//    }
//
//    public double getConductivity() {
//        return conductivity;
//    }
//
//    @Override
//    public Class<? extends TurbineDynamoCoilBE.Meta> getTileClass() {
//        return tileClass;
//    }
//
//    @Override
//    public String getSerializedName() {
//        return name;
//    }
//}
