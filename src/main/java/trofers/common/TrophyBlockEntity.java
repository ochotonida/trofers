package trofers.common;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.util.Constants;
import trofers.common.init.ModBlockEntityTypes;

import javax.annotation.Nullable;

public class TrophyBlockEntity extends BlockEntity {

    private float displayHeightOffset;
    private float displayScale;
    private float animationSpeed;
    private float animationOffset;
    private final int[] colors = new int[]{0xFFFFFF, 0xFFFFFF, 0xFFFFFF};
    private TrophyAnimation animation = TrophyAnimation.FIXED;
    private ItemStack item = ItemStack.EMPTY;
    @Nullable
    private String name;

    public TrophyBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.TROPHY.get(), pos, state);
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
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
        if (animationOffset == 0 && level != null) {
            animationOffset = level.getRandom().nextFloat() * 420;
        }
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
        if (block instanceof TrophyBlock trophy) {
            return trophy.getHeight();
        }
        return 6;
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(getBlockPos().offset(-1, 0, -1), getBlockPos().offset(1, 1.5, 1));
    }

    private void onContentsChanged() {
        if (level != null && !level.isClientSide) {
            BlockState state = level.getBlockState(getBlockPos());
            level.sendBlockUpdated(getBlockPos(), state, state, Constants.BlockFlags.BLOCK_UPDATE);
            setChanged();
        }
    }

    @Override
    public CompoundTag getUpdateTag() {
        return save(saveTrophy(new CompoundTag()));
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return new ClientboundBlockEntityDataPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(Connection connection, ClientboundBlockEntityDataPacket packet) {
        if (level != null) {
            loadTrophy(packet.getTag());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        loadTrophy(tag);
    }

    public void loadTrophy(CompoundTag tag) {
        if (tag.contains("DisplayScale")) {
            displayScale = tag.getFloat("DisplayScale");
        } else if (getBlockState().getBlock() instanceof TrophyBlock trophy) {
            displayScale = trophy.getDefaultDisplayScale();
        } else {
            displayScale = 0;
        }

        displayHeightOffset = tag.getFloat("DisplayHeight");
        animationSpeed = tag.contains("AnimationSpeed") ? tag.getFloat("AnimationSpeed") : 1;
        item = tag.contains("Item") ? ItemStack.of(tag.getCompound("Item")) : ItemStack.EMPTY;
        setName(tag.contains("Name", Constants.NBT.TAG_STRING) ? tag.getString("Name") : null);
        animation = TrophyAnimation.byName(tag.getString("Animation"));

        CompoundTag colorTag = tag.getCompound("Colors");
        readColor(colorTag, 0, "Top");
        readColor(colorTag, 1, "Middle");
        readColor(colorTag, 2, "Bottom");

        if (level != null && level.isClientSide()) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Constants.BlockFlags.RERENDER_MAIN_THREAD);
        }
    }

    private void readColor(CompoundTag tag, int index, String name) {
        if (tag.contains(name)) {
            colors[index] = getCombinedColor(tag.getCompound(name));
        } else {
            colors[index] = 0xFFFFFF;
        }
    }

    public static int getCombinedColor(CompoundTag tag) {
        return tag.getInt("Red") << 16 | tag.getInt("Green") << 8 | tag.getInt("Blue");
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        saveTrophy(tag);
        return super.save(tag);
    }

    public CompoundTag saveTrophy(CompoundTag tag) {
        if (displayHeightOffset != 0) {
            tag.putFloat("DisplayHeight", displayHeightOffset);
        }
        if (displayScale != 0) {
            tag.putFloat("DisplayScale", displayScale);
        }
        if (animationSpeed != 0 && animationSpeed != 1 && animation != TrophyAnimation.FIXED) {
            tag.putFloat("AnimationSpeed", animationSpeed);
        }
        if (!item.isEmpty()) {
            tag.put("Item", item.serializeNBT());
        }
        if (name != null) {
            tag.putString("Name", name);
        }
        tag.putString("Animation", animation.getName());

        CompoundTag colorTag = new CompoundTag();
        saveColor(colorTag, 0, "Top");
        saveColor(colorTag, 1, "Middle");
        saveColor(colorTag, 2, "Bottom");
        if (!colorTag.isEmpty()) {
            tag.put("Colors", colorTag);
        }
        return tag;
    }

    private void saveColor(CompoundTag tag, int index, String name) {
        if (colors[index] != 0xFFFFF) {
            int color = colors[index];
            CompoundTag colorTag = new CompoundTag();
            colorTag.putInt("Red", (color >> 16) & 255);
            colorTag.putInt("Green", (color >> 8) & 255);
            colorTag.putInt("Blue", color & 255);
            tag.put(name, colorTag);
        }
    }
}
