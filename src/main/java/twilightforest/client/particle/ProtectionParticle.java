package twilightforest.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class ProtectionParticle extends SingleQuadParticle {

	public ProtectionParticle(ClientLevel level, double x, double y, double z, double velX, double velY, double velZ, TextureAtlasSprite sprite) {
		super(level, x, y, z, sprite);
		this.xd = velX;
		this.yd = velY;
		this.zd = velZ;
		this.quadSize = 1.0F;
		this.lifetime = 30 + this.random.nextInt(20);
		this.hasPhysics = false;
		this.rCol = 0.0F;
		this.gCol = 1.0F;
		this.bCol = 0.0F;
		this.alpha = 1.0F;
	}

	@Override
	protected Layer getLayer() {
		return Layer.TRANSLUCENT;
	}

	@Override
	public int getLightCoords(float partialTicks) {
		return 0xF000F0;
	}

	@Override
	public void tick() {
		this.xo = this.x;
		this.yo = this.y;
		this.zo = this.z;
		if (this.age++ >= this.lifetime) {
			this.remove();
		} else {
			float f = (float) this.age / (float) this.lifetime;
			this.alpha = f > 0.7F ? 1.0F - (f - 0.7F) / 0.3F : 1.0F;
			this.quadSize = 1.0F - f * 0.5F;
			this.move(this.xd, this.yd, this.zd);
		}
	}

	public record Factory(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {

		@Override
		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double vx, double vy, double vz, RandomSource random) {
			ProtectionParticle particle = new ProtectionParticle(level, x, y, z, vx, vy, vz, this.sprite.get(random));
			return particle;
		}
	}
}
