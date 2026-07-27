package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.ColorParticleOption;
import net.minecraft.util.RandomSource;

public class MagicEffectParticle extends SingleQuadParticle {

	protected MagicEffectParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
		super(level, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
	}

	@Override
	public SingleQuadParticle.Layer getLayer() {
		return SingleQuadParticle.Layer.TRANSLUCENT;
	}

	@Override
	public int getLightCoords(float partialTick) {
		return 0xF000F0;
	}

	public static class Factory implements ParticleProvider<ColorParticleOption> {
		private final SpriteSet sprite;

		public Factory(SpriteSet sprite) {
			this.sprite = sprite;
		}

		@Override
		public Particle createParticle(ColorParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
			MagicEffectParticle particle = new MagicEffectParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, this.sprite.get(random));
			particle.setColor(type.getRed(), type.getGreen(), type.getBlue());
			particle.setAlpha(type.getAlpha());
			return particle;
		}
	}
}
