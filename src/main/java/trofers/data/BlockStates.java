package trofers.data;

import net.minecraft.core.Direction;
import trofers.Trofers;
import trofers.common.TrophyBlock;
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
            buildTrophy(builder, trophy.get());
        });
    }

    public static void buildTrophy(ModelBuilder<?> modelBuilder, TrophyBlock block) {
        int height = block.getHeight();
        int width = 2 * (height - 2);

        // noinspection ConstantConditions
        String name = block.getRegistryName().getPath();
        String texturePath = Trofers.MODID + ":block/" + name;
        modelBuilder
                .texture("particle", "#top")
                .texture("top", texturePath + "_top")
                .texture("side", texturePath + "_side");
        centeredBox(modelBuilder, width, 0, 2, 2);
        centeredBox(modelBuilder, width - 2, 2, height - 2, 1);
        centeredBox(modelBuilder, width, height - 2, height, 0);
    }

    public static void centeredBox(ModelBuilder<?> builder, int width, int minY, int maxY, int tintIndex) {
        builder.element()
                .from(8 - width / 2F, minY, 8 - width / 2F)
                .to(8 + width / 2F, maxY, 8 + width / 2F)
                .allFaces((direction, face) -> face.tintindex(tintIndex).texture(direction.getAxis() == Direction.Axis.Y ? "#top" : "#side"));
    }
}
