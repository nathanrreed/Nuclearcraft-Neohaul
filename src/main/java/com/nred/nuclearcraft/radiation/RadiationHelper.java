package com.nred.nuclearcraft.radiation;

import com.nred.nuclearcraft.block_entity.radiation.ITileRadiationEnvironment;
import com.nred.nuclearcraft.capability.radiation.IRadiation;
import com.nred.nuclearcraft.capability.radiation.entity.IEntityRads;
import com.nred.nuclearcraft.capability.radiation.resistance.IRadiationResistance;
import com.nred.nuclearcraft.capability.radiation.source.IRadiationSource;
import com.nred.nuclearcraft.radiation.RadPotionEffects.RadEffect;
import com.nred.nuclearcraft.util.ModCheck;
import com.nred.nuclearcraft.util.NCMath;
import com.nred.nuclearcraft.util.UnitHelper;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static com.nred.nuclearcraft.config.NCConfig.*;
import static com.nred.nuclearcraft.registration.AttachmentRegistration.RADIATION_SOURCE;
import static com.nred.nuclearcraft.registration.BlockRegistration.GEIGER_COUNTER_BLOCK;
import static com.nred.nuclearcraft.registration.CapabilityRegistration.*;
import static com.nred.nuclearcraft.registration.DataComponentRegistration.RADIATION_RESISTANCE_ITEM;
import static com.nred.nuclearcraft.registration.ItemRegistration.GEIGER_COUNTER;

public class RadiationHelper {

    // Capability Getters

    public static IEntityRads getEntityRadiation(LivingEntity entity) {
        return entity.getCapability(CAPABILITY_ENTITY_RADS, null);
    }

    public static IRadiationSource getRadiationSource(ItemStack provider) {
        return provider.getCapability(ITEM_CAPABILITY_RADIATION_SOURCE, null);
    }

    public static IRadiationSource getRadiationSource(ChunkAccess chunk) {
        return chunk.getData(RADIATION_SOURCE.get());
    }

    public static IRadiationResistance getRadiationResistance(ICapabilityProvider<BaseCapability<IRadiationResistance, Direction>, @Nullable Object, @Nullable IRadiationResistance> provider) {
        return provider.getCapability(CAPABILITY_RADIATION_RESISTANCE, null);
    }


    public static boolean hasInfiniteRadiationResistance(ICapabilityProvider provider) {
        IRadiationResistance resistance = getRadiationResistance(provider);
        return resistance != null && resistance.getTotalRadResistance() == Double.POSITIVE_INFINITY;
    }

//    public static IItemHandler getTileInventory(ICapabilityProvider provider, Direction side) { TODO
//        if (!(provider instanceof BlockEntity) || provider instanceof TileDummy || hasInfiniteRadiationResistance(provider) || !provider.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side)) {
//            return null;
//        }
//        return provider.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side);
//    }
//
//    public static IFluidHandler getTileTanks(ICapabilityProvider provider, Direction side) {
//        if (!(provider instanceof BlockEntity) || provider instanceof TileDummy || hasInfiniteRadiationResistance(provider) || !provider.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side)) {
//            return null;
//        }
//        return provider.getCapability(Capabilities.FLUID_HANDLER_CAPABILITY, side);
//    }

    // Radiation Level Modification

    public static void addToSourceBuffer(IRadiationSource source, double addedRadiation) {
        source.setRadiationBuffer(source.getRadiationBuffer() + addedRadiation);
    }

    /**
     * Only use for radiation leaks, etc.
     */
    public static void addToSourceRadiation(IRadiationSource source, double addedRadiation) {
        source.setRadiationLevel(source.getRadiationLevel() + addedRadiation);
    }

    // ITileRadiationEnvironment -> ChunkBuffer

    public static void addScrubbingFractionToChunk(IRadiationSource chunkSource, ITileRadiationEnvironment tile) {
        if (chunkSource == null) {
            return;
        }
        if (radiation_scrubber_non_linear) {
            if (tile.getRadiationContributionFraction() < 0D) {
                chunkSource.setEffectiveScrubberCount(chunkSource.getEffectiveScrubberCount() - tile.getRadiationContributionFraction());
            }
        } else {
            addToSourceBuffer(chunkSource, tile.getRadiationContributionFraction() * tile.getCurrentChunkRadiationBuffer());

            if (tile.getRadiationContributionFraction() < 0D) {
                chunkSource.setScrubbingFraction(chunkSource.getScrubbingFraction() - tile.getRadiationContributionFraction());
                chunkSource.setEffectiveScrubberCount(chunkSource.getEffectiveScrubberCount() - tile.getRadiationContributionFraction());
            }
        }
    }

