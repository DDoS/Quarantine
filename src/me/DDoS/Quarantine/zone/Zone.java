package me.DDoS.Quarantine.zone;

import me.DDoS.Quarantine.zone.subzone.SubZone;
import me.DDoS.Quarantine.zone.region.MainRegion;
import me.DDoS.Quarantine.leaderboard.Leaderboard;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.DDoS.Quarantine.zone.reward.Reward;
import me.DDoS.Quarantine.util.QUtil;
import me.DDoS.Quarantine.player.ZonePlayer;
import me.DDoS.Quarantine.Quarantine;
import me.DDoS.Quarantine.player.LobbyPlayer;
import me.DDoS.Quarantine.player.QPlayer;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author DDoS
 */
public class Zone {

    private final Quarantine plugin;
    private final MainRegion region;
    private final String zoneName;
    private Location lobby;
    private Location entrance;
    private final long interval;
    private final int defaultMoney;
    private final int maxNumOfPlayers;
    private int mobCheckTaskID = -1;
    private final boolean clearDrops;
    private final boolean oneTimeKeys;
    private final List<ItemStack> kit;
    private final List<SubZone> subZones;
    private final Map<EntityType, Reward> mobRewards;
    private final Leaderboard leaderboard;
    //
    private final Map<String, QPlayer> players = new HashMap<String, QPlayer>();
    private final Map<String, Integer> deadPlayerXP = new HashMap<String, Integer>();

    public Zone(Quarantine plugin, MainRegion region, String zoneName, Location lobby, Location entrance,
            int defaultMoney, int maxNumOfPlayers, boolean clearDrops, boolean oneTimeKeys,
            List<SubZone> subZones, List<ItemStack> kit, Map<EntityType, Reward> mobRewards, World world, long interval) {

        this.plugin = plugin;
        this.region = region;
        this.zoneName = zoneName;
        this.lobby = lobby;
        this.entrance = entrance;
        this.interval = interval;
        this.defaultMoney = defaultMoney;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.clearDrops = clearDrops;
        this.oneTimeKeys = oneTimeKeys;
        this.kit = kit;
        this.subZones = subZones;
        this.mobRewards = mobRewards;

        if (Leaderboard.USE) {

            leaderboard = new Leaderboard(plugin, zoneName);

        } else {

            leaderboard = null;

        }
    }

    public String getName() {

        return zoneName;

    }

    public void disconnectLB() {

        if (leaderboard == null) {

            return;

        }

        leaderboard.disconnect();

    }

    public Leaderboard getLB() {

        return leaderboard;

    }

    public boolean hasPlayer(String playerName) {

        return players.containsKey(playerName);

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

    public int getDefaultMoney() {

        return defaultMoney;

    }

    public List<ItemStack> getKit() {

        return kit;

    }

    public Collection<QPlayer> getPlayers() {

        return players.values();

    }

    public int getNumOfPlayers() {

        return players.size();

    }

    public int getMaxNumOfPlayers() {

        return maxNumOfPlayers;

    }

    public boolean tellMoney(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).tellMoney();
        return true;

    }

