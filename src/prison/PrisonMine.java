package prison;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class PrisonMine {

    private String name;
    private int mlevel;
    private boolean pvp;
    private int height;
    private int diametr;
    private int x, y, z;
    private int blocks;
    private int delay;
    private Material[] types;

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);

    public PrisonMine(String name, int mlevel, boolean pvp, int height, int diametr, int x, int y, int z, int delay, int blocks, Material... types) {
        this.name = name;
        this.mlevel = mlevel;
        this.pvp = pvp;
        this.height = height;
        this.diametr = diametr;
        this.x = x;
        this.y = y;
        this.z = z;
        this.blocks = blocks;
        this.types = types;
        this.delay = delay;
    }
    public void updateMine (PrisonMine mine) {
        new BukkitRunnable() {
            @Override
            public void run() {

                Random random = new Random();

                Location loc = new Location(Bukkit.getWorld("world"), mine.getX(), mine.getY(), mine.getZ());
                int diametr = mine.getDiametr();
                for (int i = 0; i < mine.getHeight(); i++) {
                    for (int q = 0; q < diametr; q++) {
                        for (int l = 0; l < diametr; l++) {
                            loc.getBlock().setType(mine.getTypes()[random.nextInt(mine.getBlocks())]);
                            loc.subtract(1, 0, 0);
                            if (l == diametr - 1) loc.subtract(-l-1, 0, 0);
                        }
                        loc.subtract(0, 0, 1);
                        if (q == diametr - 1) loc.subtract(0, 0, -q-1);
                    }
                    loc.subtract(0, 1, 0);
                }

                Bukkit.broadcastMessage(mine.getName() + " была обновлена");

            }
        }.runTaskTimer(plugin, 0, mine.getDelay() * 20L);
    }

    public String getName() {
        return name;
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
    public int getBlocks() {
        return blocks;
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
    public Material[] getTypes() {
        return types;
    }
}