    public static double getAltScrubbingFraction(double scrubbers) {
        return scrubbers <= 0D ? 0D : 1D - Math.pow(radiation_scrubber_param[0], -Math.pow(scrubbers / radiation_scrubber_param[1], Math.pow(scrubbers / radiation_scrubber_param[2] + 1D, 1D / radiation_scrubber_param[3])));
    }

    // ItemStack -> ChunkBuffer

    public static void transferRadiationFromStackToChunkBuffer(ItemStack stack, IRadiationSource chunkSource, double multiplier) {
        if (chunkSource == null) {
            return;
        }
        addToSourceBuffer(chunkSource, getRadiationFromStack(stack, multiplier));
    }

    public static double getRadiationFromStack(ItemStack stack, double multiplier) {
        if (stack.isEmpty()) {
            return 0D;
        }
        IRadiationSource stackSource = getRadiationSource(stack);
        return stackSource == null ? 0D : stackSource.getRadiationLevel() * stack.getCount() * multiplier;
    }

    // FluidStack -> ChunkBuffer

    public static double getRadiationFromFluid(FluidStack stack, double multiplier) {
        if (stack == null) {
            return 0D;
        }
        return RadSources.FLUID_MAP.getDouble(stack.getFluid().getFluidType().getDescriptionId()) * stack.getAmount() * multiplier / 1000D;
    }

    // Source -> ChunkBuffer

    public static void transferRadiationFromProviderToChunkBuffer(ICapabilityProvider provider, Direction side, IRadiationSource chunkSource) {
        if (chunkSource == null) {
            return;
        }

//        double rawRadiation = 0D;
//        if (radiation_hardcore_containers > 0D) {
//            IItemHandler inventory = getTileInventory(provider, side);
//            if (inventory != null) {
//                for (int i = 0; i < inventory.getSlots(); ++i) {
//                    ItemStack stack = inventory.getStackInSlot(i);
//                    rawRadiation += getRadiationFromStack(stack, radiation_hardcore_containers);
//                }
//            }
//
//            IFluidHandler tanks = getTileTanks(provider, side);
//            if (tanks != null) {
//                IFluidTankProperties[] props = tanks.getTankProperties();
//                if (props != null) {
//                    for (IFluidTankProperties prop : props) {
//                        FluidStack stack = prop.getContents();
//                        rawRadiation += getRadiationFromFluid(stack, radiation_hardcore_containers);
//                    }
//                }
//            }
//        }
//
//        IRadiationSource radiationSource = getRadiationSource(provider);
//        if (radiationSource != null) {
//            rawRadiation += radiationSource.getRadiationLevel();
//        }
//
//        if (provider instanceof ITileMultiblockPart<?, ?> part) {
//            IRadiationSource multiblockSource = part.getMultiblockRadiationSource();
//            if (multiblockSource != null) {
//                rawRadiation += multiblockSource.getRadiationLevel();
//            }
//        }
//
//        double resistance = 0D;
//        IRadiationResistance providerResistance = getRadiationResistance(provider);
//        if (providerResistance != null) {
//            resistance = providerResistance.getTotalRadResistance();
//        }
//
//        double radiation = rawRadiation <= 0D ? 0D : NCMath.sq(rawRadiation) / (rawRadiation + resistance);
//        addToSourceBuffer(chunkSource, radiation);
    }

//    // Inventory -> ChunkBuffer TODO
//
//    public static void transferRadsFromInventoryToChunkBuffer(Inventory inventory, IRadiationSource chunkSource) {
//        if (!radiation_hardcore_stacks) {
//            return;
//        }
//        for (ItemStack stack : inventory.items) {
//            if (!stack.isEmpty()) {
//                transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
//            }
//        }
//        for (ItemStack stack : inventory.armour) {
//            if (!stack.isEmpty()) {
//                transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
//            }
//        }
//        for (ItemStack stack : inventory.offhand) {
//            if (!stack.isEmpty()) {
//                transferRadiationFromProviderToChunkBuffer(stack, null, chunkSource);
//            }
//        }
//    }

    // Chunk Set Previous Radiation and Spread

    public static void spreadRadiationFromChunk(LevelChunk chunk, LevelChunk targetChunk) {
        if (chunk != null && chunk.loaded) {
            IRadiationSource chunkSource = getRadiationSource(chunk);
            if (chunkSource == null) {
                return;
            }

            if (targetChunk != null && targetChunk.loaded) {
                IRadiationSource targetChunkSource = getRadiationSource(targetChunk);
                if (targetChunkSource != null && !chunkSource.isRadiationNegligible() && (targetChunkSource.getRadiationLevel() == 0D || chunkSource.getRadiationLevel() / targetChunkSource.getRadiationLevel() > 1D + radiation_spread_gradient)) {
                    double radiationSpread = (chunkSource.getRadiationLevel() - targetChunkSource.getRadiationLevel()) * radiation_spread_rate;
                    chunkSource.setRadiationLevel(chunkSource.getRadiationLevel() - radiationSpread);
                    targetChunkSource.setRadiationLevel(targetChunkSource.getRadiationLevel() + radiationSpread * (1D - targetChunkSource.getScrubbingFraction()));
                }
            }

            chunkSource.setRadiationBuffer(0D);
            if (chunkSource.isRadiationNegligible()) {
                chunkSource.setRadiationLevel(0D);
            }
        }
    }

