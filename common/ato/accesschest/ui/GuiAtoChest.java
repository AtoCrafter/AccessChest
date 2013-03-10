package ato.accesschest.ui;

import ato.accesschest.Properties;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 * この MOD で追加されるチェストを開いた時の画面
 */
public class GuiAtoChest extends GuiContainer {

    private ContainerAtoChestClient container;
    /**
     * サーバーに操作を知らせるパケットを送るため
     */
    private PacketSenderClient sender;
    //    private ContainerAccessChestSlave container;
    /**
     * テキスト入力フォーム
     */
    private GuiTextField filterTextField;
    /**
     * スクロール中かどうか
     */
    private boolean isScrolling;
//    private boolean wasClicking;
//
//    private static final Utils utils = Utils.getInstance();

    private GuiButton renameButton;
    private GuiButton clearButton;
    private GuiButton sortButton;
    private GuiButton storeInvButton;
    private GuiButton storeEqpButton;
    private GuiButton ejectButton;

    private final static int GUI_RENAME_BUTTON_ID = 1;
    private final static int GUI_SORT_BUTTON_ID = 2;
    private final static int GUI_CLEAR_BUTTON_ID = 3;
    private final static int GUI_STOREEQP_BUTTON_ID = 4;
    private final static int GUI_STOREINV_BUTTON_ID = 5;
    private final static int GUI_EJECT_BUTTON_ID = 6;

    public GuiAtoChest(ContainerAtoChestClient container) {
        super(container);
        this.container = container;
        xSize = 256;
        ySize = 256;
        sender = new PacketSenderClient();
    }
//
//    public GuiAccessChest(ContainerAccessChestSlave container) {
//        super(container);
//        this.container = container;
//        xSize = 256;
//        ySize = 256;
//        isScrolling = false;
//        wasClicking = false;
//    }

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
        StringTranslate trans = StringTranslate.getInstance();
        renameButton = new GuiButton(GUI_RENAME_BUTTON_ID, left, line1, butWidth, butHeight, trans.translateKey("gui.button.rename"));
        clearButton = new GuiButton(GUI_CLEAR_BUTTON_ID, left, line2, butWidth, butHeight, trans.translateKey("gui.button.clear"));
        sortButton = new GuiButton(GUI_SORT_BUTTON_ID, left, line3, butWidth, butHeight, trans.translateKey("gui.button.sort"));
        storeInvButton = new GuiButton(GUI_STOREINV_BUTTON_ID, left, line1, butWidth, butHeight, trans.translateKey("gui.button.storeInventory"));
        storeEqpButton = new GuiButton(GUI_STOREEQP_BUTTON_ID, left, line2, butWidth, butHeight, trans.translateKey("gui.button.storeEquipment"));
        ejectButton = new GuiButton(GUI_EJECT_BUTTON_ID, left, line3, butWidth, butHeight, trans.translateKey("gui.button.eject"));
        updateButtonsDisplay(false);
        // ボタンの登録
        controlList.clear();
        controlList.add(clearButton);
        controlList.add(renameButton);
        controlList.add(sortButton);
        controlList.add(storeInvButton);
        controlList.add(storeEqpButton);
        controlList.add(ejectButton);
        // テキスト入力フォームの作成
        filterTextField = new GuiTextField(fontRenderer, left, line0, 68, 16);
        filterTextField.setMaxStringLength(10);
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
        int i = mc.renderEngine.getTexture("/ato/accesschest/AccessChestGui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k + 9, 0, 0, xSize, ySize - 18);
        int sm = container.getScrollMax();
        if (sm != 0) {
            int scroll = (int) ((142 - 15) * (double) container.getScrollIndex() / sm);
            drawTexturedModalRect(j + 232, k + 17 + scroll, 2, 239, 12, 15);
        }
    }

    // 操作関連

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        // ホイールによるスクロールのチェック
        int wheelDiff = Math.max(-1, Math.min(Mouse.getDWheel(), 1));
        if (wheelDiff != 0) {
            container.setScrollIndex(container.getScrollIndex() - wheelDiff * Properties.ROWS_ON_SCROLL);
            sender.sendScrollIndex(container.getScrollIndex());
        }
        // ドラッグによるスクロールのチェック
        if (isScrolling && Mouse.isButtonDown(0)) {
            // y 座標の計算式は GuiScreen#handleMouseInput を参照した
            scrollbarDragged(this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1);
            sender.sendScrollIndex(container.getScrollIndex());
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
        if (!Mouse.isButtonDown(0)) {
            isScrolling = false;
            return;
        }
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
            filterTextField.textboxKeyTyped(c, code);

            String text = filterTextField.getText();
            sender.sendFilter(text);
        } else {
            if (code == FMLClientHandler.instance().getClient().gameSettings.keyBindChat.keyCode
                    || code == FMLClientHandler.instance().getClient().gameSettings.keyBindCommand.keyCode) {
                filterTextField.setFocused(true);
            } else {
                super.keyTyped(c, code);
            }
        }
    }

//    @Override
//    protected void actionPerformed(GuiButton guibutton) {
//        try {
//            PacketGeneratorGui packet = new PacketGeneratorGui();
//            if ( guibutton.id == GUI_SORT_BUTTON_ID ) {
//                packet.instruction = PacketGeneratorGui.SET_PRIORITY;
//                if ( Keyboard.isKeyDown(Keyboard.KEY_1) ) {
//                    packet.intData = 1;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_2) ) {
//                    packet.intData = 2;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_3) ) {
//                    packet.intData = 3;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_4) ) {
//                    packet.intData = 4;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_5) ) {
//                    packet.intData = 5;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_6) ) {
//                    packet.intData = 6;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_7) ) {
//                    packet.intData = 7;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_8) ) {
//                    packet.intData = 8;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_9) ) {
//                    packet.intData = 9;
//                } else if ( Keyboard.isKeyDown(Keyboard.KEY_0) ) {
//                    packet.intData = 100;
//                } else if ( Keyboard.isKeyDown(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.keyCode) ) {
//                    int defaultPri = ComparatorAccessChest.DEFAULT_PRIORITY;
//                    packet.intData = defaultPri;
//                } else {
//                    packet.instruction = PacketGeneratorGui.SORT;
//                }
//            } else if ( guibutton.id == GUI_RENAME_BUTTON_ID ) {
//                packet.instruction = PacketGeneratorGui.SET_NAME;
//                packet.textData = filterTextField.getText();
//            } else if ( guibutton.id == GUI_CLEAR_BUTTON_ID ) {
//                packet.instruction = PacketGeneratorGui.SET_FILTER;
//                packet.textData = "";
//                filterTextField.setText("");
//            } else if ( guibutton.id == GUI_STOREINV_BUTTON_ID ) {
//                packet.instruction = PacketGeneratorGui.STORE_INV;
//            } else if ( guibutton.id == GUI_STOREEQP_BUTTON_ID ) {
//                packet.instruction = PacketGeneratorGui.STORE_EQUIP;
//            } else if ( guibutton.id == GUI_EJECT_BUTTON_ID ) {
//                if ( Keyboard.isKeyDown(FMLClientHandler.instance().getClient().gameSettings.keyBindSneak.keyCode) ) {
//                    packet.instruction = PacketGeneratorGui.EJECT;
//                }
//            }
//            if ( packet.isValid() ) utils.sendPacket(packet.generate());
//        } catch ( IOException e ) {
//            e.printStackTrace();
//        }
//    }

    // その他

    @Override
    public void setWorldAndResolution(Minecraft mc, int par2, int par3) {
        super.setWorldAndResolution(mc, par2, par3);
        sender.setMinecraft(mc);
    }
}
