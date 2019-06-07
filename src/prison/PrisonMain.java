package prison;

import commands.*;
import listeners.*;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Objective;
import sql.MySQL;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;
import java.util.UUID;

public class PrisonMain extends JavaPlugin {

    private Statement statement;

    private HashMap<UUID, PrisonPlayer> stats;
    private LinkedList<PrisonMine> mines;
    private HashMap<UUID, Objective> scores;

    private String info;
    private String error;
    private String fatal_error;

    private int GLOBAL_TIME = 0;

    private float GLOBAL_MONEY_BOOSTER = 1;
    private int GLOBAL_BLOCK_BOOSTER = 1;
    private int BLOCK_BOOSTER_END = -1;
    private int MONEY_BOOSTER_END = -1;

    private World world;

    @Override
    public void onEnable() {

        registerConfig();
        if (!getConfig().getBoolean("prison")) return;
        if (getConfig().getInt("pin_code") != 4361) return;

        info = ChatColor.translateAlternateColorCodes('&', getConfig().getString("info"));
        error = ChatColor.translateAlternateColorCodes('&', getConfig().getString("error"));
        fatal_error = ChatColor.translateAlternateColorCodes('&', getConfig().getString("fatal_error"));

        MySQL base = new MySQL(getConfig().getString("user"), getConfig().getString("password"), getConfig().getString("host"), getConfig().getString("database"), getConfig().getInt("port"));
        try {
            getLogger().info("[!] Connecting to DataBase.");
            statement = base.openConnection().createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `PrisonPlayers` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid TEXT, gold FLOAT, level INT, kills INT, deaths INT, blocks TEXT, clan TEXT, enter INT);");
            getLogger().info("[!] Connected to DataBase.");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("[!] Connection exception.");
            Bukkit.broadcastMessage(getFatalPrefix() + "Упс... Произошла фатальная ошибка номер 2, просим как можно быстрее оповестите администраторов.");
        }

        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new HandListener(), this);
        Bukkit.getPluginManager().registerEvents(new InteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new RatKillListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new DamageListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);

        Bukkit.getPluginCommand("mine").setExecutor(new MinesCommand());
        Bukkit.getPluginCommand("upgrade").setExecutor(new UpgradeCommand());
        Bukkit.getPluginCommand("level").setExecutor(new LevelCommand());
        Bukkit.getPluginCommand("gift").setExecutor(new GiftCommand());
        Bukkit.getPluginCommand("deposit").setExecutor(new DepositCommand());
        Bukkit.getPluginCommand("fraction").setExecutor(new FractionCommand());
        Bukkit.getPluginCommand("base").setExecutor(new BaseCommand());
        Bukkit.getPluginCommand("shop").setExecutor(new ShopCommand());

        stats = new HashMap<>();
        mines = new LinkedList<>();
        scores = new HashMap<>();

        world = Bukkit.getWorld("world");
        world.setAutoSave(false);
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule mobGriefing false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobLoot false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doFireTick false");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule doMobSpawning false");

        for (Player p : Bukkit.getOnlinePlayers())
            ConnectionListener.getInstance().loadStats(p);

        for (int i = 0; i < getConfig().getInt("mines_value"); i++) {
            mines.add(new PrisonMine(
                    getConfig().getString("mines." + (i+1) + ".name"),
                    getConfig().getInt("mines." + (i+1) + ".min_level"),
                    getConfig().getBoolean("mines." + (i+1) + ".pvp"),
                    getConfig().getInt("mines." + (i+1) + ".h"),
                    getConfig().getInt("mines." + (i+1) + ".diametr"),
                    getConfig().getInt("mines." + (i+1) + ".x"),
                    getConfig().getInt("mines." + (i+1) + ".y"),
                    getConfig().getInt("mines." + (i+1) + ".z"),
                    getConfig().getInt("mines." + (i+1) + ".time"),
                    getConfig().getString("mines." + (i+1) + ".types"),
                    Material.getMaterial(getConfig().getString("mines." + (i+1) + ".material")),
                    getConfig().getInt("mines." + (i+1) + ".tpx"),
                    getConfig().getInt("mines." + (i+1) + ".tpy"),
                    getConfig().getInt("mines." + (i+1) + ".tpz")));
            mines.get(i).updateMine(mines.get(i));
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                PrisonMain.getInstance().incLOBAL_TIME();
                Random random = new Random();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PrisonScoreboard.getInstance().updateScoreboard(p);
                    if (PrisonMain.getInstance().getGLOBAL_TIME() % 900 == 0) {
                        int effect = random.nextInt(PrisonMain.getInstance().getConfig().getInt("needs.amount")) + 1;
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(PrisonMain.getInstance().getConfig().getString("needs." + effect + ".effect")), 100000, 3));
                        p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + PrisonMain.getInstance().getConfig().getString("needs." + effect + ".message"));
                    }
                }
                if (PrisonMain.getInstance().getGLOBAL_TIME() % 120 == 0) {
                    if (Bukkit.getWorld("world").getEntities().stream().filter(x -> x.getType().equals(EntityType.SILVERFISH)).toArray().length < 30)
                    for (int i = 1; i < getConfig().getInt("rats.amount") + 1; i++)
                        Bukkit.getWorld("world").spawnEntity(new Location(getWorld(), getConfig().getInt("rats." + i + ".x"), getConfig().getInt("rats." + i + ".y"), getConfig().getInt("rats." + i + ".z")), EntityType.SILVERFISH);
                }
                if (PrisonMain.getInstance().getBLOCK_BOOSTER_END() == 0) {
                    PrisonMain.getInstance().setGLOBAL_BLOCK_BOOSTER(1);
                    PrisonMain.getInstance().setBLOCK_BOOSTER_END(-1);
                    Bukkit.broadcastMessage(PrisonMain.getInstance().getInfoPrefix() + "Бустер блоков истек.");
                } else if (PrisonMain.getInstance().getBLOCK_BOOSTER_END() > 0)
                    PrisonMain.getInstance().setBLOCK_BOOSTER_END(PrisonMain.getInstance().getBLOCK_BOOSTER_END() - 1);
                if (PrisonMain.getInstance().getMONEY_BOOSTER_END() == 0) {
                    PrisonMain.getInstance().setGLOBAL_MONEY_BOOSTER(1);
                    PrisonMain.getInstance().setMONEY_BOOSTER_END(-1);
                    Bukkit.broadcastMessage(PrisonMain.getInstance().getInfoPrefix() + "Бустер денег истек.");
                } else if (PrisonMain.getInstance().getMONEY_BOOSTER_END() > 0)
                    PrisonMain.getInstance().setMONEY_BOOSTER_END(PrisonMain.getInstance().getMONEY_BOOSTER_END() - 1);
            }
        }.runTaskTimer(PrisonMain.getInstance(), 10 * 20L, 20L);
    }

    @Override
    public void onDisable() {
        for (Player p : Bukkit.getOnlinePlayers())
            ConnectionListener.getInstance().saveStats(p);
    }
    private void registerConfig ()
    {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
    public static PrisonMain getInstance () {
        return PrisonMain.getPlugin(PrisonMain.class);
    }

    public LinkedList<PrisonMine> getMines() {
        return mines;
    }

    public HashMap<UUID, PrisonPlayer> getStats() {
        return stats;
    }

    public HashMap<UUID, Objective> getScores() {
        return scores;
    }

    public Statement getStatement() {
        return statement;
    }

    public void incLOBAL_TIME() {
        this.GLOBAL_TIME++;
    }

    public int getGLOBAL_TIME() {
        return GLOBAL_TIME;
    }

    public float getGLOBAL_MONEY_BOOSTER() {
        return GLOBAL_MONEY_BOOSTER;
    }
    public void setGLOBAL_MONEY_BOOSTER(float GLOBAL_MONEY_BOOSTER) {
        this.GLOBAL_MONEY_BOOSTER = GLOBAL_MONEY_BOOSTER;
    }
    public int getGLOBAL_BLOCK_BOOSTER() {
        return GLOBAL_BLOCK_BOOSTER;
    }
    public void setGLOBAL_BLOCK_BOOSTER(int GLOBAL_BLOCK_BOOSTER) {
        this.GLOBAL_BLOCK_BOOSTER = GLOBAL_BLOCK_BOOSTER;
    }

    public int getBLOCK_BOOSTER_END() {
        return BLOCK_BOOSTER_END;
    }
    public int getMONEY_BOOSTER_END() {
        return MONEY_BOOSTER_END;
    }
    public void setBLOCK_BOOSTER_END(int BLOCK_BOOSTER_END) {
        this.BLOCK_BOOSTER_END = BLOCK_BOOSTER_END;
    }
    public void setMONEY_BOOSTER_END(int BMONEY_BOOSTER_END) {
        this.MONEY_BOOSTER_END = BMONEY_BOOSTER_END;
    }

    public String getInfoPrefix() {
        return info;
    }
    public String getErrorPrefix() {
        return error;
    }
    public String getFatalPrefix() {
        return fatal_error;
    }

    public String formatTime (int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
    public void deleteOneItem (Player p) {
        if (p.getItemInHand().getAmount() == 1) p.setItemInHand(new ItemStack(Material.AIR));
        else p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
    }

    public World getWorld() {
        return world;
    }

    public void teleport (Player p, Location loc) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
        p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы будете телепортированы через 5 секунд.");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.teleport(loc);
                p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы были телепортированы.");
            }
        }.runTaskLaterAsynchronously(PrisonMain.getInstance(), 5 * 20L);
    }
}