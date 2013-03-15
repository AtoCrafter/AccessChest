package ato.accesschest.repository;

/**
 * Access Chest の実体
 */
public class RepositoryAccessChest extends Repository {

    public RepositoryAccessChest(int color, int grade) {
        super(NBTPool.instance.getNBT(color), color, grade);
    }

    @Override
    public String getInvName() {
        String str = "Access Chest";
//        String name = AccessChestManager.name.getChestName(ownerName, colorNumber);
//        if ( !name.equals("") ) str += name;
        return str;
    }
}
