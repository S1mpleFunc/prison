package prison;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

public enum PrisonClans {

    NORMAL("§7Заключенный", null, null),
    ASIAT("§6Азиат", new Location(Bukkit.getWorld("world"), 5,5,5), Material.GOLD_INGOT),
    NIGGER("§8Ниггер", new Location(Bukkit.getWorld("world"), 5,5,5), Material.COAL),
    WHITE("§fБелый", new Location(Bukkit.getWorld("world"), 5,5,5), Material.IRON_INGOT),
    ;
    String name;
    Location location;
    Material material;

    PrisonClans (String name, Location location, Material material) {
        this.name = name;
        this.location = location;
        this.material = material;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public Material getMaterial() {
        return material;
    }

    public static PrisonClans getByName (String s) {
        for (PrisonClans c : PrisonClans.values())
            if (c.getName().equalsIgnoreCase(s))
                return c;
        return null;
    }
}
