package ato.accesschest;

import net.minecraftforge.common.Configuration;

import java.io.File;

/**
 * コンフィグで設定可能な値の一覧
 */
public class Config {

    /**
     * Access Chest ブロックの ID
     */
    public final int blockIDAC;
    /**
     * Compressed Chest のブロックの ID
     */
    public final int blockIDCC;
    /**
     * スクロール行数
     */
    public final int rowOnScroll;
    /**
     * 一括排出で排出されるアイテムスタックの数の最大値
     */
    public final int ejectStackMaxNum;

    public Config(File file) {
        Configuration config = new Configuration(file);
        config.load();
        blockIDAC = config.getBlock("AccessChest", 3295).getInt();
        blockIDCC = config.getBlock("CompressedChest", 3296).getInt();
        rowOnScroll = config.get("general", "rowsScroll", 8).getInt();
        ejectStackMaxNum = config.get("general", "ejectStackLimit", 96).getInt();
        config.save();
    }
}
