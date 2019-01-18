package theAct.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theAct.TheActMod;

public class LickPower extends AbstractPower {

	public static final String powerID = TheActMod.makeID("LickPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public LickPower() {
		this.owner = null;
		this.amount = 0;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		//description
	}
}