package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryAccessChest;
import net.minecraft.world.World;

public class TileEntityAccessChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade, boolean isOriginal) {
        if (worldObj != null) {
            return new RepositoryAccessChest(worldObj, color, grade, isOriginal);
        } else {
            return null;
        }
    }

    @Override
    public void setWorldObj(World world) {
        super.setWorldObj(world);
        setColorAndGrade(color, grade, isOriginal);
    }
}
