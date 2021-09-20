package trofers.common.trophy.block;

import net.minecraft.ResourceLocationException;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Constants;
import trofers.Trofers;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends BlockEntity {

    @Nullable
    private Trophy trophy;
    @Nullable
    private ResourceLocation trophyID;

    private float animationOffset;

    public TrophyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TROPHY.get(), pos, state);
    }

    @Nullable
    public Trophy getTrophy() {
        return trophy;
    }

    public void setTrophy(@Nullable Trophy trophy) {
        this.trophy = trophy;
        if (trophy != null) {
            trophyID = trophy.getId();
        } else {
            trophyID = null;
        }
        onContentsChanged();
    }

    @Nullable
    public ResourceLocation getTrophyID() {
        return trophyID;
    }

    public double getDisplayHeight() {
        if (trophy == null) {
            return 0;
        }
        return getTrophyHeight() + trophy.getDisplayHeight();
    }

    public float getAnimationOffset() {
        if (animationOffset == 0 && level != null) {
            animationOffset = level.getRandom().nextFloat() * 4000;
        }
        return animationOffset;
    }

    public int getTrophyHeight() {
        Block block = getBlockState().getBlock();
        if (block instanceof TrophyBlock trophy) {
            return trophy.getHeight();
        }
        return 6;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 16, 1));
    }

    private void onContentsChanged() {
        if (level != null) {
            if (!level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.BLOCK_UPDATE);
                setChanged();
            } else {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
            }
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(new CompoundTag());
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        load(tag);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        if (level != null) {
            load(packet.getTag());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTrophy(tag);
    }

    public void loadTrophy(CompoundTag tag) {
        trophy = null;
        trophyID = null;

        if (tag.contains("Trophy", Constants.NBT.TAG_STRING)) {
            try {
                trophyID = new ResourceLocation(tag.getString("Trophy"));
            } catch (ResourceLocationException exception) {
                Trofers.LOGGER.error(String.format("Failed to load trophy for block entity at %s", getBlockPos()), exception);
            }

            trophy = TrophyManager.get(trophyID);
            if (trophy == null) {
                Trofers.LOGGER.error(String.format("Invalid trophy id for block entity at %s: %s", getBlockPos(), trophyID));
            }
        }

        if (getLevel() != null && getLevel().isClientSide()) {
            getLevel().sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        saveTrophy(tag);
        return super.save(tag);
    }

    public void saveTrophy(CompoundTag tag) {
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
    }
}
