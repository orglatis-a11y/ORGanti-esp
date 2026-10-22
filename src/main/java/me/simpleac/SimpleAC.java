package me.simpleac;

import io.papermc.paper.event.packet.PlayerChunkLoadEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAC extends JavaPlugin implements Listener {

    private static final String BYPASS = "simpleac.bypass";
    private static final BlockFace[] FACES = {BlockFace.UP, BlockFace.DOWN, BlockFace.NORTH,
            BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    private final Set<Material> hidden = EnumSet.noneOf(Material.class);
    private final Map<UUID, Map<Long, List<int[]>>> ores = new ConcurrentHashMap<>();
    private final Map<UUID, Set<Long>> faked = new HashMap<>();
    private final Map<UUID, Set<UUID>> hiddenFrom = new HashMap<>(); // viewer -> players hidden from them
    private int reveal, radius, playerReveal;
    private boolean hidePlayers;
    private BlockData stone, deepslate;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reveal = getConfig().getInt("reveal-distance", 5);
        radius = getConfig().getInt("hide-radius", 128);
        hidePlayers = getConfig().getBoolean("hide-players", true);
        playerReveal = getConfig().getInt("player-reveal-distance", 6);
        for (String s : getConfig().getStringList("hidden-blocks")) {
