package ato.accesschest.game;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

/**
 * ゲーム内での Compressed Chest ブロック
 */
public class BlockCompressedChest extends BlockAtoChest {

    public BlockCompressedChest(int id) {
        super(id);
        setBlockName("compressedchest");
    }

    @Override
    public TileEntity createNewTileEntity(World var1) {
        return new TileEntityCompressedChest();
    }
}
