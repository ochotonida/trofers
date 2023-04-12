package trofers.data.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import trofers.Trofers;
import trofers.forge.common.block.PillarTrophyBlock;
import trofers.forge.common.block.PlateTrophyBlock;
import trofers.forge.common.block.TrophyBlock;
import trofers.forge.common.init.ModBlocks;

public class BlockStates extends BlockStateProvider {

    public BlockStates(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Trofers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.TROPHIES.forEach(trophy -> {
            String modelLocation = Trofers.MOD_ID + ":block/" + trophy.getId().getPath();
            ModelBuilder<?> builder = models().withExistingParent(modelLocation, "block");
            horizontalBlock(trophy.get(), state -> builder);
            if (trophy.get() instanceof PillarTrophyBlock) {
                createPillar(builder, trophy.get());
            } else if (trophy.get() instanceof PlateTrophyBlock){
                createPlate(builder, trophy.get());
            }
        });
    }

    public static void createPillar(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        int size = block.getSize();
        int width = 2 * (size - 2);
        texturedCenteredBox(modelBuilder, width, 0, 2, 0);
        texturedCenteredBox(modelBuilder, width - 2, 2, size - 2, 1);
        texturedCenteredBox(modelBuilder, width, size - 2, size, 0);
        setTextures(modelBuilder, block);
    }

    public static void createPlate(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        int size = block.getSize();
        int width = 2 * (size - 2);
        texturedCenteredBox(modelBuilder, width, 0, 2, 0);
        centeredBox(modelBuilder, width, 0, 2)
                .face(Direction.UP).tintindex(1).texture("#overlay");

        setTextures(modelBuilder, block);
        // noinspection ConstantConditions
        String overlayTexture = Trofers.MOD_ID + ":block/" + ForgeRegistries.BLOCKS.getKey(block).getPath() + "_overlay";
        modelBuilder.texture("overlay", overlayTexture);
        modelBuilder.renderType("cutout");
    }

    public static void setTextures(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        // noinspection ConstantConditions
        String name = ForgeRegistries.BLOCKS.getKey(block).getPath();
        String texturePath = Trofers.MOD_ID + ":block/" + name.replace("plate", "pillar");
        modelBuilder
                .texture("particle", "#top")
                .texture("top", texturePath + "_top")
                .texture("side", texturePath + "_side");
    }

    public static void texturedCenteredBox(ModelBuilder<?> builder, int width, int minY, int maxY, int tintIndex) {
        centeredBox(builder, width, minY, maxY)
                .allFaces((direction, face) -> face
                                .tintindex(tintIndex)
                                .texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#side")
                );
    }

    public static ModelBuilder<?>.ElementBuilder centeredBox(ModelBuilder<?> builder, int width, int minY, int maxY) {
        return builder.element()
                .from(8 - width / 2F, minY, 8 - width / 2F)
                .to(8 + width / 2F, maxY, 8 + width / 2F);
    }
}
