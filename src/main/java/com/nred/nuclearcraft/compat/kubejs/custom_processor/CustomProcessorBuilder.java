package com.nred.nuclearcraft.compat.kubejs.custom_processor;

import com.nred.nuclearcraft.block.processor.ProcessorBlock;
import com.nred.nuclearcraft.block_entity.processor.ProcessorEntityImpl.BasicEnergyProcessorEntityDyn;
import com.nred.nuclearcraft.block_entity.processor.ProcessorEntityImpl.BasicUpgradableEnergyProcessorEntityDyn;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.block_entity.processor.info.builder.ProcessorMenuInfoBuilderImpl.BasicUpgradableProcessorMenuInfoBuilder;
import com.nred.nuclearcraft.compat.recipe_viewer.info.RecipeViewerProcessorCategoryInfo;
import com.nred.nuclearcraft.menu.processor.ProcessorMenuImpl;
import com.nred.nuclearcraft.recipe.NCRecipes;
import com.nred.nuclearcraft.recipe.NCRecipes.BasicProcessorRecipeHandlerDyn;
import com.nred.nuclearcraft.util.ContainerInfoHelper;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.block.entity.BlockEntityInfo;
import dev.latvian.mods.kubejs.client.LangKubeEvent;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.StringUtilsWrapper;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.List;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.registerProcessorInfo;
import static com.nred.nuclearcraft.handler.BlockEntityInfoHandler.registerRecipeViewerCategoryInfo;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.registration.BlockEntityRegistration.PROCESSOR_ENTITY_TYPE;
import static com.nred.nuclearcraft.registration.BlockRegistration.PROCESSOR_MAP;
import static com.nred.nuclearcraft.registration.MenuRegistration.PROCESSOR_MENU_TYPES;
import static com.nred.nuclearcraft.registration.RecipeTypeRegistration.PROCESSOR_RECIPE_DYN_TYPES;

@ReturnsSelf
public class CustomProcessorBuilder extends BlockBuilder {
    private static final ResourceLocation MODEL = ncLoc("block/processor");

    private final ProcessorMenuInfoBuilder<?, ?, ?, ?, ?> info_handler;

    private String menuTitle = null;
    private String recipeViewerTitle = null;

    public CustomProcessorBuilder(ResourceLocation i, boolean upgradable) {
        super(i);

        defaultTags.add(BlockTags.MINEABLE_WITH_PICKAXE.location());

        String name = this.id.toString();

        if (upgradable) {
            info_handler = new BasicUpgradableProcessorMenuInfoBuilder<>(name, BasicUpgradableEnergyProcessorEntityDyn.class, (pos, blockState) -> new BasicUpgradableEnergyProcessorEntityDyn(pos, blockState, name), (containerId, inventory, entity) -> new ProcessorMenuImpl.BasicUpgradableEnergyProcessorMenuDyn(PROCESSOR_MENU_TYPES.get(name).get(), containerId, inventory, entity));
        } else {
            info_handler = new BasicProcessorMenuInfoBuilder<>(name, BasicEnergyProcessorEntityDyn.class, (pos, blockState) -> new BasicEnergyProcessorEntityDyn(pos, blockState, name), (containerId, inventory, entity) -> new ProcessorMenuImpl.BasicEnergyProcessorMenuDyn(PROCESSOR_MENU_TYPES.get(name).get(), containerId, inventory, entity));
        }

        blockEntityInfo = new BlockEntityInfo(this);
        itemBuilder = new CustomProcessorItemBuilder(this.id);
    }

    public static class NCSlots {
        public static int[] standardSlot(int x, int y) {
            return ContainerInfoHelper.standardSlot(x, y);
        }

        public static int[] bigSlot(int x, int y) {
            return ContainerInfoHelper.bigSlot(x, y);
        }
    }

    @Info("Sets GUI's width and height")
    public CustomProcessorBuilder setGuiWH(int w, int h) {
        info_handler.setGuiWH(w, h);
        return this;
    }

    @Info("Sets item input slots sizes and positions")
    public CustomProcessorBuilder setItemInputSlots(List<?> slots) {
        info_handler.setItemInputSlots(slots.toArray(int[][]::new));
        return this;
    }

    @Info("Sets item input slots sizes and positions")
    public CustomProcessorBuilder setFluidInputSlots(List<?> slots) {
        info_handler.setFluidInputSlots(slots.toArray(int[][]::new));
        return this;
    }

    @Info("Sets item input slots sizes and positions")
    public CustomProcessorBuilder setItemOutputSlots(List<?> slots) {
        info_handler.setItemOutputSlots(slots.toArray(int[][]::new));
        return this;
    }

    @Info("Sets item input slots sizes and positions")
    public CustomProcessorBuilder setFluidOutputSlots(List<?> slots) {
        info_handler.setFluidOutputSlots(slots.toArray(int[][]::new));
        return this;
    }

