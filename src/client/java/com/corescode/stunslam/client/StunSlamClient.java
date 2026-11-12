package com.corescode.stunslam.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionResult;

public class StunSlamClient implements ClientModInitializer {

	private static final int STUN_SLAM_WINDOW = 5;

	private Entity shieldStunnedTarget;

	private int stunTicksRemaining = 0;

	private long clientTicks = 0;

	private long shieldStunTime = 0;

	@Override
	public void onInitializeClient() {

		// Load Stun Slam configuration
		StunSlamConfig.load();

		// Initialize HUD
		StunSlamHud.initialize();

		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {

			if (!world.isClientSide()) {
				return InteractionResult.PASS;
			}

			if (player.getMainHandItem().is(ItemTags.AXES)
					&& entity instanceof LivingEntity target
					&& target.isBlocking()) {

				shieldStunnedTarget = target;
				stunTicksRemaining = STUN_SLAM_WINDOW;
				shieldStunTime = clientTicks;

				System.out.println(
						"[Stun Slam] SHIELD STUN DETECTED! Target: "
								+ target.getName().getString()
				);

				return InteractionResult.PASS;
			}

			if (player.getMainHandItem().is(Items.MACE)
					&& entity == shieldStunnedTarget
					&& stunTicksRemaining > 0
					&& player.getDeltaMovement().y < 0
					&& player.fallDistance > 0.5f) {

				long elapsedTicks =
						clientTicks - shieldStunTime;

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

				return InteractionResult.PASS;
			}

			return InteractionResult.PASS;
		});

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