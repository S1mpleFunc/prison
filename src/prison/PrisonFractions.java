package prison;

import org.bukkit.Location;
import org.bukkit.Material;

public enum PrisonFractions {

    NORMAL("§7Заключенный", null, null),
    ASIAT("§6Азиат", new Location(PrisonMain.getInstance().getWorld(), 5,5,5), Material.GOLD_INGOT),
    NIGGER("§8Ниггер", new Location(PrisonMain.getInstance().getWorld(), 5,5,5), Material.COAL),
    WHITE("§fБелый", new Location(PrisonMain.getInstance().getWorld(), 5,5,5), Material.IRON_INGOT),
    ;
    String name;
    Location location;
    Material material;

    PrisonFractions (String name, Location location, Material material) {
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

    public static PrisonFractions getByName (String s) {
        for (PrisonFractions c : PrisonFractions.values())
            if (c.getName().equalsIgnoreCase(s))
                return c;
        return null;
    }
}
