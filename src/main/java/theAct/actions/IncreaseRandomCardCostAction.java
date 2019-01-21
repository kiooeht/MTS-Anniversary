package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import theAct.powers.WebbedPower;

public class IncreaseRandomCardCostAction extends AbstractGameAction {

    public IncreaseRandomCardCostAction(int amt) {
    	this.amount = amt;
        this.duration = Settings.ACTION_DUR_FAST;
        this.actionType = AbstractGameAction.ActionType.WAIT;
    }

    @Override
    public void update() {
        if (this.isDone) return;
        this.isDone = true;
        
        CardGroup changeable = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);;
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
        	if(c.cost >= 0) {
        		changeable.addToRandomSpot(c);
        	}
        }
        for(int i = 0; i <amount; i++) {
	        if(changeable.size() > 0) {
		        final AbstractCard c = changeable.getRandomCard(true);
		        c.modifyCostForCombat(1);
		        c.superFlash();
		        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, null, WebbedPower.POWER_ID, 1));
	        }
        }
    }
}
