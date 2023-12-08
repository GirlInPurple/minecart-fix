package xyz.minecartfix.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.minecartfix.MinecartFixInit.blacklistedPlayers;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartMixin extends Entity {

	public AbstractMinecartMixin(EntityType<?> type, World world) {super(type, world);}

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		Entity target = this.getFirstPassenger();

		if (target == null) {return;}
		if (blacklistedPlayers.contains(target)) {return;}
		if (this.getVelocity().length() == 0) {return;}

		target.setYaw(90 + this.getYaw());
		target.setPitch(this.getPitch());
	}
}