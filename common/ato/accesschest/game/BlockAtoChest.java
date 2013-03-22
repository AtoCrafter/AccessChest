package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.Properties;
import ato.accesschest.repository.Repository;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

/**
 * この MOD で追加されるゲーム内のブロックの抽象クラス
 */
public abstract class BlockAtoChest extends BlockContainer {

    public static int RENDER_ID;

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
        return RENDER_ID;
    }

    /**
     * ブロックが設置されたときの処理
     *
     * @see net.minecraft.block.BlockEnderChest#onBlockPlacedBy
     */
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving living) {
        byte direction = 0;
        switch (MathHelper.floor_double((double) (living.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3) {
            case 0:
                direction = 2;
                break;
            case 1:
                direction = 5;
                break;
            case 2:
                direction = 3;
                break;
            case 3:
                direction = 4;
                break;
        }
        world.setBlockMetadataWithNotify(x, y, z, direction);
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta) {
        TileEntityAtoChest tileEntity = (TileEntityAtoChest) world.getBlockTileEntity(x, y, z);
        Repository repo = tileEntity.getRepository();
        ItemStack item = new ItemStack(
                Item.itemsList[id],
                1,
                AccessChest.colorgrade2id(repo.getColor(), repo.getGrade(), repo.isOriginal()));
        EntityItem entity = new EntityItem(world, x + 0.5D, y + 0.5D, z + 0.5D, item);
        entity.delayBeforeCanPickup = 10;
        world.spawnEntityInWorld(entity);
        super.breakBlock(world, x, y, z, id, meta);
    }

    @Override
    public int quantityDropped(Random par1Random) {
        return 0;
    }
}
