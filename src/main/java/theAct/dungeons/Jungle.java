package theAct.dungeons;

import basemod.customacts.CustomDungeon;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import theAct.TheActMod;
import theAct.monsters.JungleEncounterIDList;
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
        super(new TheJungleScene(), NAME, ID);
        setMainMusic(TheActMod.assetPath("audio/music/jungle_main.ogg"));

        addTempMusic("MUSHROOMGANG", TheActMod.assetPath("audio/music/boss_mushroom.ogg"));
        addTempMusic("BOSSSPIDER", TheActMod.assetPath("audio/music/boss_spider.ogg"));
        addTempMusic("BOSSTOTEM", TheActMod.assetPath("audio/music/boss_totem.ogg"));

        addTempMusic("SNECKOIDOL", TheActMod.assetPath("audio/music/snecko_idol.ogg"));

        addTempMusic("JUNGLEELITE", TheActMod.assetPath("audio/music/jungle_elite.ogg"));
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
