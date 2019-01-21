package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FireBreathingPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class Flamango extends CustomRelic {

    public static final String ID = TheActMod.makeID("Flamango");
    // TODO: Add images
    public static final Texture IMAGE_PATH = null;
    public static final Texture IMAGE_OUTLINE_PATH = null;
    private int DAMAGE_PER_ATTACK = 2;

    public Flamango() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + DAMAGE_PER_ATTACK + DESCRIPTIONS[1];
    }

    public void atBattleStart() {
        flash();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new FireBreathingPower(AbstractDungeon.player, DAMAGE_PER_ATTACK), DAMAGE_PER_ATTACK));
        AbstractDungeon.actionManager.addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
    }

    public AbstractRelic makeCopy() {
        return new Flamango();
    }
}
