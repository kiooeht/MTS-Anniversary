package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;
import theAct.powers.SlothfulPower;

public class WildPear extends CustomRelic {
    public static final String ID = TheActMod.makeID("WildPear");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String [] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "theActAssets/images/relics/WildPear.png";

    private static final int HP_MANIPULATOR = 10;
    private static final int SLOTH_AMT = 1;

    public WildPear(){
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription(){
        return DESCRIPTIONS[0] + HP_MANIPULATOR + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SlothfulPower(AbstractDungeon.player, SLOTH_AMT), SLOTH_AMT));
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_MANIPULATOR, true);
    }

    @Override
    public AbstractRelic makeCopy(){
        return new WildPear();
    }
}
