package ato.accesschest.game;

import ato.accesschest.AccessChest;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockAccess;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * この MOD のチェストをレンダリングするためのクラス
 *
 * @see net.minecraft.client.renderer.tileentity.TileEntityEnderChestRenderer
 */
@SideOnly(Side.CLIENT)
public class TileEntityAtoChestRenderer extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {


    private static final int[] COLOR_CODES = new int[]{
            0x1e1b1b, 0xb3312c, 0x3b511a, 0x51301a, 0x253192, 0x7b2fbe, 0x287697, 0x9a9a9a, 0x434343, 0xd88198,
            0x41cd34, 0xdecf2a, 0x6689d3, 0xc354cd, 0xeb8844, 0xf0f0f0
    };
    /**
     * インベントリ内でブロックを描画するためのダミーのタイルエンティティ
     */
    private static final TileEntityAtoChest tileEntityAtoChestDammy = new TileEntityAccessChest();
    private ModelChest model = new ModelChest();

    public void renderAtoChest(int direction, int colorNum, float lidAngle, float prevLidAngle, double par2, double par4, double par6, float par8) {

        int colorCode = COLOR_CODES[colorNum];
        float r = (float) (colorCode >> 16 & 0xFF) / 255F;
        float g = (float) (colorCode >> 8 & 0xFF) / 255F;
        float b = (float) (colorCode & 0xFF) / 255F;

        this.bindTextureByName("/mods/accesschest/item/atochest.png");
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(r, g, b, 1.0F);
        GL11.glTranslatef((float) par2, (float) par4 + 1.0F, (float) par6 + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        short rotate = -90;
        switch (direction) {
            case 2:
                rotate = 180;
                break;
            case 3:
                rotate = 0;
                break;
            case 4:
                rotate = 90;
                break;
            case 5:
                rotate = -90;
                break;
        }

        GL11.glRotatef((float) rotate, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        float var11 = prevLidAngle + (lidAngle - prevLidAngle) * par8;
        var11 = 1.0F - var11;
        var11 = 1.0F - var11 * var11 * var11;
        this.model.chestLid.rotateAngleX = -(var11 * (float) Math.PI / 2.0F);
        this.model.renderAll();
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntityRaw, double par2, double par4, double par6, float par8) {
        TileEntityAtoChest tileEntity = (TileEntityAtoChest) tileEntityRaw;
        int direction = 0;
        int colorNum = tileEntity.getColor();
        if (tileEntity.func_70309_m()) {
            direction = tileEntity.getBlockMetadata();
        }
        renderAtoChest(direction, colorNum, tileEntity.lidAngle, tileEntity.prevLidAngle, par2, par4, par6, par8);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
        tileEntityAtoChestDammy.setColorAndGrade(AccessChest.id2color(metadata), 0, false);
        TileEntityRenderer.instance.renderTileEntityAt(tileEntityAtoChestDammy, 0.0D, 0.0D, 0.0D, 0.0F);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        return false; // SpecialRenderer の renderTileEntityAt の方でレンダリングするのでこちらではしない
    }

    @Override
    public boolean shouldRender3DInInventory() {
        return true;
    }

    @Override
    public int getRenderId() {
        return 0; // RenderingRegistry#registerBlockHandler の JavaDoc 曰く、このバージョンでは呼ばれないらしいので使わない
    }
}
