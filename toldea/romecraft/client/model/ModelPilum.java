package toldea.romecraft.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

public class ModelPilum extends ModelBase {
	ModelRenderer SpearBase;
	ModelRenderer SpearPoint1;
	ModelRenderer SpearPoint2;
	ModelRenderer Handle;
	ModelRenderer HandleGuard;

	public ModelPilum() {
		textureWidth = 24;
		textureHeight = 46;

		SpearBase = new ModelRenderer(this, 8, 0);
		SpearBase.addBox(0F, 0F, 0F, 1, 20, 1);
		SpearBase.setRotationPoint(0.5F, 0F, 0.5F);
		SpearBase.setTextureSize(textureWidth, textureHeight);

		SpearPoint1 = new ModelRenderer(this, 12, 0);
		SpearPoint1.addBox(0F, 0F, 0F, 3, 5, 2);
		SpearPoint1.setRotationPoint(-0.5F, 1.5F, 0F);
		SpearPoint1.setTextureSize(textureWidth, textureHeight);

		SpearPoint2 = new ModelRenderer(this, 12, 7);
		SpearPoint2.addBox(0F, 0F, 0F, 5, 3, 1);
		SpearPoint2.setRotationPoint(-1.5F, 2.5F, 0.5F);
		SpearPoint2.setTextureSize(textureWidth, textureHeight);

		Handle = new ModelRenderer(this, 0, 0);
		Handle.addBox(0F, 0F, 0F, 2, 44, 2);
		Handle.setRotationPoint(0F, 20F, 0F);
		Handle.setTextureSize(textureWidth, textureHeight);

		HandleGuard = new ModelRenderer(this, 12, 11);
		HandleGuard.addBox(0F, 0F, 0F, 3, 6, 3);
		HandleGuard.setRotationPoint(-0.5F, 21F, -0.5F);
		HandleGuard.setTextureSize(textureWidth, textureHeight);
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		SpearBase.render(f5);
		
		SpearPoint1.render(f5);
		SpearPoint2.render(f5);
		
		Handle.render(f5);
		
		HandleGuard.render(f5);
	}
}
