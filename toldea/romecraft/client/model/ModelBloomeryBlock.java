package toldea.romecraft.client.model;

import javax.swing.text.html.parser.Entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelBloomeryBlock extends ModelBase {
	private ModelRenderer BloomeryBlock;
	
	public ModelBloomeryBlock() {
		textureWidth = 56;
		textureHeight = 28;

		BloomeryBlock = new ModelRenderer(this, 0, 0);
		BloomeryBlock.addBox(-7F, -7f, -7f, 14, 14, 14);
		BloomeryBlock.setRotationPoint(0F, 0F, 0F);
		BloomeryBlock.setTextureSize(textureWidth, textureHeight);
	}
	
	public void renderBloomery(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		BloomeryBlock.render(f5);
	}
}
