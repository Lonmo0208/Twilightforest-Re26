package twilightforest.client.state.entity;

import net.minecraft.client.renderer.entity.state.WolfRenderState;

public class HostileWolfRenderState extends WolfRenderState {
	/** Health percentage 0.0 (dead) to 1.0 (full) */
	public float healthPercent;
	/** Brightness for mist wolf rendering */
	public float brightness;
}
