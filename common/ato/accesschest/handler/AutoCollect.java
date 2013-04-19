package ato.accesschest.handler;

import ato.accesschest.AccessChest;
import ato.accesschest.game.ItemAccessChest;
import ato.accesschest.repository.RepositoryAccessChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

/**
 * アイテムを拾った時にインベントリが一杯なら自動で手持ちのアクセスチェスト内に格納します。
 */
public class AutoCollect {

    /**
     * 手持ちのインベントリに Access Chest があれば、そこに収納する
     *
     * @return 収納したか
     */
    @ForgeSubscribe
    public void pickupItem(EntityItemPickupEvent event) {
        EntityPlayer player = event.entityPlayer;
        ItemStack item = event.item.func_92014_d();
        if (player.inventory.addItemStackToInventory(item)) return;

        ItemStack[] inventory = player.inventory.mainInventory;
        for (int i = 0; i < inventory.length; ++i) {
            if (inventory[i] != null && inventory[i].getItem() instanceof ItemAccessChest) {
                int damage = inventory[i].getItemDamage();
                RepositoryAccessChest repo = new RepositoryAccessChest(
                        player.worldObj,
                        AccessChest.id2color(damage),
                        AccessChest.id2grade(damage),
                        AccessChest.id2isOriginal(damage));
                repo.storeItem(item);
                if (item.stackSize <= 0) break;
            }
        }
    }
}
