package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddActionLaterAction extends AbstractGameAction { // This is the third time I've copy-pasted this, might as well add it to stslib or something. - BD

    private AbstractGameAction actionToQueue;
    private int actionPriority;

    public AddActionLaterAction(AbstractGameAction actionToQueue, int actionPriority) {
        this.actionToQueue = actionToQueue;
        this.actionPriority = actionPriority;
    }

    public void update() {
        if (this.actionPriority > 0) {
            AbstractDungeon.actionManager.addToBottom(new AddActionLaterAction(this.actionToQueue, this.actionPriority - 1));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(this.actionToQueue);
        }
        this.isDone = true;
    }
}
