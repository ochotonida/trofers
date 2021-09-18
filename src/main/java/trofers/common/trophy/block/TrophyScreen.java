package trofers.common.trophy.block;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fmlclient.gui.widget.ExtendedButton;
import trofers.Trofers;
import trofers.common.network.NetworkHandler;
import trofers.common.network.SetTrophyPacket;
import trofers.common.trophy.Trophy;
import trofers.common.trophy.TrophyManager;

import java.util.*;
import java.util.stream.Collectors;

public class TrophyScreen extends Screen {

    private static final int HORIZONTAL_PADDING = 120;
    private static final int VERTICAL_PADDING = 30;
    private static final int BUTTON_SIZE = 24;
    private static final int BUTTON_SPACING = 8;
    private static final int CANCEL_BUTTON_WIDTH = 64;
    private static final int UPPER_BUTTON_SIZE = 16;
    private static final int MIN_ROWS = 2;
    private static final int MIN_COLUMNS = 2;

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
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
        renderBackground(poseStack);
        super.render(poseStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (minecraft != null && minecraft.options.keyInventory.isActiveAndMatches(mouseKey)) {
            this.onClose();
            return true;
        }
        return false;
    }

    @Override
    protected void init() {
        int previousRows = rows;
        int previousColumns = columns;

        columns = (width - HORIZONTAL_PADDING * 2 - BUTTON_SIZE) / (BUTTON_SIZE + BUTTON_SPACING) + 1;
        columns = Math.max(columns, MIN_COLUMNS);

        columnStart = width / 2 - (BUTTON_SIZE * columns + BUTTON_SPACING * (columns - 1)) / 2;
        rowStart = VERTICAL_PADDING + UPPER_BUTTON_SIZE + 16;

        rows = (height - rowStart - VERTICAL_PADDING - BUTTON_SIZE) / (BUTTON_SIZE + BUTTON_SPACING) + 1;
        rows = Math.max(rows, MIN_ROWS);

        createUpperButtons();

        if (columns == previousColumns && rows == previousRows) {
            setCurrentPage(currentPage);
        } else {
            setCurrentPage(0);
        }
    }

    private void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
        trophyButtons.forEach(this::removeWidget);

        List<Trophy> trophies = TrophyManager.values().stream().sorted(Comparator.comparing(trophy -> trophy.getId().toString())).collect(Collectors.toCollection(ArrayList::new));
        int index = currentPage * columns * rows;

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                if (index >= trophies.size()) {
                    break;
                }

                ItemStack stack = new ItemStack(trophyItem);
                Trophy trophy = trophies.get(index++);
                stack.getOrCreateTagElement("BlockEntityTag").putString("Trophy", trophy.getId().toString());

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

    private void createUpperButtons() {
        addRenderableWidget(new ExtendedButton(
                width / 2 - CANCEL_BUTTON_WIDTH / 2,
                VERTICAL_PADDING,
                CANCEL_BUTTON_WIDTH,
                UPPER_BUTTON_SIZE,
                new TranslatableComponent(String.format("button.%s.cancel", Trofers.MODID)),
                button -> onClose()
        ));

        previousButton = addRenderableWidget(new ExtendedButton(
                width / 2 - CANCEL_BUTTON_WIDTH / 2 - BUTTON_SPACING - UPPER_BUTTON_SIZE,
                VERTICAL_PADDING,
                UPPER_BUTTON_SIZE,
                UPPER_BUTTON_SIZE,
                new TranslatableComponent(String.format("button.%s.previous", Trofers.MODID)),
                button -> setCurrentPage(currentPage - 1)
        ));
        nextButton = addRenderableWidget(new ExtendedButton(
                width / 2 + CANCEL_BUTTON_WIDTH / 2 + BUTTON_SPACING,
                VERTICAL_PADDING,
                UPPER_BUTTON_SIZE,
                UPPER_BUTTON_SIZE,
                new TranslatableComponent(String.format("button.%s.next", Trofers.MODID)),
                button -> setCurrentPage(currentPage + 1)
        ));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private void setTrophy(Trophy trophy) {
        NetworkHandler.INSTANCE.sendToServer(new SetTrophyPacket(trophy, blockPos));
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
            itemRenderer.renderAndDecorateItem(item, x + (width - 16) / 2, y + (height - 16) / 2);
            itemRenderer.renderGuiItemDecorations(font, item, x + (width - 16) / 2, y + (height - 16) / 2);

            if (isHovered()) {
                renderToolTip(poseStack, mouseX, mouseY);
            }
        }

        @Override
        public void renderToolTip(PoseStack poseStack, int mouseX, int mouseY) {
            renderTooltip(poseStack, item, mouseX, mouseY);
        }
    }
}
