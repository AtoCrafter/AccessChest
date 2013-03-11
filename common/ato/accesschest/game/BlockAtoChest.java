package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * この MOD で追加されるゲーム内のブロックの抽象クラス
 */
public abstract class BlockAtoChest extends BlockContainer {

    protected BlockAtoChest(int id) {
        super(id, 0, Material.rock);
        setHardness(0.8f);
        setCreativeTab(CreativeTabs.tabDecorations);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int dist, float distX, float distY, float distZ) {
        if (!world.isRemote) {
            player.openGui(
                    AccessChest.instance,
                    Properties.GUI_ID_TILEENTITY,
                    world, x, y, z);
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 22;
    }

//    @Override
//    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
//        TileEntityAbstChest chest = (TileEntityAbstChest)world.getBlockTileEntity(x, y, z);
//        ItemAbstChest item = null;
//        if ( chest.getType() == 0 ) {
//            item = (ItemAbstChest)Item.itemsList[ConfigValues.blockAccessChestID];
//        } else if ( chest.getType() == 1 ) {
//            item = (ItemAbstChest)Item.itemsList[ConfigValues.blockCompressedChestID];
//        }
//        ItemStack itemstack = new ItemStack(item, 1, item.getDamage(chest.getGrade(), chest.getColor()));
//        EntityItem entity = new EntityItem(world, x+0.5D, y+0.5D, z+0.5D, itemstack);
//        entity.delayBeforeCanPickup = 10;
//        world.spawnEntityInWorld(entity);
//        super.breakBlock(world, x, y, z, id, meta);
//    }
//
//    @Override
//    public int quantityDropped(Random par1Random) {
//        return 0;
//    }
//
//    /**
//     * ブロックが設置されたときの処理
//     * ※ BlockEnderChest の同名処理を参考に
//     */
//    @Override
//    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLiving par5EntityLiving) {
//        super.onBlockPlacedBy(par1World, par2, par3, par4, par5EntityLiving);    //To change body of overridden methods use File | Settings | File Templates.
//    }
}
