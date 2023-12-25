package xyz.minecartfix.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecartEntity.class)
public abstract class AbstractMinecartMixin extends Entity {

	public AbstractMinecartMixin(EntityType<?> type, World world) {
		super(type, world);
	}

	/**
	 * Gets the direction the entity is going.
	 * In this case, it's used to find what direction the minecart is going.
	 *
	 * @return a {@link net.minecraft.util.math.Vec3d} of the entity its being used on.
	 * */
	@Unique
	public Vec3d getDirectionVector() {
		float yaw = this.getYaw();
		float pitch = this.getPitch();
		double x = -Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		double y = -Math.sin(Math.toRadians(pitch));
		double z = Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch));
		return new Vec3d(x, y, z);
	}

	/**
	 * The main mixin.
	 * Checks what direction the minecart is going and sets the yaw of the target to that direction
	 * It currently does not work.
	 * */
	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		Entity target = this.getFirstPassenger();
		if (target == null) {return;}
		Vec3d vector = getDirectionVector();
		// I hate floating point with a burning passion.
		double approximatelyZero = 1.2246468;
		if (Math.abs(vector.x) >= approximatelyZero && Math.abs(vector.z) == approximatelyZero) { // east
			target.sendMessage(Text.literal("east | "+vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) >= approximatelyZero && Math.abs(vector.z) >= approximatelyZero) { // north-east
			target.sendMessage(Text.literal("north-east | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) == approximatelyZero && Math.abs(vector.z) >= approximatelyZero) { // north
			target.sendMessage(Text.literal("north | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) <= approximatelyZero && Math.abs(vector.z) >= approximatelyZero) { // north-west
			target.sendMessage(Text.literal("north-west | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) <= approximatelyZero && Math.abs(vector.z) == approximatelyZero) { // west
			target.sendMessage(Text.literal("west | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) <= approximatelyZero && Math.abs(vector.z) <= approximatelyZero) { // south-west
			target.sendMessage(Text.literal("south-west | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) == approximatelyZero && Math.abs(vector.z) <= approximatelyZero) { // south
			target.sendMessage(Text.literal("south | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		} else if (Math.abs(vector.x) >= approximatelyZero && Math.abs(vector.z) <= approximatelyZero) { // south-east
			target.sendMessage(Text.literal("south-east | " + vector+" | approx | "+Math.abs(vector.x) + ", "+Math.abs(vector.z)));
		}
	}

	/**
	 * An example function to set the yaw of the target.
	 * @param target The entity in the minecart.
	 * */
	@Unique
	private void tempSetYaw(Entity target) {
		target.setYaw(this.getYaw());
	}
}