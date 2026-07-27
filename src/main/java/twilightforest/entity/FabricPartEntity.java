package twilightforest.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Stub replacement for NeoForge's Entity.
 * In Fabric, multipart entities are handled differently.
 */
public abstract class FabricPartEntity<T extends Entity> extends Entity {
    private final T parent;

    public FabricPartEntity(EntityType<?> type, Level level, T parent) {
        super(type, level);
        this.parent = parent;
    }

    public T getParent() {
        return parent;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
    }
}