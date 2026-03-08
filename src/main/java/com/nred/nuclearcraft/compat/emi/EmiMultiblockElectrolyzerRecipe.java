package com.nred.nuclearcraft.compat.emi;

import com.nred.nuclearcraft.compat.emi.part.RecipeViewerRecipe;
import com.nred.nuclearcraft.compat.emi.part.TankWithAmount;
import com.nred.nuclearcraft.compat.recipe_viewer.RecipeViewerImpl.MultiblockElectrolyzerRecipeViewer;
import com.nred.nuclearcraft.recipe.machine.ElectrolyzerElectrolyteRecipe;
import com.nred.nuclearcraft.recipe.machine.MultiblockElectrolyzerRecipe;
import com.nred.nuclearcraft.util.NCMath;
import dev.emi.emi.api.neoforge.NeoForgeEmiIngredient;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.compat.emi.ModEmiPlugin.EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY;

public class EmiMultiblockElectrolyzerRecipe extends RecipeViewerRecipe {
    private final List<ElectrolyzerElectrolyteRecipe> electrolytes;
    private final Map<String, Double> efficency_map;

    public EmiMultiblockElectrolyzerRecipe(ResourceLocation id, MultiblockElectrolyzerRecipe recipe, List<ElectrolyzerElectrolyteRecipe> electrolytes) {
        super(EMI_MULTIBLOCK_ELECTROLYZER_CATEGORY, id, new MultiblockElectrolyzerRecipeViewer(recipe));
        this.electrolytes = electrolytes;

        this.inputs.addAll(recipe.itemIngredients.stream().map(ModEmiPlugin::getEmiItemIngredient).toList());
        this.inputs.addAll(recipe.fluidIngredients.stream().map(ModEmiPlugin::getEmiFluidIngredient).toList());
        this.outputs.addAll(recipe.itemProducts.stream().map(ModEmiPlugin::getEmiItemStack).toList());
        this.outputs.addAll(recipe.fluidProducts.stream().map(ModEmiPlugin::getEmiFluidStack).toList());

        efficency_map = new HashMap<>();
        for (ElectrolyzerElectrolyteRecipe electrolyteRecipe : electrolytes) {
            efficency_map.put(electrolyteRecipe.electrolyte().getStacks()[0].getDescriptionId(), electrolyteRecipe.getElectrolyzerElectrolyteEfficiency());
        }
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        super.addWidgets(widgets);

        widgets.add(new TankWithAmount(EmiIngredient.of(electrolytes.stream().map(e ->
                NeoForgeEmiIngredient.of(e.electrolyte())).toList()), MultiblockElectrolyzerRecipeViewer.electrolyte.left(), MultiblockElectrolyzerRecipeViewer.electrolyte.top()) {
            @Override
            public List<ClientTooltipComponent> getTooltip(int mouseX, int mouseY) {
                int current = (int) (System.currentTimeMillis() / 1000 % this.stack.getEmiStacks().size());
                String name = ((Fluid) this.stack.getEmiStacks().get(current).getKey()).getFluidType().getDescriptionId();

                return List.of(
                        ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte", Component.translatable(name).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.AQUA).getVisualOrderText()),
                        ClientTooltipComponent.create(Component.translatable(MODID + ".recipe_viewer.electrolyte_efficiency", Component.literal(NCMath.pcDecimalPlaces(efficency_map.get(name), 1)).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.LIGHT_PURPLE).getVisualOrderText())
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