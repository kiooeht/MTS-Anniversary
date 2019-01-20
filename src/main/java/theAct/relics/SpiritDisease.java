package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.SpiritPoop;
import theAct.TheActMod;

public class SpiritDisease extends CustomRelic
{

    public static final String ID = TheActMod.makeID("SpiritDisease");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    // Get object containing the strings that are displayed in the game.
    public static final String NAME = relicStrings.NAME;
    public static final String [] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "theActAssets/images/relics/SpiritDisease.png";

    public SpiritDisease()
    {
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 1;
    }

    @Override
    public void onChestOpenAfter(boolean bossChest)
    {
        if(!bossChest && this.counter > 0) {
            flash();
            AbstractDungeon.getCurrRoom().removeOneRelicFromRewards();
            AbstractDungeon.getCurrRoom().addRelicToRewards(new SpiritPoop());
            setCounter(0);
        }
    }

    @Override
    public void setCounter(int counter)
    {
        this.counter = counter;
        if(counter == 0) {
            this.img = ImageMaster.loadImage("theActAssets/images/relics/SpiritDiseaseUsed.png");
            usedUp();
            this.counter = -2;
        }
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new SpiritDisease();
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0];
    }
}
