package com.nred.nuclearcraft.payload;

import com.nred.nuclearcraft.recipe.RecipeUnitInfo;
import com.nred.nuclearcraft.block_entity.internal.fluid.Tank.TankInfo;
import com.nred.nuclearcraft.util.PosHelper;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public abstract class NCPacket implements CustomPacketPayload {
    public void sendToAll() {
        PacketDistributor.sendToAllPlayers(this);
    }

    public void sendTo(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            PacketDistributor.sendToPlayer(serverPlayer, this);
        }
    }

    public <T extends Player> void sendTo(Iterable<T> players) {
        for (T player : players) {
            sendTo(player);
        }
    }

    //	public void sendToAllAround(NetworkRegistry.TargetPoint point) {
//		getWrapper().sendToAllAround(this, point);
//	}
//
//	public void sendToAllTracking(NetworkRegistry.TargetPoint point) {
//		getWrapper().sendToAllTracking(this, point);
//	}
//
//	public void sendToAllTracking(Entity entity) {
//		getWrapper().sendToAllTracking(this, entity);
//	}
//
//	public void sendToDimension(int dimensionId) {
//		getWrapper().sendToDimension(this, dimensionId);
//	}
//
	public void sendToServer() {
        PacketDistributor.sendToServer(this);
	}

    public void toBytes(RegistryFriendlyByteBuf buf) {
    }

    protected static String readString(RegistryFriendlyByteBuf buf) {
        return ByteBufCodecs.STRING_UTF8.decode(buf);
    }

    protected static void writeString(RegistryFriendlyByteBuf buf, String string) {
        ByteBufCodecs.STRING_UTF8.encode(buf, string);
    }

    protected static ResourceLocation readLocation(RegistryFriendlyByteBuf buf) {
        return ResourceLocation.STREAM_CODEC.decode(buf);
    }

    protected static void writeLocation(RegistryFriendlyByteBuf buf, ResourceLocation location) {
        ResourceLocation.STREAM_CODEC.encode(buf, location);
    }

    protected static BlockPos readPos(RegistryFriendlyByteBuf buf) {
        return BlockPos.STREAM_CODEC.decode(buf);
    }

    protected static void writePos(RegistryFriendlyByteBuf buf, BlockPos pos) {
        if (pos == null) {
            pos = PosHelper.DEFAULT_NON;
        }
        BlockPos.STREAM_CODEC.encode(buf, pos);
    }

    protected static IntList readInts(RegistryFriendlyByteBuf buf) {
        return IntList.of(ByteBufCodecs.INT.apply(ByteBufCodecs.list()).decode(buf).stream().mapToInt(Integer::intValue).toArray());
    }

    protected static void writeInts(RegistryFriendlyByteBuf buf, IntList ints) {
        ByteBufCodecs.INT.apply(ByteBufCodecs.list()).encode(buf, ints);
    }

    protected static ItemStack readStack(RegistryFriendlyByteBuf buf) {
        return ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
    }

    protected static void writeStack(RegistryFriendlyByteBuf buf, ItemStack stack) {
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, stack);
    }

    protected static List<ItemStack> readStacks(RegistryFriendlyByteBuf buf) {
        return ItemStack.OPTIONAL_LIST_STREAM_CODEC.decode(buf);
    }

    protected static void writeStacks(RegistryFriendlyByteBuf buf, List<ItemStack> stacks) {
        ItemStack.OPTIONAL_LIST_STREAM_CODEC.encode(buf, stacks);
    }

    protected static List<TankInfo> readTankInfos(RegistryFriendlyByteBuf buf) {
        int count = buf.readInt();
        List<TankInfo> infos = new ArrayList<>();
        for (int i = 0; i < count; ++i) {
            infos.add(new TankInfo(readLocation(buf), buf.readInt()));
        }
        return infos;
    }

    protected static void writeTankInfos(RegistryFriendlyByteBuf buf, List<TankInfo> infos) {
        buf.writeInt(infos.size());
        for (TankInfo info : infos) {
            writeLocation(buf, info.location());
            buf.writeInt(info.amount());
        }
    }

    protected static RecipeUnitInfo readRecipeUnitInfo(RegistryFriendlyByteBuf buf) {
        return new RecipeUnitInfo(readString(buf), buf.readInt(), buf.readDouble());
    }

    protected static void writeRecipeUnitInfo(RegistryFriendlyByteBuf buf, RecipeUnitInfo info) {
        writeString(buf, info.unit);
        buf.writeInt(info.startingPrefix);
        buf.writeDouble(info.rateMultiplier);
    }
}