    public boolean tellKeys(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).tellKeys();
        return true;

    }

    public boolean tellRank(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).tellRank();
        return true;

    }

    public boolean tellScore(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).tellScore();
        return true;

    }

    public boolean tellTopFive(Player player, int page) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).tellTopFive(page);
        return true;

    }

    public boolean passPlayerTeleportEvent(PlayerTeleportEvent event) {

        if (!players.containsKey(event.getPlayer().getName())) {

            return false;

        }

        if (players.get(event.getPlayer().getName()).teleportLeave(event)) {

            players.remove(event.getPlayer().getName());

            if (players.isEmpty()) {

                removeAllMobs();

            }
        }

        return true;

    }

    public boolean passCreatureSpawnEvent(CreatureSpawnEvent event) {

        if (isInZone(event.getLocation())) {

            if (!event.getSpawnReason().equals(SpawnReason.CUSTOM)) {

                event.setCancelled(true);

            }

            return true;

        }

        return false;

    }

    public boolean passEntityDeathEvent(LivingEntity entity, EntityDeathEvent event) {

        for (SubZone subZone : subZones) {

            if (subZone.removeAndSpawnNewEntity(entity)) {

                if (clearDrops) {

                    event.getDrops().clear();

                }

                Player player = getKiller(event.getEntity());

                if (player != null) {

                    if (!players.containsKey(player.getName())) {

                        return true;

                    }

                    QPlayer qPlayer = players.get(player.getName());

                    if (!qPlayer.isZonePlayer()) {

                        return true;

                    }

                    ZonePlayer qzPlayer = (ZonePlayer) qPlayer;
                    Reward reward = mobRewards.get(entity.getType());

                    if (reward != null) {

                        qzPlayer.addMoney(reward.getRandomMoneyAmount());
                        qzPlayer.addScore(reward.getScoreReward());

                    }
                }

                return true;

            }
        }

        return false;

    }

    public boolean passPlayerDeathEvent(Player player, EntityDeathEvent event) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).dieLeave(event);
        players.remove(player.getName());

        if (players.isEmpty()) {

            removeAllMobs();

        }

        return true;

    }

    public boolean passPlayerQuitEvent(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        players.get(player.getName()).quitLeave();
        players.remove(player.getName());

        if (players.isEmpty()) {

            removeAllMobs();

        }

        return true;

    }

    public boolean passPlayerRespawnEvent(PlayerRespawnEvent event, Quarantine plugin) {

        if (!deadPlayerXP.containsKey(event.getPlayer().getName())) {

            return false;

        }

        Player player = event.getPlayer();
        event.setRespawnLocation(lobby);
        QUtil.tell(player, "You lost.");
        QUtil.tell(player, "You may leave the lobby by teleporting away.");
        player.giveExp(deadPlayerXP.get(player.getName()));
        deadPlayerXP.remove(player.getName());

        return true;

    }

    public boolean passEntityCombustEvent(EntityCombustEvent event) {

        if (event.getEntity() instanceof LivingEntity) {

            LivingEntity ent = (LivingEntity) event.getEntity();

            for (SubZone subZone : subZones) {

                if (subZone.containsMob(ent)) {

                    event.setCancelled(true);
                    return true;

                }
            }
        }

        return false;

    }

    public boolean passChunkUnloadEvent(ChunkUnloadEvent event) {

        if (!isInZone(event.getChunk())) {

            return false;

        }

        if (!players.isEmpty()) {

            event.setCancelled(true);

        }

        return true;

    }

    public boolean passPlayerInteractEvent(PlayerInteractEvent event) {

        if (!players.containsKey(event.getPlayer().getName())) {

            return false;

        }

        if (!event.hasBlock()) {

            return true;

        }

        QPlayer qPlayer = players.get(event.getPlayer().getName());

        if (!qPlayer.isZonePlayer()) {

            return true;

        }

        ZonePlayer qzPlayer = (ZonePlayer) qPlayer;

        if (!checkForSign(event.getClickedBlock())) {

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK
                    || event.getAction() == Action.LEFT_CLICK_BLOCK) {

                if (event.getClickedBlock().getType() != Material.STONE_BUTTON) {

                    return true;

                }

                if (!handleLock(qzPlayer, event.getClickedBlock())) {

                    event.setCancelled(true);

                }
            }

            return true;

        }

        Sign sign = (Sign) event.getClickedBlock().getState();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {

            return true;

        }

        if (sign.getLine(0).equalsIgnoreCase("[Quarantine]")) {

            handleZoneSign(qzPlayer, sign);

        }

        return true;

    }

    private boolean handleLock(ZonePlayer player, Block block) {

        Sign sign = getSignNextTo(block);

        if (sign == null) {

            return true;

        }

        if (sign.getLine(0).equalsIgnoreCase("[Quarantine]") && sign.getLine(1).equalsIgnoreCase("Key Lock")) {

            if (!player.useKey(sign.getLine(2), oneTimeKeys)) {

                QUtil.tell(player.getPlayer(), "You need to purchase the key '" + sign.getLine(2) + "' to open this door.");
                return false;

            }
        }

        return true;

    }

    private void handleZoneSign(ZonePlayer player, Sign sign) {

        String line1 = sign.getLine(1);

        if (line1.equalsIgnoreCase("Buy Item")) {

            String[] sa = sign.getLine(2).split("-");

            ItemStack item = QUtil.toItemStack(sa[0], Integer.parseInt(sa[1]));

            if (item != null) {

                player.buyItem(item, Integer.parseInt(sa[2]));

            } else {

                QUtil.tell(player.getPlayer(), "Invalid sign or ID");

            }

            return;

        }

        if (line1.equalsIgnoreCase("Buy Random Item")) {

            Sign sign2 = getSignNextTo(sign.getBlock());

            if (sign2 == null) {

                return;

            }

            String[] splits = sign.getLine(2).split("-");
            List<ItemStack> items = QUtil.parseItemList(sign2.getLines(), Integer.parseInt(splits[0]));

            player.buyItem(items.get(new Random().nextInt(items.size())), Integer.parseInt(splits[1]));

            return;

        }

        if (line1.equalsIgnoreCase("Sell Item")) {

            String[] sa = sign.getLine(2).split("-");
            ItemStack item = QUtil.toItemStack(sa[0], Integer.parseInt(sa[1]));

            if (item != null) {

                player.sellItem(item, Integer.parseInt(sa[2]));

            }

            return;

        }

        if (line1.equalsIgnoreCase("Buy Key")) {

            player.addKey(sign.getLine(2), Integer.parseInt(sign.getLine(3)));
            return;

        }

        if (line1.equalsIgnoreCase("Enchantment")) {

            String[] sa = sign.getLine(2).split("-");
            player.addEnchantment(Integer.parseInt(sa[0]), Integer.parseInt(sa[1]), Integer.parseInt(sa[2]));

        }
    }

    private QPlayer getPlayer(Player player) {

        if (!players.containsKey(player.getName())) {

            return new LobbyPlayer(player, this);

        }

        return players.get(player.getName());

    }

    public void joinPlayer(Player player) {

        if (players.size() >= maxNumOfPlayers) {

            QUtil.tell(player, "The zone is full.");
            return;

        }

        QPlayer qPlayer = getPlayer(player);

        if (qPlayer.join()) {

            players.put(player.getName(), qPlayer);
            spawnStartingMobs();

        }
    }

    public boolean enterPlayer(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        QPlayer qPlayer = players.get(player.getName());

        qPlayer.enter();

        if (!qPlayer.isZonePlayer()) {

            ZonePlayer qzPlayer = new ZonePlayer(qPlayer);
            players.put(player.getName(), qzPlayer);

        }

        return true;

    }

    public boolean leavePlayer(Player player) {

        if (!players.containsKey(player.getName())) {

            return false;

        }

        if (players.get(player.getName()).commandLeave()) {

            players.remove(player.getName());

            if (players.isEmpty()) {

                removeAllMobs();

            }
        }

        return true;

    }

    public void removeAllPlayers() {

        for (QPlayer player : players.values()) {

            player.forceLeave();

        }

        removeAllMobs();
        players.clear();

    }

    public void saveLocations(FileConfiguration config) {

        ConfigurationSection configSec1 = config.getConfigurationSection("Zones." + zoneName);

        configSec1.set("lobby.x", lobby.getX());
        configSec1.set("lobby.y", lobby.getY());
        configSec1.set("lobby.z", lobby.getZ());
        configSec1.set("lobby.yaw", lobby.getYaw());
        configSec1.set("lobby.pitch", lobby.getPitch());

        configSec1.set("entrance.x", entrance.getX());
        configSec1.set("entrance.y", entrance.getY());
        configSec1.set("entrance.z", entrance.getZ());
        configSec1.set("entrance.yaw", entrance.getYaw());
        configSec1.set("entrance.pitch", entrance.getPitch());

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

        if (mobCheckTaskID != -1) {

            return;

        }

        mobCheckTaskID = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable() {

            @Override
            public void run() {

                for (SubZone subZone : subZones) {

                    subZone.checkForDeadMobs();

                }

                Quarantine.log.info("[Quarantine] Finished checking mobs.");

            }
        }, interval, interval);

        Quarantine.log.info("[Quarantine] Started mob check task for zone: " + zoneName);

    }

    private void stopMobCheckTask() {

        plugin.getServer().getScheduler().cancelTask(mobCheckTaskID);
        mobCheckTaskID = -1;
        Quarantine.log.info("[Quarantine] Stopped mob check task for zone: " + zoneName);

    }

    private Sign getSignNextTo(Block block) {

        if (checkForSign(block.getRelative(BlockFace.UP))) {

            return (Sign) block.getRelative(BlockFace.UP).getState();

        }

        if (checkForSign(block.getRelative(BlockFace.DOWN))) {

            return (Sign) block.getRelative(BlockFace.DOWN).getState();

        }

        if (checkForSign(block.getRelative(BlockFace.EAST))) {

            return (Sign) block.getRelative(BlockFace.EAST).getState();

        }

        if (checkForSign(block.getRelative(BlockFace.WEST))) {

            return (Sign) block.getRelative(BlockFace.WEST).getState();

        }

        if (checkForSign(block.getRelative(BlockFace.NORTH))) {

            return (Sign) block.getRelative(BlockFace.NORTH).getState();

        }

        if (checkForSign(block.getRelative(BlockFace.SOUTH))) {

            return (Sign) block.getRelative(BlockFace.SOUTH).getState();

        }

        return null;

    }

    private boolean checkForSign(Block block) {

        switch (block.getType()) {

            case WALL_SIGN:
                return true;

            case SIGN_POST:
                return true;

            default:
                return false;

        }
    }

    private Player getKiller(Entity ent) {

        Player player;

        EntityDamageEvent e1 = ent.getLastDamageCause();
        EntityDamageByEntityEvent e2 = (e1 instanceof EntityDamageByEntityEvent) ? (EntityDamageByEntityEvent) e1 : null;
        Entity damager = (e2 != null) ? e2.getDamager() : null;
        player = (e2 != null && damager instanceof Player) ? (Player) damager : null;

        if (player == null) {

            LivingEntity shooter = (e2 != null && damager instanceof Projectile) ? ((Projectile) damager).getShooter() : null;
            player = (shooter != null && shooter instanceof Player) ? (Player) shooter : null;

        }

        return player;

    }

    public void registerDeadPlayer(String playerName, int amount) {

        deadPlayerXP.put(playerName, amount);

    }
}
