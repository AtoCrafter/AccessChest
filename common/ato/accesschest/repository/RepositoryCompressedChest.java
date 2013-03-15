package ato.accesschest.repository;

import net.minecraft.nbt.NBTTagCompound;

/**
 * Compressed Chest の実体
 */
public class RepositoryCompressedChest extends Repository {

    public RepositoryCompressedChest(int grade) {
        super(new DataManagerNBT());
    }

    @Override
    public String getInvName() {
        return "Compressed Chest";
    }

    public void readFromNBT(NBTTagCompound nbt) {
        ((DataManagerNBT) data).readFromNBT(nbt);
    }

    public void writeToNBT(NBTTagCompound nbt) {
        ((DataManagerNBT) data).writeToNBT(nbt);
    }
}
