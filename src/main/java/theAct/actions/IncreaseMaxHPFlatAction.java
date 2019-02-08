//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.actions;

import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class IncreaseMaxHPFlatAction extends AbstractGameAction {
    private boolean showEffect;
    private int increaseAmount;

    public IncreaseMaxHPFlatAction(AbstractMonster m, int increaseAmount, boolean showEffect) {
        if (Settings.FAST_MODE) {
            this.startDuration = Settings.ACTION_DUR_XFAST;
        } else {
            this.startDuration = Settings.ACTION_DUR_FAST;
        }

        this.duration = this.startDuration;
        this.showEffect = showEffect;
        this.increaseAmount = increaseAmount;
        this.target = m;
    }

    public void update() {
        if (this.duration == this.startDuration) {
            this.target.increaseMaxHp(this.increaseAmount, this.showEffect);
        }

        this.tickDuration();
    }
}
