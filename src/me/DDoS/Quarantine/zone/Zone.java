package me.DDoS.Quarantine.zone;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import java.io.IOException;

import java.util.Set;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.event.entity.PlayerDeathEvent;

import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.player.ZonePlayer;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.LobbyPlayer;
import me.DDoS.Quarantine.player.QPlayer;
import me.DDoS.Quarantine.zone.subzone.SubZone;
import me.DDoS.Quarantine.zone.region.Region;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import me.DDoS.Quarantine.player.PlayerType;
import me.DDoS.Quarantine.player.inventory.Kit;

/**
 *
 * @author DDoS
 */
public class Zone {

    private final Quarantine plugin;
    private final Region region;
    private final List<SubZone> subZones;
    private final ZoneProperties properties;
    private Location lobby;
    private Location entrance;
    private final Map<String, Kit> kits;
    private final Map<EntityType, Reward> mobRewards;
    private final Leaderboard leaderboard;
    //
    private final Map<String, QPlayer> players = new HashMap<String, QPlayer>();

    public Zone(Quarantine plugin, Region region, List<SubZone> subZones, ZoneProperties properties,
            Location lobby, Location entrance, Map<String, Kit> kits, Map<EntityType, Reward> mobRewards) {

        this.plugin = plugin;
        this.region = region;
        this.properties = properties;
        this.lobby = lobby;
        this.entrance = entrance;
        this.kits = kits;
        this.subZones = subZones;
        this.mobRewards = mobRewards;

        if (Leaderboard.ENABLED) {

            leaderboard = new Leaderboard(plugin, properties.getZoneName());

        } else {

            leaderboard = null;

        }
    }

    public Quarantine getPlugin() {
        
        return plugin;
        
    }
    
    public ZoneProperties getProperties() {

        return properties;

    }

    public void disconnectLeaderboards() {

        if (leaderboard == null) {

            return;

        }

        leaderboard.disconnect();

    }

    public Leaderboard getLeaderboard() {

        return leaderboard;

    }

    public boolean hasPlayer(String playerName) {

        return players.containsKey(playerName);

    }

    public boolean hasMob(LivingEntity living) {

        for (SubZone subzone : subZones) {

            if (subzone.hasMob(living)) {

                return true;

            }
        }

        return false;

    }

    public Location getLobby() {

        return lobby;

    }

    public Location getEntrance() {

        return entrance;

    }

    public void setLobby(Location lobby) {

        this.lobby = lobby;

    }

    public boolean setEntrance(Location entrance) {

        if (!isInZone(entrance)) {

            return false;

        }

        this.entrance = entrance;
        return true;

    }

    public Kit getKit(String name) {

        return kits.get(name);

    }

    public Collection<Kit> getKits() {

        return kits.values();

    }

    public Set<String> getKitNames() {

        return kits.keySet();

    }

    public Collection<QPlayer> getPlayers() {

        return players.values();

    }

    public int getNumberOfPlayers() {

        return players.size();

    }

    public void giveKit(Player player, String kitName) {

        QPlayer qPlayer = players.get(player.getName());

        if (!(qPlayer instanceof LobbyPlayer)) {

            QUtil.tell(player, "You can only request kits from the lobby, "
                    + "if you don't have any saved inventory.");
            return;

        }

        giveKit((LobbyPlayer) qPlayer, kitName);

    }

    private void giveKit(LobbyPlayer lPlayer, String kitName) {

        if (!kits.containsKey(kitName)) {

            QUtil.tell(lPlayer.getPlayer(), "Could not find the requested kit.");
            return;

        }

        if (!plugin.getPermissions().hasPermission(lPlayer.getPlayer(), "quarantine.kit."
                + properties.getZoneName() + "." + kitName)) {

            QUtil.tell(lPlayer.getPlayer(), "You don't have permission for this kit.");

        } else {

            lPlayer.giveKit(kits.get(kitName));

        }
    }

