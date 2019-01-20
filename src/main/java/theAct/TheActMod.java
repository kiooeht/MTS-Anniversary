package theAct;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.abstracts.CustomSavable;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.city.Byrd;
import com.megacrit.cardcrawl.monsters.city.Snecko;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.cards.PetSnecko;
import theAct.cards.colorless.Gourd;
import theAct.cards.curses.EternalShame;
import theAct.dungeons.Jungle;
import theAct.events.River;
import theAct.events.SneckoCultEvent;
import theAct.monsters.*;
import theAct.monsters.TotemBoss.TotemBoss;
import theAct.events.SneckoIdol;
import theAct.monsters.MUSHROOMPOWER.MushroomGenki;
import theAct.monsters.MUSHROOMPOWER.MushroomKuudere;
import theAct.monsters.MUSHROOMPOWER.MushroomYandere;
import theAct.monsters.SpyderBoss.FatSpyder;
import theAct.monsters.SpyderBoss.SneakySpyder;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.monsters.SpyderBoss.WebberSpyder;
import theAct.patches.GetDungeonPatches;
import theAct.potions.*;
import theAct.relics.SneckoAutograph;
import theAct.events.*;
import theAct.monsters.*;
import theAct.monsters.TotemBoss.TotemBoss;
import theAct.relics.*;

import java.nio.charset.StandardCharsets;

