package ato.accesschest.game;

import ato.accesschest.Properties;
import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryDammy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntityEnderChest;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * TileEntity として扱うための Repository のラッパー
 */
public abstract class TileEntityAtoChest extends TileEntityEnderChest implements IInventory {

    /**
     * 実体
     */
    protected Repository repo;

    public Repository getRepository() {
        if (repo == null) {
            return new RepositoryDammy(0);
        } else {
            return repo;
        }
    }

    protected abstract Repository createRepository(int color, int grade, boolean isOriginal);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        byte color = nbt.getByte("Color");
        byte grade = nbt.getByte("Grade");
        boolean isOriginal = nbt.getBoolean("Original");
        setColorAndGrade(color, grade, isOriginal);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttc) {
        super.writeToNBT(nbttc);
        nbttc.setByte("Color", (byte) (getRepository().getColor() & 0xF));
        nbttc.setByte("Grade", (byte) (getRepository().getGrade() & 0xF));
        nbttc.setBoolean("Original", getRepository().isOriginal());
    }

    @Override
    public Packet getDescriptionPacket() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(data);
        try {
            out.writeInt(xCoord);
            out.writeInt(yCoord);
            out.writeInt(zCoord);
            out.writeByte((byte) getRepository().getColor());
            out.writeByte((byte) getRepository().getGrade());
            out.writeBoolean(getRepository().isOriginal());
            return new Packet250CustomPayload(Properties.CHANNEL_TILEENTITY, data.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 色及び、クラスの結びつけ
     * ブロックが置かれた時やロード時、サーバーからクライアントへのパケットなどで呼ばれる
     */
    public void setColorAndGrade(int color, int grade, boolean isOriginal) {
        repo = createRepository(color, grade, isOriginal);
    }

    public int getColor() {
        return getRepository().getColor();
    }

    /* IInventory を実装するため、そのまま Repository に移譲 */

    @Override
    public int getSizeInventory() {
        return getRepository().getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return getRepository().getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        return getRepository().decrStackSize(var1, var2);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return getRepository().getStackInSlotOnClosing(var1);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        getRepository().setInventorySlotContents(var1, var2);
    }

    @Override
    public String getInvName() {
        return getRepository().getInvName();
    }

    @Override
    public int getInventoryStackLimit() {
        return getRepository().getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        if (worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this) {
            return player.getDistanceSq(xCoord + 0.5D, yCoord + 0.5D, zCoord + 0.5D) <= 64;
        } else {
            return false;
        }
    }

    @Override
    public void openChest() {
        getRepository().openChest();
        ++numUsingPlayers;
    }

    @Override
    public void closeChest() {
        getRepository().closeChest();
        --numUsingPlayers;
    }

    @Override
    public boolean isInvNameLocalized() {
        return true;
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return true;
    }
}