    // Player Radiation Resistance

    public static double getArmorInventoryRadResistance(LivingEntity entity) {
        if (entity == null) {
            return 0D;
        }
        double resistance = 0D;
        for (ItemStack armor : entity.getArmorSlots()) {
            resistance += getArmorRadResistance(armor);
        }

        if (ModCheck.curiosLoaded()) {
            IItemHandler baublesHandler = entity.getCapability(top.theillusivec4.curios.api.CuriosCapability.ITEM_HANDLER, null);
            if (baublesHandler != null) {
                for (int i = 0; i < baublesHandler.getSlots(); ++i) {
                    resistance += getArmorRadResistance(baublesHandler.getStackInSlot(i));
                }
            }
        }

        return resistance;
    }

    private static double getArmorRadResistance(ItemStack armor) {
        if (armor.isEmpty()) {
            return 0D;
        }
        double resistance = 0D;
        if (armor.has(RADIATION_RESISTANCE_ITEM)) {
            resistance += armor.get(RADIATION_RESISTANCE_ITEM).getTotalRadResistance();
        }
        return resistance;
    }

    // Entity Radiation Resistance

    public static double addRadsToEntity(IEntityRads entityRads, LivingEntity entity, double rawRadiation, boolean ignoreResistance, boolean ignoreMultipliers, double updateRate) {
        if (rawRadiation <= 0D) {
            return 0D;
        }
        if (!ignoreMultipliers) {
            if (entity.isInWater()) {
                rawRadiation *= radiation_swim_mult;
            } else if (radiation_rain_mult != 1D && entity.isInWater()) {
                rawRadiation *= radiation_rain_mult;
            }
        }
        double resistance = ignoreResistance ? Math.min(0D, entityRads.getInternalRadiationResistance()) : entityRads.getFullRadiationResistance();

        double addedRadiation = resistance > 0D ? NCMath.sq(rawRadiation) / (rawRadiation + resistance) : rawRadiation * (1D - resistance);
        entityRads.setTotalRads(entityRads.getTotalRads() + addedRadiation * updateRate, true);
        return addedRadiation;
    }

    public static double getEntityArmorRadResistance(LivingEntity entity) {
        double resistance = getArmorInventoryRadResistance(entity);
        if (radiation_horse_armor_public && entity instanceof Horse horse) {
            resistance += getHorseArmorRadResistance(horse);
        }
        return resistance;
    }

    private static double getHorseArmorRadResistance(Horse horse) {
        double resistance = 0D;
        CompoundTag compound = new CompoundTag();
//        horse.writeEntityToNBT(compound); TODO
//
//        ItemStack armour = new ItemStack(compound.getCompound("ArmorItem"));
//        if (ArmorHelper.isHorseArmor(armour.getItem())) {
//            resistance += getArmorRadResistance(armour);
//        }
        return resistance;
    }

    // Inventory -> Player

    public static double transferRadsFromInventoryToPlayer(IEntityRads playerRads, Player player, double updateRate) {
        double radiationLevel = 0D;
        Inventory inventory = player.getInventory();
        for (ItemStack stack : inventory.items) {
            if (!stack.isEmpty()) {
                radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
            }
        }
        for (ItemStack stack : inventory.armor) {
            if (!stack.isEmpty()) {
                radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
            }
        }
        for (ItemStack stack : inventory.offhand) {
            if (!stack.isEmpty()) {
                radiationLevel += transferRadsFromStackToPlayer(stack, playerRads, player, updateRate);
            }
        }
        return radiationLevel;
    }

    private static double transferRadsFromStackToPlayer(ItemStack stack, IEntityRads playerRads, Player player, double updateRate) {
        IRadiationSource stackSource = getRadiationSource(stack);
        if (stackSource == null) {
            return 0D;
        }
        return addRadsToEntity(playerRads, player, stackSource.getRadiationLevel() * stack.getCount(), false, false, updateRate);
    }

    // Source -> Player

    public static double transferRadsToPlayer(IRadiationSource source, IEntityRads playerRads, Player player, double updateRate) {
        if (source == null) {
            return 0D;
        }
        return addRadsToEntity(playerRads, player, source.getRadiationLevel(), false, false, updateRate);
    }

    // Biome -> Player
	
	/*public static double transferBackgroundRadsToPlayer(Biome biome, IEntityRads playerRads, EntityPlayer player, double updateRate) {
		Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
		if (biomeRadiation == null) {
			return 0D;
		}
		return addRadsToPlayer(playerRads, player, biomeRadiation, updateRate);
	}*/

