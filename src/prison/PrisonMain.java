package prison;

import listeners.ConnectionListener;
import listeners.HandListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import sql.MySQL;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class PrisonMain extends JavaPlugin {

    public Statement statement;
    public HashMap<UUID, PrisonPlayer> stats;
    public HashMap<Integer, PrisonMine> mines;

    @Override
    public void onEnable() {
        MySQL base = new MySQL(getConfig().getString("user"), getConfig().getString("password"), getConfig().getString("host"), getConfig().getString("database"), getConfig().getInt("port"));
        try {
            getLogger().info("[!] Connecting to DataBase.");
            statement = base.openConnection().createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS `PrisonPlayers` (id INT NOT NULL AUTO_INCREMENT PRIMARY KEY, uuid TEXT, gold INT, level INT, kills INT, deaths INT, blocks TEXT);");
            getLogger().info("[!] Connected to DataBase.");
        } catch (ClassNotFoundException | SQLException e) {
            getLogger().info("[!] Connection exception.");
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("fatal_error")) + "Упс... Произошла фатальная ошибка номер 2, просим как можно быстрее оповестите администраторов.");
        }
        Bukkit.getPluginManager().registerEvents(new ConnectionListener(), this);
        Bukkit.getPluginManager().registerEvents(new HandListener(), this);

        stats = new HashMap<>();
        mines = new HashMap<>();

        for (int i = 0; i < getConfig().getInt("mines_value"); i++) {
            String[] mine = getConfig().getString("mine" + (i + 1)).split("/");
            String[] types = mine[10].split(";");
            Material[] materials = new Material[types.length];
            for (int m = 0; m < types.length; m++)
                materials[m] = Material.getMaterial(types[m]);
            mines.put((i + 1), new PrisonMine(mine[0], Integer.parseInt(mine[1]), Boolean.parseBoolean(mine[2]), Integer.parseInt(mine[3]), Integer.parseInt(mine[4]), Integer.parseInt(mine[5]), Integer.parseInt(mine[6]), Integer.parseInt(mine[7]), Integer.parseInt(mine[8]), Integer.parseInt(mine[9]), materials));
            mines.get(i + 1).updateMine(mines.get(i + 1));
        }
    }

    @Override
    public void onDisable() {
    }
}