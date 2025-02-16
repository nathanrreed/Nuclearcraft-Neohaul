package com.nred.nuclearcraft.screen;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nred.nuclearcraft.gui.ChangeSideConfigButton;
import com.nred.nuclearcraft.gui.CycleButton;
import com.nred.nuclearcraft.gui.RedstoneModeToggleButton;
import com.nred.nuclearcraft.gui.SimpleImageButton;
import com.nred.nuclearcraft.menu.FluidSlot;
import com.nred.nuclearcraft.menu.ProcessorMenu;
import com.nred.nuclearcraft.payload.FluidClearPayload;
import com.nred.nuclearcraft.recipe.base_types.ProcessorRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.neoforge.client.event.ContainerScreenEvent.Render.Background;
import net.neoforged.neoforge.client.event.ContainerScreenEvent.Render.Foreground;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.nred.nuclearcraft.config.Config.PROCESSOR_CONFIG_MAP;
import static com.nred.nuclearcraft.helpers.GuiHelper.blitTile;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.helpers.RecipeHelpers.*;
import static com.nred.nuclearcraft.helpers.SimpleHelper.getFEString;
import static com.nred.nuclearcraft.registration.ItemRegistration.UPGRADE_MAP;

public abstract class ProcessorScreen<T extends ProcessorMenu> extends AbstractContainerScreen<T> {
    private final ResourceLocation BASE;
    private final String processorType;
    private final int offset;
    private static ScreenRectangle ENERGY_BAR = new ScreenRectangle(7, 5, 18, 76);
    public SimpleImageButton sideConfigButton;
    private static final WidgetSprites sideConfigSprites = new WidgetSprites(ncLoc("button/side_config_on"), ncLoc("button/side_config_off"));

    public RedstoneModeToggleButton redstoneModeButton;
    public ArrayList<ChangeSideConfigButton> configButtons;

    // Side Config Vars
    private ResourceLocation background;
    private ArrayList<CycleButton> configCycleButtons = new ArrayList<>(6);
    private static final ResourceLocation back = ncLoc("block/processor/back");
    private static final ResourceLocation side = ncLoc("block/processor/side");
    private static final ResourceLocation top = ncLoc("block/processor/top");
    private static final ResourceLocation bottom = ncLoc("block/processor/bottom");
    private final ResourceLocation front;

    private Screens currentScreen = Screens.NORMAL;
    private String configType;
    private int configLeft = 0;
    private int configTop = 0;
    private final int progressX;
    private final int progressY;
    public ResourceLocation recipeKey;

    public ProcessorScreen(T menu, Inventory playerInventory, Component title, String type, int progressX, int progressY, int offset) {
        super(menu, playerInventory, title);
        this.BASE = ncLoc("screen/" + type);
        this.processorType = type;
        this.progressX = progressX;
        this.progressY = progressY;
        this.offset = offset;

        this.titleLabelX = 88 - Minecraft.getInstance().font.width(this.title) / 2;
        this.inventoryLabelX = -1000000;
        this.inventoryLabelY = -1000000;

        this.front = ncLoc("block/processor/" + processorType + "_front_off");
    }

    public ProcessorScreen(T menu, Inventory playerInventory, Component title, String type, int progressX, int progressY) {
        this(menu, playerInventory, title, type, progressX, progressY, 0);
    }