    // Source -> Entity

    public static void transferRadsFromSourceToEntity(IRadiationSource source, IEntityRads entityRads, LivingEntity entity, double updateRate) {
        if (source == null) {
            return;
        }
        entityRads.setRadiationLevel(addRadsToEntity(entityRads, entity, source.getRadiationLevel(), false, false, updateRate));
    }

    // Biome -> Entity
	
	/*public static void transferBackgroundRadsToEntity(Biome biome, IEntityRads entityRads, EntityLiving entityLiving, double updateRate) {
		Double biomeRadiation = RadBiomes.RAD_MAP.get(biome);
		if (biomeRadiation != null) {
			entityRads.setRadiationLevel(addRadsToEntity(entityRads, entityLiving, biomeRadiation, updateRate));
		}
	}*/

    // Entity Symptoms

    public static void applyEntityEffects(LivingEntity entity, IEntityRads entityRads, double durationMult, List<Double> radLevelList, List<List<RadEffect>> radEffectLists, Object2ObjectMap<AttributeModifier, Holder<Attribute>> radAttributeMap) {
        int radLevelCount = radLevelList.size();
        if (radLevelCount != radEffectLists.size()) {
            return;
        }

        radAttributeMap.forEach((k, v) -> {
            AttributeInstance attributeInstance = entity.getAttributes().getInstance(v);
            if (attributeInstance != null) {
                attributeInstance.removeModifier(k);
            }
        });

        double radPercentage = entityRads.getRadsPercentage();

        for (int i = 0; i < radLevelCount; ++i) {
            final int j = radLevelCount - 1 - i;
            if (radPercentage >= radLevelList.get(j)) {
                for (RadEffect effect : radEffectLists.get(j)) {
                    effect.accept(entity, durationMult);
                }
                break;
            }
        }
    }

    // Radiation HUD

    public static boolean shouldShowHUD(Player player) {
        if (player.getCapability(CAPABILITY_ENTITY_RADS, null) == null) {
            return false;
        }
        if (!radiation_require_counter) {
            return true;
        }

        return player.getInventory().contains(GEIGER_COUNTER.toStack()) || player.getInventory().contains(GEIGER_COUNTER_BLOCK.toStack());
    }

    // Text Colours

    public static ChatFormatting getRadsTextColor(IEntityRads entityRads) {
        double radsPercent = entityRads.getRadsPercentage();
        return radsPercent < 30 ? ChatFormatting.WHITE : radsPercent < 50 ? ChatFormatting.YELLOW : radsPercent < 70 ? ChatFormatting.GOLD : radsPercent < 90 ? ChatFormatting.RED : ChatFormatting.DARK_RED;
    }

    public static ChatFormatting getRadiationTextColor(double radiation) {
        return radiation < 0.000000001D ? ChatFormatting.WHITE : radiation < 0.001D ? ChatFormatting.YELLOW : radiation < 0.1D ? ChatFormatting.GOLD : radiation < 1D ? ChatFormatting.RED : ChatFormatting.DARK_RED;
    }

    public static ChatFormatting getRadiationTextColor(IRadiation irradiated) {
        return getRadiationTextColor(irradiated.getRadiationLevel());
    }

    public static ChatFormatting getRawRadiationTextColor(IEntityRads entityRads) {
        return getRadiationTextColor(entityRads.getRawRadiationLevel());
    }

    public static ChatFormatting getFoodRadiationTextColor(double radiation) {
        return radiation <= -100D ? ChatFormatting.LIGHT_PURPLE : radiation <= -10D ? ChatFormatting.BLUE : radiation < 0D ? ChatFormatting.AQUA : radiation < 0.1D ? ChatFormatting.WHITE : radiation < 1D ? ChatFormatting.YELLOW : radiation < 10D ? ChatFormatting.GOLD : radiation < 100D ? ChatFormatting.RED : ChatFormatting.DARK_RED;
    }

    public static ChatFormatting getFoodResistanceTextColor(double resistance) {
        return resistance < 0D ? ChatFormatting.GRAY : ChatFormatting.WHITE;
    }

// Unit Prefixing

    public static String radsPrefix(double rads, boolean rate) {
        String unit = rate ? "Rad/t" : "Rad";
        return radiation_unit_prefixes > 0 ? NCMath.sigFigs(rads, radiation_unit_prefixes) + " " + unit : UnitHelper.prefix(rads, 3, unit);
    }

    public static String radsColoredPrefix(double rads, boolean rate) {
        return getRadiationTextColor(rads) + radsPrefix(rads, rate);
    }

// Rad Resistance Sig Figs

    public static String resistanceSigFigs(double resistance) {
        return NCMath.sigFigs(resistance, Math.max(2, radiation_unit_prefixes));
    }
}