package ato.accesschest.game;

import ato.accesschest.repository.RepositoryAccessChest;

public class TileEntityAccessChest extends TileEntityAtoChest {

    public TileEntityAccessChest(int color, int grade) {
        super(new RepositoryAccessChest(color, grade));
    }
}
