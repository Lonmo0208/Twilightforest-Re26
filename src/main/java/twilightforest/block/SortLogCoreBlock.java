package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import twilightforest.config.TFConfig;
import twilightforest.init.TFParticleType;
import twilightforest.network.ParticlePacket;
import twilightforest.tags.TFEntityTypeTags;
import twilightforest.util.BlockCapabilityDirectionalCache;
import twilightforest.util.WorldUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SortLogCoreBlock extends SpecialMagicLogBlock {

	private final BlockCapabilityDirectionalCache<Object> capabilityCache = new BlockCapabilityDirectionalCache<>();

	public SortLogCoreBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean doesCoreFunction() {
		return !TFConfig.disableSortingCore;
	}

	//TODO: Rewrite with Fabric Transfer API
	@Override
	void performTreeEffect(ServerLevel level, BlockPos pos, RandomSource rand) {
		// This method requires NeoForge capability system (IItemHandler).
		// Needs to be rewritten using Fabric Transfer API (net.fabricmc.fabric.api.transfer.v1).
	}
}
