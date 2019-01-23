package theAct.actions;

import com.evacipated.cardcrawl.mod.stslib.patches.core.AbstractCreature.TempHPField;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;

public class InstantAddTemporaryHPAction extends AbstractGameAction { // WHY IS THE OTHER ONE SO LONG

    public InstantAddTemporaryHPAction(AbstractCreature target, AbstractCreature source, int amount) {
        this.setValues(target, source, amount);
        this.actionType = ActionType.HEAL;
    }

    public void update() {
        TempHPField.tempHp.set(this.target, (Integer)TempHPField.tempHp.get(this.target) + this.amount);
        if (this.amount > 0) {
            AbstractDungeon.effectsQueue.add(new HealEffect(this.target.hb.cX - this.target.animX, this.target.hb.cY, this.amount));
            this.target.healthBarUpdatedEvent();
        }
        this.isDone = true;
    }
}