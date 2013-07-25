package ato.accesschest.ui;

import ato.accesschest.AccessChest;
import cpw.mods.fml.client.FMLClientHandler;
import invtweaks.api.container.ChestContainer;
import invtweaks.api.container.ContainerSection;
import invtweaks.api.container.ContainerSectionCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringTranslate;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * この MOD で追加されるチェストを開いた時の画面
 */
@ChestContainer(
        rowSize = 12
)
public class GuiAtoChest extends GuiContainer {

    private final static int GUI_RENAME_BUTTON_ID = 1;
    private final static int GUI_SORT_BUTTON_ID = 2;
    private final static int GUI_CLEAR_BUTTON_ID = 3;
    private final static int GUI_STOREEQP_BUTTON_ID = 4;
    private final static int GUI_STOREINV_BUTTON_ID = 5;
    private final static int GUI_EJECT_BUTTON_ID = 6;
    private ContainerAtoChestClient container;
    /**
     * サーバーに操作を知らせるパケットを送るため
     */
    private PacketSenderClient sender;
    /**
     * テキスト入力フォーム
     */
    private GuiTextField filterTextField;
    /**
     * スクロール中かどうか
     */
    private boolean isScrolling;
    private GuiButton renameButton;
    private GuiButton clearButton;
    private GuiButton sortButton;
    private GuiButton storeInvButton;
    private GuiButton storeEqpButton;
    private GuiButton ejectButton;

    public GuiAtoChest(ContainerAtoChestClient container) {
        super(container);
        this.container = container;
        xSize = 256;
        ySize = 256;
        sender = new PacketSenderClient();
    }

    // レンダリング関連

    @Override
    public void initGui() {
        super.initGui();
        // 座標の計算
        int ox = (width - xSize) / 2;
        int oy = (height - ySize) / 2;
        int left = ox + 176;
        int line0 = oy + 9 + 156;
        int line1 = oy + 9 + 173;
        int line2 = line1 + 19;
        int line3 = line2 + 19;
        int butWidth = 68;
        int butHeight = 20;
        // ボタンの作成
        clearButton = new GuiButton(GUI_CLEAR_BUTTON_ID, left, line1, butWidth, butHeight, I18n.func_135053_a("gui.button.clear"));
        renameButton = new GuiButton(GUI_RENAME_BUTTON_ID, left, line2, butWidth, butHeight, I18n.func_135053_a("gui.button.rename"));
        sortButton = new GuiButton(GUI_SORT_BUTTON_ID, left, line3, butWidth, butHeight, I18n.func_135053_a("gui.button.sort"));
        storeInvButton = new GuiButton(GUI_STOREINV_BUTTON_ID, left, line1, butWidth, butHeight, I18n.func_135053_a("gui.button.storeInventory"));
        storeEqpButton = new GuiButton(GUI_STOREEQP_BUTTON_ID, left, line2, butWidth, butHeight, I18n.func_135053_a("gui.button.storeEquipment"));
        ejectButton = new GuiButton(GUI_EJECT_BUTTON_ID, left, line3, butWidth, butHeight, I18n.func_135053_a("gui.button.eject"));
        updateButtonsDisplay(false);
        // ボタンの登録
        buttonList.clear();
        buttonList.add(clearButton);
        buttonList.add(renameButton);
        buttonList.add(sortButton);
        buttonList.add(storeInvButton);
        buttonList.add(storeEqpButton);
        buttonList.add(ejectButton);
        // テキスト入力フォームの作成
        filterTextField = new GuiTextField(fontRenderer, left, line0, 68, 16);
        filterTextField.setMaxStringLength(100);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        filterTextField.updateCursorCounter();
    }

    @Override
    public void drawScreen(int x, int y, float par3) {
        super.drawScreen(x, y, par3);
        GL11.glDisable(GL11.GL_LIGHTING);
        filterTextField.drawTextBox();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.func_110434_K().func_110577_a(new ResourceLocation("accesschest", "gui/atochest.png"));
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k + 9, 0, 0, xSize, ySize - 18);

        int sm = container.getScrollMax();
        if (sm != 0) {
            int scroll = (int) ((142 - 15) * (double) container.getScrollIndex() / sm);
            drawTexturedModalRect(j + 232, k + 17 + scroll, 2, 239, 12, 15);
        }