    @Info("Sets item x and y coords of the players inventory")
    public CustomProcessorBuilder setPlayerGuiXY(int x, int y) {
        info_handler.setPlayerGuiXY(x, y);
        return this;
    }

    @Info("Sets the x, y, width, height, u, v of the progress bar")
    public CustomProcessorBuilder setProgressBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
        info_handler.setProgressBarGuiXYWHUV(x, y, w, h, u, v);
        return this;
    }

    @Info("Sets the x, y, width, height, u, v of the energy bar")
    public CustomProcessorBuilder setEnergyBarGuiXYWHUV(int x, int y, int w, int h, int u, int v) {
        info_handler.setEnergyBarGuiXYWHUV(x, y, w, h, u, v);
        return this;
    }

    @Info("Sets item x and y coords of the machine config")
    public CustomProcessorBuilder setMachineConfigGuiXY(int x, int y) {
        info_handler.setMachineConfigGuiXY(x, y);
        return this;
    }

    @Info("Sets item x and y coords of the redstone config")
    public CustomProcessorBuilder setRedstoneControlGuiXY(int x, int y) {
        info_handler.setRedstoneControlGuiXY(x, y);
        return this;
    }

    @Info("Sets recipe handler's name")
    public CustomProcessorBuilder setRecipeHandlerName(String recipeHandlerName) {
        info_handler.setRecipeHandlerName(recipeHandlerName);
        return this;
    }

    @Info("Sets texture for use in recipe viewers")
    public CustomProcessorBuilder setRecipeViewerTexture(String texture) {
        info_handler.setRecipeViewerTexture(texture);
        return this;
    }

    @Info("Sets texture for use in the processor screen")
    public CustomProcessorBuilder setScreenTexture(String texture) {
        info_handler.setScreenTexture(texture);
        return this;
    }

    @Info("Sets x, y, width and height of the recipe viewer background")
    public CustomProcessorBuilder setRecipeViewerBackgroundXYWH(int x, int y, int w, int h) {
        info_handler.setRecipeViewerBackgroundXYWH(x, y, w, h);
        return this;
    }

    @Info("Sets x, y, width and height of the recipe viewer tooltip area")
    public CustomProcessorBuilder setRecipeViewerTooltipXYWH(int x, int y, int w, int h) {
        info_handler.setRecipeViewerTooltipXYWH(x, y, w, h);
        return this;
    }

    @Info("Sets x, y, width and height of the recipe viewer clickable area")
    public CustomProcessorBuilder setRecipeViewerClickAreaXYWH(int x, int y, int w, int h) {
        info_handler.setRecipeViewerClickAreaXYWH(x, y, w, h);
        return this;
    }

    @Info("Sets the title in the processors menu")
    public CustomProcessorBuilder setMenuTitle(String title) {
        menuTitle = title;
        return this;
    }

    @Info("Sets the title in the recipe viewer")
    public CustomProcessorBuilder setRecipeViewerTitle(String title) {
        recipeViewerTitle = title;
        return this;
    }

    @Info("Sets x and y to be extend from the standard size")
    public CustomProcessorBuilder standardExtend(int x, int y) {
        info_handler.standardExtend(x, y);
        return this;
    }

    @Info("Disables the progress bar")
    public CustomProcessorBuilder disableProgressBar() {
        info_handler.disableProgressBar();
        return this;
    }

    @Info("Sets the particles given off by the processor")
    public CustomProcessorBuilder setParticles(String... particles) {
        info_handler.setParticles(particles);
        return this;
    }

    @Info("Sets default time to complete and operation")
    public CustomProcessorBuilder setInputTankCapacity(int capacity) {
        info_handler.setInputTankCapacity(capacity);
        return this;
    }

    @Info("Sets default power to complete and operation")
    public CustomProcessorBuilder setOutputTankCapacity(int capacity) {
        info_handler.setOutputTankCapacity(capacity);
        return this;
    }

    @Info("Sets default time to complete and operation")
    public CustomProcessorBuilder setDefaultProcessTime(int processTime) {
        info_handler.setDefaultProcessTime(processTime);
        return this;
    }

    @Info("Sets default power to complete and operation")
    public CustomProcessorBuilder setDefaultProcessPower(int processPower) {
        info_handler.setDefaultProcessPower(processPower);
        return this;
    }

    @Info("Sets whether processor is a generator of power")
    public CustomProcessorBuilder setIsGenerator(boolean isGenerator) {
        info_handler.setIsGenerator(isGenerator);
        return this;
    }

    @Info("Sets whether processor consumes inputs instantly")
    public CustomProcessorBuilder setConsumesInputs(boolean consumesInputs) {
        info_handler.setConsumesInputs(consumesInputs);
        return this;
    }

    @Info("Sets whether processor can lose progress")
    public CustomProcessorBuilder setLosesProgress(boolean losesProgress) {
        info_handler.setLosesProgress(losesProgress);
        return this;
    }

    @Info("Sets x, y, width and height of the speed upgrade slot")
    public CustomProcessorBuilder setSpeedUpgradeSlot(int x, int y, int w, int h) {
        if (info_handler instanceof BasicUpgradableProcessorMenuInfoBuilder<?, ?, ?> upgradable_info_handler) {
            upgradable_info_handler.setSpeedUpgradeSlot(x, y, w, h);
        } else {
            throw new RuntimeException("Tried to add upgrade slot to non upgradable processor");
        }
        return this;
    }

    @Info("Sets x, y, width and height of the energy upgrade slot")
    public CustomProcessorBuilder setEnergyUpgradeSlot(int x, int y, int w, int h) {
        if (info_handler instanceof BasicUpgradableProcessorMenuInfoBuilder<?, ?, ?> upgradable_info_handler) {
            upgradable_info_handler.setEnergyUpgradeSlot(x, y, w, h);
        } else {
            throw new RuntimeException("Tried to add upgrade slot to non upgradable processor");
        }
        return this;
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        ResourceLocation modelActive = newID("block/", "_on");
        ResourceLocation modelInactive = newID("block/", "_off");

        bs.variant("facing=east,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(90));
        bs.variant("facing=east,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(90));
        bs.variant("facing=north,nuclearcraftneohaul_active=false", v -> v.model(modelInactive));
        bs.variant("facing=north,nuclearcraftneohaul_active=true", v -> v.model(modelActive));
        bs.variant("facing=south,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(180));
        bs.variant("facing=south,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(180));
        bs.variant("facing=west,nuclearcraftneohaul_active=false", v -> v.model(modelInactive).y(270));
        bs.variant("facing=west,nuclearcraftneohaul_active=true", v -> v.model(modelActive).y(270));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator gen) {
        gen.blockModel(id.withSuffix("_on"), m -> {
            m.parent(MODEL);
            m.texture("front", baseTexture + "_on");
        });
        gen.blockModel(id.withSuffix("_off"), m -> {
            m.parent(MODEL);
            m.texture("front", baseTexture + "_off");
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        m.parent(MODEL);
        m.texture("front", baseTexture + "_off");
    }

    @Override
    public void generateLang(LangKubeEvent lang) {
        super.generateLang(lang);

        String name;
        if (displayName != null) {
            name = displayName.getString();
        } else {
            name = StringUtilsWrapper.snakeCaseToTitleCase(id.getPath());
        }
        lang.add(id.getNamespace(), MODID + ".menu.title." + id, menuTitle != null ? menuTitle : name);
        lang.add("emi", "emi.category." + id.getNamespace() + "." + id.getPath(), recipeViewerTitle != null ? recipeViewerTitle : name);
    }

    @Override
    public Block createObject() {
        String name = this.id.toString();

        if (PROCESSOR_MAP.containsKey(name)) {
            throw new RuntimeException("Attempted to be override " + name + " Processor");
        }

        PROCESSOR_MAP.put(name, DeferredBlock.createBlock(this.id));
        return new ProcessorBlock<>(name);
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        String name = this.id.toString();

        if (PROCESSOR_MENU_TYPES.containsKey(name) || PROCESSOR_ENTITY_TYPE.containsKey(name) || PROCESSOR_RECIPE_DYN_TYPES.containsKey(name)) {
            throw new RuntimeException("Attempted to be override " + name + " Processor");
        }

        registerProcessorInfo(info_handler);
        registerRecipeViewerCategoryInfo(new RecipeViewerProcessorCategoryInfo<>(name, List.of(DeferredBlock.createBlock(id))));

        ((BlockItemBuilder) itemBuilder).blockBuilder = this;
        registry.add(Registries.ITEM, itemBuilder);
        registry.add(Registries.MENU, new CustomProcessorMenuBuilder(id));
        registry.add(Registries.BLOCK_ENTITY_TYPE, new CustomProcessorBlockEntityBuilder(id, blockEntityInfo));
        registry.add(Registries.RECIPE_TYPE, new CustomProcessorRecipeBuilder(this.id));
        registry.add(Registries.RECIPE_SERIALIZER, new CustomProcessorRecipeSerializerBuilder(this.id));
        NCRecipes.putHandler(new BasicProcessorRecipeHandlerDyn(name, info_handler.itemInputGuiXYWH.size(), info_handler.fluidInputGuiXYWH.size(), info_handler.itemOutputGuiXYWH.size(), info_handler.fluidOutputGuiXYWH.size()));
    }
}