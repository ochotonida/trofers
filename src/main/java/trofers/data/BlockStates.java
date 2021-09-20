package trofers.data;

import net.minecraft.core.Direction;
import trofers.Trofers;
import trofers.common.trophy.block.PillarTrophyBlock;
import trofers.common.trophy.block.PlateTrophyBlock;
import trofers.common.trophy.block.TrophyBlock;
import trofers.common.init.ModBlocks;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BlockStates extends BlockStateProvider {

    public BlockStates(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Trofers.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        ModBlocks.TROPHIES.forEach(trophy -> {
            String modelLocation = Trofers.MODID + ":block/" + trophy.getId().getPath();
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
        centeredBox(modelBuilder, width, 0, 2, 0);
        centeredBox(modelBuilder, width - 2, 2, size - 2, 1);
        centeredBox(modelBuilder, width, size - 2, size, 0);
        setTextures(modelBuilder, block);
    }

    public static void createPlate(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        int size = block.getSize();
        int width = 2 * (size - 2);
        centeredBox(modelBuilder, width, 0, 2, 0);
        setTextures(modelBuilder, block);
    }

    public static void setTextures(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        // noinspection ConstantConditions
        String name = block.getRegistryName().getPath();
        String texturePath = Trofers.MODID + ":block/" + name.replace("plate", "pillar");
        modelBuilder
                .texture("particle", "#top")
                .texture("top", texturePath + "_top")
                .texture("side", texturePath + "_side");
    }

    public static void centeredBox(ModelBuilder<?> builder, int width, int minY, int maxY, int tintIndex) {
        builder.element()
                .from(8 - width / 2F, minY, 8 - width / 2F)
                .to(8 + width / 2F, maxY, 8 + width / 2F)
                .allFaces((direction, face) -> face.tintindex(tintIndex).texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#side"));
    }
}
