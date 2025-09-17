package com.nred.nuclearcraft.menu.turbine;

import com.nred.nuclearcraft.block.turbine.TurbineControllerEntity;
import com.nred.nuclearcraft.menu.AbstractControllerMenu;
import com.nred.nuclearcraft.multiblock.turbine.Turbine;
import it.zerono.mods.zerocore.lib.block.AbstractModBlockEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;

import static com.nred.nuclearcraft.registration.BlockRegistration.TURBINE_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.TURBINE_CONTROLLER_MENU_TYPE;

public class TurbineControllerMenu extends AbstractControllerMenu {
    //    public static final int INPUT_1 = 2;
//    public static final int INPUT_2 = 3;
//    public static final int INPUT_3 = 4;
//    public static final int INPUT_4 = 5;
//    public static final int OUTPUT = 6;
    public final Turbine turbine;

    public TurbineControllerMenu(int containerId, Inventory inventory, ContainerLevelAccess access, final TurbineControllerEntity controller) {
        super(TURBINE_CONTROLLER_MENU_TYPE.get(), containerId, inventory, access);
//
//        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_1, 46, 31)));
//        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_2, 66, 31)));
//        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_3, 46, 51)));
//        ITEM_INPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, INPUT_4, 66, 51)));
//        ITEM_OUTPUTS.add(this.addSlot(new CustomSlotItemHandler(itemHandler, OUTPUT, 126, 41)));

        turbine = controller.getMultiblockController().orElseThrow(IllegalStateException::new);
    }

    // Client Constructor
    public TurbineControllerMenu(int containerId, Inventory inventory, FriendlyByteBuf extraData) {
        this(containerId, inventory, ContainerLevelAccess.NULL, AbstractModBlockEntity.getGuiClientBlockEntity(extraData));
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, TURBINE_MAP.get("turbine_controller").get());
    }
}