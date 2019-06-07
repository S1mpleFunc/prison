package commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;
import prison.PrisonPlayer;

public class BaseCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        if (!PrisonMain.getInstance().getStats().containsKey(p.getUniqueId()))
            return false;
        PrisonPlayer prisonPlayer = PrisonMain.getInstance().getStats().get(p.getUniqueId());
        if (ChatColor.stripColor(prisonPlayer.getPrisonClanName()).equalsIgnoreCase("заключенный")) {
            p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Выберите фракцию!");
            return false;
        }
        PrisonMain.getInstance().teleport(p, prisonPlayer.getPrisonClansLocation());
        return false;
    }
}
