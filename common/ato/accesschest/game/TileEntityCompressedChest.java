package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryCompressedChest;
import net.minecraft.nbt.NBTTagCompound;

public class TileEntityCompressedChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade, boolean isOriginal) {
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

    @Override
    public void openChest() {
        super.openChest();
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessChest.config.blockIDCC, 1, numUsingPlayers);
    }

    @Override
    public void closeChest() {
        super.closeChest();
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessChest.config.blockIDCC, 1, numUsingPlayers);
    }
}
