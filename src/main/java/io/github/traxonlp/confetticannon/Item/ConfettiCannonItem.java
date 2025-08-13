package io.github.traxonlp.confetticannon.Item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.DustParticleEffect;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import java.util.List;

public class ConfettiCannonItem extends Item {

	public ConfettiCannonItem(Settings settings) {
		super(settings);
	}

	@Override
	public ActionResult use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);

		if (!world.isClient) {
			ServerWorld serverWorld = (ServerWorld) world;
			Vec3d playerPos = user.getPos();
			Vec3d lookVec = user.getRotationVec(1.0F);

			// Konfetti-Spawn-Position
			double startX = playerPos.x + lookVec.x * 1.0;
			double startY = playerPos.y + user.getEyeHeight(user.getPose()) - 0.1;
			double startZ = playerPos.z + lookVec.z * 1.0;

			// Konfetti-Partikel spawnen
			for (int i = 0; i < 100; i++) {
				double x = startX;
				double y = startY;
				double z = startZ;

				double baseSpeed = 1.8 + world.random.nextDouble() * 0.5;
				double spreadFactor = 0.15;

				double velX = lookVec.x * baseSpeed + (world.random.nextDouble() - 0.5) * spreadFactor;
				double velY = lookVec.y * baseSpeed + (world.random.nextDouble() - 0.5) * spreadFactor + 0.1;
				double velZ = lookVec.z * baseSpeed + (world.random.nextDouble() - 0.5) * spreadFactor;

				int red = world.random.nextInt(256);
				int green = world.random.nextInt(256);
				int blue = world.random.nextInt(256);
				int color = (red << 16) | (green << 8) | blue;

				float size = 2.0f + world.random.nextFloat() * 2.0f;

				DustParticleEffect dustEffect = new DustParticleEffect(color, size);
				serverWorld.spawnParticles(dustEffect, x, y, z, 1, velX, velY, velZ, 0.0);
			}

			// SPIELER-SCHUBS-EFFEKT
			double pushRadius = 5.0; // 5 Block Radius um die Konfetti-Position
			double pushStrength = 1.2; // Stärke des Schubses

			// Suchbereich um die Konfetti-Position definieren
			Box searchBox = new Box(
				startX - pushRadius, startY - pushRadius, startZ - pushRadius,
				startX + pushRadius, startY + pushRadius, startZ + pushRadius
			);

			// Alle Spieler im Bereich finden (außer dem Kanonen-Benutzer)
			List<PlayerEntity> nearbyPlayers = serverWorld.getEntitiesByClass(
				PlayerEntity.class,
				searchBox,
				entity -> entity != user // Nicht den Kanonen-Benutzer selbst
			);

			// Jeden gefundenen Spieler in Blickrichtung schubsen
			for (PlayerEntity targetPlayer : nearbyPlayers) {
				Vec3d pushDirection = lookVec.normalize(); // Normalisierte Blickrichtung

				// Schubs-Velocity berechnen
				double pushX = pushDirection.x * pushStrength;
				double pushY = pushDirection.y * pushStrength + 0.3; // Etwas nach oben
				double pushZ = pushDirection.z * pushStrength;

				// Spieler schubsen
				targetPlayer.addVelocity(pushX, pushY, pushZ);
				targetPlayer.velocityModified = true; // Wichtig für Synchronisation

				// Optional: Sound-Effekt beim geschubsten Spieler
				serverWorld.playSound(null, targetPlayer.getBlockPos(),
					SoundEvents.ENTITY_PLAYER_HURT_ON_FIRE, // Oder anderer Sound
					SoundCategory.PLAYERS, 0.5F, 1.5F);
			}

			// Haupt-Sound-Effekt für die Konfetti-Kanone
			serverWorld.playSound(null, user.getBlockPos(),
				SoundEvents.ENTITY_FIREWORK_ROCKET_BLAST,
				SoundCategory.PLAYERS, 1.0F, 1.0F);

			user.getItemCooldownManager().set(itemStack, 20);
		}

		return ActionResult.SUCCESS;
	}
}
