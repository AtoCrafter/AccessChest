package ato.accesschest.game;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * ゲーム内での Compressed Chest ブロック
 */
public class BlockCompressedChest extends BlockAtoChest {

    public BlockCompressedChest(int id) {
        super(id);
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityCompressedChest();
    }

    /**
     * @see net.minecraft.block.BlockChest#breakBlock(net.minecraft.world.World, int, int, int, int, int)
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        TileEntityCompressedChest tileEntity = (TileEntityCompressedChest) world.getBlockTileEntity(x, y, z);
        if (tileEntity != null) {
            for (int i = 0; i < tileEntity.getSizeInventory(); ++i) {
                ItemStack is = tileEntity.getStackInSlot(i);
                if (is != null) {
                    EntityItem entityItem = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, is);
                    entityItem.motionY = 0.2F;
                    world.spawnEntityInWorld(entityItem);
                }
            }
        }
        super.breakBlock(world, x, y, z, id, meta);
    }
}
