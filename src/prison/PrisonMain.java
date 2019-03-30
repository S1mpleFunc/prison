package prison;

import listeners.ConnectionListener;
import listeners.HandListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import sql.MySQL;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.UUID;

public class PrisonMain extends JavaPlugin {

    public Statement statement;
    public HashMap<UUID, PrisonPlayer> stats;

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
    }

    @Override
    public void onDisable() {
    }
}