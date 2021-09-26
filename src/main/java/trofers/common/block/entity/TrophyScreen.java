package trofers.common.block.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.searchtree.MutableSearchTree;
import net.minecraft.client.searchtree.ReloadableSearchTree;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmlclient.gui.widget.ExtendedButton;
import net.minecraftforge.resource.IResourceType;
import net.minecraftforge.resource.VanillaResourceType;
import trofers.Trofers;
import trofers.common.network.NetworkHandler;
import trofers.common.network.SetTrophyPacket;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

public class TrophyScreen extends Screen {

    private static final int HORIZONTAL_PADDING = 80;
    private static final int VERTICAL_PADDING = 20;
    private static final int BUTTON_SIZE = 40;
    private static final int BUTTON_SPACING = 8;
    private static final int CANCEL_BUTTON_WIDTH = 96;
    private static final int UPPER_BUTTON_SIZE = 20;
    private static final int SEARCH_BAR_HEIGHT = 12;
    private static final int SEARCH_BAR_SPACING = 4;
    private static final int MIN_ROWS = 2;
    private static final int MIN_COLUMNS = 2;
    private static final int MAX_COLUMNS = 16;
    private static final float ITEM_SCALE = 2;

    private List<Trophy> trophies;

    private EditBox searchBox;
    private Button previousButton;
    private Button nextButton;
    private final Set<Button> trophyButtons = new HashSet<>();

    private int currentPage;
    private int rows;
    private int columns;
    private int rowStart;
    private int columnStart;

    private final Item trophyItem;
    private final BlockPos blockPos;

    public TrophyScreen(Item trophyItem, BlockPos blockPos) {
        super(TextComponent.EMPTY);
        this.trophyItem = trophyItem;
        this.blockPos = blockPos;
        this.currentPage = -1;

        trophies = SearchTreeManager.searchTree.search("");
    }

