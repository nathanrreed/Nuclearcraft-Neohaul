package com.nred.nuclearcraft.info;

import com.nred.nuclearcraft.block.fluid.*;
import com.nred.nuclearcraft.fluid.GasFluid;
import com.nred.nuclearcraft.fluid.NCSourceFluid;
import com.nred.nuclearcraft.handler.SizedChanceFluidIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.FlowingFluid;
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
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.Registers.*;

public class Fluids {
    public final DeferredHolder<Fluid, BaseFlowingFluid> still;
    public final DeferredHolder<Fluid, BaseFlowingFluid> flowing;
    public final boolean gaseous;
    private BaseFlowingFluid.Properties properties;
    public final DeferredItem<BucketItem> bucket;
    public final IClientFluidTypeExtensions client;
    public final DeferredBlock<? extends NCFluidBlock> block;
    public final DeferredHolder<FluidType, FluidType> type;

    public static final TypeInfo ACID_TYPE = new TypeInfo(8, false, "liquid_still", "liquid_flow", FluidType.Properties.create(), AcidFluidBlock::new);
    public static final TypeInfo CHOCOLATE_TYPE = new TypeInfo(6, false, "molten_still", "molten_flow", FluidType.Properties.create().density(1325).viscosity(5000).temperature(330).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), ChocolateFluidBlock::new);
    public static final TypeInfo COOLANT_TYPE = new TypeInfo(4, false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(15000).temperature(400).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), CoolantFluidBlock::new);
    public static final TypeInfo CRYOTHEUM_TYPE = new TypeInfo(8, false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(8000).temperature(50).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), CryotheumFluidBlock::new);
    public static final TypeInfo FISSION_TYPE = new TypeInfo(8, false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(8000).temperature(1200).lightLevel(10).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), FissionFluidBlock::new);
    public static final TypeInfo FLAMMABLE_TYPE = new TypeInfo(8, false, "liquid_still", "liquid_flow", FluidType.Properties.create().density(800).viscosity(800).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), FlammableFluidBlock::new);
    public static final TypeInfo GAS_TYPE = new TypeInfo(8, true, "gas", "gas", FluidType.Properties.create().density(-10).viscosity(40).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH), GasFluidBlock::new);
    public static final TypeInfo GLOWSTONE_TYPE = new TypeInfo(8, false, "molten_still", "molten_flow", FluidType.Properties.create().density(-500).viscosity(8000).temperature(1200).lightLevel(15).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), GlowstoneFluidBlock::new);
    public static final TypeInfo HOT_COOLANT_TYPE = new TypeInfo(4, false, "molten_still", "molten_flow", FluidType.Properties.create().density(4000).viscosity(10000).temperature(800).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), HotCoolantFluidBlock::new);
    public static final TypeInfo HOT_GAS_TYPE = new TypeInfo(8, true, "gas", "gas", FluidType.Properties.create().density(-10).viscosity(40).temperature(1000).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH), HotGasFluidBlock::new);
    public static final TypeInfo MOLTEN_TYPE = new TypeInfo(4, false, "molten_still", "molten_flow", FluidType.Properties.create().density(5000).viscosity(8000).temperature(1200).lightLevel(10).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), MoltenFluidBlock::new);
    public static final TypeInfo PARTICLE_TYPE = new TypeInfo(8, false, "particle", "particle", FluidType.Properties.create().density(10000).viscosity(40).lightLevel(7).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), ParticleFluidBlock::new);
    public static final TypeInfo PLASMA_TYPE = new TypeInfo(8, false, "plasma", "plasma", FluidType.Properties.create().density(50).viscosity(100).temperature(1000000).lightLevel(15).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH), PlasmaFluidBlock::new);
    public static final TypeInfo SALT_SOLUTION_TYPE = new TypeInfo(8, false, "salt_solution_still", "salt_solution_flow", FluidType.Properties.create().sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY), SaltSolutionFluidBlock::new);
    public static final TypeInfo STEAM_TYPE = new TypeInfo(8, true, "steam", "steam", FluidType.Properties.create().density(-10).viscosity(40).temperature(550).sound(SoundActions.BUCKET_FILL, SoundEvents.FIRE_EXTINGUISH).sound(SoundActions.BUCKET_EMPTY, SoundEvents.FIRE_EXTINGUISH), SteamFluidBlock::new);
    public static final TypeInfo SUGAR_TYPE = new TypeInfo(6, false, "molten_still", "molten_flow", FluidType.Properties.create().density(1150).viscosity(8000).temperature(350).sound(SoundActions.BUCKET_FILL, SoundEvents.BUCKET_FILL_LAVA).sound(SoundActions.BUCKET_EMPTY, SoundEvents.BUCKET_EMPTY_LAVA), SugarFluidBlock::new);

    private static BlockBehaviour.Properties blockProperties(int tint) {
        return BlockBehaviour.Properties.of().mapColor(ARGBtoMapColor(tint)).replaceable().noCollission().pushReaction(PushReaction.DESTROY).noLootTable().liquid();
    }

    public Fluids(String name, boolean same, int tint, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, same, tint, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, boolean same, int tint, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, tint, same, false, blockSource, blockFlowing, block);
    }

    public Fluids(String name, int tint, boolean same, boolean gaseous, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, tint, same, gaseous, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, int tint, boolean same, boolean gaseous, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, name, tint, same, gaseous, blockSource, blockFlowing, block);
    }

    public Fluids(String name, String file, int tint, boolean same, boolean gaseous, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, file, tint, same, gaseous, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, String file, int tint, boolean same, boolean gaseous, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, tint, new TypeInfo(8, gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create(), block), blockSource, blockFlowing);
    }

    public Fluids(String name, boolean same, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, same, false, tint, density, viscosity, temperature, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, boolean same, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, same, false, tint, density, viscosity, temperature, light_level, blockSource, blockFlowing, block);
    }

    public Fluids(String name, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, same, gaseous, tint, density, temperature, viscosity, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, name, same, gaseous, tint, density, viscosity, temperature, light_level, blockSource, blockFlowing, block);
    }

    public Fluids(String name, String file, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, file, same, gaseous, tint, density, temperature, viscosity, light_level, NCSourceFluid::new, BaseFlowingFluid.Flowing::new, block);
    }

    public Fluids(String name, String file, boolean same, boolean gaseous, int tint, int density, int temperature, int viscosity, int light_level, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        this(name, tint, new TypeInfo(8, gaseous, file + (same ? "" : "_still"), file + (same ? "" : "_flow"), FluidType.Properties.create().density(density).viscosity(viscosity).temperature(temperature).lightLevel(light_level), block), blockSource, blockFlowing);
    }

    public Fluids(String name, int tint, TypeInfo type, int temperature) {
        this(name, tint, type, temperature, NCSourceFluid::new, BaseFlowingFluid.Flowing::new);
    }

    public Fluids(String name, int tint, TypeInfo type, int temperature, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing) {
        this(name, tint, new TypeInfo(type.level, type.gaseous, type.still, type.flowing, type.properties.temperature(temperature), type.block), blockSource, blockFlowing);
    }

    public Fluids(String name, int tint, TypeInfo type) {
        this(name, tint, type, NCSourceFluid::new, BaseFlowingFluid.Flowing::new);
    }

    public Fluids(String name, int tint, TypeInfo type, BiFunction<BaseFlowingFluid.Properties, Integer, NCSourceFluid> blockSource, Function<BaseFlowingFluid.Properties, ? extends BaseFlowingFluid> blockFlowing) {
        this.type = FLUID_TYPES.register(name + "_type", () -> new FluidType(type.properties));
        this.gaseous = type.gaseous;

        if (gaseous) {
            this.still = FLUIDS.register(name, () -> new GasFluid(this.properties));
        } else {
            this.still = FLUIDS.register(name, () -> blockSource.apply(this.properties, type.level));
        }
        this.flowing = FLUIDS.register(name + "_flowing", () -> blockFlowing.apply(this.properties));
        this.block = BLOCKS.register(name, () -> type.block.apply(this.still.get(), blockProperties(tint)));
        this.bucket = ITEMS.register(name + "_bucket", () -> new BucketItem(this.still.get(), new Item.Properties().craftRemainder(Items.BUCKET).stacksTo(1)));
        this.properties = new BaseFlowingFluid.Properties(this.type, this.still, this.flowing).bucket(this.bucket).block(this.block).tickRate(new FluidType(type.properties).getViscosity() / 200);
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

    public record TypeInfo(int level, boolean gaseous, String still, String flowing, FluidType.Properties properties, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
        public TypeInfo(TypeInfo old, BiFunction<FlowingFluid, BlockBehaviour.Properties, ? extends NCFluidBlock> block) {
            this(old.level, old.gaseous, old.still, old.flowing, old.properties, block);
        }

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

    public static SizedChanceFluidIngredient sizedIngredient(Fluids input, int amount) {
        return SizedChanceFluidIngredient.of(input.still.get(), amount);
    }

    public static SizedChanceFluidIngredient sizedIngredient(Fluids input, int chance, int amount) {
        return SizedChanceFluidIngredient.of(input.still.get(), amount, chance, 0);
    }
}