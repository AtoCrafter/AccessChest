package ato.accesschest.game;

import ato.accesschest.AccessChest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * ゲーム内での Compressed Chest アイテム
 */
public class ItemCompressedChest extends ItemAtoChest {

    public ItemCompressedChest(int id) {
        super(id);
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 1)));
        list.add(new ItemStack(id, 1, AccessChest.colorgrade2id(15, 2)));
    }
}