@SpireInitializer
public class TheActMod implements
        PostInitializeSubscriber,
        EditKeywordsSubscriber,
        EditCardsSubscriber,
        EditStringsSubscriber,
        EditRelicsSubscriber,
        CustomSavable<Boolean>,
        PostUpdateSubscriber
{


    public static final Logger logger = LogManager.getLogger(TheActMod.class.getSimpleName());

    public static boolean wentToTheJungle = false;

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

        // Add monsters here


        //Normal Enemies
        BaseMod.addMonster(SilentTribesmen.ENCOUNTER_ID, SilentTribesmen.NAME, () -> new MonsterGroup(
            new AbstractMonster[] { new SilentTribesmen(-385.0F, 0.0F), new SilentTribesmen(90.0F, 0.0F)}));
        BaseMod.addMonster(SilentTribesmen.EVENT_ID, SilentTribesmen.NAME, () -> new MonsterGroup(
                new AbstractMonster[] { new SilentTribesmen(-385.0F, -15.0F), new SilentTribesmen(-133.0F, 0.0F), new SilentTribesmen(125.0F, -30.0F)}));
        BaseMod.addMonster(MushroomYandere.ENCOUNTER_ID, () -> new MonsterGroup(
                new AbstractMonster[]{
                        new MushroomYandere(-385.0F, -15.0F),
                        new MushroomKuudere(-133.0F, 0.0F),
                        new MushroomGenki(125.0F, -30.0F)
                }));
        BaseMod.addMonster(makeID("Silent_and_trap"), () -> new MonsterGroup(
                new AbstractMonster[]{
                		new SwingingAxe(-350.0F, 100.0F),
                		new SneakySpyder(-223.0F, 330.0F),
                        new WebberSpyder(-179.0F, -10.0F, true),
                        new SilentTribesmen(90.0F, 0.0F)
                }));
        BaseMod.addMonster(makeID("6_Spyders"), () -> new MonsterGroup(
                new AbstractMonster[]{
                		new SneakySpyder(-849.0F, 360.0F),
                		new SneakySpyder(-473.0F, 420.0F),
                		new FatSpyder(-72.0F, 288.0F),
                		new FatSpyder(72.0F, 328.0F),
                        new WebberSpyder(-199.0F, -10.0F, false),
                        new WebberSpyder(39.0F, 10.0F, true)
                }));
        BaseMod.addMonster(makeID("Flameango_and_Byrd"), () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(50),
                        new Byrd(-175.0F, 150.0F)
                    }));
        BaseMod.addMonster(makeID("2_Flameangoes"), () -> new MonsterGroup(
                new AbstractMonster[] {
                        new Flameango(80),
                        new Flameango(-200)
                    }));
        BaseMod.addMonster(makeID("2_Snecko_Cultists"), () -> new MonsterGroup(
                new AbstractMonster[] {
                        new SneckoCultist(80, 20),
                        new SneckoCultist(-200, -20)
                    }));
        BaseMod.addMonster(SlimyTreeVines.ID, () -> new SlimyTreeVines());
        BaseMod.addMonster(FunGuy.ID, FunGuy::new);
        BaseMod.addMonster(SwingingAxe.ID, () -> {return new SwingingAxe();});
        BaseMod.addMonster(SlimyTreeVines.ID, () -> new SlimyTreeVines());
        BaseMod.addMonster(MamaSnecko.ID,() -> new MamaSnecko());
        BaseMod.addMonster(SwingingAxe.ID, () -> new SwingingAxe());
        BaseMod.addMonster(Lyon.ID, Lyon::new);
        //Elites
        BaseMod.addMonster(Cassacara.ID, () -> new Cassacara());
        BaseMod.addMonster(Phrog.ID,() -> new MonsterGroup(
            new AbstractMonster[] {
                new Phrog(-175,0, false),
                new Phrog(175, 0, true)
            }));
        BaseMod.addMonster(SneckoCultist.ID,() -> new MonsterGroup(
                new AbstractMonster[] {
                        new SneckoCultist(-175,15),
                        new SneckoCultist(175, -15)
                }));
        //Bosses
        BaseMod.addMonster(TotemBoss.ID, TotemBoss::new);
        BaseMod.addBoss(Jungle.ID, TotemBoss.ID, assetPath("images/map/totemBoss.png"), assetPath("images/map/totemBossOutline.png"));

        BaseMod.addMonster(SpyderBoss.ID, SpyderBoss::new);
        BaseMod.addBoss(Jungle.ID, SpyderBoss.ID, assetPath("images/map/totemBoss.png"), assetPath("images/map/totemBossOutline.png")); // A R T

        //Potions
        BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
        BaseMod.addPotion(Antidote.class, Color.TAN, Color.WHITE, Color.GOLDENROD, Antidote.POTION_ID);
        BaseMod.addPotion(TwistedElixir.class, Color.OLIVE, Color.NAVY, Color.BLACK, TwistedElixir.POTION_ID);
        BaseMod.addPotion(HauntedGourd.class, Color.ORANGE, Color.GREEN, Color.WHITE, HauntedGourd.POTION_ID);
        BaseMod.addPotion(SneckoExtract.class, Color.BLACK, Color.BLACK, Color.BLACK, SneckoExtract.POTION_ID);
        BaseMod.addPotion(SpyderVenom.class, Color.LIME, Color.GREEN, Color.YELLOW, SpyderVenom.POTION_ID);

        // Add Encounters here
        BaseMod.addBoss(Jungle.ID, FunGuy.ID, "theActAssets/images/monsters/placeholderBossIcon.png", "theActAssets/images/monsters/placeholderBossOutline.png");

        // Add dungeon
        GetDungeonPatches.addDungeon(Jungle.ID, Jungle.builder());
        GetDungeonPatches.addNextDungeon(Jungle.ID, TheBeyond.ID);

        //savable boolean
        BaseMod.addSaveField("wentToTheJungle", this);
    }

    @Override
    public void receiveEditCards() {
        BaseMod.addCard(new PetSnecko());
        BaseMod.addCard(new EternalShame());
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

    }

    @Override
    public void receiveEditKeywords()
    {
        String language = "eng";
        // TODO: Language support

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
    public void receiveEditStrings()
    {
        String language = "eng";
        // TODO: Language support
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
    public Boolean onSave() {
        logger.info("Saving wentToTheJungle boolean: " + wentToTheJungle);
        return wentToTheJungle;
    }

    @Override
    public void onLoad(Boolean loadedBoolean) {
        if (loadedBoolean != null) {
            wentToTheJungle = loadedBoolean;
        } else {
            wentToTheJungle = false;
        }
        logger.info("Loading wentToTheJungle boolean: " + wentToTheJungle);
    }

    @Override
    public void receivePostUpdate() {
        if (AbstractDungeon.player == null) {
            return;
        }
        if (AbstractDungeon.player.hasRelic(SneckoAutograph.ID)) {
            SneckoAutograph.iHatePostUpdate();
        }
    }
}
