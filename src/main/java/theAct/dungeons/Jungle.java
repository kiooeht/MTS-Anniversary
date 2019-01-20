package theAct.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.rooms.EmptyRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import theAct.TheActMod;
import theAct.monsters.Phrog;
import theAct.monsters.TotemBoss.TotemBoss;
import theAct.patches.GetDungeonPatches;
import theAct.scenes.TheJungleScene;

import java.util.ArrayList;
import java.util.Collections;

public class Jungle extends AbstractDungeon
{
    public static final String ID = TheActMod.makeID("Jungle");

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];
    public static final String NUM = TEXT[1];

    public Jungle(AbstractPlayer p, ArrayList<String> emptyList)
    {
        super(NAME, ID, p, emptyList);

        if (scene != null) {
            scene.dispose();
        }
        scene = new TheJungleScene(); // TODO
        fadeColor = Color.valueOf("1e0f0aff"); // TODO ?



        initializeLevelSpecificChances();
        mapRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + AbstractDungeon.actNum * 100);
        generateMap();

        CardCrawlGame.music.changeBGM(id);
        AbstractDungeon.currMapNode = new MapRoomNode(0, -1);
        AbstractDungeon.currMapNode.room = new EmptyRoom();
    }

    public Jungle(AbstractPlayer p, SaveFile saveFile)
    {
        super(NAME, p, saveFile);

        CardCrawlGame.dungeon = this;
        if (scene != null) {
            scene.dispose();
        }
        scene = new TheJungleScene(); // TODO
        fadeColor = Color.valueOf("1e0f0aff"); // TODO ?

        initializeLevelSpecificChances();
        miscRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + saveFile.floor_num);
        CardCrawlGame.music.changeBGM(id);
        mapRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + saveFile.act_num * 100);
        generateMap();
        firstRoomChosen = true;

        populatePathTaken(saveFile);
    }

    public static GetDungeonPatches.AbstractDungeonBuilder builder() {
        return new GetDungeonPatches.AbstractDungeonBuilder() {
            @Override
            public AbstractDungeon build(AbstractPlayer p, ArrayList<String> theList) {
                return new Jungle(p,theList);
            }
            @Override
            public AbstractDungeon build(AbstractPlayer p, SaveFile save) {
                return new Jungle(p,save);
            }
        };
    }

    @Override
    protected void initializeLevelSpecificChances()
    {
        // TODO: This is copied from TheCity
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;

        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;

        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;

        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.125F;
        } else {
            cardUpgradedChance = 0.25F;
        }
    }

    @Override
    protected void generateMonsters()
    {
        // TODO: This is copied from TheCity
        generateWeakEnemies(2);
        generateStrongEnemies(12);
        generateElites(10);
    }

    @Override
    protected void generateWeakEnemies(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("Shell Parasite", 2.0F));
        monsters.add(new MonsterInfo("3 Byrds", 2.0f));
        monsters.add(new MonsterInfo(Flameango.ID, 2.0F));
        monsters.add(new MonsterInfo(FunGuy.ID,2.0f));
        monsters.add(new MonsterInfo(SilentTribesmen.ENCOUNTER_ID, 1.0f));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateStrongEnemies(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Flameango.ID, 2.0F));
        monsters.add(new MonsterInfo(FunGuy.ID,2.0f));
        monsters.add(new MonsterInfo(SilentTribesmen.ENCOUNTER_ID, 1.0f));
        monsters.add(new MonsterInfo("Snake Plant", 1.0F));
        monsters.add(new MonsterInfo(Lyon.ID, 2.0f));

        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateElites(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("Gremlin Leader", 1.0F));
        monsters.add(new MonsterInfo(Cassacara.ID, 1.0F));
        monsters.add(new MonsterInfo(Phrog.ID, 1.0f));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, true);
    }

    @Override
    protected ArrayList<String> generateExclusions()
    {
        // TODO: This is copied from TheCity
        ArrayList<String> retVal = new ArrayList<>();
        switch (monsterList.get(monsterList.size() - 1))
        {
            case "Spheric Guardian":
                retVal.add("Sentry and Sphere");
                break;
            case "3 Byrds":
                retVal.add("Chosen and Byrds");
                break;
            case "Chosen":
                retVal.add("Chosen and Byrds");
                retVal.add("Cultist and Chosen");
                break;
        }
        return retVal;
    }

    @Override
    protected void initializeBoss()
    {
        // TODO: This is copied from TheCity
        bossList.clear();
        if (Settings.isDailyRun) {
            bossList.add(TotemBoss.ID);
            //bossList.add("Collector");
            //bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
       // } else if (!UnlockTracker.isBossSeen("CHAMP")) {
        //    bossList.add("Champ");
       // } else if (!UnlockTracker.isBossSeen("AUTOMATON")) {
       //     bossList.add("Automaton");
       // } else if (!UnlockTracker.isBossSeen("COLLECTOR")) {
       //     bossList.add("Collector");
        } else {
            bossList.add(TotemBoss.ID);
            //bossList.add("Collector");
            //bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        }

        if (bossList.size() == 1) {
            bossList.add(bossList.get(0));
        } else if (bossList.isEmpty()) {
            logger.warn("Boss list was empty. How?");
            bossList.add(TotemBoss.ID);
            //bossList.add("Collector");
            //bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        }

        /*
        if (Settings.isDailyRun) {
            bossList.add("Automaton");
            bossList.add("Collector");
            bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        } else if (!UnlockTracker.isBossSeen("CHAMP")) {
            bossList.add("Champ");
        } else if (!UnlockTracker.isBossSeen("AUTOMATON")) {
            bossList.add("Automaton");
        } else if (!UnlockTracker.isBossSeen("COLLECTOR")) {
            bossList.add("Collector");
        } else {
            bossList.add("Automaton");
            bossList.add("Collector");
            bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        }

        if (bossList.size() == 1) {
            bossList.add(bossList.get(0));
        } else if (bossList.isEmpty()) {
            logger.warn("Boss list was empty. How?");
            bossList.add("Automaton");
            bossList.add("Collector");
            bossList.add("Champ");
            Collections.shuffle(bossList, new java.util.Random(monsterRng.randomLong()));
        }
        */
    }

    @Override
    protected void initializeEventList()
    {
        // TODO: This is copied from TheCity
        eventList.add("Addict");
        eventList.add("Back to Basics");
        eventList.add("Beggar");
        eventList.add("Colosseum");
        eventList.add("Cursed Tome");
        eventList.add("Drug Dealer");
        eventList.add("Forgotten Altar");
        eventList.add("Ghosts");
        eventList.add("Masked Bandits");
        eventList.add("Nest");
        eventList.add("The Library");
        eventList.add("The Mausoleum");
        eventList.add("Vampires");
    }

    @Override
    protected void initializeEventImg()
    {
        if (eventBackgroundImg != null) {
            eventBackgroundImg.dispose();
            eventBackgroundImg = null;
        }
        eventBackgroundImg = ImageMaster.loadImage("images/ui/event/panel.png");
    }

    @Override
    protected void initializeShrineList()
    {
        // TODO: This is copied from TheCity
        shrineList.add("Match and Keep!");
        shrineList.add("Wheel of Change");
        shrineList.add("Golden Shrine");
        shrineList.add("Transmorgrifier");
        shrineList.add("Purifier");
        shrineList.add("Upgrade Shrine");
    }
}
