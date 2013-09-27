package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

public class ModelLoricaSegmentata extends ModelBiped {
	ModelRenderer loricasegmentata;

	public ModelLoricaSegmentata() {
		textureWidth = 64;
		textureHeight = 32;

		loricasegmentata = new ModelRenderer(this, 0, 0);
		loricasegmentata.addBox(-5F, -1F, -3F, 10, 14, 6);
		loricasegmentata.setRotationPoint(0F, 0F, 0F);
		loricasegmentata.setTextureSize(64, 32);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		this.isSneak = entity.isSneaking();
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		loricasegmentata.render(f5);
	}

	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity par7Entity) {
		if (this.isSneak) {
			loricasegmentata.rotateAngleX = 0.5F;
		} else {
			loricasegmentata.rotateAngleX = 0.0F;
		}
	}
}
