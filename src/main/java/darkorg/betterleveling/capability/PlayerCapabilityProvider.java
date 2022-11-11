package darkorg.betterleveling.capability;

import darkorg.betterleveling.api.IPlayerCapability;
import darkorg.betterleveling.impl.PlayerCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    public static Capability<IPlayerCapability> PLAYER_CAP = CapabilityManager.get(new CapabilityToken<>() {
    });

    private IPlayerCapability instance;
    private final LazyOptional<IPlayerCapability> optional = LazyOptional.of(this::getCapability);

    private IPlayerCapability getCapability() {
        return this.instance == null ? this.instance = new PlayerCapability() : this.instance;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return PLAYER_CAP.orEmpty(cap, optional);
    }

    @Override
    public CompoundTag serializeNBT() {
        return getCapability().getNBTData();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getCapability().setNBTData(nbt);
    }
}
