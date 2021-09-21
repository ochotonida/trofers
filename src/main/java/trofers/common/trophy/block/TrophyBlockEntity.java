package trofers.common.trophy.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.ResourceLocationException;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import trofers.Trofers;
import trofers.common.init.ModBlockEntityTypes;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends TileEntity {

    @Nullable
    private Trophy trophy;
    @Nullable
    private ResourceLocation trophyID;

    private float animationOffset;

    public TrophyBlockEntity() {
        super(ModBlockEntityTypes.TROPHY.get());
    }

    @Nullable
    public Trophy getTrophy() {
        return trophy;
    }

    public void setTrophy(@Nullable Trophy trophy) {
        this.trophy = trophy;
        if (trophy != null) {
            trophyID = trophy.id();
        } else {
            trophyID = null;
        }
        onContentsChanged();
    }

    @Nullable
    public ResourceLocation getTrophyID() {
        return trophyID;
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
        return 0;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 16, 1));
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
    public CompoundNBT getUpdateTag() {
        return save(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        load(state, tag);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager connection, SUpdateTileEntityPacket packet) {
        if (level != null) {
            load(getBlockState(), packet.getTag());
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadTrophy(tag);
    }

    public void loadTrophy(CompoundNBT tag) {
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
    public CompoundNBT save(CompoundNBT tag) {
        saveTrophy(tag);
        return super.save(tag);
    }

    public void saveTrophy(CompoundNBT tag) {
        if (trophyID != null) {
            tag.putString("Trophy", trophyID.toString());
        }
    }
}
