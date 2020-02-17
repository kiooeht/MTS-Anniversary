package theAct;

import actlikeit.dungeons.CustomDungeon;
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
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.cards.PetSnecko;
import theAct.cards.colorless.Gourd;
import theAct.dungeons.Jungle;
import theAct.events.*;
import theAct.monsters.JungleEncounterIDList;
import theAct.monsters.JungleHunters;
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


        CustomDungeon.addAct(CustomDungeon.THECITY, new Jungle());

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
        if(!language.equals("eng")) {
            loadLocKeywords(language);
        }
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
        if(!language.equals("eng")) {
            loadLocStrings(language);
        }
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
