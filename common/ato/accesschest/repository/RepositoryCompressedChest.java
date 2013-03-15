package ato.accesschest.repository;

/**
 * Compressed Chest の実体
 */
public class RepositoryCompressedChest extends Repository {

    public RepositoryCompressedChest(int grade) {
        super(new DataManagerArray());
    }

    @Override
    public String getInvName() {
        return "Compressed Chest";
    }
}
