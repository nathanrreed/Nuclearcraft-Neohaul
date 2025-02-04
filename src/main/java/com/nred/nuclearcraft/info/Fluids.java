package com.nred.nuclearcraft.info;

import com.nred.nuclearcraft.fluid.GasFluid;
import com.nred.nuclearcraft.fluid.LitLiquidBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import org.antlr.v4.runtime.misc.Triple;

import java.util.HexFormat;
import java.util.stream.IntStream;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.Registers.*;

public class Fluids {
    public final DeferredHolder<Fluid, BaseFlowingFluid.Source> still;
    public final DeferredHolder<Fluid, BaseFlowingFluid.Flowing> flowing;
    public final boolean gaseous;
    private BaseFlowingFluid.Properties properties;
    public final DeferredItem<BucketItem> bucket;
    public final IClientFluidTypeExtensions client;
    public final DeferredBlock<LiquidBlock> block;
    public final DeferredHolder<FluidType, FluidType> type;

    public static final TypeInfo GAS_TYPE = new TypeInfo(true, "gas", "gas", FluidType.Properties.create().density(-10).viscosity(40).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH));
    public static final TypeInfo HOT_GAS_TYPE = new TypeInfo(true, "gas", "gas", FluidType.Properties.create().density(-10).viscosity(40).temperature(1000).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH));
    public static final TypeInfo SUGAR_TYPE = new TypeInfo(false, "molten_still", "molten_flow", FluidType.Properties.create().density(1150).viscosity(8000).temperature(350).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo CHOCOLATE_TYPE = new TypeInfo(false, "molten_still", "molten_flow", FluidType.Properties.create().density(1325).viscosity(5000).temperature(330).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo MOLTEN_TYPE = new TypeInfo(false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(8000).lightLevel(10).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo FISSION_TYPE = new TypeInfo(false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(8000).lightLevel(10).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo STEAM_TYPE = new TypeInfo(true, "steam", "steam", FluidType.Properties.create().density(-10).viscosity(40).temperature(450).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH));
    public static final TypeInfo HOT_COOLANT_TYPE = new TypeInfo(false, "liquid_still", "liquid_flow", FluidType.Properties.create().density(400).viscosity(10000).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo FLAMMABLE_TYPE = new TypeInfo(false, "liquid_still", "liquid_flow", FluidType.Properties.create().density(800).viscosity(800).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo SALT_SOLUTION_TYPE = new TypeInfo(false, "liquid_still", "liquid_flow", FluidType.Properties.create().density(5000).viscosity(8000).temperature(50).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo COOLANT_TYPE = new TypeInfo(false, "liquid_still", "liquid_flow", FluidType.Properties.create().density(5000).viscosity(15000).temperature(300).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA));
    public static final TypeInfo ACID_TYPE = new TypeInfo(false, "liquid_still", "liquid_flow", FluidType.Properties.create());

    private static BlockBehaviour.Properties blockProperties(int tint) {
        return BlockBehaviour.Properties.of().mapColor(ARGBtoMapColor(tint)).replaceable().noCollission().pushReaction(PushReaction.DESTROY).noLootTable().liquid();
    }

    public Fluids(String name, boolean same, int tint) {
        this(name, tint, same, false);
    }

    public Fluids(String name, int tint, boolean same, boolean gaseous) {
        this(name, name, tint, same, gaseous);
    }

    public Fluids(String name, String file, int tint, boolean same, boolean gaseous) {
        this(name, tint, new TypeInfo(gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create()));
    }

    public Fluids(String name, boolean same, int tint, int density, int temperature, int viscosity, int light_level) {
        this(name, same, false, tint, density, viscosity, temperature, light_level);
    }

    public Fluids(String name, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level) {
        this(name, name, same, gaseous, tint, density, viscosity, temperature, light_level);
    }

    public Fluids(String name, String file, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level) {
        this(name, tint, new TypeInfo(gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create().density(density).viscosity(viscosity).temperature(temperature).lightLevel(light_level)));
    }

    public Fluids(String name, int tint, TypeInfo type, int temperature) {
        this(name, tint, new TypeInfo(type.gaseous, type.still, type.flowing, type.properties.temperature(temperature)));
    }

    public Fluids(String name, int tint, TypeInfo type) {
        this.type = FLUID_TYPES.register(name + "_type", () -> new FluidType(type.properties));
        this.gaseous = type.gaseous;

        if (gaseous) {
            this.still = FLUIDS.register(name, () -> new GasFluid(this.properties));
        } else {
            this.still = FLUIDS.register(name, () -> new BaseFlowingFluid.Source(this.properties));
        }
        this.flowing = FLUIDS.register(name + "_flowing", () -> new BaseFlowingFluid.Flowing(this.properties));
        this.block = BLOCKS.register(name, () -> new LitLiquidBlock(this.still.get(), blockProperties(tint)));
        this.bucket = ITEMS.register(name + "_bucket", () -> new BucketItem(this.still.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        this.properties = new BaseFlowingFluid.Properties(this.type, this.still, this.flowing).bucket(this.bucket).block(this.block);
        this.client = new IClientFluidTypeExtensions() {
            @Override
            public ResourceLocation getFlowingTexture() {
                return type.getFlowingTexture();
            }

            @Override
            public ResourceLocation getStillTexture() {
                return type.getStillTexture();
            }

            @Override
            public int getTintColor() {
                return tint;
            }
        };
    }

    public record TypeInfo(boolean gaseous, String still, String flowing, FluidType.Properties properties) {
        ResourceLocation getFlowingTexture() {
            return ncLoc("block/fluid/" + flowing);
        }

        ResourceLocation getStillTexture() {
            return ncLoc("block/fluid/" + still);
        }
    }

    private static Triple<Integer, Integer, Integer> getRBG(int tint) {
        StringBuilder hex = new StringBuilder(Integer.toHexString(tint));
        while (hex.length() < 6) {
            hex.insert(0, "0");
        }
        return new Triple<>(HexFormat.fromHexDigits(hex.substring(0, 2)), HexFormat.fromHexDigits(hex.substring(2, 4)), HexFormat.fromHexDigits(hex.substring(4, 6)));
    }

    // Try to find the closest color to the tint
    private static MapColor ARGBtoMapColor(int tint) {
        Triple<Integer, Integer, Integer> colorHex = getRBG(tint);

        var closest = MapColor.NONE;
        var closestNum = 1024;
        for (int i : IntStream.range(1, 61).toArray()) {
            MapColor temp = MapColor.byId(i);
            Triple<Integer, Integer, Integer> rgb = getRBG(temp.col);
            int num = Math.abs(rgb.a - colorHex.a) + Math.abs(rgb.b - colorHex.b) + Math.abs(rgb.c - colorHex.c);
            if (num < closestNum) {
                closest = temp;
                closestNum = num;
            }
        }

        return closest;
    }
}