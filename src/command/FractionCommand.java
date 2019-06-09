package command;

import menu.FractionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.UUID;

public class FractionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        if (!((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId()))
            return false;
        if (((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId()).getPrisonClansLocation() == null)
            new FractionMenu().openPlayerGUI(p);
        else
            p.sendMessage(PrisonVariables.ERROR.getO() + "Вы не можете выбрать вторую рассу.");
        return false;
    }
}
