package ato.accesschest.game;

import ato.accesschest.Properties;
import ato.accesschest.repository.Repository;
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
            repo = createRepository(0, 0);
        }
        return repo;
    }

    protected abstract Repository createRepository(int color, int grade);

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        byte color = nbt.getByte("Color");
        byte grade = nbt.getByte("Grade");
        setColorAndGrade(color, grade);
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttc) {
        super.writeToNBT(nbttc);
        nbttc.setByte("Color", (byte) (repo.getColor() & 0xF));
        nbttc.setByte("Grade", (byte) (repo.getGrade() & 0xF));
    }

    @Override
    public Packet getDescriptionPacket() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(data);
        try {
            out.writeInt(xCoord);
            out.writeInt(yCoord);
            out.writeInt(zCoord);
            out.writeByte((byte) repo.getColor());
            out.writeByte((byte) repo.getGrade());
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
    public void setColorAndGrade(int color, int grade) {
        repo = createRepository(color, grade);
    }

    /* IInventory を実装するため、そのまま Repository に移譲 */

    @Override
    public int getSizeInventory() {
        return repo.getSizeInventory();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return repo.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int var1, int var2) {
        return repo.decrStackSize(var1, var2);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return repo.getStackInSlotOnClosing(var1);
    }

    @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        repo.setInventorySlotContents(var1, var2);
    }

    @Override
    public String getInvName() {
        return repo.getInvName();
    }

    @Override
    public int getInventoryStackLimit() {
        return repo.getInventoryStackLimit();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return repo.isUseableByPlayer(var1);
    }

    @Override
    public void openChest() {
        repo.openChest();
        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord), 1, numUsingPlayers);
    }

    @Override
    public void closeChest() {
        repo.closeChest();
        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord), 1, numUsingPlayers);
    }
}
