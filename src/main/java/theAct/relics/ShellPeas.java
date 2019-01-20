package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class ShellPeas extends CustomRelic {

    public static final String ID = TheActMod.makeID("ShellPeas");
    // TODO: Add images
    public static final Texture IMAGE_PATH = null;
    public static final Texture IMAGE_OUTLINE_PATH = null;
    private int TEMPORARY_HP_PER_HIT = 2;

    public ShellPeas() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0] + TEMPORARY_HP_PER_HIT + DESCRIPTIONS[1];
    }

    public int onAttacked(DamageInfo info, int damageAmount) {
        flash();
        AbstractDungeon.actionManager.addToTop(new AddTemporaryHPAction(AbstractDungeon.player, AbstractDungeon.player, TEMPORARY_HP_PER_HIT));
        return damageAmount;
    }

    public AbstractRelic makeCopy() {
        return new ShellPeas();
    }
}
