package trofers.screen;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import trofers.block.entity.TrophyBlockEntity;
import trofers.network.NetworkHandler;
import trofers.network.SetTrophyPacket;
import trofers.trophy.Trophy;
import trofers.trophy.TrophySearchTreeManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class TrophySelectionScreen extends Screen {

    private static final int HORIZONTAL_PADDING = 80;
    private static final int VERTICAL_PADDING = 16;
    private static final int TROPHY_BUTTON_SIZE = 40;
    private static final int BUTTON_SPACING = 8;
    private static final int CANCEL_BUTTON_WIDTH = 96;
    private static final int NAVIGATION_BUTTON_SIZE = 20;
    private static final int SEARCH_BAR_HEIGHT = 12;
    private static final int SEARCH_BAR_VERTICAL_SPACING = 8;
    private static final int MIN_ROW_COUNT = 2;
    private static final int MIN_COLUMN_COUNT = 2;
    private static final int MAX_COLUMN_COUNT = 16;
    private static final float ITEM_SCALE = 2;

    private List<Trophy> trophies;

    private EditBox searchBox;
    private Button previousButton;
    private Button nextButton;
    private final Set<Button> trophyButtons = new HashSet<>();

    private int currentPage;
    private int rowCount;
    private int columnCount;
    private int rowStart;
    private int columnStart;

    private final BlockState blockState;
    private final BlockPos blockPos;

    public TrophySelectionScreen(BlockState blockState, BlockPos blockPos) {
        super(Component.empty());
        this.blockState = blockState;
        this.blockPos = blockPos;
        this.currentPage = -1;

        trophies = TrophySearchTreeManager.search("");
    }

    public static void open(BlockState blockState, BlockPos pos) {
        Minecraft.getInstance().setScreen(new TrophySelectionScreen(blockState, pos));
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean isInventoryKeyDown = minecraft != null && minecraft.options.keyInventory.matches(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (!searchBox.isFocused() && isInventoryKeyDown) {
            onClose();
            return true;
        }
        return false;
    }

    @Override
    protected void init() {
        columnCount = (width - HORIZONTAL_PADDING * 2 - TROPHY_BUTTON_SIZE) / (TROPHY_BUTTON_SIZE + BUTTON_SPACING) + 1;
        columnCount = Math.max(columnCount, MIN_COLUMN_COUNT);
        columnCount = Math.min(columnCount, MAX_COLUMN_COUNT);

        columnStart = width / 2 - (TROPHY_BUTTON_SIZE * columnCount + BUTTON_SPACING * (columnCount - 1)) / 2;
        rowStart = VERTICAL_PADDING + NAVIGATION_BUTTON_SIZE + SEARCH_BAR_HEIGHT + SEARCH_BAR_VERTICAL_SPACING * 2;

        rowCount = (height - rowStart - VERTICAL_PADDING - TROPHY_BUTTON_SIZE) / (TROPHY_BUTTON_SIZE + BUTTON_SPACING) + 1;
        rowCount = Math.max(rowCount, MIN_ROW_COUNT);

        createUpperButtons();

        setInitialFocus(searchBox);

        if (currentPage == -1) {
            setCurrentPage(0);
        }
    }

    @Override
    public void resize(Minecraft minecraft, int width, int height) {
        int previousRowCount = rowCount;
        int previousColumnCount = columnCount;

        String search = searchBox.getValue();
        List<Trophy> trophies = this.trophies;

        super.resize(minecraft, width, height);

        searchBox.setValue(search);
        this.trophies = trophies;

        if (columnCount == previousColumnCount && rowCount == previousRowCount) {
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
        int xPos = width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - NAVIGATION_BUTTON_SIZE;


        previousButton = new Button(xPos, VERTICAL_PADDING, NAVIGATION_BUTTON_SIZE, NAVIGATION_BUTTON_SIZE, Component.literal("<"), button -> setCurrentPage(currentPage - 1));
        addRenderableWidget(previousButton);

        xPos += NAVIGATION_BUTTON_SIZE + BUTTON_SPACING;
        Button cancelButton = new Button(xPos, VERTICAL_PADDING, CANCEL_BUTTON_WIDTH, NAVIGATION_BUTTON_SIZE, CommonComponents.GUI_CANCEL, button -> onClose());
        addRenderableWidget(cancelButton);

        xPos += CANCEL_BUTTON_WIDTH + BUTTON_SPACING;
        nextButton = new Button(xPos, VERTICAL_PADDING, NAVIGATION_BUTTON_SIZE, NAVIGATION_BUTTON_SIZE, Component.literal(">"), button -> setCurrentPage(currentPage + 1));
        addRenderableWidget(nextButton);

        int searchBoxX = width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - NAVIGATION_BUTTON_SIZE;
        int searchBoxY = VERTICAL_PADDING + NAVIGATION_BUTTON_SIZE + SEARCH_BAR_VERTICAL_SPACING;
        int searchBoxWidth = CANCEL_BUTTON_WIDTH + NAVIGATION_BUTTON_SIZE * 2 + BUTTON_SPACING * 2;
        Component searchBoxNarration = Component.translatable("itemGroup.search");
        searchBox = new EditBox(font, searchBoxX, searchBoxY, searchBoxWidth, SEARCH_BAR_HEIGHT, searchBoxNarration);
        searchBox.setBordered(true);
        searchBox.setResponder(this::onEditSearchBox);
        addRenderableWidget(searchBox);
    }

    @Override
    public void tick() {
        searchBox.tick();
    }

    public void onEditSearchBox(String text) {
        List<Trophy> searchResult = TrophySearchTreeManager.search(text);

        if (!searchResult.equals(trophies)) {
            trophies = searchResult;
            setCurrentPage(0);
        }
    }

    private void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        trophyButtons.forEach(this::removeWidget);
        trophyButtons.clear();

        int index = currentPage * columnCount * rowCount;

        for (int row = 0; row < rowCount; row++) {
            for (int column = 0; column < columnCount; column++) {
                if (index >= trophies.size()) {
                    break;
                }

                Trophy trophy = trophies.get(index++);
                ItemStack stack = trophy.createItem(blockState.getBlock());

                int x = columnStart + column * (TROPHY_BUTTON_SIZE + BUTTON_SPACING);
                int y = rowStart + row * (TROPHY_BUTTON_SIZE + BUTTON_SPACING);
                Button trophyButton = new TrophyButton(x, y, TROPHY_BUTTON_SIZE, stack, trophy);
                trophyButtons.add(addRenderableWidget(trophyButton));
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

    private class TrophyButton extends Button {

        private final ItemStack item;
        private final Trophy trophy;
        private final int x;
        private final int y;

        private TrophyButton(int xPos, int yPos, int size, ItemStack item, Trophy trophy) {
            super(xPos, yPos, size, size, Component.empty(), button -> {
            }, new OnTooltip() {
                @Override
                public void onTooltip(Button button, PoseStack poseStack, int i, int j) {
                    TrophySelectionScreen.this.renderTooltip(poseStack, item.getHoverName(), i, j);
                }

                @Override
                public void narrateTooltip(Consumer<Component> consumer) {
                    consumer.accept(item.getHoverName());
                }
            });
            this.trophy = trophy;
            this.item = item;
            this.x = xPos;
            this.y = yPos;
        }

        @Override
        public void onClick(double x, double y) {
            setTrophy(trophy);
        }

        private void setTrophy(Trophy trophy) {
            NetworkHandler.CHANNEL.sendToServer(new SetTrophyPacket(trophy, blockPos));
            if (Minecraft.getInstance().player != null) {
                if (Minecraft.getInstance().player.level.getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity) {
                    blockEntity.setTrophy(trophy);
                }
            }
            onClose();
        }

        @Override
        public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
            renderButtonParts(poseStack, mouseX, mouseY);
            if (this.isHoveredOrFocused()) {
                this.renderToolTip(poseStack, mouseX, mouseY);
            }

            tryRenderScaledGuiItem(
                    poseStack,
                    item,
                    x + (int) (width - 16 * ITEM_SCALE) / 2,
                    y + (int) (height - 16 * ITEM_SCALE) / 2,
                    ITEM_SCALE
            );
        }

        public void renderButtonParts(PoseStack poseStack, int i, int j) {
            renderButtonPart(poseStack, i, j, 16, 0, 0);
            renderButtonPart(poseStack, i, j, 8, 16, 4);
            renderButtonPart(poseStack, i, j, 16, 24, 4);
        }

        public void renderButtonPart(PoseStack poseStack, int i, int j, int size, int offset, int textureOffset) {
            Minecraft minecraft = Minecraft.getInstance();
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_LOCATION);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
            int k = this.getYImage(this.isHoveredOrFocused());
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.blit(poseStack, this.x, this.y + offset, 0, 46 + k * 20 + textureOffset, this.width / 2, size);
            this.blit(poseStack, this.x + this.width / 2, this.y + offset, 200 - this.width / 2, 46 + k * 20 + textureOffset, this.width / 2, size);
            this.renderBg(poseStack, minecraft, i, j);
        }


        @SuppressWarnings("SameParameterValue")
        private void tryRenderScaledGuiItem(PoseStack poseStack, ItemStack item, int x, int y, float scale) {
            if (!item.isEmpty()) {
                BakedModel bakedmodel = itemRenderer.getModel(item, null, Minecraft.getInstance().player, 0);
                poseStack.pushPose();
                poseStack.translate(0, 0, 50);
                try {
                    renderScaledGuiItem(poseStack, item, x, y, scale, bakedmodel);
                } catch (Exception exception) {
                    CrashReport crashReport = CrashReport.forThrowable(exception, "Rendering item");
                    CrashReportCategory category = crashReport.addCategory("Item being rendered");
                    category.setDetail("Item Type", () -> String.valueOf(item.getItem()));
                    category.setDetail("Registry Name", () -> String.valueOf(Registry.ITEM.getKey(item.getItem())));
                    category.setDetail("Item Damage", () -> String.valueOf(item.getDamageValue()));
                    category.setDetail("Item NBT", () -> String.valueOf(item.getTag()));
                    category.setDetail("Item Foil", () -> String.valueOf(item.hasFoil()));
                    throw new ReportedException(crashReport);
                }
                poseStack.popPose();
            }
        }

        protected void renderScaledGuiItem(PoseStack poseStack, ItemStack item, int x, int y, float scale, BakedModel model) {
            poseStack.pushPose();
            poseStack.translate(x, y, 100);
            poseStack.translate(16 * scale / 2, 16 * scale / 2, 0);
            poseStack.scale(1, -1, 1);
            poseStack.scale(scale, scale, scale);
            poseStack.scale(16, 16, 16);
            MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
            boolean usesFlatLighting = !model.usesBlockLight();
            if (usesFlatLighting) {
                Lighting.setupForFlatItems();
            }

            PoseStack modelViewStack = RenderSystem.getModelViewStack();
            modelViewStack.pushPose();
            modelViewStack.mulPoseMatrix(poseStack.last().pose());
            RenderSystem.applyModelViewMatrix();
            itemRenderer.render(item, ItemTransforms.TransformType.GUI, false, new PoseStack(), bufferSource, 15728880, OverlayTexture.NO_OVERLAY, model);
            bufferSource.endBatch();
            RenderSystem.enableDepthTest();
            if (usesFlatLighting) {
                Lighting.setupFor3DItems();
            }

            poseStack.popPose();
            modelViewStack.popPose();
            RenderSystem.applyModelViewMatrix();
        }
    }
}
