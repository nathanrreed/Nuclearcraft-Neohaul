package com.nred.nuclearcraft.compat.jade;

import com.nred.nuclearcraft.block_entity.energy.ITileEnergy;
import com.nred.nuclearcraft.block_entity.internal.energy.EnergyTileWrapper;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.Capabilities;
import org.jetbrains.annotations.Nullable;
import snownee.jade.api.*;
import snownee.jade.api.view.*;
import snownee.jade.util.CommonProxy;

import java.util.List;
import java.util.Objects;

import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static snownee.jade.util.CommonProxy.getDefaultStorage;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEnergyStorage(Energy.INSTANCE, Object.class);
        registration.registerProgress(Progress.INSTANCE, Object.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEnergyStorageClient(Energy.INSTANCE);
        registration.registerProgressClient(Progress.INSTANCE);
    }

    public static @Nullable List<ViewGroup<CompoundTag>> wrapEnergyStorage(Accessor<?> accessor) {
        EnergyTileWrapper energyStorage = (EnergyTileWrapper) getDefaultStorage(accessor, Capabilities.EnergyStorage.BLOCK, Capabilities.EnergyStorage.ENTITY);
        if (energyStorage != null) {
            ViewGroup<CompoundTag> group = new ViewGroup<>(List.of(EnergyView.of(energyStorage.getEnergyStoredLong(), energyStorage.getMaxEnergyStoredLong())));
            group.getExtraData().putString("Unit", "FE");
            return List.of(group);
        } else {
            return null;
        }
    }

    public enum Energy implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, EnergyView> {
        INSTANCE;

        public ResourceLocation getUid() {
            return ncLoc("energy");
        }

        public List<ClientViewGroup<EnergyView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
            return groups.stream().map(($) -> {
                String unit = $.getExtraData().getString("Unit");
                return new ClientViewGroup<>($.views.stream().map((tag) -> EnergyView.read(tag, unit)).filter(Objects::nonNull).toList());
            }).toList();
        }

        public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
            if (accessor.getTarget() instanceof ITileEnergy) {
                return wrapEnergyStorage(accessor);
            } else {
                return null;
            }
        }

        public boolean shouldRequestData(Accessor<?> accessor) {
            return CommonProxy.hasDefaultEnergyStorage(accessor);
        }

        public int getDefaultPriority() {
            return 9999;
        }
    }

    public enum Progress implements IServerExtensionProvider<CompoundTag>, IClientExtensionProvider<CompoundTag, ProgressView> {
        INSTANCE;

        public ResourceLocation getUid() {
            return ncLoc("progress");
        }

        public List<ClientViewGroup<ProgressView>> getClientGroups(Accessor<?> accessor, List<ViewGroup<CompoundTag>> groups) {
            return groups.stream().map(($) -> new ClientViewGroup<>($.views.stream().map(ProgressView::read).toList())).toList();
        }

        public @Nullable List<ViewGroup<CompoundTag>> getGroups(Accessor<?> accessor) {
            if (accessor.getTarget() instanceof IProcessor<?, ?, ?> entity) {
                return List.of(new ViewGroup<>(List.of(ProgressView.create((float) (entity.getCurrentTime() / entity.getBaseProcessTime())))));
            } else {
                return null;
            }
        }
    }
}