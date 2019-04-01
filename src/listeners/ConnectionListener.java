package listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import prison.PrisonMain;
import prison.PrisonPlayer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionListener implements Listener {

    PrisonMain plugin = PrisonMain.getPlugin(PrisonMain.class);

    @EventHandler
    public void onJoinEvent(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        try {
            ResultSet rs = plugin.statement.executeQuery("SELECT * FROM `PrisonPlayers` WHERE uuid = '" + p.getUniqueId() + "';");
            if (rs.next()) {
                //id	uuid	  gold	    level	   kills	  deaths	blocks
                plugin.stats.put(p.getUniqueId(), new PrisonPlayer(
                        rs.getInt("id"),
                        rs.getInt("gold"),
                        rs.getInt("level"),
                        rs.getInt("kills"),
                        rs.getInt("deaths"),
                        rs.getString("blocks"))
                );

            } else {
                plugin.statement.executeUpdate("INSERT INTO `PrisonPlayers` (uuid, gold, level, kills, deaths, blocks) VALUES('" + p.getUniqueId() + "', 0, 1, 0, 0, 'STONE 0 LEAVES 0 LOG 0 SAND 0 GRAVEL 0 DIRT 0 COBBLESTONE 0 NETHERRACK 0');");
                p.sendMessage("Новый профиль создан");
            }

        } catch (SQLException ex) {
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error")) + "Упс... Произошла ошибка номер 1, обратитесь к персоналу сообщив номер ошибки.");
        }

        e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("info")));
        // e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', prisonMain.getConfig().getString("error")));
        //e.getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', prisonMain.getConfig().getString("fatal_error")));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        e.setQuitMessage(null);
        PrisonPlayer prisonPlayer = null;
        if(plugin.stats.containsKey(p.getUniqueId()))
            prisonPlayer = plugin.stats.get(p.getUniqueId());
        else
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error")) + "Упс... Произошла ошибка номер 5, у " + p.getName() + ", если вы видете ЭТО значит, я что то сломал, простите.");
        try {
            ResultSet rs = plugin.statement.executeQuery("SELECT * FROM `PrisonPlayers` WHERE uuid = '" + p.getUniqueId() + "';");
            if (rs.next()) {
                // int new_kills = rs.getInt("kills") + NEW KILLS;
                // int new_coins = rs.getInt("gold") + NEW GOLD;
                // int new_deaths = rs.getInt("deaths") + NEW DEATHS;
                plugin.statement.executeUpdate("UPDATE `PrisonPlayers` SET blocks ='" + prisonPlayer.getBlocks() + "' WHERE uuid = '" + p.getUniqueId() + "';");
            }
        } catch (SQLException ex) {
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', plugin.getConfig().getString("error")) +
                    "Упс... Произошла ошибка номер 4, статистика " + p.getName() + " не сохранилась, пожалуйста отправте это сообщение администратору в полном виде." + prisonPlayer.getBlocks());
        }
        plugin.stats.remove(p.getUniqueId());
    }
}

