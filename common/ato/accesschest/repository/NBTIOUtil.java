package ato.accesschest.repository;

import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
        NBTTagCompound nbt = CompressedStreamTools.readCompressed(in);
        in.close();
        return nbt;
    }

    /**
     * 指定した色の Access Chest の NBT データを保存する
     */
    public void saveAccessChestNBT(int color, NBTTagCompound nbt) throws IOException {
        File f = getNBTFile(color);
        FileOutputStream out = new FileOutputStream(f);
        CompressedStreamTools.writeCompressed(nbt, out);
        out.close();
    }

    /**
     * 指定した色の Access Chest の NBT ファイルを返す
     */
    private File getNBTFile(int color) {
        MinecraftServer server = MinecraftServer.getServer();
        if (server == null) return new File("");
        File dir = new File(server.worldServerForDimension(0).getChunkSaveLocation(), "AccessChest");
        if (!dir.exists()) {
            dir.mkdir();
        }
        return new File(dir, "AC" + color + ".dat");
    }
}
