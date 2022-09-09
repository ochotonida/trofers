package trofers.data;

import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import trofers.Trofers;
import trofers.common.init.ModBlocks;

public class ItemModels extends ItemModelProvider {

    public ItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Trofers.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ModBlocks.TROPHIES.forEach(block -> getBuilder(block.getId().getPath())
                .parent(new ModelFile.UncheckedModelFile("builtin/entity"))
                .guiLight(BlockModel.GuiLight.SIDE)
                .transforms()
                    .transform(ItemTransforms.TransformType.GUI)
                        .rotation(30, 225, 0)
                        .scale(0.625F)
                    .end()
                    .transform(ItemTransforms.TransformType.FIXED)
                        .scale(0.5F)
                    .end()
                    .transform(ItemTransforms.TransformType.GROUND)
                        .translation(0, 3, 0)
                        .scale(0.25F)
                    .end()
                    .transform(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND)
                        .rotation(75, 45, 0)
                        .translation(0, 2, 1.5F)
                        .scale(0.375F)
                    .end()
                    .transform(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND)
                        .rotation(0, 135, 0)
                        .translation(0, block.getId().getPath().contains("plate") ? 2 + 5 * 0.4F : 2, 0)
                        .scale(0.4F)
                    .end()
                .end());
    }
}
