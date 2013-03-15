package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryCompressedChest;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCompressedChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade) {
        return new RepositoryCompressedChest(color, grade);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ((RepositoryCompressedChest) repo).readFromNBT(nbt);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        ((RepositoryCompressedChest) repo).writeToNBT(nbt);
    }
}
