package trofers.common;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.util.Constants;
import trofers.common.init.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends TileEntity {

    private float displayHeightOffset;
    private float displayScale;
    private float animationSpeed;
    private final float animationOffset;
    private final int[] colors = new int[] {0xFFFFFF, 0xFFFFFF, 0xFFFFFF};
    private TrophyAnimation animation = TrophyAnimation.FIXED;
    private ItemStack item = ItemStack.EMPTY;
    @Nullable
    private String name;

    public TrophyBlockEntity() {
        super(ModBlockEntityTypes.TROPHY.get());
        if (Minecraft.getInstance().level != null) {
            animationOffset = Minecraft.getInstance().level.getRandom().nextFloat() * 420;
        } else {
            animationOffset = 0;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack stack) {
        item = stack;
        onContentsChanged();
    }

    public int getColor(int index) {
        return colors[index];
    }

    public void setColor(int index, int color) {
        colors[index] = color;
        onContentsChanged();
    }

    public float getDisplayScale() {
        return displayScale;
    }

    public float getDisplayHeight() {
        return getTrophyHeight() * animation.getDisplayHeightMultiplier() + displayHeightOffset;
    }

    public float getAnimationSpeed() {
        return animationSpeed;
    }

    public float getAnimationOffset() {
        return animationOffset;
    }

    public TrophyAnimation getAnimation() {
        return animation;
    }

    public void setAnimation(TrophyAnimation animation) {
        this.animation = animation;
        onContentsChanged();
    }

    public int getTrophyHeight() {
        Block block = getBlockState().getBlock();
        if (block instanceof TrophyBlock) {
            return ((TrophyBlock) block).getHeight();
        }
        return 6;
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        return new AxisAlignedBB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 1.5, 1));
    }

    private void onContentsChanged() {
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, Constants.BlockFlags.BLOCK_UPDATE);
            setChanged();
        }
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return save(saveTrophy(new CompoundNBT()));
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket packet) {
        if (level != null) {
            loadTrophy(packet.getTag(), level.getBlockState(getBlockPos()));
        }
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        loadTrophy(tag, state);
    }

    public void loadTrophy(CompoundNBT tag, BlockState state) {
        if (tag.contains("DisplayScale")) {
            displayScale = tag.getFloat("DisplayScale");
        } else if (state.getBlock() instanceof TrophyBlock) {
            displayScale = ((TrophyBlock) state.getBlock()).getDefaultDisplayScale();
        } else {
            displayScale = 0;
        }

        displayHeightOffset = tag.getFloat("DisplayHeight");
        animationSpeed = tag.contains("AnimationSpeed") ? tag.getFloat("AnimationSpeed") : 1;
        item = tag.contains("Item") ? ItemStack.of(tag.getCompound("Item")) : ItemStack.EMPTY;
        setName(tag.contains("Name", Constants.NBT.TAG_STRING) ? tag.getString("Name") : null);
        animation = TrophyAnimation.byName(tag.getString("Animation"));

        CompoundNBT colorTag = tag.getCompound("Colors");
        readColor(colorTag, 0, "Top");
        readColor(colorTag, 1, "Middle");
        readColor(colorTag, 2, "Bottom");

        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), state, state, Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    private void readColor(CompoundNBT tag, int index, String name) {
        if (tag.contains(name)) {
            colors[index] = getCombinedColor(tag.getCompound(name));
        } else {
            colors[index] = 0xFFFFFF;
        }
    }

    public static int getCombinedColor(CompoundNBT tag) {
        return tag.getInt("Red") << 16 | tag.getInt("Green") << 8 | tag.getInt("Blue");
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        saveTrophy(tag);
        return super.save(tag);
    }

    public CompoundNBT saveTrophy(CompoundNBT tag) {
        if (displayHeightOffset != 0) {
            tag.putFloat("DisplayHeight", displayHeightOffset);
        }
        if (displayScale != 0) {
            tag.putFloat("DisplayScale", displayScale);
        }
        if (animationSpeed != 1 && animation != TrophyAnimation.FIXED) {
            tag.putFloat("AnimationSpeed", animationSpeed);
        }
        if (!item.isEmpty()) {
            tag.put("Item", item.serializeNBT());
        }
        if (name != null) {
            tag.putString("Name", name);
        }
        tag.putString("Animation", animation.getName());

        CompoundNBT colorTag = new CompoundNBT();
        saveColor(colorTag, 0, "Top");
        saveColor(colorTag, 1, "Middle");
        saveColor(colorTag, 2, "Bottom");
        if (!colorTag.isEmpty()) {
            tag.put("Colors", colorTag);
        }
        return tag;
    }

    private void saveColor(CompoundNBT tag, int index, String name) {
        if (colors[index] != 0xFFFFF) {
            int color = colors[index];
            CompoundNBT colorTag = new CompoundNBT();
            colorTag.putInt("Red", (color >> 16) & 255);
            colorTag.putInt("Green", (color >> 8) & 255);
            colorTag.putInt("Blue", color & 255);
            tag.put(name, colorTag);
        }
    }
}
