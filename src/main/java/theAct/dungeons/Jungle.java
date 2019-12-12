package theAct.dungeons;

import actlikeit.dungeons.CustomDungeon;
import basemod.BaseMod;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import theAct.TheActMod;
import theAct.monsters.*;
import theAct.monsters.MUSHROOMPOWER.MushroomGenki;
import theAct.monsters.MUSHROOMPOWER.MushroomKuudere;
import theAct.monsters.MUSHROOMPOWER.MushroomYandere;
import theAct.monsters.SpyderBoss.DefenderSpyder;
import theAct.monsters.SpyderBoss.HunterSpyder;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.monsters.TotemBoss.TotemBoss;
import theAct.scenes.TheJungleScene;

import java.util.ArrayList;

public class Jungle extends CustomDungeon
{
    public static final String ID = TheActMod.makeID("Jungle");

    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);
    public static final String[] TEXT = uiStrings.TEXT;
    public static final String NAME = TEXT[0];

    public Jungle()
    {
        super(NAME, ID);
        setMainMusic(TheActMod.assetPath("audio/music/jungle_main.ogg"));

        isFinalAct(true);

        addTempMusic("MUSHROOMGANG", TheActMod.assetPath("audio/music/boss_mushroom.ogg"));
        addTempMusic("BOSSSPIDER", TheActMod.assetPath("audio/music/boss_spider.ogg"));
        addTempMusic("BOSSTOTEM", TheActMod.assetPath("audio/music/boss_totem.ogg"));

        addTempMusic("SNECKOIDOL", TheActMod.assetPath("audio/music/snecko_idol.ogg"));

        addTempMusic("JUNGLEELITE", TheActMod.assetPath("audio/music/jungle_elite.ogg"));

        // Weak Pool
        addMonster(JungleEncounterIDList.MUSHROOM_GANG_ENCOUNTER_ID, "Mushroom Gang" , () -> new MonsterGroup(
                new AbstractMonster[]{
                        new MushroomYandere(-385.0F, -15.0F),
                        new MushroomKuudere(-133.0F, 0.0F),
                        new MushroomGenki(125.0F, -30.0F)
                }), 1.5F);
        addMonster(JungleEncounterIDList.FLAMEANGO_AND_BYRD_ENCOUNTER_ID, "Flameango and Byrd", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(50),
                        new Byrd(-305.0F, 110.0F)
                }));
        addMonster(JungleEncounterIDList.GIANT_WRAT_ENCOUNTER_ID, () -> new GiantWrat(-85.0F, -15.0F), 1.5F);
        addMonster(JungleEncounterIDList.FIVE_SPYDERS_ENCOUNTER_ID, "Spider Swarm", () -> new MonsterGroup(
                new AbstractMonster[]{
                        new HunterSpyder(-650.0F, 300.0F, 0),
                        new DefenderSpyder(-425.0F, 250.0F, 1),
                        new HunterSpyder(-200.0F, 220.0F, 2),
                        new DefenderSpyder(25.0F, 300.0F, 3),
                        new HunterSpyder(175.0F, 120.0F, 4)
                }));
        BaseMod.addMonsterEncounter(Jungle.ID, new MonsterInfo(MonsterHelper.SHELL_PARASITE_ENC, 1F));

        // Strong Pool
        addStrongMonster(JungleEncounterIDList.TWO_SENCKO_CULTISTS_ENCOUNTER_ID, "2 Snecko Cultists", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new SneckoCultist(-250, -20),
                        new SneckoCultist(80, 20, true)
                }), 1.5F);
        addStrongMonster(JungleEncounterIDList.TWO_JUNGLE_HUNTERS_ENCOUNTER_ID, "2 Jungle Hunters", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new JungleHunters(-385.0F, -15.0F),
                        new JungleHunters(150.0F, -30.0F)
                }), 1.5F);
        addStrongMonster(JungleEncounterIDList.SLIMY_TREE_VINES_ENCOUNTER_ID, SlimyTreeVines::new, 1.5F);
        addStrongMonster(JungleEncounterIDList.TWO_FLAMEANGOS_ENCOUNTER_ID, "2 Flameangos", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(-250),
                        new Flameango(80)
                }));
        addStrongMonster(JungleEncounterIDList.SNECKO_CULTIST_AND_TRAP_ENCOUNTER_ID, "Snecko Cultist and Trap", () -> new MonsterGroup(
                new AbstractMonster[]{
                        new SwingingAxe(-450.0F, 100.0F),
                        new SneckoCultist(120, 0)
                }));
        addStrongMonster(JungleEncounterIDList.LYON_ENCOUNTER_ID, Lyon::new, 1.5F);

        // Elite Pool
        addEliteEncounter(JungleEncounterIDList.MAMA_SNECKO_ENCOUNTER_ID, MamaSnecko::new);
        addEliteEncounter(JungleEncounterIDList.TWO_PHROGS_ENCOUNTER_ID, "Two Phrogs", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Phrog(-175,0, false),
                        new Phrog(175, 0, true)
                }));
        addEliteEncounter(JungleEncounterIDList.CASSACARA_ENCOUNTER_ID, () -> new Cassacara(50.0F, 0.0F));

        // Bosses
        addBoss(TotemBoss.ID, TotemBoss::new, TheActMod.assetPath("images/map/totemBoss.png"), TheActMod.assetPath("images/map/totemBossOutline.png"));
        addBoss(SpyderBoss.ID, SpyderBoss::new, TheActMod.assetPath("images/map/spiderBoss.png"), TheActMod.assetPath("images/map/spiderBossOutline.png")); // A R T
        addBoss(FunGuy.ID, FunGuy::new, TheActMod.assetPath("images/map/mushroomBoss.png"), TheActMod.assetPath("images/map/mushroomBossOutline.png"));
    }

    public Jungle(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList)
    {
        super(cd, p, emptyList);
    }
    public Jungle(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile)
    {
        super(cd, p, saveFile);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new TheJungleScene();
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
