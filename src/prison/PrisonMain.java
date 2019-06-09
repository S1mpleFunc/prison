package prison;

import command.*;
import listener.*;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import sql.MySQL;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class PrisonMain extends JavaPlugin {

    @Override
    public void onEnable() {

        registerConfig();
        if (!getConfig().getBoolean("prison")) return;
        if (getConfig().getInt("pin_code") != 4361) return;

        MySQL base = new MySQL(getConfig().getString("user"), getConfig().getString("password"), getConfig().getString("host"), getConfig().getString("database"), getConfig().getInt("port"));
        try {
            getLogger().info("[!] Connecting to DataBase.");
            PrisonVariables.STATEMENT.setO(base.openConnection().createStatement());
            ((Statement)PrisonVariables.STATEMENT.getO()).executeUpdate("CREATE TABLE IF NOT EXISTS `PrisonPlayers` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid TEXT, gold FLOAT, level INT, kills INT, deaths INT, blocks TEXT, clan TEXT, enter INT);");
            getLogger().info("[!] Connected to DataBase.");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("[!] Connection exception.");
            Bukkit.broadcastMessage(PrisonVariables.FATAL_ERROR.getO() + "Упс... Произошла фатальная ошибка номер 2, просим как можно быстрее оповестите администраторов.");
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

        World world = (World) PrisonVariables.WORLD.getO();
        world.setAutoSave(false);
        world.setTime(6000);
        world.setMonsterSpawnLimit(0);
        world.setAnimalSpawnLimit(0);

        for (Player p : Bukkit.getOnlinePlayers())
            ConnectionListener.getInstance().loadStats(p);

        for (int i = 0; i < getConfig().getInt("mines_value"); i++) {
            ((LinkedList<PrisonMine>)PrisonVariables.MINES.getO()).add(new PrisonMine(
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
                    getConfig().getInt("mines." + (i+1) + ".tpz"),
                    this));
            ((LinkedList<PrisonMine>)PrisonVariables.MINES.getO()).get(i).updateMine(((LinkedList<PrisonMine>)PrisonVariables.MINES.getO()).get(i));
        }
        PrisonBosses.updeteBosses();
        new BukkitRunnable() {
            @Override
            public void run() {
                PrisonMain.getInstance().incLOBAL_TIME();
                world.setTime(0);
                Random random = new Random();
                for (Player p : Bukkit.getOnlinePlayers()) {
                    PrisonScoreboard.getInstance().updateScoreboard(p);
                    if ((int)PrisonVariables.GLOBAL_TIME.getO() % 900 == 0) {
                        int effect = random.nextInt(PrisonMain.getInstance().getConfig().getInt("needs.amount") - 1) + 1;
                        p.addPotionEffect(new PotionEffect(PotionEffectType.getByName(PrisonMain.getInstance().getConfig().getString("needs." + effect + ".effect")), 100000, 3));
                        p.sendMessage(PrisonVariables.INFO.getO() + PrisonMain.getInstance().getConfig().getString("needs." + effect + ".message"));
                    }
                }
                if ((int)PrisonVariables.GLOBAL_TIME.getO() % 120 == 0) {
                    if (Bukkit.getWorld("world").getEntities().stream().filter(x -> x.getType().equals(EntityType.SILVERFISH)).toArray().length < 20)
                        for (int i = 1; i < getConfig().getInt("rats.amount") + 1; i++)
                            Bukkit.getWorld("world").spawnEntity(new Location(((World)PrisonVariables.WORLD.getO()), getConfig().getInt("rats." + i + ".x"), getConfig().getInt("rats." + i + ".y"), getConfig().getInt("rats." + i + ".z")), EntityType.SILVERFISH);
                }
                if ((int)PrisonVariables.BLOCK_BOOSTER_END.getO() == 0) {
                    PrisonVariables.GLOBAL_BLOCK_BOOSTER.setO(1);
                    PrisonVariables.BLOCK_BOOSTER_END.setO(-1);
                    Bukkit.broadcastMessage(PrisonVariables.INFO.getO() + "Бустер блоков истек.");
                } else if ((int)PrisonVariables.BLOCK_BOOSTER_END.getO() > 0)
                    PrisonVariables.BLOCK_BOOSTER_END.setO((int)PrisonVariables.BLOCK_BOOSTER_END.getO() - 1);
                if ((int)PrisonVariables.GLOBAL_MONEY_BOOSTER.getO() == 0) {
                    PrisonVariables.GLOBAL_MONEY_BOOSTER.setO(1);
                    PrisonVariables.MONEY_BOOSTER_END.setO(-1);
                    Bukkit.broadcastMessage(PrisonVariables.INFO.getO() + "Бустер денег истек.");
                } else if ((int)PrisonVariables.MONEY_BOOSTER_END.getO() > 0)
                    PrisonVariables.MONEY_BOOSTER_END.setO((int)PrisonVariables.MONEY_BOOSTER_END.getO() - 1);
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

    public void incLOBAL_TIME() {
        PrisonVariables.GLOBAL_TIME.setO((int)PrisonVariables.GLOBAL_TIME.getO() + 1);
    }


    public String formatTime (int time) {
        return String.format("%02d:%02d", time / 60, time % 60);
    }
    public void deleteOneItem (Player p) {
        if (p.getItemInHand().getAmount() == 1) p.setItemInHand(new ItemStack(Material.AIR));
        else p.getItemInHand().setAmount(p.getItemInHand().getAmount() - 1);
    }

    public void teleport (Player p, Location loc) {
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 120, 1));
        p.sendMessage(PrisonVariables.INFO.getO() + "Вы будете телепортированы через 5 секунд.");
        new BukkitRunnable() {
            @Override
            public void run() {
                p.teleport(loc);
                p.sendMessage(PrisonVariables.INFO.getO() + "Вы были телепортированы.");
            }
        }.runTaskLaterAsynchronously(PrisonMain.getInstance(), 5 * 20L);
    }
    public void dropPlayerAround (Player p, Location center, double power) {
        Location playerLocation = p.getLocation();
        double x = (playerLocation.getX() - center.getX()) * 10;
        double y = playerLocation.getY() - 30;
        double z = (playerLocation.getZ() - center.getZ()) * 10;

        p.setVelocity(new Vector(x,y,z).multiply(power));
    }

    public static PrisonMain getInstance () {
        return PrisonMain.getPlugin(PrisonMain.class);
    }
}