    public void handlePlayerTeleport(PlayerTeleportEvent event) {

        if (players.get(event.getPlayer().getName()).teleportLeave(event)) {

            players.remove(event.getPlayer().getName());

            if (players.isEmpty()) {

                removeAllMobs();

            }
        }
    }

    public void handleEntityDeath(EntityDeathEvent event) {

        LivingEntity entity = event.getEntity();

        for (SubZone subZone : subZones) {

            if (subZone.hasMob(entity)) {

                subZone.refreshEntity(entity);

                if (properties.clearDrops()) {

                    event.getDrops().clear();

                }

                if (properties.clearXP()) {

                    event.setDroppedExp(0);

                }

                Player player = getKiller(event.getEntity());

                if (player != null) {

                    if (!players.containsKey(player.getName())) {

                        return;

                    }

                    QPlayer qPlayer = players.get(player.getName());

                    if (qPlayer.getType() != PlayerType.ZONE_PLAYER) {

                        return;

                    }

                    ZonePlayer qzPlayer = (ZonePlayer) qPlayer;
                    Reward reward = mobRewards.get(entity.getType());

                    if (reward != null) {

                        qzPlayer.giveMoneyForKill(reward.getRandomMoneyAmount());
                        qzPlayer.addScore(reward.getScoreReward());

                    }
                }

                return;

            }
        }
    }

    public void handlePlayerDeath(PlayerDeathEvent event) {

        String playerName = event.getEntity().getName();
        players.get(playerName).dieLeave(event);
        players.remove(playerName);

        if (players.isEmpty()) {

            removeAllMobs();

        }
    }

    public void handlePlayerQuit(Player player) {

        players.get(player.getName()).quitLeave();
        players.remove(player.getName());

        if (players.isEmpty()) {

            removeAllMobs();

        }
    }

    public void handleChunkUnload(ChunkUnloadEvent event) {

        if (!players.isEmpty()) {

            event.setCancelled(true);

        }
    }

    public void handlePlayerInteractButton(PlayerInteractEvent event, Player player) {

        QPlayer qPlayer = players.get(player.getName());

        if (qPlayer.getType() != PlayerType.ZONE_PLAYER) {

            return;

        }

        ZonePlayer qzPlayer = (ZonePlayer) qPlayer;

        if (!handleLock(qzPlayer, event.getClickedBlock())) {

            event.setCancelled(true);

        }
    }

    public void handlePlayerInteractSign(PlayerInteractEvent event, Sign sign) {

        QPlayer qPlayer = players.get(event.getPlayer().getName());
        PlayerType type = qPlayer.getType();

        if (type == PlayerType.ZONE_PLAYER) {

            event.setCancelled(handleZoneSign((ZonePlayer) qPlayer, sign));

        } else if (type == PlayerType.LOBBY_PLAYER) {

            event.setCancelled(handleLobbySign((LobbyPlayer) qPlayer, sign));

        }
    }

    private boolean handleLock(ZonePlayer player, Block block) {

        Sign sign = getSignNextTo(block);

        if (sign == null) {

            return true;

        }

        if (sign.getLine(0).equalsIgnoreCase("[Quarantine]") && sign.getLine(1).equalsIgnoreCase("Key Lock")) {

            if (!player.useKey(sign.getLine(2), properties.oneTimeUseKeys())) {

                QUtil.tell(player.getPlayer(), "You need to purchase the key '" + sign.getLine(2) + "' to open this door.");
                return false;

            }
        }

        return true;

    }

    private boolean handleLobbySign(LobbyPlayer lPlayer, Sign sign) {

        if (sign.getLine(1).equalsIgnoreCase("Get Kit")) {

            giveKit(lPlayer, sign.getLine(2));
            return true;

        }

        return false;
    }

