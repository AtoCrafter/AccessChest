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
import net.minecraft.tileentity.TileEntity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * TileEntity として扱うための Repository のラッパー
 */
public abstract class TileEntityAtoChest extends TileEntity implements IInventory {
    // TileEntityEnderChest を継承しないのは、難読化後の AbstractMethodError バグ対策
    // TileEntityEnderChest は IInventory を継承していないのにもかかわらず、openChest() などの
    // IInventory と同じメソッド名を持っているため、TileEntityAtoChest の一部メソッドが、
    // TileEntityEnderChest のメソッドのオーバーライドとして難読化されており、
    // IInventory のメソッドの難読化後のメソッド名と異なっており、バグが発生する

    private static final Random rand = new Random();
    /**
     * 現在のチェストの蓋の角度（0 から 1）
     */
    public float lidAngle;
    /**
     * 前 tick の蓋の角度
     */
    public float prevLidAngle;
    /**
     * 実体
     */
    protected Repository repo;
    /**
     * チェストを開いているプレイヤーの数
     */
    protected int numUsingPlayers;

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
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    /**
     * @see net.minecraft.tileentity.TileEntityEnderChest#updateEntity()
     */
    @Override
    public void updateEntity() {
        super.updateEntity();

        syncNumPlayers();

        this.prevLidAngle = this.lidAngle;
        float var1 = 0.1F;
        double var4;

        if (this.numUsingPlayers > 0 && this.lidAngle == 0.0F) {
            double var2 = (double) this.xCoord + 0.5D;
            var4 = (double) this.zCoord + 0.5D;
            this.worldObj.playSoundEffect(var2, (double) this.yCoord + 0.5D, var4, "random.chestopen", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
        }

        if (this.numUsingPlayers == 0 && this.lidAngle > 0.0F || this.numUsingPlayers > 0 && this.lidAngle < 1.0F) {
            float var8 = this.lidAngle;

            if (this.numUsingPlayers > 0) {
                this.lidAngle += var1;
            } else {
                this.lidAngle -= var1;
            }

            if (this.lidAngle > 1.0F) {
                this.lidAngle = 1.0F;
            }

            float var3 = 0.5F;

            if (this.lidAngle < var3 && var8 >= var3) {
                var4 = (double) this.xCoord + 0.5D;
                double var6 = (double) this.zCoord + 0.5D;
                this.worldObj.playSoundEffect(var4, (double) this.yCoord + 0.5D, var6, "random.chestclosed", 0.5F, this.worldObj.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (this.lidAngle < 0.0F) {
                this.lidAngle = 0.0F;
            }
        }
    }

    /**
     * @see net.minecraft.tileentity.TileEntityEnderChest#receiveClientEvent(int, int)
     */
    @Override
    public boolean receiveClientEvent(int par1, int par2) {
        if (par1 == 1) {
            this.numUsingPlayers = par2;
            return true;
        } else {
            return super.receiveClientEvent(par1, par2);
        }
    }

    /**
     * 定期的に numUsingPlayers を同期
     */
    private void syncNumPlayers() {
        if (rand.nextInt(20) == 0) {
            this.worldObj.addBlockEvent(this.xCoord, this.yCoord, this.zCoord,
                    worldObj.getBlockId(xCoord, yCoord, zCoord), 1, this.numUsingPlayers);
        }
    }

    /**
     * @see net.minecraft.tileentity.TileEntityEnderChest#invalidate()
     */
    @Override
    public void invalidate() {
        this.updateContainingBlockInfo();
        super.invalidate();
    }
}
