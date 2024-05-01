package trofers.screen;

import com.mojang.blaze3d.platform.Lighting;
import dev.architectury.networking.NetworkManager;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import trofers.block.entity.TrophyBlockEntity;
import trofers.network.SetTrophyPacket;
import trofers.trophy.Trophy;
import trofers.trophy.TrophySearchTreeManager;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private List<ResourceLocation> trophies;

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
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
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
        List<ResourceLocation> trophies = this.trophies;

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
            searchBox.setFocused(false);
        }
        return result;
    }

    private void createUpperButtons() {
        int xPos = width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - NAVIGATION_BUTTON_SIZE;

        previousButton = Button.builder(Component.literal("<"), button -> setCurrentPage(currentPage - 1))
                .pos(xPos, VERTICAL_PADDING)
                .width(NAVIGATION_BUTTON_SIZE)
                .build();
        addRenderableWidget(previousButton);

        xPos += NAVIGATION_BUTTON_SIZE + BUTTON_SPACING;
        Button cancelButton = Button.builder(CommonComponents.GUI_CANCEL, button -> onClose())
                .pos(xPos, VERTICAL_PADDING)
                .width(CANCEL_BUTTON_WIDTH)
                .build();
        addRenderableWidget(cancelButton);

        xPos += CANCEL_BUTTON_WIDTH + BUTTON_SPACING;
        nextButton = Button.builder(Component.literal(">"), button -> setCurrentPage(currentPage + 1))
                .pos(xPos, VERTICAL_PADDING)
                .width(NAVIGATION_BUTTON_SIZE)
                .build();
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

    public void onEditSearchBox(String text) {
        List<ResourceLocation> searchResult = TrophySearchTreeManager.search(text);

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

                ResourceLocation trophyId = trophies.get(index++);
                ItemStack stack = Trophy.createItem(blockState.getBlock(), trophyId);

                int x = columnStart + column * (TROPHY_BUTTON_SIZE + BUTTON_SPACING);
                int y = rowStart + row * (TROPHY_BUTTON_SIZE + BUTTON_SPACING);
                Button trophyButton = new TrophyButton(x, y, TROPHY_BUTTON_SIZE, stack, trophyId);
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
        private final ResourceLocation trophyId;
        private final int x;
        private final int y;

        private TrophyButton(int xPos, int yPos, int size, ItemStack item, ResourceLocation trophyId) {
            super(xPos, yPos, size, size, Component.empty(), button -> {}, supplier -> item.getHoverName().copy());
            setTooltip(Tooltip.create(item.getHoverName()));
            this.trophyId = trophyId;
            this.item = item;
            this.x = xPos;
            this.y = yPos;
        }

        @Override
        public void onClick(double x, double y) {
            NetworkManager.sendToServer(new SetTrophyPacket(trophyId, blockPos));
            if (Minecraft.getInstance().player != null) {
                if (Minecraft.getInstance().player.level().getBlockEntity(blockPos) instanceof TrophyBlockEntity blockEntity) {
                    // Don't wait on the server to sync the change back to the client
                    blockEntity.setTrophy(trophyId, null);
                }
            }
            onClose();
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            super.renderWidget(guiGraphics, mouseX, mouseY, partialTicks);

            tryRenderScaledGuiItem(
                    guiGraphics,
                    item,
                    x + (int) (width - 16 * ITEM_SCALE) / 2,
                    y + (int) (height - 16 * ITEM_SCALE) / 2,
                    ITEM_SCALE
            );
        }

        @SuppressWarnings("SameParameterValue")
        private void tryRenderScaledGuiItem(GuiGraphics graphics, ItemStack stack, int x, int y, float scale) {
            if (!stack.isEmpty()) {
                BakedModel bakedModel = Minecraft.getInstance().getItemRenderer().getModel(stack, Minecraft.getInstance().level, null, 0);
                graphics.pose().pushPose();
                graphics.pose().translate(x + (8 * scale), y + (8 * scale), 150);

                try {
                    graphics.pose().scale(16 * scale, -16 * scale, 16 * scale);
                    boolean bl = !bakedModel.usesBlockLight();
                    if (bl) {
                        Lighting.setupForFlatItems();
                    }

                    Minecraft.getInstance()
                            .getItemRenderer()
                            .render(stack, ItemDisplayContext.GUI, false, graphics.pose(), graphics.bufferSource(), 15728880, OverlayTexture.NO_OVERLAY, bakedModel);
                    graphics.flush();
                    if (bl) {
                        Lighting.setupFor3DItems();
                    }
                } catch (Throwable var12) {
                    CrashReport crashReport = CrashReport.forThrowable(var12, "Rendering item");
                    CrashReportCategory crashReportCategory = crashReport.addCategory("Item being rendered");
                    crashReportCategory.setDetail("Item Type", () -> String.valueOf(stack.getItem()));
                    crashReportCategory.setDetail("Item Components", () -> String.valueOf(stack.getComponents()));
                    crashReportCategory.setDetail("Item Foil", () -> String.valueOf(stack.hasFoil()));
                    throw new ReportedException(crashReport);
                }

                graphics.pose().popPose();
            }
        }
    }
}
