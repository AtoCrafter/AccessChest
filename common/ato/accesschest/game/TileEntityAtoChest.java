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
import net.minecraft.world.World;

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
    private int color;
    private int grade;
    private boolean isOriginal;

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
        color = nbt.getByte("Color");
        grade = nbt.getByte("Grade");
        isOriginal = nbt.getBoolean("Original");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbttc) {
        super.writeToNBT(nbttc);
        nbttc.setByte("Color", (byte) (color & 0xF));
        nbttc.setByte("Grade", (byte) (grade & 0xF));
        nbttc.setBoolean("Original", isOriginal);
    }

    @Override
    public Packet getDescriptionPacket() {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(data);
        try {
            out.writeInt(xCoord);
            out.writeInt(yCoord);
            out.writeInt(zCoord);
            out.writeByte((byte) color);
            out.writeByte((byte) grade);
            out.writeBoolean(isOriginal);
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
        this.color = color;
        this.grade = grade;
        this.isOriginal = isOriginal;
        repo = createRepository(color, grade, isOriginal);
    }

    @Override
    public void setWorldObj(World par1World) {
        super.setWorldObj(par1World);
        setColorAndGrade(color, grade, isOriginal);
    }

    public int getColor() {
        return color;
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
    public boolean isUseableByPlayer(EntityPlayer var1) {
        return getRepository().isUseableByPlayer(var1);
    }

    @Override
    public void openChest() {
        getRepository().openChest();
        ++numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord), 1, numUsingPlayers);
    }

    @Override
    public void closeChest() {
        getRepository().closeChest();
        --numUsingPlayers;
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, worldObj.getBlockId(xCoord, yCoord, zCoord), 1, numUsingPlayers);
    }
}
