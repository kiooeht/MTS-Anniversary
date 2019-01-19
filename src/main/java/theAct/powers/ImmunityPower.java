package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class ImmunityPower extends Power {

	public static final String powerID = TheActMod.makeID("ImmunityPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public ImmunityPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("digestPower84.png", "digestPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public int onLoseHp(int damageAmount) {
		return 0;
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

}