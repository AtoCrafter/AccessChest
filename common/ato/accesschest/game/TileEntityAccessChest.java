package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryAccessChest;

public class TileEntityAccessChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade, boolean isOriginal) {
        if (worldObj != null) {
            return new RepositoryAccessChest(worldObj, color, grade, isOriginal);
        } else {
            return null;
        }
    }
}
