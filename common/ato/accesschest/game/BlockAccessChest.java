package ato.accesschest.game;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Random;

/**
 * ゲーム内での Access Chest ブロック
 */
public class BlockAccessChest extends BlockAtoChest {

    public BlockAccessChest(int id) {
        super(id);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityAccessChest();
    }

    /**
     * エンダーチェスト同じようにパーティクルエフェクトを出す
     *
     * @see net.minecraft.block.BlockEnderChest#randomDisplayTick(net.minecraft.world.World, int, int, int, java.util.Random)
     */
    public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
        for (int i = 0; i < 3; ++i) {
            double var9 = (double) ((float) y + rand.nextFloat());
            int var19 = rand.nextInt(2) * 2 - 1;
            int var20 = rand.nextInt(2) * 2 - 1;
            double var11 = (double) z + 0.5D + 0.25D * (double) var20;
            double var15 = ((double) rand.nextFloat() - 0.5D) * 0.125D;
            double var17 = (double) (rand.nextFloat() * 1.0F * (float) var20);
            double var7 = (double) x + 0.5D + 0.25D * (double) var19;
            double var13 = (double) (rand.nextFloat() * 1.0F * (float) var19);
            world.spawnParticle("portal", var7, var9, var11, var13, var15, var17);
        }
    }
}
