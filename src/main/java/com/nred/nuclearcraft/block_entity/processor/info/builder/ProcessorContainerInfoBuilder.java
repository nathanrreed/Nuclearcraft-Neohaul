package com.nred.nuclearcraft.block_entity.processor.info.builder;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block_entity.processor.IProcessor;
import com.nred.nuclearcraft.block_entity.processor.info.ProcessorMenuInfo;
import com.nred.nuclearcraft.handler.ContainerInfoBuilder;
import com.nred.nuclearcraft.menu.MenuFunction;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;

public abstract class ProcessorContainerInfoBuilder<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorMenuInfo<TILE, PACKET, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ContainerInfoBuilder<BUILDER> {
    public final Class<TILE> tileClass;
    public final MenuFunction<TILE> menuFunction;
    public final BiFunction<BlockPos, BlockState, TILE> tileSupplier;

    public List<String> particles = new ArrayList<>();

    public int inputTankCapacity = 16000;
    public int outputTankCapacity = 16000;

    public Supplier<Integer> defaultProcessTime = () -> 1;
    public Supplier<Integer> defaultProcessPower = () -> 0;

    public boolean isGenerator = false;

    public boolean consumesInputs = false;
    public boolean losesProgress = false;

    public String ccComponentName;

    protected ProcessorContainerInfoBuilder(String name, Class<TILE> tileClass, BiFunction<BlockPos, BlockState, TILE> tileSupplier, MenuFunction<TILE> menuFunction) {
        super(name);

        this.tileClass = tileClass;
        this.menuFunction = menuFunction;
        this.tileSupplier = tileSupplier;

        ccComponentName = MODID + "_" + name;
    }

    public ProcessorBlockInfo<TILE> buildBlockInfo() {
        return new ProcessorBlockInfo<>(name, tileClass, tileSupplier, particles);
    }

    public abstract INFO buildContainerInfo();

    public BUILDER setParticles(String... particles) {
        this.particles = Lists.newArrayList(particles);
        return getThis();
    }

    public BUILDER setDefaultProcessTime(Supplier<Integer> processTime) {
        defaultProcessTime = processTime;
        return getThis();
    }

    public BUILDER setDefaultProcessPower(Supplier<Integer> processPower) {
        defaultProcessPower = processPower;
        return getThis();
    }

    public BUILDER setIsGenerator(boolean isGenerator) {
        this.isGenerator = isGenerator;
        if (isGenerator) {
            consumesInputs = true;
            losesProgress = false;
        }
        return getThis();
    }

    public BUILDER setConsumesInputs(boolean consumesInputs) {
        this.consumesInputs = consumesInputs;
        return getThis();
    }

    public BUILDER setLosesProgress(boolean losesProgress) {
        this.losesProgress = losesProgress;
        return getThis();
    }
}