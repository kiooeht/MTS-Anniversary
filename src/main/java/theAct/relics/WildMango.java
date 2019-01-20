package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.powers.SlowPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class WildMango extends CustomRelic {
    public static final String ID = TheActMod.makeID("WildMango");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String [] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "theActAssets/images/relics/WildMango.png";

    private static final int HP_MANIPULATOR = 14;
    private static final int SLOW = 1;

    public WildMango(){
        super(ID,
                ImageMaster.loadImage(IMG_PATH),
                RelicTier.SPECIAL,
                LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription(){
        return DESCRIPTIONS[0] + HP_MANIPULATOR + DESCRIPTIONS[1];
    }

    @Override
    public void atBattleStart() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new SlowPower(AbstractDungeon.player, SLOW), SLOW));
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_MANIPULATOR, true);
    }

    @Override
    public AbstractRelic makeCopy(){
        return new WildMango();
    }
}
