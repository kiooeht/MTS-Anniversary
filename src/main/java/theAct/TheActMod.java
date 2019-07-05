package theAct;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.ReflectionHacks;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.audio.Sfx;
import com.megacrit.cardcrawl.audio.SoundMaster;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.EnemyData;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.cards.PetSnecko;
import theAct.cards.colorless.Gourd;
import theAct.dungeons.Jungle;
import theAct.events.*;
import theAct.monsters.*;
import theAct.monsters.MUSHROOMPOWER.MushroomGenki;
import theAct.monsters.MUSHROOMPOWER.MushroomKuudere;
import theAct.monsters.MUSHROOMPOWER.MushroomYandere;
import theAct.monsters.SpyderBoss.DefenderSpyder;
import theAct.monsters.SpyderBoss.HunterSpyder;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.monsters.TotemBoss.TotemBoss;
import theAct.potions.*;
import theAct.relics.*;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@SpireInitializer
public class TheActMod implements
        PostInitializeSubscriber,
        EditKeywordsSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        PostUpdateSubscriber
{
    public static final Logger logger = LogManager.getLogger(TheActMod.class.getSimpleName());

    public static void initialize()
    {
        BaseMod.subscribe(new TheActMod());
    }

    public static String makeID(String id)
    {
        return "theJungle:" + id;
    }

    public static String assetPath(String path)
    {
        return "theActAssets/" + path;
    }

    @Override
    public void receivePostInitialize()
    {
        // Settings panel
        ModPanel settingsPanel = new ModPanel();


        BaseMod.registerModBadge(ImageMaster.loadImage(assetPath("modBadge.png")), "MTS Anniversary Act", "Everyone", "TODO", settingsPanel);

        // Add events here
        BaseMod.addEvent(River.ID, River.class, Jungle.ID);
        BaseMod.addEvent(SneckoCultEvent.ID, SneckoCultEvent.class, Jungle.ID);
        BaseMod.addEvent(SneckoIdol.ID, SneckoIdol.class, Jungle.ID);
        BaseMod.addEvent(FauxPas.ID, FauxPas.class, Jungle.ID);
        BaseMod.addEvent(GremlinQuiz.ID, GremlinQuiz.class, Jungle.ID);
        BaseMod.addEvent(ExcessResources.ID, ExcessResources.class, Jungle.ID);
        BaseMod.addEvent(LostInTheJungle.ID, LostInTheJungle.class, Jungle.ID);
        BaseMod.addEvent(KidnappersEvent.ID, KidnappersEvent.class, Jungle.ID);
        BaseMod.addEvent(HappyBirthday.ID, HappyBirthday.class, Jungle.ID);
        BaseMod.addEvent(JungleGarden.ID, JungleGarden.class, Jungle.ID);
        BaseMod.addEvent(StalkMarket.ID, StalkMarket.class, Jungle.ID);
        BaseMod.addEvent(ShamanCauldron.ID, ShamanCauldron.class, Jungle.ID);


        BaseMod.addAct(TheCity.ID, new Jungle());

        // Weak Pool
        BaseMod.addMonster(JungleEncounterIDList.MUSHROOM_GANG_ENCOUNTER_ID, "Mushroom Gang" , () -> new MonsterGroup(
                new AbstractMonster[]{
                        new MushroomYandere(-385.0F, -15.0F),
                        new MushroomKuudere(-133.0F, 0.0F),
                        new MushroomGenki(125.0F, -30.0F)
                }), Jungle.ID, EnemyData.MonsterType.WEAK, 1.5F);
        BaseMod.addMonster(JungleEncounterIDList.FLAMEANGO_AND_BYRD_ENCOUNTER_ID, "Flameango and Byrd", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(50),
                        new Byrd(-305.0F, 110.0F)
                }), Jungle.ID, EnemyData.MonsterType.WEAK);
        BaseMod.addMonster(JungleEncounterIDList.GIANT_WRAT_ENCOUNTER_ID, () -> new GiantWrat(-85.0F, -15.0F), Jungle.ID, EnemyData.MonsterType.WEAK, 1.5F);
        BaseMod.addMonster(JungleEncounterIDList.FIVE_SPYDERS_ENCOUNTER_ID, "Spider Swarm", () -> new MonsterGroup(
                new AbstractMonster[]{
                		new HunterSpyder(-650.0F, 300.0F, 0),
                		new DefenderSpyder(-425.0F, 250.0F, 1),
                		new HunterSpyder(-200.0F, 220.0F, 2),
                		new DefenderSpyder(25.0F, 300.0F, 3),
                		new HunterSpyder(175.0F, 120.0F, 4)
                }), Jungle.ID, EnemyData.MonsterType.WEAK);
        BaseMod.addMonsterEncounter(Jungle.ID, new MonsterInfo(MonsterHelper.SHELL_PARASITE_ENC, 1F));

        // Strong Pool
        BaseMod.addMonster(JungleEncounterIDList.TWO_SENCKO_CULTISTS_ENCOUNTER_ID, "2 Snecko Cultists", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new SneckoCultist(-250, -20),
                        new SneckoCultist(80, 20, true)
                }), Jungle.ID, EnemyData.MonsterType.STRONG, 1.5F);
        BaseMod.addMonster(JungleEncounterIDList.TWO_JUNGLE_HUNTERS_ENCOUNTER_ID, "2 Jungle Hunters", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new JungleHunters(-385.0F, -15.0F),
                        new JungleHunters(150.0F, -30.0F)
                }), Jungle.ID, EnemyData.MonsterType.STRONG, 1.5F);
        BaseMod.addMonster(JungleEncounterIDList.SLIMY_TREE_VINES_ENCOUNTER_ID, SlimyTreeVines::new, Jungle.ID, EnemyData.MonsterType.STRONG, 1.5F);
        BaseMod.addMonster(JungleEncounterIDList.TWO_FLAMEANGOS_ENCOUNTER_ID, "2 Flameangos", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(-250),
                        new Flameango(80)
                }), Jungle.ID, EnemyData.MonsterType.STRONG);
        BaseMod.addMonster(JungleEncounterIDList.SNECKO_CULTIST_AND_TRAP_ENCOUNTER_ID, "Snecko Cultist and Trap", () -> new MonsterGroup(
                new AbstractMonster[]{
                        new SwingingAxe(-450.0F, 100.0F),
                        new SneckoCultist(120, 0)
                }), Jungle.ID, EnemyData.MonsterType.STRONG);
        BaseMod.addMonster(JungleEncounterIDList.LYON_ENCOUNTER_ID, Lyon::new, Jungle.ID, EnemyData.MonsterType.STRONG, 1.5F);

        // Elite Pool
        BaseMod.addMonster(JungleEncounterIDList.MAMA_SNECKO_ENCOUNTER_ID, MamaSnecko::new, Jungle.ID, EnemyData.MonsterType.ELITE);
        BaseMod.addMonster(JungleEncounterIDList.TWO_PHROGS_ENCOUNTER_ID, "Two Phrogs", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Phrog(-175,0, false),
                        new Phrog(175, 0, true)
                }), Jungle.ID, EnemyData.MonsterType.ELITE);
        BaseMod.addMonster(JungleEncounterIDList.CASSACARA_ENCOUNTER_ID, () -> new Cassacara(50.0F, 0.0F), Jungle.ID, EnemyData.MonsterType.ELITE);

        // Event Pool
        BaseMod.addMonster(JungleEncounterIDList.THREE_JUNGLE_HUNTERS_ENCOUNTER_ID, "3 Jungle Hunters", () -> new MonsterGroup(
                new AbstractMonster[] {
                        new JungleHunters(-385.0F, -15.0F),
                        new JungleHunters(-100.0F, 10.0F),
                        new JungleHunters(150.0F, -30.0F)
                }));

        // Potions
        BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
        BaseMod.addPotion(Antidote.class, Color.TAN, Color.WHITE, Color.GOLDENROD, Antidote.POTION_ID);
        BaseMod.addPotion(TwistedElixir.class, Color.OLIVE, Color.NAVY, Color.BLACK, TwistedElixir.POTION_ID);
        BaseMod.addPotion(HauntedGourd.class, Color.ORANGE, Color.GREEN, Color.WHITE, HauntedGourd.POTION_ID);
        BaseMod.addPotion(SneckoExtract.class, Color.BLACK, Color.BLACK, Color.BLACK, SneckoExtract.POTION_ID);
        BaseMod.addPotion(SpyderVenom.class, Color.LIME, Color.GREEN, Color.YELLOW, SpyderVenom.POTION_ID);

        // Add bosses here
        BaseMod.addBoss(Jungle.ID, TotemBoss.ID, TotemBoss::new, assetPath("images/map/totemBoss.png"), assetPath("images/map/totemBossOutline.png"));
        BaseMod.addBoss(Jungle.ID, SpyderBoss.ID, FunGuy::new, assetPath("images/map/spiderBoss.png"), assetPath("images/map/spiderBossOutline.png")); // A R T
        BaseMod.addBoss(Jungle.ID, FunGuy.ID, SpyderBoss::new, assetPath("images/map/mushroomBoss.png"), assetPath("images/map/mushroomBossOutline.png"));

        // Add sounds
        addSound(makeID("totemSmash"), assetPath("audio/sounds/totemSmash.ogg"));
        addSound(makeID("sneckoCultist1"), assetPath("audio/sounds/sneckoCultist1.ogg"));
        addSound(makeID("sneckoCultist2"), assetPath("audio/sounds/sneckoCultist2.ogg"));
    }

    @Override
    public void receiveEditCards()
    {
        BaseMod.addCard(new PetSnecko());
        BaseMod.addCard(new Gourd());
    }

    @Override
    public void receiveEditRelics()
    {
        //event relics
        BaseMod.addRelic(new PaperFaux(), RelicType.SHARED);
        BaseMod.addRelic(new WildMango(), RelicType.SHARED);
        BaseMod.addRelic(new WildStrawberry(), RelicType.SHARED);
        BaseMod.addRelic(new WildPear(), RelicType.SHARED);
        BaseMod.addRelic(new BirthdayCakeSlice(),  RelicType.SHARED);
        BaseMod.addRelic(new BirthdayCake(),  RelicType.SHARED);
        BaseMod.addRelic(new SneckoAutograph(), RelicType.SHARED);
        BaseMod.addRelic(new ParSnip(), RelicType.SHARED);
        BaseMod.addRelic(new Flamango(), RelicType.SHARED);
        BaseMod.addRelic(new ShellPeas(), RelicType.SHARED);
        BaseMod.addRelic(new Creamberry(), RelicType.SHARED);
        BaseMod.addRelic(new SpiritDisease(), RelicType.SHARED);
    }

    private String languageSupport()
    {
        switch (Settings.language) {
            case ZHS:
                return "zhs";
            case KOR:
                return "kor";
            default:
                return "eng";
        }
    }

    void loadLocKeywords(String language)
    {
        Gson gson = new Gson();
        String json = Gdx.files.internal(assetPath("localization/" + language + "/keywords.json")).readString(String.valueOf(StandardCharsets.UTF_8));
        Keyword[] keywords = gson.fromJson(json, Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveEditKeywords()
    {
        String language = languageSupport();

        // Load english first to avoid crashing if translation doesn't exist for something
        loadLocKeywords("eng");
        loadLocKeywords(language);
    }

    private void loadLocStrings(String language)
    {
        String path = "localization/" + language + "/";

        BaseMod.loadCustomStringsFile(EventStrings.class, assetPath(path + "events.json"));
        BaseMod.loadCustomStringsFile(UIStrings.class, assetPath(path + "ui.json"));
        BaseMod.loadCustomStringsFile(PotionStrings.class, assetPath(path + "potions.json"));
        BaseMod.loadCustomStringsFile(CardStrings.class, assetPath(path + "cards.json"));
        BaseMod.loadCustomStringsFile(MonsterStrings.class, assetPath(path + "monsters.json"));
        BaseMod.loadCustomStringsFile(PowerStrings.class, assetPath(path + "powers.json"));
        BaseMod.loadCustomStringsFile(RelicStrings.class, assetPath(path + "relics.json"));
        BaseMod.loadCustomStringsFile(ScoreBonusStrings.class, assetPath(path + "score_bonuses.json"));
    }

    @Override
    public void receiveEditStrings()
    {
        String language = languageSupport();

        // Load english first to avoid crashing if translation doesn't exist for something
        loadLocStrings("eng");
        loadLocStrings(language);
    }

    private static void addSound(String id, String path)
    {
        @SuppressWarnings("unchecked")
        HashMap<String,Sfx> map = (HashMap<String,Sfx>) ReflectionHacks.getPrivate(CardCrawlGame.sound, SoundMaster.class, "map");
        map.put(id, new Sfx(path, false));
    }

    @Override
    public void receivePostUpdate()
    {
        if (AbstractDungeon.player == null) {
            return;
        }
        if (AbstractDungeon.player.hasRelic(SneckoAutograph.ID)) {
            SneckoAutograph.iHatePostUpdate();
        }
    }
}
