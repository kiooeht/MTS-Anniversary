package theAct.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnCardDrawPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class WebbedPower extends Power implements OnCardDrawPower {
	public static final String POWER_ID = TheActMod.makeID("Webbed");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = strings.NAME;
	private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);
	
	private int cardAmount = 0;

	public WebbedPower(AbstractCreature owner, int amount) {
		this.ID = POWER_ID;
		this.name = strings.NAME;
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.setImage(IMG + "84.png", IMG + "32.png");
		this.updateDescription();
	}

	public void updateDescription() {
		if(amount == 1)
			this.description =	strings.DESCRIPTIONS[0];
		else if(cardAmount == 0)
			this.description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2]; 
		else if(cardAmount == 1)
			this.description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2] + strings.DESCRIPTIONS[3] + cardAmount + strings.DESCRIPTIONS[5];
		else
			this.description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2] + strings.DESCRIPTIONS[3] + cardAmount + strings.DESCRIPTIONS[4];
	}	
	
	 @Override
	    public void onCardDraw(AbstractCard c){
	            if (amount > 0) {
	                if ((c.cost >= 0)) {
	                    c.superFlash();
	                    c.modifyCostForCombat(1);;
	                    reducePower(1);
	                    cardAmount++;
	                    updateDescription();
	                }
	            }

	    }

	    @Override
	    public void reducePower(int stackAmount) {
	        if (this.amount - stackAmount <= 0) {
	            this.fontScale = 8.0F;
	            this.amount = 0;
	        } else {
	            this.fontScale = 8.0F;
	            this.amount -= stackAmount;
	        }

	    }
	    
	    @Override
	    public void atEndOfTurn(boolean isPlayer) {
	    	if(this.amount <= 0)
	            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, POWER_ID));
	    	else
	    		cardAmount = 0;
	    }
	

	
}