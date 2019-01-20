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
import theAct.TheActMod;
import theAct.monsters.*;
import theAct.monsters.MUSHROOMPOWER.MushroomYandere;
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

    protected void generateWeakEnemies(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo("Shell Parasite", 2.0F));
        //monsters.add(new MonsterInfo(GIANT WRAT HERE, 3.0f));
        monsters.add(new MonsterInfo(TheActMod.makeID("Flameango_and_Byrd"), 2.0F));
        monsters.add(new MonsterInfo(TheActMod.makeID("6_Spyders"), 3.0f));
        monsters.add(new MonsterInfo(MushroomYandere.ENCOUNTER_ID, 3.0f));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(SlimyTreeVines.ENCOUNTER_NAME, 3.0F));
        monsters.add(new MonsterInfo(SilentTribesmen.ENCOUNTER_ID, 3.0f));
        monsters.add(new MonsterInfo("Snake Plant", 2.0F));
        monsters.add(new MonsterInfo(Lyon.ID, 3.0f));
        monsters.add(new MonsterInfo(TheActMod.makeID("2_Flameangoes"), 2.0F));
        monsters.add(new MonsterInfo(TheActMod.makeID("2_Snecko_Cultists"), 3.0F));
        monsters.add(new MonsterInfo(TheActMod.makeID("Silent_and_trap"), 2.0F));

        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

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
        bossList.clear();
        // Bosses are added via BaseMod in TheActMod.receivePostInitialize()
    }

    @Override
    protected void initializeEventList()
    {
        // Events are added via BaseMod in TheActMod.receivePostInitialize()
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
