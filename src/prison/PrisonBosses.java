package prison;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;


public enum PrisonBosses {

    SPIDER("§6§lМатка", new Location((World) PrisonVariables.WORLD.getO(), 214, 40, 432), 200, 4, EntityType.SPIDER, 250, 60 * 20 * 20),
    BLAZE("§c§lОгенные недра", new Location((World) PrisonVariables.WORLD.getO(), 214, 40, 434), 500, 3, EntityType.BLAZE, 500, 60 * 20 * 35),
    GOLEM("§f§lЖивое существо", new Location((World) PrisonVariables.WORLD.getO(), 214, 40, 436), 1000, 6, EntityType.IRON_GOLEM, 750, 60 * 20 * 50),
    ENDERMAN("§5§lТёмный трибунал", new Location((World) PrisonVariables.WORLD.getO(), 214, 40, 438), 1500, 8, EntityType.ENDERMAN, 1000, 60 * 20 * 90)
    ,;

    private String name;
    private Location loc;
    private int HP;
    private double damage;
    private EntityType entityType;
    private int reward;
    private int respawnTime;

    PrisonBosses (String name, Location loc, int HP, double damage, EntityType entityType, int reward, int respawnTime) {
        this.name = name;
        this.loc = loc;
        this.HP = HP;
        this.damage = damage;
        this.entityType = entityType;
        this.reward = reward;
        this.respawnTime = respawnTime;
    }

    public static void updeteBosses () {
        for (PrisonBosses prisonBosses : values()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (((LinkedList<LivingEntity>)PrisonVariables.BOSSES.getO()).size() > 4) return;

                    LivingEntity boss = (LivingEntity) ((World) PrisonVariables.WORLD.getO()).spawnEntity(prisonBosses.getLocation(), prisonBosses.getEntityType());
                    boss.setCustomName(prisonBosses.getName() + " §lHP: §c§l " + prisonBosses.getHP());
                    boss.setMaxHealth(prisonBosses.getHP());
                    boss.setHealth(prisonBosses.getHP());
                    boss.setCustomNameVisible(true);
                    ((LinkedList<LivingEntity>)PrisonVariables.BOSSES.getO()).add(boss);
                }
            }.runTaskTimer(PrisonMain.getInstance(), 15 * 20L, prisonBosses.getRespawnTime());
        }
    }

    public static PrisonBosses getBossByEntityType (EntityType entityType) {
        for (PrisonBosses bosses : PrisonBosses.values())
            if (bosses.getEntityType().equals(entityType))
                return bosses;
        return null;
    }

    public String getName() {
        return name;
    }

    public int getReward() {
        return reward;
    }

    public double getDamage() {
        return damage;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public int getHP() {
        return HP;
    }

    public int getRespawnTime() {
        return respawnTime;
    }

    public Location getLocation() {
        return loc;
    }
}