        int opacity = 63;
        for (int a = 12 * 8 - 1; a >= container.inventorySlots.size() - 9 * 4; --a) {
            int x = a % 12;
            int y = a / 12;
            int xpos = j + 12 + x * 18;
            int ypos = k + 9 + 8 + y * 18;
            this.drawGradientRect(xpos, ypos, xpos + 16, ypos + 16, opacity << 24, opacity << 24);
        }
    }

    // 操作関連

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        // ホイールによるスクロールのチェック
        int wheelDiff = Math.max(-1, Math.min(Mouse.getDWheel(), 1));
        if (wheelDiff != 0) {
            container.setScrollIndex(container.getScrollIndex() - wheelDiff * AccessChest.config.rowOnScroll);
            sender.sendScrollIndex(container.getScrollIndex());
        }
        // ドラッグによるスクロールのチェック
        if (isScrolling && Mouse.isButtonDown(0)) {
            // y 座標の計算式は GuiScreen#handleMouseInput を参照した
            scrollbarDragged(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1);
            sender.sendScrollIndex(container.getScrollIndex());
        } else {
            isScrolling = false;
        }
    }

    @Override
    protected void mouseClicked(int x, int y, int code) {
        super.mouseClicked(x, y, code);
        filterTextField.mouseClicked(x, y, code);
        // スクロールバーをクリックしたかのチェック
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        if (j + 232 <= x && x < j + 232 + 12 &&
                k + 17 <= y && y < k + 17 + 142) {
            isScrolling = true;
        }
    }

    /**
     * スクロールバーがドラッグされた
     */
    private void scrollbarDragged(int y) {
        int k = (height - ySize) / 2;
        float p = Math.max(0.0f, Math.min((y - (k + 17) - 15 / 2) / (float) (142 - 15), 1.0f));
        container.setScrollIndex(Math.round(container.getScrollMax() * p));
    }

    @Override
    public void handleKeyboardInput() {
        super.handleKeyboardInput();        // Button Switch
        updateButtonsDisplay(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT));
    }

    /**
     * SHIFT キー押下によるボタン群の表示、非表示を切り替える
     *
     * @param shift SHIFT キーが黄化されているかどうか
     */
    private void updateButtonsDisplay(boolean shift) {
        renameButton.drawButton = !shift;
        clearButton.drawButton = !shift;
        sortButton.drawButton = !shift;
        storeInvButton.drawButton = shift;
        storeEqpButton.drawButton = shift;
        ejectButton.drawButton = shift;
    }

    @Override
    protected void keyTyped(char c, int code) {
        if (filterTextField.isFocused() && code != 1) {
            if (code == Keyboard.KEY_RETURN) {
                // exactly match
                sender.sendFilter("exact:" + filterTextField.getText());
            } else {
                String prev = filterTextField.getText();
                filterTextField.textboxKeyTyped(c, code);

                String text = filterTextField.getText();
                if (text != prev) {
                    sender.sendFilter(text);
                }
            }
        } else {
            if (code == FMLClientHandler.instance().getClient().gameSettings.keyBindChat.keyCode
                    || code == FMLClientHandler.instance().getClient().gameSettings.keyBindCommand.keyCode) {
                filterTextField.setFocused(true);
            } else {
                super.keyTyped(c, code);
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        switch (button.id) {
            case GUI_RENAME_BUTTON_ID:
                sender.sendRename(filterTextField.getText());
                break;
            case GUI_SORT_BUTTON_ID:
                if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
                    sender.sendCustomSort(1);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
                    sender.sendCustomSort(2);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
                    sender.sendCustomSort(3);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_4)) {
                    sender.sendCustomSort(4);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_5)) {
                    sender.sendCustomSort(5);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_6)) {
                    sender.sendCustomSort(6);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_7)) {
                    sender.sendCustomSort(7);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_8)) {
                    sender.sendCustomSort(8);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_9)) {
                    sender.sendCustomSort(9);
                } else if (Keyboard.isKeyDown(Keyboard.KEY_0)) {
                    sender.sendCustomSort(0);
                } else {
                    int prevScroll = container.getScrollIndex();

                    sender.sendSort();
                    sender.sendFilter(filterTextField.getText());

                    // 以前のスクロール位置に戻す
                    container.setScrollIndex(prevScroll);
                    sender.sendScrollIndex(container.getScrollIndex());
                }
                break;
            case GUI_CLEAR_BUTTON_ID:
                filterTextField.setText("");
                sender.sendFilter(filterTextField.getText());
                break;
            case GUI_EJECT_BUTTON_ID:
                sender.sendEject();
                break;
            case GUI_STOREINV_BUTTON_ID:
                sender.sendStoreInventory();
                break;
            case GUI_STOREEQP_BUTTON_ID:
                sender.sendStoreEquipment();
                break;
        }
    }

    // その他

    @Override
    public void setWorldAndResolution(Minecraft mc, int par2, int par3) {
        super.setWorldAndResolution(mc, par2, par3);
        sender.setMinecraft(mc);
    }

    /**
     * 各スロットの種類を指定
     * InventoryTweaks への対応
     */
    @ContainerSectionCallback
    public Map<ContainerSection, List<Slot>> getSlotTypes() {
        HashMap<ContainerSection, List<Slot>> map = new HashMap<ContainerSection, List<Slot>>();
        List<Slot> slots = container.inventorySlots;
        int size = slots.size();

        map.put(ContainerSection.INVENTORY_HOTBAR, slots.subList(size - 9, size));
        map.put(ContainerSection.INVENTORY_NOT_HOTBAR, slots.subList(size - 36, size - 9));
        map.put(ContainerSection.CHEST, slots.subList(0, size - 36));

        return map;
    }
}
