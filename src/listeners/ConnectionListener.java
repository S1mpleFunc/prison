package listeners;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import prison.PrisonMain;
import prison.PrisonPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class ConnectionListener implements Listener {

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);

    @EventHandler
    public void onJoinEvent (PlayerJoinEvent e) {

        Player p = e.getPlayer();
        try {
            ResultSet rs = plugin.statement.executeQuery("SELECT * FROM `PrisonPlayers` WHERE uuid = '" + p.getUniqueId() + "';");
            if (rs.next()) {
                //id	uuid	  gold	    level	   kills	  deaths	blocks
                plugin.stats.put(p.getUniqueId(), new PrisonPlayer(
                        rs.getInt("id"),
                        rs.getString("uuid"),
                        rs.getInt("gold"),
                        rs.getInt("level"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getString("blocks")
                        )
                );
                p.sendMessage("ВВВВВВВВВ");
            }
            else {
                plugin.statement.executeUpdate("INSERT INTO `PrisonPlayers` (uuid, gold, level, kills, deaths, blocks) VALUES('" + p.getUniqueId() + "', 0, 1, 0, 0, 'STONE');");
                p.sendMessage("Новый профиль создан");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("info")));
        // e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', prisonMain.getConfig().getString("error")));
        //e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', prisonMain.getConfig().getString("fatal_error")));
    }
}
