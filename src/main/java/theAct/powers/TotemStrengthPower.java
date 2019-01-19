package theAct.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class TotemStrengthPower extends Power {

	public static final String powerID = TheActMod.makeID("TotemStrengthPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public TotemStrengthPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("digestPower84.png", "digestPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

}