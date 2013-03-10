package ato.accesschest.game;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import java.util.List;

/**
 * この MOD で追加されるゲーム内のアイテムの抽象クラス
 */
public abstract class ItemAtoChest extends ItemBlock {

    public ItemAtoChest(int id) {
        super(id);
        setHasSubtypes(true);
        setMaxDamage(0);
    }

//    @Override
//    public boolean placeBlockAt(ItemStack itemstack, EntityPlayer player, World world,
//                                int x, int y, int z, int side, float distX, float distY, float distZ) {
//        if ( !player.isSneaking() ) return false;
//
//        int blockId = getBlockID();
//
//        // rotate direction
//        byte direction = 0;
//        int playerDirection = MathHelper.floor_double((double) ((player.rotationYaw * 4F) / 360F) + 0.5D) & 3;
//        switch ( playerDirection ) {
//            case 0: direction = 2; break;
//            case 1: direction = 5; break;
//            case 2: direction = 3; break;
//            case 3: direction = 4; break;
//        }
//
//        if ( !world.setBlockAndMetadataWithNotify(x, y, z, blockId, direction) ) return false;
//
//        if ( world.getBlockId(x, y, z) == blockId ) {
//            Block.blocksList[blockId].updateBlockMetadata(world, x, y, z, side, distX, distY, distZ);
//            Block.blocksList[blockId].onBlockPlacedBy(world, x, y, z, player);
//        }
//
//        // link block with entity of AccessChest
//        int damage = itemstack.getItemDamage();
//        TileEntityAbstChest tileEntity = (TileEntityAbstChest)world.getBlockTileEntity(x, y, z);
//        linkAbstChest(tileEntity, player, getColor(damage), getGrade(damage), world.isRemote);
//
//        return true;
//    }
//
//    protected abstract void linkAbstChest(TileEntityAbstChest tileEntity, EntityPlayer player, int color, int grade, boolean isRemote);

    @Override
    public void getSubItems(int id, CreativeTabs tab, List list) {
        list.add(new ItemStack(id, 1, 0x0F));
        list.add(new ItemStack(id, 1, 0x1F));
        list.add(new ItemStack(id, 1, 0x2F));
        list.add(new ItemStack(id, 1, 0x3F));
    }
}
