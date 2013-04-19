package ato.accesschest.repository;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * Version 2.2.1 までのセーブデータを読み込み現在のセーブデータの形式に変換する
 */
public class OldSavedataConverter {

    /**
     * 変換の実行
     */
    public NBTTagCompound getOldNBT(int color) {
        try {
            return getAccessChestNBT(color);
        } catch (IOException e) {
            e.printStackTrace();
            return new NBTTagCompound();
        }
    }

    /**
     * 古いセーブデータが存在するか
     */
    public boolean doesOldSavedataExists(int color) {
        File oldFile = getOldNBTFile(color);
        return oldFile.exists();
    }

    /**
     * 指定した色の Access Chest の NBT ファイルを返す
     */
    private File getOldNBTFile(int color) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return new File("");
        File dir = new File(server.worldServerForDimension(0).getChunkSaveLocation(), "AccessChest");
        return new File(dir, "AC" + color + ".dat");

    }

    /**
     * 指定した色の Access Chest の NBT データを返す
     */
    private NBTTagCompound getAccessChestNBT(int color) throws IOException {
        FileInputStream in = new FileInputStream(getOldNBTFile(color));
        NBTTagCompound nbt = CompressedStreamTools.readCompressed(in);
        in.close();
        return nbt;
    }
}
