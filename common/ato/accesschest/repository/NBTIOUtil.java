package ato.accesschest.repository;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * NBT ファイルの入出力によく使う関数を集めたクラス
 */
public class NBTIOUtil {

    /**
     * 指定した色の Access Chest の NBT データを返す
     */
    public NBTTagCompound getAccessChestNBT(int color) throws IOException {
        FileInputStream in = new FileInputStream(getNBTFile(color));
        return CompressedStreamTools.readCompressed(in);
    }

    /**
     * 指定した色の Access Chest の NBT ファイルを返す
     */
    private File getNBTFile(int color) {
        return new File(MinecraftServer.getServer().worldServerForDimension(0).getChunkSaveLocation(), "AccessChest-Test.dat");
    }
}
