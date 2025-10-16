package com.nred.nuclearcraft.block_entity.info;

import com.google.common.collect.Lists;
import com.nred.nuclearcraft.block.processor.IProcessor;
import com.nred.nuclearcraft.handler.ContainerInfoBuilder;
import com.nred.nuclearcraft.payload.processor.ProcessorUpdatePacket;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.config.Config2.processor_power_multiplier;
import static com.nred.nuclearcraft.config.Config2.processor_time_multiplier;

public abstract class ProcessorContainerInfoBuilder<TILE extends BlockEntity & IProcessor<TILE, PACKET, INFO>, PACKET extends ProcessorUpdatePacket, INFO extends ProcessorContainerInfo<TILE, PACKET, INFO>, BUILDER extends ProcessorContainerInfoBuilder<TILE, PACKET, INFO, BUILDER>> extends ContainerInfoBuilder<BUILDER> {
    public final Class<TILE> tileClass;
//    public final Supplier<TILE> tileSupplier;

//	public final Class<? extends Container> containerClass;
//	public final MenuFunction menuFunction;
//
//	public final Class<? extends GuiContainer> guiClass;
//	public final GuiFunction<TILE> guiFunction;
//
//	public final ContainerFunction<TILE> configContainerFunction;
//	public final GuiFunction<TILE> configGuiFunction;
//
//	protected CreativeTabs creativeTab = NCTabs.machine;

    protected List<String> particles = new ArrayList<>();

    protected int inputTankCapacity = 16000;
    protected int outputTankCapacity = 16000;

    protected double defaultProcessTime = processor_time_multiplier;
    protected double defaultProcessPower = 0;

    protected boolean isGenerator = false;

    protected boolean consumesInputs = false;
    protected boolean losesProgress = false;

    protected String ocComponentName;

    protected ProcessorContainerInfoBuilder(String name, Class<TILE> tileClass) {
        super(name);

        this.tileClass = tileClass;
//        this.tileSupplier = tileSupplier;

//		this.containerClass = containerClass;
//		this.menuFunction = menuFunction;
//
//		this.guiClass = guiClass;
//		this.guiFunction = guiFunction;

//		this.configContainerFunction = configContainerFunction;
//		this.configGuiFunction = configGuiFunction;

        ocComponentName = MODID + "_" + name;
    }

//	protected ProcessorContainerInfoBuilder(String modId, String name, Class<TILE> tileClass, Supplier<TILE> tileSupplier, Class<? extends Container> containerClass, ContainerFunction<TILE> containerFunction, Class<? extends GuiContainer> guiClass, GuiInfoTileFunction<TILE> guiFunction) {
//		this(modId, name, tileClass, tileSupplier, containerClass, containerFunction, guiClass, GuiFunction.of(modId, name, containerFunction, guiFunction), ContainerSideConfig::new, GuiFunction.of(modId, name, ContainerSideConfig::new, proxy.clientGet(() -> GuiProcessor.SideConfig::new)));
//	}

//	public ProcessorBlockInfo<TILE> buildBlockInfo() {
//		return new ProcessorBlockInfo<>(modId, name, tileClass, tileSupplier, creativeTab, particles);
//	}

    public abstract INFO buildContainerInfo();

//	public BUILDER setCreativeTab(CreativeTabs tab) {
//		creativeTab = tab;
//		return getThis();
//	}

//	public BUILDER setCreativeTab(String tab) {
//		return setCreativeTab(NCTabs.getCreativeTab(tab));
//	}

    public BUILDER setParticles(String... particles) {
        this.particles = Lists.newArrayList(particles);
        return getThis();
    }

    public BUILDER setInputTankCapacity(int capacity) {
        inputTankCapacity = capacity;
        return getThis();
    }

    public BUILDER setOutputTankCapacity(int capacity) {
        outputTankCapacity = capacity;
        return getThis();
    }

    public BUILDER setDefaultProcessTime(double processTime) {
        defaultProcessTime = processor_time_multiplier * processTime;
        return getThis();
    }

    public BUILDER setDefaultProcessPower(double processPower) {
        defaultProcessPower = processor_power_multiplier * processPower;
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

    public BUILDER setOCComponentName(String ocComponentName) {
        this.ocComponentName = ocComponentName;
        return getThis();
    }
}