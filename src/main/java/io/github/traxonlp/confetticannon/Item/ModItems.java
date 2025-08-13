package io.github.traxonlp.confetticannon.Item;

import net.fabricmc.fabric.api.client.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.client.data.BlockStateModelGenerator;
import net.minecraft.client.data.ItemModelGenerator;
import net.minecraft.client.data.Models;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import io.github.traxonlp.confetticannon.confetticannon;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ModItems {

	public static final Item CONFETTI_CANNON = registerItem("confetti_cannon");

	private static Item registerItem(String name) {
		Identifier identifier =  Identifier.of(confetticannon.ID, name);
		RegistryKey<Item> key = RegistryKey.of(RegistryKeys.ITEM, identifier);

		Item.Settings settings =
			new Item.Settings().registryKey(key)
				.rarity(Rarity.EPIC);
		if (name.equals("confetti_cannon")) {
			return Registry.register(Registries.ITEM, key, new ConfettiCannonItem(settings));
		}
		return Registry.register(Registries.ITEM, key, new Item(settings));
	}


	private static void itemGroupIngedients(FabricItemGroupEntries entries) {
		entries.add(CONFETTI_CANNON);
	}

	public static void registerModItems() {
		confetticannon.LOGGER.info("Registering Mod Items for " + confetticannon.ID);

		ItemGroupEvents.modifyEntriesEvent
			(ItemGroups.TOOLS).register(ModItems::itemGroupIngedients);
	}
	
}
