package theAct.actions;

import com.evacipated.cardcrawl.mod.stslib.actions.tempHp.AddTemporaryHPAction;
import com.megacrit.cardcrawl.core.AbstractCreature;

public class InstantAddTemporaryHPAction extends AddTemporaryHPAction
{
    public InstantAddTemporaryHPAction(AbstractCreature target, AbstractCreature source, int amount)
    {
        super(target, source, amount);
    }

    public void update()
    {
        super.update();
        if (duration == 0.5f) {
            duration = 0.1f;
        }
    }
}