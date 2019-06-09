package prison;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class PrisonScoreboard {

    private ScoreboardManager manager;

    public void setScoreboard (Player p)
    {
        manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lPRISON");
        PrisonMain.getInstance().getScores().put(p.getUniqueId(), objective);
        if (!PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Перезайдите на сервер.");
            return;
        }
        PrisonPlayer player = PrisonMain.getInstance().getStats().get(p.getUniqueId());
        Score empty = objective.getScore("");
        empty.setScore(14);
        Score level = objective.getScore("§f§lУровень: §b" + player.getLevel());
        level.setScore(13);
        Score blocks = objective.getScore("§f§lБлоков: §6" + player.getBlocksValue());
        blocks.setScore(12);
        Score coins = objective.getScore("§f§lДенег: §a" + player.getGold() + "$");
        coins.setScore(11);
        Score rats = objective.getScore("§f§lКрыс: §c" + player.getRatsValue());
        rats.setScore(10);
        Score kills = objective.getScore("§f§lУбийств: §c" + player.getKills());
        kills.setScore(9);
        Score frac = objective.getScore("§f§lФракция: §c" + player.getPrisonClanName());
        frac.setScore(8);
        Score empty2 = objective.getScore("  ");
        empty2.setScore(3);
        Score online = objective.getScore("§bОнлайн: §f§l" + Bukkit.getOnlinePlayers().size());
        online.setScore(2);
        Score empty1 = objective.getScore("   ");
        empty1.setScore(1);
        Score site = objective.getScore("§f§l   www.§5§lhentai-heaven§f§l.org");
        site.setScore(0);

        p.setScoreboard(objective.getScoreboard());
    }
    public void updateScoreboard (Player p) {
        Objective objective = PrisonMain.getInstance().getScores().get(p.getUniqueId());
        if (!PrisonMain.getInstance().getStats().containsKey(p.getUniqueId())) {
            p.sendMessage(PrisonMain.getInstance().getErrorPrefix() + "Перезайдите на сервер.");
            return;
        }
        for (String s : p.getScoreboard().getEntries())
            objective.getScoreboard().resetScores(s);

        PrisonPlayer player = PrisonMain.getInstance().getStats().get(p.getUniqueId());
        Score empty = objective.getScore("");
        empty.setScore(14);
        Score level = objective.getScore("§fУровень: §b" + player.getLevel());
        level.setScore(13);
        Score blocks = objective.getScore("§fБлоков: §6" + player.getBlocksValue());
        blocks.setScore(12);
        Score coins = objective.getScore("§fДенег: §a" + player.getGold() + "$");
        coins.setScore(11);
        Score rats = objective.getScore("§fКрыс: §c" + player.getRatsValue());
        rats.setScore(10);
        Score kills = objective.getScore("§fУбийств: §c" + player.getKills());
        kills.setScore(9);
        Score frac = objective.getScore("§fФракция: §c" + player.getPrisonClanName());
        frac.setScore(8);
        if (p.isOp()) {
            Score empty1 = objective.getScore(" ");
            empty1.setScore(7);
            Score time = objective.getScore("§7Непрерывно работает: " + PrisonMain.getInstance().formatTime(PrisonMain.getInstance().getGLOBAL_TIME()));
            time.setScore(6);
        }
        if (PrisonMain.getInstance().getBLOCK_BOOSTER_END() > 0) {
            Score block = objective.getScore("§6§lБустер блоков: §f§l" + PrisonMain.getInstance().formatTime(PrisonMain.getInstance().getBLOCK_BOOSTER_END()));
            block.setScore(5);
        }
        if (PrisonMain.getInstance().getMONEY_BOOSTER_END() > 0) {
            Score money = objective.getScore("§6§lБустер денег: §f§l" + PrisonMain.getInstance().formatTime(PrisonMain.getInstance().getMONEY_BOOSTER_END()));
            money.setScore(4);
        }
        Score empty2 = objective.getScore("  ");
        empty2.setScore(3);
        Score online = objective.getScore("§bОнлайн: §f§l" + Bukkit.getOnlinePlayers().size());
        online.setScore(2);
        Score empty1 = objective.getScore("   ");
        empty1.setScore(1);
        Score site = objective.getScore("§f§l   www.§5§lhentai-heaven§f§l.org");
        site.setScore(0);

         p.setScoreboard(objective.getScoreboard());
    }
    public static PrisonScoreboard getInstance () {
        return new PrisonScoreboard();
    }
}
