package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryAccessChest;

public class TileEntityAccessChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade, boolean isOriginal) {
        return new RepositoryAccessChest(color, grade, isOriginal);
    }
}
