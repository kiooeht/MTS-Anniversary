package theAct.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;

import theAct.TheActMod;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Bright;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Clouding;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Clumping;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Leeching;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Toxin;
import theAct.powers.abstracts.Power;

public class FungalInfectionPower extends Power {

	public static final String powerID = TheActMod.makeID("FungalInfectionPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public FungalInfectionPower(AbstractCreature owner, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.DEBUFF;
		this.isTurnBased = true;
		this.name = strings.NAME;
        this.loadRegion("sporeCloud");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
	}

	@Override
	public void atEndOfRound() {
		AbstractCard c;
		switch (AbstractDungeon.miscRng.random(5)) {
		case 0:
			c = new SS_Clouding();
			break;
		case 1:
			c = new SS_Bright();
			break;
		case 2:
			c = new SS_Clumping();
			break;
		case 3:
			c = new SS_Leeching();
			break;
		default:
			c = new SS_Toxin();//this one is slightly more common
			break;
		}
		
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, false, false));
		if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this.powerID));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, this.powerID, 1));
        }
	}
}