    public static void open(Item item, BlockPos pos) {
        Minecraft.getInstance().setScreen(new TrophyScreen(item, pos));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key key = InputConstants.getKey(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (
                !searchBox.isFocused()
                && minecraft != null
                && minecraft.options.keyInventory.isActiveAndMatches(key)
        ) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    protected void init() {
        columns = (width - HORIZONTAL_PADDING * 2 - BUTTON_SIZE) / (BUTTON_SIZE + BUTTON_SPACING) + 1;
        columns = Math.max(columns, MIN_COLUMNS);
        columns = Math.min(columns, MAX_COLUMNS);

        columnStart = width / 2 - (BUTTON_SIZE * columns + BUTTON_SPACING * (columns - 1)) / 2;
        rowStart = VERTICAL_PADDING + UPPER_BUTTON_SIZE + SEARCH_BAR_HEIGHT + SEARCH_BAR_SPACING + 16;

        rows = (height - rowStart - VERTICAL_PADDING - BUTTON_SIZE) / (BUTTON_SIZE + BUTTON_SPACING) + 1;
        rows = Math.max(rows, MIN_ROWS);

        createUpperButtons();

        setInitialFocus(searchBox);

        if (currentPage == -1) {
            setCurrentPage(0);
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        int previousRows = rows;
        int previousColumns = columns;

        String search = searchBox.getValue();
        List<Trophy> trophies = this.trophies;

        super.resize(minecraft, width, height);

        searchBox.setValue(search);
        this.trophies = trophies;

        if (columns == previousColumns && rows == previousRows) {
            setCurrentPage(currentPage);
        } else {
            setCurrentPage(0);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean result = super.mouseClicked(pMouseX, pMouseY, pButton);
        if (getFocused() != searchBox) {
            searchBox.setFocus(false);
        }
        return result;
    }

    private void createUpperButtons() {
        previousButton = addRenderableWidget(new ExtendedButton(
                width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - UPPER_BUTTON_SIZE,
                VERTICAL_PADDING,
                UPPER_BUTTON_SIZE,
                UPPER_BUTTON_SIZE,
                new TextComponent("<"),
                button -> setCurrentPage(currentPage - 1)
        ));

        addRenderableWidget(new ExtendedButton(
                width / 2 - CANCEL_BUTTON_WIDTH / 2,
                VERTICAL_PADDING,
                CANCEL_BUTTON_WIDTH,
                UPPER_BUTTON_SIZE,
                new TranslatableComponent(String.format("button.%s.cancel", Trofers.MODID)),
                button -> onClose()
        ));

        nextButton = addRenderableWidget(new ExtendedButton(
                width / 2 + CANCEL_BUTTON_WIDTH / 2 + BUTTON_SPACING,
                VERTICAL_PADDING,
                UPPER_BUTTON_SIZE,
                UPPER_BUTTON_SIZE,
                new TextComponent(">"),
                button -> setCurrentPage(currentPage + 1)
        ));

        searchBox = new EditBox(
                font,
                width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - UPPER_BUTTON_SIZE,
                VERTICAL_PADDING + UPPER_BUTTON_SIZE + SEARCH_BAR_SPACING,
                CANCEL_BUTTON_WIDTH + UPPER_BUTTON_SIZE * 2 + BUTTON_SPACING * 2,
                SEARCH_BAR_HEIGHT,
                new TranslatableComponent("itemGroup.search")
        );
        searchBox.setBordered(true);
        searchBox.setResponder(this::onEditSearchBox);
        addRenderableWidget(searchBox);
    }

    @Override
    public void tick() {
        searchBox.tick();
    }

    public void onEditSearchBox(String text) {
        List<Trophy> searchResult = SearchTreeManager.searchTree.search(text);

        if (!searchResult.equals(trophies)) {
            trophies = searchResult;
            setCurrentPage(0);
        }
    }

    private void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        trophyButtons.forEach(this::removeWidget);

        int index = currentPage * columns * rows;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (index >= trophies.size()) {
                    break;
                }

                ItemStack stack = new ItemStack(trophyItem);
                Trophy trophy = trophies.get(index++);
                stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophy.id().toString());

                int x = columnStart + column * (BUTTON_SIZE + BUTTON_SPACING);
                int y = rowStart + row * (BUTTON_SIZE + BUTTON_SPACING);
                trophyButtons.add(addRenderableWidget(new ItemButton(x, y, BUTTON_SIZE, stack, button -> setTrophy(trophy))));
            }
        }

        previousButton.active = nextButton.active = false;
        if (index < trophies.size() - 1) {
            nextButton.active = true;
        }
        if (currentPage > 0) {
            previousButton.active = true;
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setTrophy(Trophy trophy) {
        NetworkHandler.INSTANCE.sendToServer(new SetTrophyPacket(trophy, blockPos));
        if (Minecraft.getInstance().player != null) {
            if (Minecraft.getInstance().player.level.getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity) {
                blockEntity.setTrophy(trophy);
            }
        }
        onClose();
    }

    private class ItemButton extends ExtendedButton {

        private final ItemStack item;

        public ItemButton(int xPos, int yPos, int size, ItemStack item, OnPress handler) {
            super(xPos, yPos, size, size, TextComponent.EMPTY, handler);
            this.item = item;
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            super.renderButton(poseStack, mouseX, mouseY, partialTicks);

            renderScaledGuiItem(
                    item,
                    x + (int) (width - 16 * ITEM_SCALE) / 2,
                    y + (int) (height - 16 * ITEM_SCALE) / 2,
                    ITEM_SCALE
            );

            if (isHovered()) {
                renderToolTip(poseStack, mouseX, mouseY);
            }
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
            renderTooltip(poseStack, item, mouseX, mouseY);
        }

        @SuppressWarnings("SameParameterValue")
        private void renderScaledGuiItem(ItemStack item, int x, int y, float scale) {
            if (!item.isEmpty()) {
                BakedModel bakedmodel = itemRenderer.getModel(item, null, Minecraft.getInstance().player, 0);
                itemRenderer.blitOffset += 50;
                try {
                    renderGuiItem(item, x, y, scale, bakedmodel);
                } catch (Exception exception) {
                    CrashReport crashReport = CrashReport.forThrowable(exception, "Rendering item");
                    CrashReportCategory category = crashReport.addCategory("Item being rendered");
                    category.setDetail("Item Type", () -> String.valueOf(item.getItem()));
                    category.setDetail("Registry Name", () -> String.valueOf(item.getItem().getRegistryName()));
                    category.setDetail("Item Damage", () -> String.valueOf(item.getDamageValue()));
                    category.setDetail("Item NBT", () -> String.valueOf(item.getTag()));
                    category.setDetail("Item Foil", () -> String.valueOf(item.hasFoil()));
                    throw new ReportedException(crashReport);
                }
                itemRenderer.blitOffset -= 50;
            }
        }

        @SuppressWarnings("deprecation")
        protected void renderGuiItem(ItemStack item, int x, int y, float scale, BakedModel model) {
            Minecraft.getInstance().textureManager.getTexture(TextureAtlas.LOCATION_BLOCKS).setFilter(false, false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_BLOCKS);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            RenderSystem.setShaderColor(1, 1, 1, 1);
            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.translate(x, y, 100 + itemRenderer.blitOffset);
            modelViewStack.translate(16 * scale / 2, 16 * scale / 2, 0);
            modelViewStack.scale(1, -1, 1);
            modelViewStack.scale(scale, scale, scale);
            modelViewStack.scale(16, 16, 16);
            RenderSystem.applyModelViewMatrix();
            PoseStack poseStack = new PoseStack();
            MultiBufferSource.BufferSource buffer = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean flag = !model.usesBlockLight();
            if (flag) {
                Lighting.setupForFlatItems();
            }

            itemRenderer.render(item, ItemTransforms.TransformType.GUI, false, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY, model);
            buffer.endBatch();
            RenderSystem.enableDepthTest();
            if (flag) {
                Lighting.setupFor3DItems();
            }

            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }

    @SuppressWarnings("deprecation")
    public static class SearchTreeManager implements ResourceManagerReloadListener {

        @SuppressWarnings("ConstantConditions")
        private static final MutableSearchTree<Trophy> searchTree = new ReloadableSearchTree<>(
                trophy -> Stream.of(
                        ChatFormatting.stripFormatting((
                                trophy.name() == null
                                        ? new TranslatableComponent("block.trofers.trophy")
                                        : trophy.name().getString()
                                ).toString()
                        ).trim()
                ),
                trophy -> Stream.of(trophy.id())
        );

        @Override
        public void onResourceManagerReload(ResourceManager manager) {
            searchTree.refresh();
        }

        @Nullable
        @Override
        public IResourceType getResourceType() {
            return VanillaResourceType.LANGUAGES;
        }

        public static void fillSearchTree() {
            searchTree.clear();

            TrophyManager.values()
                    .stream()
                    .filter(trophy -> !trophy.isHidden())
                    .sorted(Comparator.comparing(trophy -> trophy.id().toString()))
                    .forEach(searchTree::add);

            searchTree.refresh();
        }
    }
}
