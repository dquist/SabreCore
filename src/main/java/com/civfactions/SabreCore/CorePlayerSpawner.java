package com.civfactions.SabreCore;

import java.util.HashMap;
import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.World.Environment;

import com.civfactions.SabreApi.PlayerSpawner;
import com.civfactions.SabreApi.SabreApi;
import com.civfactions.SabreApi.SabrePlayer;
import com.civfactions.SabreApi.data.Documentable;
import com.civfactions.SabreApi.data.SabreDocument;
import com.civfactions.SabreApi.util.Guard;
import com.civfactions.SabreCore.util.PlayerSpawnResult;

public class CorePlayerSpawner implements PlayerSpawner, Documentable {
	
	private final SabreApi sabre;
	
	// Track the last spawn location for each player
	private HashMap<SabrePlayer, PlayerSpawnResult> lastRandomSpawn;
	
	// Blocks that should not be spawned on
	private HashSet<Material> nospawnBlocks;
	
	private String respawnWorld;
	private int respawnRadius;

	/**
	 * Creates a new PlayerSpawner instance
	 */
	public CorePlayerSpawner(final SabreApi sabre) {
		Guard.ArgumentNotNull(sabre, "sabre");
		
		this.sabre = sabre;
		
		lastRandomSpawn = new HashMap<SabrePlayer, PlayerSpawnResult>();
		
		nospawnBlocks = new HashSet<Material>();
		nospawnBlocks.add(Material.WATER);
		nospawnBlocks.add(Material.STATIONARY_WATER);
		nospawnBlocks.add(Material.LAVA);
		nospawnBlocks.add(Material.STATIONARY_LAVA);
		nospawnBlocks.add(Material.LEAVES);
		nospawnBlocks.add(Material.LEAVES_2);
		nospawnBlocks.add(Material.FIRE);
		nospawnBlocks.add(Material.CACTUS);
		nospawnBlocks.add(Material.VINE);
		nospawnBlocks.add(Material.ACACIA_FENCE_GATE);
		nospawnBlocks.add(Material.BIRCH_FENCE_GATE);
		nospawnBlocks.add(Material.DARK_OAK_FENCE_GATE);
		nospawnBlocks.add(Material.FENCE_GATE);
		nospawnBlocks.add(Material.JUNGLE_FENCE_GATE);
		nospawnBlocks.add(Material.SPRUCE_FENCE_GATE);
	}
	
	
	/**
	 * Bed spawns a player in the default world if their bed exists, otherwise random spawns them
	 * @param p The player to spawn
	 */
	public Location spawnPlayerBed(final SabrePlayer p, final World world) {
		Location l = p.getBedLocation();
		boolean useBed = false;
		
		if (l != null) {
			if (l.getBlock().getType() == Material.BED_BLOCK) {
				if (sabre.getBlockPermission(l).canPlayerAccess(p)) {
					useBed = true;
				}
			}
		}
		
		if (useBed) {
			p.setBedLocation(l);
		} else {
			if (l != null) {
				p.msg(Lang.playerBedMissing);
			}
			
			p.setBedLocation(null);
			l = spawnPlayerRandom(p, world);
		}

		lastRandomSpawn.put(p, new PlayerSpawnResult(l, useBed));
		return l;
	}
	
	
	/**
	 * Bed spawns a player in the default world if their bed exists, otherwise random spawns them
	 * @param p The player to spawn
	 */
	public Location spawnPlayerBed(final SabrePlayer p) {
		return spawnPlayerBed(p, Bukkit.getServer().getWorld(respawnWorld));
	}
	
	
	/**
	 * Random spawns a player in the given world
	 * @param p The player to spawn
	 * @param world The world to use
	 */
	public Location spawnPlayerRandom(final SabrePlayer p, final World world) {
		Location spawnLocation = chooseSpawn(world, respawnRadius);
		spawnLocation = p.teleportToGround(spawnLocation);
		lastRandomSpawn.put(p, new PlayerSpawnResult(spawnLocation, false));
		p.msg(Lang.playerYouWakeUp);
		return spawnLocation;
	}
	
	
	/**
	 * Random spawns a player in the default world
	 * @param p The player to spawn
	 */
	public Location spawnPlayerRandom(final SabrePlayer sp) {
		return spawnPlayerRandom(sp, Bukkit.getServer().getWorld(respawnWorld));
	}

	
	/**
	 * Chooses a random spawn location in the given world
	 * @param world The world to spawn in
	 * @param distance The max radius
	 * @return The chosen spawn location
	 */
	private Location chooseSpawn(World world, int distance) {		
		double xmin = -distance;
		double xmax = distance;
		double zmin = -distance;
		double zmax = distance;				
		double xrand = 0;
		double zrand = 0;
		double xcenter = xmin + (xmax - xmin)/2;
		double zcenter = zmin + (zmax - zmin)/2;
		double y = -1;
		
		while (y == -1) {
			double r = Math.random() * (xmax - xcenter);
			double phi = Math.random() * 2 * Math.PI;

			xrand = xcenter + Math.cos(phi) * r;
			zrand = zcenter + Math.sin(phi) * r;

			y = getValidHighestY(world, xrand, zrand);
		}
	
		return new Location(world, xrand, y, zrand);
	}

	
	/**
	 * Get's the highest valid block to spawn on at a x,z coordinate
	 * @param world The world
	 * @param x The X location
	 * @param z The Z location
	 * @return The highest valid Y value or -1 if none found
	 */
	private double getValidHighestY(World world, double x, double z) {
		
		world.getChunkAt(new Location(world, x, 0, z)).load();

		double y = 0;
		Material blockType = Material.AIR;

		if(world.getEnvironment().equals(Environment.NETHER)) {
			Material blockYType = world.getBlockAt((int) x, (int) y, (int) z).getType();
			Material blockY2Type = world.getBlockAt((int) x, (int) (y+1), (int) z).getType();
			while(y < 128 && !(blockYType == Material.AIR && blockY2Type == Material.AIR)) {				
				y++;
				blockYType = blockY2Type;
				blockY2Type = world.getBlockAt((int) x, (int) (y+1), (int) z).getType();
			}
			if(y == 127) {
				return -1;
			}
		} else {
			y = 257;
			while(y >= 0 && blockType == Material.AIR) {
				y--;
				blockType = world.getBlockAt((int) x, (int) y, (int) z).getType();
			}
			if(y == 0) {
				return -1;
			}
		}

		if (!nospawnBlocks.contains(blockType)){
			return y;
		}		

		return -1;
	}
	
	/**
	 * Gets the last spawn location for a given player
	 * @param sp The player
	 * @return The last spawn location
	 */
	public PlayerSpawnResult getLastSpawnLocation(final SabrePlayer sp) {
		return lastRandomSpawn.get(sp);
	}


	@Override
	public String getDocumentKey() {
		return "general";
	}


	@Override
	public SabreDocument getDocument() {
		return new SabreDocument()
				.append("spawn_world", respawnWorld)
				.append("spawn_radius", respawnRadius);
	}


	@Override
	public Documentable loadDocument(SabreDocument doc) {
		respawnWorld = doc.getString("spawn_world", "world");
		respawnRadius = doc.getInteger("spawn_radius", 3000);
		
		return this;
	}
}
