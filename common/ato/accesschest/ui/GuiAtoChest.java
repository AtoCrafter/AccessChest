package ato.accesschest.ui;

import ato.accesschest.Properties;
import com.sun.xml.internal.ws.client.SenderException;
import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.inventory.Container;
import net.minecraft.util.StringTranslate;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

/**
 * この MOD で追加されるチェストを開いた時の画面
 */
public class GuiAtoChest extends GuiContainer {

    private ContainerAtoChest container;
    private PacketSender sender;
//    private ContainerAccessChestSlave container;
    private GuiTextField filterTextField;
//    private boolean isScrolling;
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

    public GuiAtoChest(ContainerAtoChest container) {
        super(container);
        this.container = container;
        xSize = 256;
        ySize = 256;
        sender = new PacketSender();
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

    @Override
    public void initGui() {
        super.initGui();
        int ox = (width - xSize) / 2;
        int oy = (height - ySize) / 2;
        controlList.clear();
        filterTextField = new GuiTextField(fontRenderer, 176, 9+156, 68, 16);
        filterTextField.setMaxStringLength(10);
        int left = ox + 176;
        int line1 = oy + 9 + 173;
        int line2 = line1 + 19;
        int line3 = line2 + 19;
        int butWidth = 68;
        int butHeight = 20;
        StringTranslate trans = StringTranslate.getInstance();
        renameButton   = new GuiButton(GUI_RENAME_BUTTON_ID,   left, line1, butWidth, butHeight, trans.translateKey("gui.button.rename"));
        clearButton    = new GuiButton(GUI_CLEAR_BUTTON_ID,    left, line2, butWidth, butHeight, trans.translateKey("gui.button.clear"));
        sortButton     = new GuiButton(GUI_SORT_BUTTON_ID,     left, line3, butWidth, butHeight, trans.translateKey("gui.button.sort"));
        storeInvButton = new GuiButton(GUI_STOREINV_BUTTON_ID, left, line1, butWidth, butHeight, trans.translateKey("gui.button.storeInventory"));
        storeEqpButton = new GuiButton(GUI_STOREEQP_BUTTON_ID, left, line2, butWidth, butHeight, trans.translateKey("gui.button.storeEquipment"));
        ejectButton    = new GuiButton(GUI_EJECT_BUTTON_ID,    left, line3, butWidth, butHeight, trans.translateKey("gui.button.eject"));
        controlList.add(clearButton);
        controlList.add(renameButton);
        controlList.add(sortButton);
        controlList.add(storeInvButton);
        controlList.add(storeEqpButton);
        controlList.add(ejectButton);
    }

//    @Override
//    public void updateScreen() {
//        super.updateScreen();
//        filterTextField.updateCursorCounter();
//    }
//
//    @Override
//    public void drawScreen(int mouseX, int mouseY, float par3) {
//        boolean clicking = Mouse.isButtonDown(0);
//        // if click scrollbar (Not drag)
//        int j = (width - xSize) / 2;
//        int k = (height - ySize) / 2;
//        if ( !wasClicking && clicking &&
//                j+232 <= mouseX && mouseX < j+232+12 &&
//                k+17 <= mouseY && mouseY < k+17+142 ) {
//            isScrolling = true;
//        }
//        // stop scrolling
//        if ( !clicking ) {
//            isScrolling = false;
//        }
//        // store if click
//        wasClicking = clicking;
//        // scroll
//        if ( isScrolling ) {
//            float p = (mouseY - (k+17) - 15/2) / (float)(142-15);
//            if ( p < 0 ) { p = 0.0f; }
//            if ( p > 1 ) { p = 1.0f; }
//            container.scrollTo(p);
//            PacketGeneratorGui packet = new PacketGeneratorGui();
//            packet.instruction = PacketGeneratorGui.SET_SCROLL;
//            packet.intData = container.getCurrentScroll();
//            try {
//                utils.sendPacket(packet.generate());
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        // Button Switch
//        boolean shift = Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
//        renameButton.drawButton   = !shift;
//        clearButton.drawButton    = !shift;
//        sortButton.drawButton     = !shift;
//        storeInvButton.drawButton =  shift;
//        storeEqpButton.drawButton =  shift;
//        ejectButton.drawButton    =  shift;
//        super.drawScreen(mouseX, mouseY, par3);
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int x, int y) {
//        super.drawGuiContainerForegroundLayer(x, y);
//        filterTextField.drawTextBox();
//    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i1, int i2) {
        int i = mc.renderEngine.getTexture("/ato/accesschest/AccessChestGui.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(i);
        int j = (width - xSize) / 2;
        int k = (height - ySize) / 2;
        drawTexturedModalRect(j, k + 9, 0, 0, xSize, ySize - 18);
//        int sm = container.getScrollMax();
//        if ( sm != 0 ) {
//            int scroll = (int)((142-15) * (double)container.getCurrentScroll() / sm);
//            drawTexturedModalRect(j+232, k+17+scroll, 2, 239, 12, 15);
//        }
    }

    @Override
    public void handleMouseInput() {
        super.handleMouseInput();
        int wheelDiff = Mouse.getDWheel();
        if (wheelDiff != 0) {
            container.setScrollIndex(container.getScrollIndex() - wheelDiff * Properties.ROWS_ON_SCROLL);
            sender.sendScrollIndex(mc, container.getScrollIndex());
        }
    }

//    @Override
//    protected void keyTyped(char c, int code) {
//        if ( filterTextField.isFocused() &&
//                code != 1 ) {
//            filterTextField.textboxKeyTyped(c, code);
//
//            String text = filterTextField.getText();
//            PacketGeneratorGui packet = new PacketGeneratorGui();
//            if ( text.contains("mine:") ) {
//                packet.instruction = PacketGeneratorGui.SET_OWN;
//                packet.intData = 1;
//            } else if ( text.contains("ours:") ) {
//                packet.instruction = PacketGeneratorGui.SET_OWN;
//                packet.intData = 0;
//            } else {
//                packet.instruction = PacketGeneratorGui.SET_FILTER;
//                packet.textData = text;
//            }
//            if ( packet.isValid() ) {
//                try {
//                    utils.sendPacket( packet.generate() );
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        } else {
//            if ( code == FMLClientHandler.instance().getClient().gameSettings.keyBindChat.keyCode ) {
//                filterTextField.setFocused(true);
//            } else {
//                super.keyTyped(c, code);
//            }
//        }
//    }
//
//    @Override
//    protected void mouseClicked(int x, int y, int code) {
//        super.mouseClicked(x, y, code);
//        filterTextField.mouseClicked(x-guiLeft, y-guiTop, code);
//    }
//
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
}
