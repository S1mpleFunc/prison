package prison;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class PrisonMine {

    private String name;
    private int mlevel;
    private boolean pvp;
    private int height;
    private int diametr;
    private int x, y, z, tpx, tpy, tpz;
    private int delay;
    private String types;
    private Material material;

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);

    public PrisonMine(String name, int mlevel, boolean pvp, int height, int diametr, int x, int y, int z, int delay, String types, Material material, int tpx, int tpy, int tpz) {
        this.name = name;
        this.material = material;
        this.mlevel = mlevel;
        this.pvp = pvp;
        this.height = height;
        this.diametr = diametr;
        this.x = x;
        this.y = y;
        this.z = z;
        this.types = types;
        this.delay = delay;
        this.tpx = tpx;
        this.tpy = tpy;
        this.tpz = tpz;
    }
    public void updateMine (PrisonMine mine) {
        new BukkitRunnable() {
            @Override
            public void run() {

                Random random = new Random();

                Location loc = new Location(PrisonMain.getInstance().getWorld(), mine.getX(), mine.getY(), mine.getZ());
                int diametr = mine.getDiametr();
                Location endLoc = new Location(PrisonMain.getInstance().getWorld(), mine.getX() - diametr, mine.getY() - mine.getHeight(), mine.getZ() - diametr);

                for (Player p : Bukkit.getOnlinePlayers()) {
                    Location plyerLoc = p.getLocation();
                    if (plyerLoc.getX() - endLoc.getX() <  diametr && plyerLoc.getX() - loc.getX() <  diametr &&
                            plyerLoc.getY() - endLoc.getY() <  diametr && plyerLoc.getY() - loc.getY() <  diametr) {
                        p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы был перемещенны вверх.");
                        plyerLoc.setY(loc.getY() + 1);
                        p.teleport(plyerLoc);
                    }
                }

                for (int i = 0; i < mine.getHeight(); i++) {
                    for (int q = 0; q < diametr; q++) {
                        for (int l = 0; l < diametr; l++) {
                            loc.getBlock().setType(Material.getMaterial(mine.getTypes().split(" ")[random.nextInt(mine.getTypes().split(" ").length)]));
                            loc.subtract(1, 0, 0);
                            if (l == diametr - 1) loc.subtract(-l-1, 0, 0);
                        }
                        loc.subtract(0, 0, 1);
                        if (q == diametr - 1) loc.subtract(0, 0, -q-1);
                    }
                    loc.subtract(0, 1, 0);
                }
            }
        }.runTaskTimer(plugin, 0, mine.getDelay() * 20L);
    }

    public boolean getPvp() {
        return pvp;
    }
    public String getName() {
        return name;
    }
    public Material getMaterial() {
        return material;
    }
    public int getMlevel() {
        return mlevel;
    }
    public int getX() {
        return x;
    }
    public int getDelay() {
        return delay;
    }
    public int getY() {
        return y;
    }
    public int getDiametr() {
        return diametr;
    }
    public int getHeight() {
        return height;
    }
    public int getZ() {
        return z;
    }
    public String getTypes() {
        return types;
    }

    public Location getMineLocation() {
        return new Location(PrisonMain.getInstance().getWorld(), this.tpx, this.tpy, this.tpz);
    }
}
