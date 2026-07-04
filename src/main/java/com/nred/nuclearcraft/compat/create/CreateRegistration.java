package com.nred.nuclearcraft.compat.create;

import com.nred.nuclearcraft.block.item.NCItemBlock;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.api.stress.BlockStressValues;
import com.simibubi.create.content.kinetics.base.OrientedRotatingVisual;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.foundation.item.ItemDescription;
import com.simibubi.create.foundation.item.KineticStats;
import com.simibubi.create.foundation.item.TooltipModifier;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.NCServerConfig.create_bearing_max_stress;
import static com.nred.nuclearcraft.registration.CreativeTabsRegistration.MULTIBLOCKS_TAB;
import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.pickaxeOnly;

public class CreateRegistration {
    private static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    static {
        REGISTRATE.setCreativeTab(MULTIBLOCKS_TAB).setTooltipModifierFactory(item ->
                new ItemDescription.Modifier(item, FontHelper.Palette.STANDARD_CREATE)
                        .andThen(TooltipModifier.mapNull(KineticStats.create(item))));
    }

    public static BlockEntry<GeneratingKineticTurbineRotorBearingBlock> CREATE_TURBINE_ROTOR_BEARING;
    public static BlockEntityEntry<GeneratingKineticTurbineRotorBearingEntity> CREATE_TURBINE_ROTOR_BEARING_ENTITY;

    public static void register(IEventBus modEventBus) {
        REGISTRATE.setModEventBus(modEventBus);

        CREATE_TURBINE_ROTOR_BEARING = REGISTRATE.block("create_turbine_rotor_bearing", GeneratingKineticTurbineRotorBearingBlock::new)
                .initialProperties(SharedProperties::stone)
                .properties(BlockBehaviour.Properties::forceSolidOn)
                .transform(pickaxeOnly())
                .blockstate(new GeneratingKineticTurbineRotorGenerator()::generate)
                .onRegister(BlockStressValues.setGeneratorSpeed(256, true))
                .onRegister(block -> BlockStressValues.CAPACITIES.register(block, () -> create_bearing_max_stress))
                .item((block, properties) -> new NCItemBlock(block, properties, Component.translatable(MODID + ".tooltip.create_turbine_bearing")))
                .transform(customItemModel())
                .register();

        CREATE_TURBINE_ROTOR_BEARING_ENTITY = REGISTRATE
                .blockEntity("create_turbine_rotor_bearing", GeneratingKineticTurbineRotorBearingEntity::new)
                .visual(() -> OrientedRotatingVisual.of(AllPartialModels.SHAFT_HALF), true)
                .validBlocks(CREATE_TURBINE_ROTOR_BEARING)
                .renderer(() -> GeneratingKineticTurbineRotorBearingRenderer::new)
                .register();

        REGISTRATE.registerEventListeners(modEventBus);
    }
}