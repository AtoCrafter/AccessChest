package ato.accesschest.handler;

import ato.accesschest.game.ItemAccessChest;
import cpw.mods.fml.client.registry.KeyBindingRegistry;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;

import java.util.EnumSet;

/**
 * この MOD で使用する特殊キーを扱うためのキーハンドラ
 */
public class KeybindHandler extends KeyBindingRegistry.KeyHandler {

    private static final KeyBinding key = new KeyBinding("AccessChest TransferKey", Keyboard.KEY_X);

    public KeybindHandler() {
        super(new KeyBinding[]{key}, new boolean[]{false});
    }

    @Override
    public void keyDown(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd, boolean isRepeat) {
        if (kb == key) {
            ItemAccessChest.canTransfer = true;
        }
    }

    @Override
    public void keyUp(EnumSet<TickType> types, KeyBinding kb, boolean tickEnd) {
        if (kb == key) {
            ItemAccessChest.canTransfer = false;
        }
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return "AccessChestSpecailKey";
    }
}
