package ato.accesschest.repository;

/**
 * 実態のないリポジトリ
 * Gui 画面を開いた時にデータを持たないクライアント側で使う
 */
public class RepositoryDammy extends Repository {

    public RepositoryDammy() {
        super(new DataManagerArray());
    }

    @Override
    public String getInvName() {
        return "dammy";
    }

    @Override
    public void onInventoryChanged() {
 //
    }
}