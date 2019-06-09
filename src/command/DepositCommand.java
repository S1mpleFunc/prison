package command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;
import prison.PrisonPlayer;
import prison.PrisonVariables;

import java.util.HashMap;
import java.util.UUID;

public class DepositCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        try {
            PrisonPlayer sender = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
            if (Double.parseDouble(strings[1]) > sender.getGold()) {
                p.sendMessage(PrisonVariables.ERROR.getO() + "У вас недостаточно средств.");
                return false;
            }
            if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(strings[0]))) {
                p.sendMessage(PrisonVariables.ERROR.getO() + "Данный игрок оффлайн.");
                return false;
            }
            Player get = Bukkit.getPlayer(strings[0]);
            float value = Float.parseFloat(strings[1]);
            if (value <= 0) {
                p.sendMessage(PrisonVariables.ERROR.getO() + "Хорошая попытка, мамин программист.");
                return false;
            }
            sender.setGold(sender.getGold() - value);
            PrisonPlayer getter = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(get.getUniqueId());
            getter.setGold(getter.getGold() + value);
            p.sendMessage(PrisonVariables.INFO.getO() + "Вы успешно передали " + strings[0] + " " + value + "$.");
            get.sendMessage(PrisonVariables.INFO.getO() + "Вы получили " + value + "$ от " + p.getName() + ".");
        } catch (Exception e) {
            p.sendMessage(PrisonVariables.ERROR.getO() + "Транзакция отменена.");
        }
        return true;
    }
}
