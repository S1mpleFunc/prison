package prison;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

public class PrisonScoreboard {

    private ScoreboardManager manager;

    public void setScoreboard (Player p)
    {
        manager = Bukkit.getScoreboardManager();
        Scoreboard board = manager.getNewScoreboard();
        Objective objective = board.registerNewObjective("board", "dummy");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        objective.setDisplayName("§6§lPRISON");
        ((HashMap<UUID, Objective>)PrisonVariables.SCORES.getO()).put(p.getUniqueId(), objective);
        if (!((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId())) {
            p.sendMessage(PrisonVariables.ERROR.getO() + "Перезайдите на сервер.");
            return;
        }
        PrisonPlayer player = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
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
        Objective objective = ((HashMap<UUID, Objective>)PrisonVariables.SCORES.getO()).get(p.getUniqueId());
        if (!((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).containsKey(p.getUniqueId())) {
            p.sendMessage(PrisonVariables.ERROR.getO() + "Перезайдите на сервер.");
            return;
        }
        for (String s : p.getScoreboard().getEntries())
            objective.getScoreboard().resetScores(s);

        PrisonPlayer player = ((HashMap<UUID, PrisonPlayer>) PrisonVariables.PLAYER_STATS.getO()).get(p.getUniqueId());
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
            Score time = objective.getScore("§7Непрерывно работает: " + PrisonMain.getInstance().formatTime((int)PrisonVariables.GLOBAL_TIME.getO()));
            time.setScore(6);
        }
        if ((int)PrisonVariables.BLOCK_BOOSTER_END.getO() > 0) {
            Score block = objective.getScore("§6§lБустер блоков: §f§l" + PrisonMain.getInstance().formatTime((int)PrisonVariables.BLOCK_BOOSTER_END.getO()));
            block.setScore(5);
        }
        if ((int)PrisonVariables.MONEY_BOOSTER_END.getO() > 0) {
            Score money = objective.getScore("§6§lБустер денег: §f§l" + PrisonMain.getInstance().formatTime((int)PrisonVariables.MONEY_BOOSTER_END.getO()));
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
