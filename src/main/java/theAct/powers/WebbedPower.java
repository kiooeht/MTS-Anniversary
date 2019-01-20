package theAct.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.actions.IncreaseRandomCardCostAction;
import theAct.powers.abstracts.Power;

public class WebbedPower extends Power {
	public static final String NAME = "Webbed";
	public static final String powerID = TheActMod.makeID(NAME);
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(NAME);

	public WebbedPower(AbstractCreature owner, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.name = strings.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		if(amount == 1)
			this.description =	strings.DESCRIPTIONS[0];
		else
			this.description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2]; 
	}	
	
	@Override
	public void atStartOfTurnPostDraw() {
		AbstractDungeon.actionManager.addToBottom(new IncreaseRandomCardCostAction(amount));
	}
	

	
}