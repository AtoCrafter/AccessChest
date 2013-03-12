package ato.accesschest.game;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * ゲーム内での Access Chest ブロック
 */
public class BlockAccessChest extends BlockAtoChest {

    public BlockAccessChest(int id) {
        super(id);
        setBlockName("accesschest");
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityAccessChest();
    }

//    // duplicated from BlockEnderChest
//    public void randomDisplayTick(World par1World, int par2, int par3, int par4, Random par5Random)
//    {
//        for (int var6 = 0; var6 < 3; ++var6)
//        {
//            double var10000 = (double)((float)par2 + par5Random.nextFloat());
//            double var9 = (double)((float)par3 + par5Random.nextFloat());
//            var10000 = (double)((float)par4 + par5Random.nextFloat());
//            double var13 = 0.0D;
//            double var15 = 0.0D;
//            double var17 = 0.0D;
//            int var19 = par5Random.nextInt(2) * 2 - 1;
//            int var20 = par5Random.nextInt(2) * 2 - 1;
//            var13 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
//            var15 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
//            var17 = ((double)par5Random.nextFloat() - 0.5D) * 0.125D;
//            double var11 = (double)par4 + 0.5D + 0.25D * (double)var20;
//            var17 = (double)(par5Random.nextFloat() * 1.0F * (float)var20);
//            double var7 = (double)par2 + 0.5D + 0.25D * (double)var19;
//            var13 = (double)(par5Random.nextFloat() * 1.0F * (float)var19);
//            par1World.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
//        }
//    }
}
