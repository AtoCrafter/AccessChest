package ato.accesschest.game;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * ゲーム内での Compressed Chest アイテム
 */
public class ItemCompressedChest extends ItemAtoChest {

    public ItemCompressedChest(int id) {
        super(id);
        setItemName("compressedchest");
    }

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0x1F));
        list.add(new ItemStack(id, 1, 0x2F));
    }
}
