package com.corescode.stunslam.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.util.ActionResult;

public class StunSlamClient implements ClientModInitializer {

	private static final int STUN_SLAM_WINDOW = 5;

	private Entity shieldStunnedTarget;
	private int stunTicksRemaining = 0;

	// Our own client tick counter
	private long clientTicks = 0;
	private long shieldStunTime = 0;

	@Override
	public void onInitializeClient() {

		StunSlamHud.initialize();

		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {

			if (!world.isClient()) {
				return ActionResult.PASS;
			}

			// ---------------------------------------------------------
			// AXE → SHIELD STUN
			// ---------------------------------------------------------

			if (player.getMainHandStack().isIn(ItemTags.AXES)
					&& entity instanceof LivingEntity target
					&& target.isBlocking()) {

				shieldStunnedTarget = target;
				stunTicksRemaining = STUN_SLAM_WINDOW;
				shieldStunTime = clientTicks;

				return ActionResult.PASS;
			}

			// ---------------------------------------------------------
			// MACE → TARGET
			// ---------------------------------------------------------

			if (player.getMainHandStack().isOf(Items.MACE)
					&& entity == shieldStunnedTarget
					&& stunTicksRemaining > 0
					&& player.getVelocity().y < 0
					&& player.fallDistance > 0.5f) {

				long elapsedTicks = clientTicks - shieldStunTime;

				System.out.println(
						"[Stun Slam] STUN SLAM DETECTED! Target: "
								+ entity.getName().getString()
								+ " | Timing: "
								+ elapsedTicks
								+ " ticks"
				);

				StunSlamHud.trigger();

				shieldStunnedTarget = null;
				stunTicksRemaining = 0;

				return ActionResult.PASS;
			}

			return ActionResult.PASS;
		});

		// -------------------------------------------------------------
		// CLIENT TICK
		// -------------------------------------------------------------

		ClientTickEvents.END_CLIENT_TICK.register(client -> {

			clientTicks++;

			if (stunTicksRemaining > 0) {
				stunTicksRemaining--;

				if (stunTicksRemaining <= 0) {
					shieldStunnedTarget = null;
				}
			}
		});
	}
}