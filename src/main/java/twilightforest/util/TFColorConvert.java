package twilightforest.util;

import org.joml.Vector3f;
import org.joml.Vector4f;

public final class TFColorConvert {
	private TFColorConvert() {
	}

	/**
	 * Converts an ARGB color integer (e.g. {@code 0xFFDBE4EC}) into an RGB vector
	 * with each channel normalized to 0..1, discarding the alpha byte.
	 */
	public static Vector3f rgb(int argb) {
		return new Vector3f(
			((argb >> 16) & 0xFF) / 255.0F,
			((argb >> 8) & 0xFF) / 255.0F,
			(argb & 0xFF) / 255.0F
		);
	}

	/**
	 * Converts an ARGB color integer (e.g. {@code 0xFFDBE4EC}) into an RGBA vector
	 * with each channel normalized to 0..1.
	 */
	public static Vector4f rgba(int argb) {
		return new Vector4f(
			((argb >> 16) & 0xFF) / 255.0F,
			((argb >> 8) & 0xFF) / 255.0F,
			(argb & 0xFF) / 255.0F,
			((argb >> 24) & 0xFF) / 255.0F
		);
	}
}