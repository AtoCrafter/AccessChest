package ato.accesschest.repository;

/**
 * Access Chest の実体
 */
public class RepositoryAccessChest extends Repository {

    private int color;

    public RepositoryAccessChest(int color) {
        super(new DataManagerNBT(color));
        this.color = color;
    }

    @Override
    public String getInvName() {
        String str = "Access Chest";
//        String name = AccessChestManager.name.getChestName(ownerName, colorNumber);
//        if ( !name.equals("") ) str += name;
        return str;
    }

    @Override
    public void onInventoryChanged() {
//
    }
}
