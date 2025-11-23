package com.nred.nuclearcraft.registration;

import com.nred.nuclearcraft.capability.radiation.resistance.RadiationResistanceItem;
import com.nred.nuclearcraft.handler.ItemUseHandler;
import com.nred.nuclearcraft.handler.PlayerRespawnHandler;
import com.nred.nuclearcraft.handler.TileInfoHandler;
import com.nred.nuclearcraft.info.Fluids;
import com.nred.nuclearcraft.item.MultitoolItem;
import com.nred.nuclearcraft.multiblock.PlacementRule;
import com.nred.nuclearcraft.radiation.*;
import com.nred.nuclearcraft.radiation.environment.RadiationEnvironmentHandler;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.RecipeHelper;
import com.nred.nuclearcraft.recipe.RecipeStats;
import com.nred.nuclearcraft.util.ModCheck;
import com.nred.nuclearcraft.worldgen.biome.NuclearWastelandBiome;
import com.nred.nuclearcraft.worldgen.region.NuclearWastelandRegion;
import mekanism.api.radiation.IRadiationManager;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.damagesource.DamageContainer;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.ModifyDefaultComponentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;
import net.neoforged.neoforge.fluids.FluidInteractionRegistry;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Concat.fluidValues;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.radiation.RadArmor.addArmorShieldingRecipes;
import static com.nred.nuclearcraft.registration.DamageTypeRegistration.*;
import static com.nred.nuclearcraft.registration.DataComponentRegistration.RADIATION_RESISTANCE_ITEM;
import static com.nred.nuclearcraft.registration.EntityRegistration.FERAL_GHOUL;
import static com.nred.nuclearcraft.registration.FluidRegistration.*;
import static com.nred.nuclearcraft.registration.ItemRegistration.*;

@EventBusSubscriber(modid = MODID)
public class CommonSetup {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) { // TODO is this the right event type
        ModCheck.init();

        PlacementRule.init();
        MultitoolItem.registerRightClickLogic();

        TileInfoHandler.init();

        RadSources.refreshRadSources(false);
        RadArmor.init();

        // TerraBlender
        Regions.register(new NuclearWastelandRegion(ncLoc("nuclear_wasteland"), 2));
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, NuclearWastelandBiome.makeRules());
        SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, MODID, NuclearWastelandBiome.makeRules());

        // Add Fluid Mixing
        for (Fluids fluid : fluidValues(GAS_MAP, MOLTEN_MAP, HOT_GAS_MAP, SUGAR_MAP, CHOCOLATE_MAP, FISSION_FLUID_MAP, STEAM_MAP, SALT_SOLUTION_MAP, ACID_MAP, FLAMMABLE_MAP, HOT_COOLANT_MAP, COOLANT_MAP, CUSTOM_FLUID_MAP, FISSION_FUEL_MAP)) {
            if (fluid.block.get().getSourceMixingState() != null && fluid.block.get().getFlowingMixingState() != null) {
                FluidInteractionRegistry.addInteraction(fluid.type.value(), new FluidInteractionRegistry.InteractionInformation(
                        NeoForgeMod.WATER_TYPE.value(),
                        fluidState -> fluidState.isSource() ? fluid.block.get().getSourceMixingState() : fluid.block.get().getFlowingMixingState()));
            }
            if (fluid.block.get().getFlowingIntoWaterState() != null) {
                FluidInteractionRegistry.addInteraction(NeoForgeMod.WATER_TYPE.value(), new FluidInteractionRegistry.InteractionInformation(
                        fluid.type.value(),
                        fluidState -> fluid.block.get().getFlowingIntoWaterState()));
            }
        }
    }

    @SubscribeEvent
    public static void postInit(ServerAboutToStartEvent event) { // TODO is this the right event type
        PlacementRule.postInit();
        NCRecipes.init(event.getServer().getRecipeManager());
        RecipeStats.init();

        // TODO ADD

        addArmorShieldingRecipes(event);

        RadArmor.postInit();
        RadDimensions.init();
        RadPotionEffects.init();
        RadSources.postInit();
        RadStructures.init();
        RadEntities.init();

//        NeoForge.EVENT_BUS.register(new RadiationCapabilityHandler()); TODO
        NeoForge.EVENT_BUS.register(new RadiationHandler());
        NeoForge.EVENT_BUS.register(new RadiationEnvironmentHandler());

        NeoForge.EVENT_BUS.register(new PlayerRespawnHandler());
        NeoForge.EVENT_BUS.register(new ItemUseHandler());
    }

    @SubscribeEvent
    public static void modifyComponents(ModifyDefaultComponentsEvent event) {
        RadArmor.postInit(); // TODO maybe not a good place for this
        for (Item item : List.of(HAZMAT_HELMET.asItem(), HAZMAT_CHESTPLATE.asItem(), HAZMAT_LEGGINGS.asItem(), HAZMAT_BOOTS.asItem())) {
            event.modify(item, builder ->
                    builder.set(RADIATION_RESISTANCE_ITEM.get(), new RadiationResistanceItem(RadArmor.ARMOR_RAD_RESISTANCE_MAP.get(RecipeHelper.pack(item))))
            );
        }
    }

    @SubscribeEvent
    public static void addToCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(FERAL_GHOUL_SPAWN_EGG);
        }
    }

    @SubscribeEvent
    public static void addSpawnPlacement(RegisterSpawnPlacementsEvent event) {
        event.register(FERAL_GHOUL.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Monster::checkMonsterSpawnRules, RegisterSpawnPlacementsEvent.Operation.OR);
    }

    @SubscribeEvent
    public static void onLivingDamagePre(LivingDamageEvent.Pre event) {
        if (event.getSource().is(IRadiationManager.INSTANCE.getRadiationDamageTypeKey()) || event.getSource().is(ACID_BURN) || event.getSource().is(CORIUM_BURN) || event.getSource().is(HOT_COOLANT_BURN)) {
            for (ItemStack stack : event.getEntity().getArmorSlots()) {
                RadiationResistanceItem rad_resistance = stack.get(RADIATION_RESISTANCE_ITEM.get());
                if (rad_resistance != null)
                    event.getContainer().addModifier(DamageContainer.Reduction.ARMOR, (container, reductionIn) -> (float) (reductionIn + rad_resistance.getTotalRadResistance())); // TODO check if this is the right way to do this
            }
        }
    }
}