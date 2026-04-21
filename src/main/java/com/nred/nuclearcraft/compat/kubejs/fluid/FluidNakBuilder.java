package com.nred.nuclearcraft.compat.kubejs.fluid;

import com.nred.nuclearcraft.fluid.NCSourceFluid;
import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.client.LoadedTexture;
import dev.latvian.mods.kubejs.fluid.FluidBuilder;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.util.ID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.fluids.FluidType;

import javax.imageio.ImageIO;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static com.nred.nuclearcraft.NuclearcraftNeohaul.MODID;
import static com.nred.nuclearcraft.helpers.Location.ncLoc;
import static com.nred.nuclearcraft.info.Fluids.COOLANT_TYPE;

public class FluidNakBuilder extends FluidBuilder {
    public FluidNakBuilder(ResourceLocation i) {
        super(i);
        fluidType.stillTexture(ncLoc("block/fluid/molten_still"));
        fluidType.flowingTexture(ncLoc("block/fluid/molten_flow"));
        fluidType.fallDistanceModifier(0F);
        fluidType.properties = COOLANT_TYPE.properties();

        this.levelDecreasePerBlock(COOLANT_TYPE.level());

        tickRate(new FluidType(fluidType.properties).getViscosity() / 200);
    }

    @Override
    public FlowingFluid createObject() {
        return new NCSourceFluid(createProperties(), COOLANT_TYPE.level());
    }

    @Override
    public void generateAssets(KubeAssetGenerator generator) {
        var stillTexture = getTextureRoot(fluidType.stillTexture);

        if (!(stillTexture.width <= 0 || stillTexture.height <= 0)) {
            generator.texture(fluidType.actualStillTexture, stillTexture.tint(fluidType.tint));
        }

        var flowingTexture = getTextureRoot(fluidType.flowingTexture);

        if (!(stillTexture.width <= 0 || stillTexture.height <= 0)) {
            generator.texture(fluidType.actualFlowingTexture, flowingTexture.tint(fluidType.tint));
        }

        generator.blockState(id, m -> m.simpleVariant("", id.withPath(ID.BLOCK)));
        generator.blockModel(id, m -> {
            m.parent(null);
            m.texture("particle", fluidType.actualStillTexture.toString());
        });

        if (bucketItem != null) {
            var fluidPath = newID("item/generated/", "_bucket_fluid");

            generator.mask(fluidPath, KubeJS.id("item/bucket_mask"), fluidType.actualStillTexture);

            generator.itemModel(bucketItem.id, m -> {
                m.parent(bucketItem.parentModel == null ? KubeJS.id("item/generated_bucket") : bucketItem.parentModel);
                m.texture("bucket_fluid", fluidPath.toString());
                m.textures(bucketItem.textures);
            });
        }
    }

    protected LoadedTexture getTextureRoot(ResourceLocation id) {
        try {
            Path path = ModList.get().getModFileById(MODID).getFile().findResource("assets", MODID, "textures", id.getPath() + ".png");
            try (var in = new BufferedInputStream(Files.newInputStream(path))) {
                var metaPath = ModList.get().getModFileById(MODID).getFile().findResource("assets", MODID, "textures", id.getPath() + ".png.mcmeta");
                return new LoadedTexture(ImageIO.read(in), Files.exists(metaPath) ? Files.readAllBytes(metaPath) : null);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
