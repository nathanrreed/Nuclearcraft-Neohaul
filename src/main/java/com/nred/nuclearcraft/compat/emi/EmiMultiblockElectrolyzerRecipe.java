package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.emi.part.TankWithAmount;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockElectrolyzerRecipeViewer;
import com.nred.nuclearcraft.datamap.ElectrolyzerElectrolyteData;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY;
import static com.nred.nuclearcraft.registration.DataMapTypeRegistration.ELECTROLYZER_ELECTROLYTE_DATA;

public class EmiMultiblockElectrolyzerRecipe extends RecipeViewerRecipe {
    private final String group;

    public EmiMultiblockElectrolyzerRecipe(ResourceLocation id, MultiblockElectrolyzerRecipe recipe) {
        super(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, id, new MultiblockElectrolyzerRecipeViewer(recipe));

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());

        this.group = recipe.getElectrolyteGroup();
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        Map<ResourceKey<Fluid>, ElectrolyzerElectrolyteData> electrolytes = BuiltInRegistries.FLUID.getDataMap(ELECTROLYZER_ELECTROLYTE_DATA).entrySet().stream().filter(e -> e.getValue().group().equals(this.group)).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        List<EmiIngredient> electrolyteIngredients = electrolytes.keySet().stream().map(fluidResourceKey -> NeoForgeEmiIngredient.of(FluidIngredient.single(Objects.requireNonNull(BuiltInRegistries.FLUID.get(fluidResourceKey))))).toList();
        widgets.add(new TankWithAmount(EmiIngredient.of(electrolyteIngredients), MultiblockElectrolyzerRecipeViewer.electrolyte.left(), MultiblockElectrolyzerRecipeViewer.electrolyte.top()) {
            @Override
            public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
                int current = (int) (System.currentTimeMillis() / 1000 % this.stack.getEmiStacks().size());
                Fluid fluid = (Fluid) this.stack.getEmiStacks().get(current).getKey();

                String name = fluid.getFluidType().getDescriptionId();

                return List.of(
                        ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte", Component.translatable(name).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA).getVisualOrderText()),
                        ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte_efficiency", Component.literal(NCMath.pcDecimalPlaces(electrolytes.get(BuiltInRegistries.FLUID.getResourceKey(fluid).get()).efficiency(), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())
                );
            }

            @Override
            public boolean mouseClicked(int mouseX, int mouseY, int button) {
                return false;
            }

            @Override
            public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
                return false;
            }
        }).drawBack(false);
    }
}