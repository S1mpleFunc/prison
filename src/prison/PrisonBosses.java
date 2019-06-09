package prison;

import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;


public enum PrisonBosses {

    SPIDER("§6§lМатка", new Location(PrisonMain.getInstance().getWorld(), 214, 40, 432), 200, 4, EntityType.SPIDER, 250, 60 * 20 * 20),
    BLAZE("§c§lОгенные недра", new Location(PrisonMain.getInstance().getWorld(), 214, 40, 434), 500, 3, EntityType.BLAZE, 500, 60 * 20 * 35),
    GOLEM("§f§lЖивое существо", new Location(PrisonMain.getInstance().getWorld(), 214, 40, 436), 1000, 6, EntityType.IRON_GOLEM, 750, 60 * 20 * 50),
    ENDERMAN("§5§lТёмный трибунал", new Location(PrisonMain.getInstance().getWorld(), 214, 40, 438), 1500, 8, EntityType.ENDERMAN, 1000, 60 * 20 * 90)
    ,;

    private String name;
    private Location loc;
    private int HP;
    private double damage;
    private EntityType entityType;
    private int reward;
    private int respawn_time;

    PrisonBosses (String name, Location loc, int HP, double damage, EntityType entityType, int reward, int respawn_time) {
        this.name = name;
        this.loc = loc;
        this.HP = HP;
        this.damage = damage;
        this.entityType = entityType;
        this.reward = reward;
        this.respawn_time = respawn_time;
    }

    public static void updeteBosses () {
        for (PrisonBosses prisonBosses : values()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (PrisonMain.getInstance().getBosses().size() > 4) return;

                    LivingEntity boss = (LivingEntity) PrisonMain.getInstance().getWorld().spawnEntity(prisonBosses.getLocation(), prisonBosses.getEntityType());
                    boss.setCustomName(prisonBosses.getName() + " §lHP: §c§l " + prisonBosses.getHP());
                    boss.setMaxHealth(prisonBosses.getHP());
                    boss.setHealth(prisonBosses.getHP());
                    boss.setCustomNameVisible(true);
                    PrisonMain.getInstance().getBosses().add(boss);
                }
            }.runTaskTimer(PrisonMain.getInstance(), 15 * 20L, prisonBosses.getRespawn_time());
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

    public int getRespawn_time() {
        return respawn_time;
    }

    public Location getLocation() {
        return loc;
    }
}