    @Override
    protected void renderSlotContents(GuiGraphics guiGraphics, ItemStack itemstack, Slot slot, @Nullable String countString) {
        if (slot instanceof FluidSlot fluidSlot && !fluidSlot.getFluid().isEmpty()) {
            blitTile(guiGraphics, Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluidSlot.getFluid().getFluid()).getStillTexture()), slot.x, slot.y, fluidSlot.size, fluidSlot.size, 16, 16, IClientFluidTypeExtensions.of(fluidSlot.getFluid().getFluid()).getTintColor());
        } else {
            super.renderSlotContents(guiGraphics, itemstack, slot, countString);
        }
    }

    public void onPress(Button button) {
        if (button instanceof ChangeSideConfigButton configButton) {
            // Set Side config type
            currentScreen = Screens.CHOSEN_SIDE_CONFIG;
            this.configType = configButton.type;

            setConfigCycleButtons();
        }
    }

    public void setConfigCycleButtons() {
        background = ncLoc("side_config/" + configType);
        configCycleButtons = new ArrayList<>(6);
        drawSide(1, 0, top, "top");
        drawSide(0, 1, side, "left");
        drawSide(1, 1, front, "front");
        drawSide(2, 1, side, "right");
        drawSide(1, 2, bottom, "bottom");
        drawSide(2, 2, back, "back");

        if (configType.contains("output")) {
            configCycleButtons.add(new CycleButton(configLeft + 7, configTop + 25, "slot_setting", configType.contains("fluid") ? "tooltip.side_config.slot_setting.tank" : "tooltip.side_config.slot_setting.slot", null));
        }

        for (CycleButton btn : configCycleButtons) {
            addRenderableWidget(btn);
        }
    }

    private void drawSide(int col, int row, ResourceLocation texture, String dir) {
        boolean output = configType.contains("output");
        boolean fluid = configType.contains("fluid");

        int startX = configLeft + (output ? 29 : 7);
        configCycleButtons.add(new CycleButton(startX + (col * 18), configTop + 7 + (row * 18), output ? (fluid ? "fluid_output" : "item_output") : (fluid ? "fluid_input" : "item_input"), "tooltip.side_config." + dir, texture));
    }

    private enum Screens {
        NORMAL, SIDE_CONFIG, CHOSEN_SIDE_CONFIG
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        super.resize(minecraft, width, height);

        currentScreen = Screens.NORMAL; // Reset to base screen //TODO try to remove
    }

    @Override
    protected void renderSlotHighlight(GuiGraphics guiGraphics, Slot slot, int mouseX, int mouseY, float partialTick) {
        if (currentScreen == Screens.NORMAL)
            super.renderSlotHighlight(guiGraphics, slot, mouseX, mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();

        ENERGY_BAR = new ScreenRectangle(7 + leftPos, 5 + topPos + offset, 18, 76);
        redstoneModeButton = new RedstoneModeToggleButton(leftPos + 47, topPos + 63 + offset, menu.info.pos(), menu.info.redstoneMode());
        sideConfigButton = new SimpleImageButton(leftPos + 27, topPos + 63 + offset, 18, 18, sideConfigSprites, button -> switchConfigView(true), Component.translatable("tooltip.side_config"));

        configButtons = new ArrayList<>(4);
        configButtons.add(new ChangeSideConfigButton(menu.ENERGY_SLOT.x + leftPos - 1, menu.ENERGY_SLOT.y + topPos - 1, "energy_upgrade", this::onPress));
        configButtons.add(new ChangeSideConfigButton(menu.SPEED_SLOT.x + leftPos - 1, menu.SPEED_SLOT.y + topPos - 1, "speed_upgrade", this::onPress));

        for (Slot slot : menu.FLUID_INPUTS) {
            configButtons.add(new ChangeSideConfigButton(slot.x + leftPos - 1, slot.y + topPos - 1, "fluid_input", this::onPress));
        }
        for (Slot slot : menu.FLUID_OUTPUTS) {
            configButtons.add(new ChangeSideConfigButton(slot.x + leftPos - 1, slot.y + topPos - 1, "fluid_output", this::onPress, menu.FLUID_OUTPUTS.size() > 2 ? 18 : 26));
        }
        for (Slot slot : menu.ITEM_INPUTS) {
            configButtons.add(new ChangeSideConfigButton(slot.x + leftPos - 1, slot.y + topPos - 1, "item_input", this::onPress));
        }
        for (Slot slot : menu.ITEM_OUTPUTS) {
            configButtons.add(new ChangeSideConfigButton(slot.x + leftPos - (menu.ITEM_OUTPUTS.size() > 2 ? 1 : 5), slot.y + topPos - (menu.ITEM_OUTPUTS.size() > 2 ? 1 : 5), "item_output", this::onPress, menu.ITEM_OUTPUTS.size() > 2 ? 18 : 26));
        }

        this.configLeft = (this.width - 68) / 2;
        this.configTop = (this.height - 68) / 2;

        this.addRenderableWidget(redstoneModeButton);
        this.addRenderableWidget(sideConfigButton);
        for (ChangeSideConfigButton button : configButtons) {
            this.addRenderableWidget(button);
        }
    }

    public void switchConfigView(boolean value) {
        currentScreen = value ? Screens.SIDE_CONFIG : Screens.NORMAL;
        for (Button button : configButtons) {
            button.visible = value;
        }
        redstoneModeButton.visible = !value;
        sideConfigButton.visible = !value;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (currentScreen != Screens.NORMAL && (keyCode == InputConstants.KEY_ESCAPE || Objects.requireNonNull(minecraft).options.keyInventory.isActiveAndMatches(InputConstants.getKey(keyCode, scanCode)))) {
            if (currentScreen != Screens.CHOSEN_SIDE_CONFIG) {
                switchConfigView(false);
            } else { // Go back one screen
                currentScreen = Screens.SIDE_CONFIG;
                for (CycleButton button : configCycleButtons) {
                    this.removeWidget(button);
                }
            }
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (FluidSlot fluidSlot : Stream.of(menu.FLUID_INPUTS, menu.FLUID_OUTPUTS).flatMap(Collection::stream).toList()) { // Draw tooltip
            if (fluidSlot.isActive() && button == 0 && hasShiftDown() && !fluidSlot.getFluid().isEmpty() && isHovering(fluidSlot.x, fluidSlot.y, fluidSlot.size, fluidSlot.size, mouseX, mouseY)) {
                fluidSlot.empty();
                PacketDistributor.sendToServer(new FluidClearPayload(fluidSlot.getIndex(), menu.info.pos()));
                return true; // Remove fluid
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (currentScreen != Screens.CHOSEN_SIDE_CONFIG) {
            super.render(guiGraphics, mouseX, mouseY, partialTick);
            for (FluidSlot fluidSlot : Stream.of(menu.FLUID_INPUTS, menu.FLUID_OUTPUTS).flatMap(Collection::stream).toList()) { // Draw tooltip
                if (!fluidSlot.getFluid().isEmpty() && isHovering(fluidSlot.x, fluidSlot.y, fluidSlot.size, fluidSlot.size, mouseX, mouseY)) {
                    FluidStack fluidStack = fluidSlot.getFluid();
                    guiGraphics.renderComponentTooltip(font, List.of(Component.translatable("tooltip.tank", fluidStack.getHoverName().copy().withStyle(ChatFormatting.GREEN), fluidStack.getAmount(), fluidSlot.getFluidCapacity()), Component.translatable("tooltip.tank.clear").withStyle(ChatFormatting.ITALIC)), mouseX, mouseY);
                }
            }

            renderTooltip(guiGraphics, mouseX, mouseY);
        } else {
            this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
            NeoForge.EVENT_BUS.post(new Background(this, guiGraphics, mouseX, mouseY));
            guiGraphics.blitSprite(background, configLeft, configTop, configType.contains("output") ? 90 : 68, 68);
            for (Renderable renderable : this.configCycleButtons) {
                renderable.render(guiGraphics, mouseX, mouseY, partialTick);
            }
            RenderSystem.disableDepthTest();
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate((float) this.leftPos, (float) this.topPos, 0.0F);
            this.hoveredSlot = null;
            NeoForge.EVENT_BUS.post(new Foreground(this, guiGraphics, mouseX, mouseY));
            guiGraphics.pose().popPose();
            RenderSystem.enableDepthTest();
        }
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        if (currentScreen != Screens.CHOSEN_SIDE_CONFIG) {
            guiGraphics.blitSprite(BASE, 256, 256, 0, 0, leftPos, topPos, 175, 256);

            // Draw FE Bar
            if (menu.energyStorage != null) {
                int varLen = ((int) Math.ceil((double) menu.energyStorage.getEnergyStored() / menu.energyStorage.getMaxEnergyStored() * (74.0 + offset)));
                guiGraphics.blitSprite(BASE, 256, 256, 176, 164 - varLen + offset, leftPos + 8, topPos + 80 - varLen + offset, 16, varLen);
            }

            // Draw Progress Bar
            guiGraphics.blitSprite(BASE, 256, 256, 176, 3, leftPos + progressX, topPos + progressY, menu.progress.get(), 38);
        }

        if (currentScreen == Screens.NORMAL) {
            // Upgrade Shadows
            guiGraphics.setColor(1f, 1f, 1f, 0.5f);
            guiGraphics.renderFakeItem(new ItemStack(UPGRADE_MAP.get("speed").get()), leftPos + 132, topPos + 64 + offset);
            guiGraphics.renderFakeItem(new ItemStack(UPGRADE_MAP.get("energy").get()), leftPos + 152, topPos + 64 + offset);
            guiGraphics.setColor(1f, 1f, 1f, 1f);

            if (ENERGY_BAR.containsPoint(mouseX, mouseY)) {
                if (menu.energyStorage != null) {
                    double recipeMult = 1;
                    Optional<RecipeHolder<?>> recipeHolder = Minecraft.getInstance().level.getRecipeManager().byKey(recipeKey);
                    if (recipeHolder.isPresent()) {
                        recipeMult = ((ProcessorRecipe) recipeHolder.get().value()).getPowerModifier();
                    }

                    guiGraphics.renderComponentTooltip(font, List.of(
                            Component.translatable("tooltip.processor.energy.stored", getFEString(menu.energyStorage.getEnergyStored()), getFEString(menu.energyStorage.getMaxEnergyStored())),
                            Component.translatable("tooltip.processor.energy.using", getFEString(calculateEnergy(processorType, recipeMult, menu.itemHandler))),
                            Component.translatable("tooltip.processor.energy.speed", getSpeedCount(menu.itemHandler)),
                            Component.translatable("tooltip.processor.energy.energy", new DecimalFormat("#.##").format(getPowerMultiplier(menu.itemHandler)))
                    ), mouseX, mouseY);
                } else {
                    guiGraphics.renderTooltip(font, Component.translatable("tooltip.processor.energy.not_required").withStyle(ChatFormatting.RED), mouseX, mouseY);
                }
            }
        }
    }
}