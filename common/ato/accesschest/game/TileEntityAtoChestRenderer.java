package ato.accesschest.game;

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

    private ModelChest model = new ModelChest();
    /**
     * インベントリ内でブロックを描画するためのダミーのタイルエンティティ
     */
    private static final TileEntityAtoChest tileEntityAtoChestDammy = new TileEntityAccessChest();

    public void renderAtoChest(int direction, float lidAngle, float prevLidAngle, double par2, double par4, double par6, float par8) {

        this.bindTextureByName("/ato/accesschest/AccessChestBlock.png");
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(TileEntity tileEntityRaw, double par2, double par4, double par6, float par8) {
        TileEntityAtoChest tileEntity = (TileEntityAtoChest) tileEntityRaw;
        int direction = 0;
        if (tileEntity.func_70309_m()) {
            direction = tileEntity.getBlockMetadata();
        }
        renderAtoChest(direction, tileEntity.lidAngle, tileEntity.prevLidAngle, par2, par4, par6, par8);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {
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
