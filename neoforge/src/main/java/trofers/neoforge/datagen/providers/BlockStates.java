package trofers.neoforge.datagen.providers;

import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ModelBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.registry.ModBlocks;

public class BlockStates extends BlockStateProvider {

    public BlockStates(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, Trofers.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        createPillar(ModBlocks.SMALL_PILLAR.get(), 6);
        createPillar(ModBlocks.MEDIUM_PILLAR.get(), 7);
        createPillar(ModBlocks.LARGE_PILLAR.get(), 8);
        createPlate(ModBlocks.SMALL_PLATE.get(), 6);
        createPlate(ModBlocks.MEDIUM_PLATE.get(), 7);
        createPlate(ModBlocks.LARGE_PLATE.get(), 8);
    }

    private ModelBuilder<?> createBuilder(TrophyBlock block) {
        String modelLocation = Trofers.MOD_ID + ":block/" + BuiltInRegistries.BLOCK.getKey(block).getPath();
        ModelBuilder<?> builder = models().withExistingParent(modelLocation, "block");
        horizontalBlock(block, state -> builder);
        return builder;
    }

    private void createPillar(TrophyBlock block, int size) {
        ModelBuilder<?> builder = createBuilder(block);
        int width = 2 * (size - 2);
        texturedCenteredBox(builder, width, 0, 2, 0);
        texturedCenteredBox(builder, width - 2, 2, size - 2, 1);
        texturedCenteredBox(builder, width, size - 2, size, 0);
        setTextures(builder, block);
    }

    private void createPlate(TrophyBlock block, int size) {
        ModelBuilder<?> builder = createBuilder(block);
        int width = 2 * (size - 2);
        texturedCenteredBox(builder, width, 0, 2, 0);
        centeredBox(builder, width, 0, 2)
                .face(Direction.UP).tintindex(1).texture("#overlay");

        setTextures(builder, block);
        String overlayTexture = Trofers.MOD_ID + ":block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + "_overlay";
        builder.texture("overlay", overlayTexture);
        builder.renderType("cutout");
    }

    private static void setTextures(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        String name = BuiltInRegistries.BLOCK.getKey(block).getPath();
        String texturePath = Trofers.MOD_ID + ":block/" + name.replace("plate", "pillar");
        modelBuilder
                .texture("particle", "#top")
                .texture("top", texturePath + "_top")
                .texture("side", texturePath + "_side");
    }

    private static void texturedCenteredBox(ModelBuilder<?> builder, int width, int minY, int maxY, int tintIndex) {
        centeredBox(builder, width, minY, maxY)
                .allFaces((direction, face) -> face
                                .tintindex(tintIndex)
                                .texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#side")
                );
    }

    private static ModelBuilder<?>.ElementBuilder centeredBox(ModelBuilder<?> builder, int width, int minY, int maxY) {
        return builder.element()
                .from(8 - width / 2F, minY, 8 - width / 2F)
                .to(8 + width / 2F, maxY, 8 + width / 2F);
    }
}
