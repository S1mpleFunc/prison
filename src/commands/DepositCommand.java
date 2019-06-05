package commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import prison.PrisonMain;
import prison.PrisonPlayer;
public class DepositCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) return false;
        Player p = (Player) commandSender;
        try {
            PrisonPlayer sender = PrisonMain.getInstance().getStats().get(p.getUniqueId());
            if (Double.parseDouble(strings[1]) > sender.getGold()) {
                p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "У вас недостаточно средств.");
                return false;
            }
            if (!Bukkit.getOnlinePlayers().contains(Bukkit.getPlayer(strings[0]))) {
                p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Данный игрок оффлайн.");
                return false;
            }
            Player get = Bukkit.getPlayer(strings[0]);
            float value = Float.parseFloat(strings[1]);
            if (value <= 0) {
                p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Хорошая попытка, мамин программист.");
                return false;
            }
            sender.setGold(sender.getGold() - value);
            PrisonPlayer getter = PrisonMain.getInstance().getStats().get(get.getUniqueId());
            getter.setGold(getter.getGold() + value);
            p.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы успешно передали " + strings[0] + " " + value + "$.");
            get.sendMessage(PrisonMain.getInstance().getInfoPrefix() + "Вы получили " + value + "$ от " + p.getName() + ".");
        } catch (Exception e) {
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Транзакция отменена.");
        }
        return true;
    }
}
