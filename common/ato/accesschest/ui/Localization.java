package ato.accesschest.ui;

import ato.accesschest.AccessChest;
import cpw.mods.fml.common.registry.LanguageRegistry;

import java.io.*;
import java.util.Properties;

/**
 * ゲーム画面に表示される文字列を管理
 */
public class Localization {

    private static final Localization instance = new Localization();

    public static void register() {
        instance.registerLocalizations();
    }

    /**
     * 全ての翻訳を登録
     */
    private void registerLocalizations() {
        registerLocalization("en_US");
        registerLocalization("ja_JP");
    }

    /**
     * 指定した言語の翻訳を登録
     */
    private void registerLocalization(String lang) {
        try {
            LanguageRegistry.instance().addStringLocalization(loadProperty(
                    AccessChest.class.getResourceAsStream(lang + ".properties")), lang);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * プロパティファイルの読み込み
     */
    private Properties loadProperty(InputStream is) throws IOException {
        Properties prop = new Properties();
        prop.load(new InputStreamReader(is, "UTF-8"));
        is.close();
        return prop;
    }
}
