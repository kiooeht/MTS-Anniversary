package theAct.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.actions.IncreaseRandomCardCostAction;
import theAct.powers.abstracts.Power;

public class WebbedPower extends Power {
	public static final String POWER_ID = TheActMod.makeID("Webbed");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = strings.NAME;
	private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);

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
		else
			this.description = strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2]; 
	}	
	
	@Override
	public void atStartOfTurnPostDraw() {
		AbstractDungeon.actionManager.addToBottom(new IncreaseRandomCardCostAction(amount));
	}
	

	
}