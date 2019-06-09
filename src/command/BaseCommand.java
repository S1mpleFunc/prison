package command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.UUID;

public class BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        if (!((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId()))
            return false;
        PrisonPlayer prisonPlayer = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
        if (ChatColor.stripColor(prisonPlayer.getPrisonClanName()).equalsIgnoreCase("заключенный")) {
            p.sendMessage(PrisonVariables.INFO.getO() + "Выберите фракцию!");
            return false;
        }
        PrisonMain.getInstance().teleport(p, prisonPlayer.getPrisonClansLocation());
        return false;
    }
}
