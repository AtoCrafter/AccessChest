package ato.accesschest.initializer;

import ato.accesschest.ui.Localization;

/**
 * 初期化処理
 * クライアントサイドのみ実行される
 */
public class ProxyClient extends ProxyCommon {

    @Override
    public void init() {
        super.init();
        registerLocalization();
    }

    /**
     * 言語ファイルの登録
     */
    private void registerLocalization() {
        Localization.register();
    }
}
