package ato.accesschest.game;

import ato.accesschest.AccessChest;
import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryAccessChest;
import net.minecraft.world.World;

public class TileEntityAccessChest extends TileEntityAtoChest {

    private int color;
    private int grade;
    private boolean isOriginal;

    @Override
    protected Repository createRepository(int color, int grade, boolean isOriginal) {
        if (worldObj != null) {
            return new RepositoryAccessChest(worldObj, color, grade, isOriginal);
        } else {
            return null;
        }
    }

    @Override
    public void setColorAndGrade(int color, int grade, boolean isOriginal) {
        // ワールドロード時は readFromNBT 後に setWorldObj が呼ばれるので、色、クラス、オリジナル情報を保存しておく
        this.color = color;
        this.grade = grade;
        this.isOriginal = isOriginal;
        super.setColorAndGrade(color, grade, isOriginal);
    }

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);
        // ワールドに固有のレポジトリデータをひもづける
        super.setColorAndGrade(color, grade, isOriginal);
    }

    @Override
    public int getColor() {
        // TileEntityAtoChestRenderer 用
        return color;
    }

    @Override
    public void openChest() {
        super.openChest();
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessChest.config.blockIDAC, 1, numUsingPlayers);
    }

    @Override
    public void closeChest() {
        super.closeChest();
        worldObj.addBlockEvent(xCoord, yCoord, zCoord, AccessChest.config.blockIDAC, 1, numUsingPlayers);
    }
}
