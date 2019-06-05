package commands;

import menu.FractionMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;

public class FractionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        if (!PrisonMain.getInstance().getStats().containsKey(p.getUniqueId()))
            return false;
        if (PrisonMain.getInstance().getStats().get(p.getUniqueId()).getPrisonClansLocation() == null)
            FractionMenu.getInstance().openPlayerGUI(p);
        else
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Вы не можете выбрать вторую рассу.");
        return false;
    }
}
