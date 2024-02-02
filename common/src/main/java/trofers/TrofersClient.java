package trofers;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import trofers.block.entity.TrophyBlockEntity;
import trofers.trophy.Trophy;

public class TrofersClient {

    private static int getTrophyColor(Trophy trophy, int index) {
        if (trophy != null) {
            if (index == 0) {
                return trophy.colors().base();
            } else if (index == 1) {
                return trophy.colors().accent();
            }
        }
        return 0xFFFFFF;
    }

    @SuppressWarnings("unused")
    public static int getTrophyBlockColor(BlockState state, @Nullable BlockAndTintGetter level, @Nullable BlockPos pos, int index) {
        // what the hell sodium 🤨🤔🤠
        if (FabricLoader.getInstance().isModLoaded("sodium") && pos != null) {
            pos = pos.offset(0, 1, 0);
            if (level != null && level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
                return getTrophyColor(blockEntity.getTrophy(), index);
            }
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i != 0 && j != 0) {
                        continue;
                    }
                    BlockPos p = pos.offset(i, 0, j);
                    if (level != null && level.getBlockEntity(p) instanceof TrophyBlockEntity blockEntity) {
                        return getTrophyColor(blockEntity.getTrophy(), index);
                    }
                }
            }
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    if (i == 0 || j == 0) {
                        continue;
                    }
                    BlockPos p = pos.offset(i, 0, j);
                    if (level != null && level.getBlockEntity(p) instanceof TrophyBlockEntity blockEntity) {
                        return getTrophyColor(blockEntity.getTrophy(), index);
                    }
                }
            }
        }

        if (level != null && pos != null && level.getBlockEntity(pos) instanceof TrophyBlockEntity blockEntity) {
            return getTrophyColor(blockEntity.getTrophy(), index);
        }
        return 0xFFFFFF;
    }

    public static int getTrophyItemColor(ItemStack stack, int index) {
        return getTrophyColor(Trophy.getTrophy(stack), index);
    }
}
