package prison;

import org.bukkit.Location;

public class PrisonPlayer {

    String block[];

    //Создание класса хранящего всю статистику игрока
    private int id;
    private float gold;
    private int level;
    private int kills;
    private int deaths;
    private String blocks;
    private PrisonFractions prisonFractions;
    private int canEnter;

    //                       id	        uuid	  gold	    level	   kills	  deaths	blocks
    public PrisonPlayer (int id, float gold, int level, int kills, int deaths,  String blocks, PrisonFractions prisonFractions, int canEnter) {
        this.id = id;
        this.gold = gold;
        this.level = level;
        this.kills = kills;
        this.deaths = deaths;
        this.blocks = blocks;
        this.prisonFractions = prisonFractions;
        this.canEnter = canEnter;
    }

    public int getId() {
        return id;
    }
    public float getGold() {
        return gold;
    }
    public int getLevel() {
        return level;
    }
    public int getKills() {
        return kills;
    }
    public int getDeaths() {
        return deaths;
    }
    public String getBlocks() {
        return blocks;
    }
    public int isCanEnter() {
        return canEnter;
    }

    public void incBlock(String material) {
        block = this.getBlocks().split(" ");
        String string = "";
        Integer value;

        for (int i = 0; i < block.length; i++) {
            if (block[i].equalsIgnoreCase(material)) {
                value = Integer.parseInt(block[ i + 1 ]) + 1 * PrisonMain.getInstance().getGLOBAL_BLOCK_BOOSTER();
                block[i + 1] = value.toString();
            }
            string += block[i] + " ";
        }

        this.blocks = string;
    }
    public void incRat () {
        block = this.getBlocks().split(" ");
        int rats = Integer.parseInt(block[block.length - 1]) + 1;
        String string = "";
        for (int i = 0; i < block.length - 1; i++)
            string += block[i] + " ";
        string += rats;
        this.blocks = string;
    }
    public int getBlocksValue() {
        int blocks = 0;
        for (int i = 0; i < this.getBlocks().split(" ").length/2 - 1; i++)
            blocks += Integer.parseInt(this.getBlocks().split(" ")[i * 2 + 1]);
        return blocks;
    }
    public int getRatsValue() {
        return Integer.parseInt(this.getBlocks().split(" ")[this.getBlocks().split(" ").length - 1]);
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }
    public void setGold(float gold) {
        this.gold = gold;
    }
    public void setKills(int kills) {
        this.kills = kills;
    }
    public void setLevel(int level) {
        this.level = level;
    }
    public void setClan(PrisonFractions prisonFractions) {
        this.prisonFractions = prisonFractions;
    }
    public void setCanEnter(int canEnter) {
        this.canEnter = canEnter;
    }

    public String getPrisonClanName() {
        return prisonFractions.getName();
    }
    public PrisonFractions getPrisonClans() {
        return prisonFractions;
    }
    public Location getPrisonClansLocation() {
        return prisonFractions.getLocation();
    }
}
