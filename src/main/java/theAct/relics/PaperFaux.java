package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class PaperFaux extends CustomRelic {

    public static final String ID = TheActMod.makeID("PaperFaux");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "theActAssets/images/relics/PaperFaux.png";
    public static final String OUTLINE_PATH = "theActAssets/images/relics/PaperFauxOutline.png";
    public static final int EXTRA_AMOUNT = 1;

    public PaperFaux() { //Relic effects coded in PaperFauxApplyPowerActionPatch
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PaperFaux();
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + EXTRA_AMOUNT + DESCRIPTIONS[1];
    }
}
