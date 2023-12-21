package xyz.minecartfix.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static xyz.minecartfix.MinecartFixInit.blacklistedPlayers;

import java.text.DecimalFormat;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartMixin extends Entity {

	public AbstractMinecartMixin(EntityType<?> type, World world, double lastXVelocity, double lastZVelocity, double remaingingTurnRadius) {super(type, world);
		this.lastXVelocity = lastXVelocity;
		this.lastZVelocity = lastZVelocity;
		this.remaingingTurnRadius = remaingingTurnRadius;
	}

	@Unique
	private double lastXVelocity;
	@Unique
	private double lastZVelocity;
	@Unique
	private double remaingingTurnRadius;

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		Entity target = this.getFirstPassenger();

		if (target == null) {return;}
		if (blacklistedPlayers.contains(target)) {return;}
		if (this.getVelocity().length() == 0) {return;}

		if (this.getVelocity().x == 0 && this.getVelocity().z > 0) {        // North
			smoothTurn(target, 0);
		} else if (this.getVelocity().x == 0 && this.getVelocity().z < 0) { // South
			smoothTurn(target, 179);
		} else if (this.getVelocity().x > 0 && this.getVelocity().z == 0) { // East
			smoothTurn(target, -90);
		} else if (this.getVelocity().x < 0 && this.getVelocity().z == 0) { // West
			smoothTurn(target, 90);
		}
		/*
		if (this.lastXVelocity > 0 && this.lastZVelocity > 0) { 		// North East
			smoothTurn(target,-135);
		} else if (this.lastXVelocity < 0 && this.lastZVelocity > 0) { 	// North West
			smoothTurn(target,135);
		} else if (this.lastXVelocity < 0 && this.lastZVelocity < 0) { 	// South West
			smoothTurn(target,45);
		} else if (this.lastXVelocity > 0 && this.lastZVelocity < 0) {  // South East
			smoothTurn(target, -45);
		}

		if (this.getVelocity().z != 0) {this.lastXVelocity = this.getVelocity().x;}
		if (this.getVelocity().x != 0) {this.lastZVelocity = this.getVelocity().z;}
		*/
	}

	@Unique
	private void smoothTurn(Entity target, float targetAngle) {
		target.setYaw(targetAngle);
		DecimalFormat decimalFormat = new DecimalFormat("#.##");
		target.sendMessage(Text.literal("xVel = " + decimalFormat.format(this.getVelocity().x) + " | zVel = " + decimalFormat.format(this.getVelocity().z)+" | this.yaw = "+Math.round(this.getYaw())));
	}
}