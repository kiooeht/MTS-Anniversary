package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class WildStrawberry extends CustomRelic {
    public static final String ID = TheActMod.makeID("WildStrawberry");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String [] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    public static final String IMG_PATH = "theActAssets/images/relics/WildStrawberry.png";

    private static final int HP_MANIPULATOR = 7;
    private static final int HP_LOSS_AMT = HP_MANIPULATOR * 2;

    public WildStrawberry(){
        super(ID, ImageMaster.loadImage(IMG_PATH), RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription(){
        return DESCRIPTIONS[0] + HP_MANIPULATOR + DESCRIPTIONS[1] + HP_LOSS_AMT + DESCRIPTIONS[2];
    }

    @Override
    public void onEquip() {
        AbstractDungeon.player.increaseMaxHp(HP_MANIPULATOR, true);
        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, HP_LOSS_AMT, DamageInfo.DamageType.HP_LOSS));
    }

    @Override
    public AbstractRelic makeCopy(){
        return new WildStrawberry();
    }
}
