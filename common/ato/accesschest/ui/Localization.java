package ato.accesschest.ui;

import ato.accesschest.AccessChest;
import cpw.mods.fml.common.registry.LanguageRegistry;

import java.io.*;
import java.util.Properties;

/**
 * ゲーム画面に表示される文字列を管理
 */
public class Localization {

    public static void register() {
        (new Localization()).registerLocalizations();
    }

    /**
     * 全ての翻訳を登録
     */
    private void registerLocalizations() {
        for (File f : getLocalizationFiles()) {
            try {
                String lang = f.getName().substring(0, f.getName().lastIndexOf("."));
                LanguageRegistry.instance().addStringLocalization(loadProperty(f), lang);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 全翻訳ファイルのリストを取得
     */
    private File[] getLocalizationFiles() {
        return (new File(AccessChest.class.getResource("lang").getPath())).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".properties");
            }
        });
    }

    /**
     * プロパティファイルの読み込み
     *
     * @param file プロパティファイル
     */
    private Properties loadProperty(File file) throws IOException {
        Properties prop = new Properties();
        InputStream in = new FileInputStream(file);
        prop.load(in);
        in.close();
        return prop;
    }
}
