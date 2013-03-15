package ato.accesschest.game;

import ato.accesschest.repository.Repository;
import ato.accesschest.repository.RepositoryCompressedChest;

public class TileEntityCompressedChest extends TileEntityAtoChest {

    @Override
    protected Repository createRepository(int color, int grade) {
        return new RepositoryCompressedChest(grade);
    }
}
