package com.nred.nuclearcraft.block.fission.manager;

import com.nred.nuclearcraft.block.GenericActiveDirectionalTooltipDeviceBlock;
import com.nred.nuclearcraft.block.IActivatable;
import com.nred.nuclearcraft.block_entity.fission.manager.FissionManagerEntity;
import com.nred.nuclearcraft.block_entity.fission.manager.IFissionManagerListener;
import it.zerono.mods.zerocore.lib.block.multiblock.IMultiblockPartType;
import it.zerono.mods.zerocore.lib.multiblock.IMultiblockController;

public abstract class FissionManagerBlock<Controller extends IMultiblockController<Controller>, PartType extends IMultiblockPartType, MANAGER extends FissionManagerEntity<MANAGER, LISTENER>, LISTENER extends IFissionManagerListener<MANAGER, LISTENER>> extends GenericActiveDirectionalTooltipDeviceBlock<Controller, PartType> implements IActivatable {
    protected final Class<MANAGER> managerClass;

    public FissionManagerBlock(MultiblockPartProperties<PartType> properties, Class<MANAGER> managerClass) {
        super(properties);
        this.managerClass = managerClass;
    }
}