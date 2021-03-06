package theAct.dungeons;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
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
        fadeColor = Color.valueOf("0f220aff");



        initializeLevelSpecificChances();
        mapRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + AbstractDungeon.actNum * 100);
        generateMap();

        CardCrawlGame.music.changeBGM("JUNGLEMAIN");
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
        fadeColor = Color.valueOf("0f220aff");

        initializeLevelSpecificChances();
        miscRng = new com.megacrit.cardcrawl.random.Random(Settings.seed + saveFile.floor_num);
        CardCrawlGame.music.changeBGM("JUNGLEMAIN");
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
        monsters.add(new MonsterInfo(MonsterHelper.SHELL_PARASITE_ENC, 2.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.GIANT_WRAT_ENCOUNTER_ID, 3.0f));
        monsters.add(new MonsterInfo(JungleEncounterIDList.FLAMEANGO_AND_BYRD_ENCOUNTER_ID, 2.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.FIVE_SPYDERS_ENCOUNTER_ID, 2.0f));
        monsters.add(new MonsterInfo(JungleEncounterIDList.MUSHROOM_GANG_ENCOUNTER_ID, 3.0f));
        MonsterInfo.normalizeWeights(monsters);
        populateMonsterList(monsters, count, false);
    }

    protected void generateStrongEnemies(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(JungleEncounterIDList.SLIMY_TREE_VINES_ENCOUNTER_ID, 3.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.TWO_JUNGLE_HUNTERS_ENCOUNTER_ID, 3.0f));
        monsters.add(new MonsterInfo(MonsterHelper.SNAKE_PLANT_ENC, 2.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.LYON_ENCOUNTER_ID, 3.0f));
        monsters.add(new MonsterInfo(JungleEncounterIDList.TWO_FLAMEANGOS_ENCOUNTER_ID, 2.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.TWO_SENCKO_CULTISTS_ENCOUNTER_ID, 3.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.SNECKO_CULTIST_AND_TRAP_ENCOUNTER_ID, 2.0F));

        MonsterInfo.normalizeWeights(monsters);
        populateFirstStrongEnemy(monsters, generateExclusions());
        populateMonsterList(monsters, count, false);
    }

    protected void generateElites(int count)
    {
        // TODO: This is copied from TheCity
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(JungleEncounterIDList.MAMA_SNECKO_ENCOUNTER_ID, 1.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.CASSACARA_ENCOUNTER_ID, 1.0F));
        monsters.add(new MonsterInfo(JungleEncounterIDList.TWO_PHROGS_ENCOUNTER_ID, 1.0f));
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