    private boolean handleZoneSign(ZonePlayer player, Sign sign) {

        String line = sign.getLine(1);

        if (line.equalsIgnoreCase("Buy Item")) {

            String[] sa = sign.getLine(2).split("-");

            ItemStack item = QUtil.toItemStack(sa[0], Integer.parseInt(sa[1]));

            if (item != null) {

                player.buyItem(item, Integer.parseInt(sa[2]));

            } else {

                QUtil.tell(player.getPlayer(), "Invalid sign or ID.");

            }

        } else if (line.equalsIgnoreCase("Buy Random Item")) {

            Sign sign2 = getSignNextTo(sign.getBlock());

            if (sign2 == null) {

                return true;

            }

            String[] splits = sign.getLine(2).split("-");
            List<ItemStack> items = QUtil.parseItemList(sign2.getLines(), Integer.parseInt(splits[0]));

            player.buyItem(items.get(new Random().nextInt(items.size())), Integer.parseInt(splits[1]));

        } else if (line.equalsIgnoreCase("Sell Item")) {

            String[] sa = sign.getLine(2).split("-");
            ItemStack item = QUtil.toItemStack(sa[0], Integer.parseInt(sa[1]));

            if (item != null) {

                player.sellItem(item, Integer.parseInt(sa[2]));

            }

        } else if (line.equalsIgnoreCase("Buy Key")) {

            player.addKey(sign.getLine(2), Integer.parseInt(sign.getLine(3)));
            return true;

        } else if (line.equalsIgnoreCase("Enchantment")) {

            String[] sa = sign.getLine(2).split("-");
            player.addEnchantment(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]), Integer.parseInt(sa[2]));

        } else if (line.equalsIgnoreCase("Buy Kit")) {

            String kitName = sign.getLine(2);

            if (!kits.containsKey(kitName)) {

                QUtil.tell(player.getPlayer(), "Invalid kit name.");

            } else {

                player.buyKit(kits.get(kitName), Integer.parseInt(sign.getLine(3)));

            }

            return true;

        } else {

            return false;

        }

        return true;

    }

    public QPlayer getPlayer(String playerName) {

        return players.get(playerName);

    }

    private QPlayer getAndCreatePlayer(Player player) {

        if (!players.containsKey(player.getName())) {

            return new LobbyPlayer(player, this);

        }

        return players.get(player.getName());

    }

    public void joinPlayer(Player player) {

        if (players.size() >= properties.getMaxNumberOfPlayers()) {

            QUtil.tell(player, "The zone is full.");
            return;

        }

        QPlayer qPlayer = getAndCreatePlayer(player);

        if (qPlayer.join()) {

            players.put(player.getName(), qPlayer);
            spawnStartingMobs();

        }
    }

    public void enterPlayer(Player player) {

        QPlayer qPlayer = players.get(player.getName());

        if (!qPlayer.enter()) {

            return;

        }

        ZonePlayer qzPlayer = new ZonePlayer(qPlayer);
        players.put(player.getName(), qzPlayer);

    }

    public void leavePlayer(Player player) {

        if (players.get(player.getName()).commandLeave()) {

            players.remove(player.getName());

            if (players.isEmpty()) {

                removeAllMobs();

            }
        }
    }

    public void removeAllPlayers() {

        for (QPlayer player : players.values()) {

            player.forceLeave();

        }

        removeAllMobs();
        players.clear();

    }

    public void saveLocations(FileConfiguration config) {

        ConfigurationSection configSec1 = config.getConfigurationSection("Zones." + properties.getZoneName());

        if (lobby != null) {

            configSec1.set("lobby.x", lobby.getX());
            configSec1.set("lobby.y", lobby.getY());
            configSec1.set("lobby.z", lobby.getZ());
            configSec1.set("lobby.yaw", lobby.getYaw());
            configSec1.set("lobby.pitch", lobby.getPitch());

        }

        if (entrance != null) {

            configSec1.set("entrance.x", entrance.getX());
            configSec1.set("entrance.y", entrance.getY());
            configSec1.set("entrance.z", entrance.getZ());
            configSec1.set("entrance.yaw", entrance.getYaw());
            configSec1.set("entrance.pitch", entrance.getPitch());

        }

        try {

            config.save("plugins/Quarantine/config.yml");

        } catch (IOException ex) {

            Quarantine.log.info("[Quarantine] Couldn't save config.");
            Quarantine.log.info("[Quarantine] Error message: " + ex.getMessage());

        }
    }

    public void reloadMobs() {

        for (SubZone subzone : subZones) {

            subzone.removeAllMobs();
            subzone.spawnStartingMobs();

        }
    }

    public boolean isInZone(Location location) {

        return region.containsLocation(location);

    }

    public boolean isInZone(Chunk chunk) {

        return region.containsChunk(chunk);

    }

    private void spawnStartingMobs() {

        startMobCheckTask();

        for (SubZone subZone : subZones) {

            if (!subZone.hasMobs()) {

                subZone.spawnStartingMobs();

            }
        }
    }

    private void removeAllMobs() {

        stopMobCheckTask();

        for (SubZone subZone : subZones) {

            if (subZone.hasMobs()) {

                subZone.removeAllMobs();

            }
        }
    }

    private void startMobCheckTask() {

        if (properties.getMobCheckTaskID() != -1) {

            return;

        }

        long interval = properties.getMobCheckTaskInterval();

        properties.setMobCheckTaskID(plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                for (SubZone subZone : subZones) {

                    subZone.checkForDeadMobs();

                }

                Quarantine.log.info("[Quarantine] Finished checking mobs.");

            }
        }, interval, interval));

        Quarantine.log.info("[Quarantine] Started mob check task for zone: " + properties.getZoneName());

    }

    private void stopMobCheckTask() {

        plugin.getServer().getScheduler().cancelTask(properties.getMobCheckTaskID());
        properties.setMobCheckTaskID(-1);
        Quarantine.log.info("[Quarantine] Stopped mob check task for zone: " + properties.getZoneName());

    }

    private Sign getSignNextTo(Block block) {

        if (QUtil.checkForSign(block.getRelative(BlockFace.UP))) {

            return (Sign) block.getRelative(BlockFace.UP).getState();

        }

        if (QUtil.checkForSign(block.getRelative(BlockFace.DOWN))) {

            return (Sign) block.getRelative(BlockFace.DOWN).getState();

        }

        if (QUtil.checkForSign(block.getRelative(BlockFace.EAST))) {

            return (Sign) block.getRelative(BlockFace.EAST).getState();

        }

        if (QUtil.checkForSign(block.getRelative(BlockFace.WEST))) {

            return (Sign) block.getRelative(BlockFace.WEST).getState();

        }

        if (QUtil.checkForSign(block.getRelative(BlockFace.NORTH))) {

            return (Sign) block.getRelative(BlockFace.NORTH).getState();

        }

        if (QUtil.checkForSign(block.getRelative(BlockFace.SOUTH))) {

            return (Sign) block.getRelative(BlockFace.SOUTH).getState();

        }

        return null;

    }

    private Player getKiller(LivingEntity entity) {

        EntityDamageEvent ede = entity.getLastDamageCause();
        DamageCause cause = ede.getCause();

        if (cause == DamageCause.FIRE || cause == DamageCause.FIRE_TICK) {

            String zoneName = properties.getZoneName();

            if (!entity.hasMetadata("quarantine_fire_damager")) {

                return null;

            }

            Player player = (Player) entity.getMetadata("quarantine_fire_damager").get(0).value();
            entity.removeMetadata("quarantine_fire_damager", plugin);
            return player;

        } else if (ede instanceof EntityDamageByEntityEvent) {

            Entity damager = ((EntityDamageByEntityEvent) ede).getDamager();

            if (damager instanceof Player) {

                return (Player) damager;

            } else if (damager instanceof Projectile) {

                LivingEntity shooter = ((Projectile) damager).getShooter();

                if (shooter instanceof Player) {

                    return (Player) shooter;

                }

            } else if (damager instanceof Tameable) {

                AnimalTamer owner = ((Tameable) damager).getOwner();

                if (owner != null && owner instanceof Player) {

                    return (Player) owner;

                }
            }
        }

        return null;

    }
}
