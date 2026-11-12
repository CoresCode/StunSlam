package com.corescode.stunslam;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StunSlam {

	public static final String MOD_ID = "stunslam";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private StunSlam() {
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
