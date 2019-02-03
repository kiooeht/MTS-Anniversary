package theAct.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
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
import theAct.cards.fungalobungalofunguyfuntimes.SS_Sloth;
import theAct.cards.fungalobungalofunguyfuntimes.SS_Toxin;
import theAct.powers.abstracts.Power;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PuffballPower extends Power {

	public static final String powerID = TheActMod.makeID("PuffballPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public PuffballPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
        this.loadRegion("sporeCloud");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		super.onAttack(info, damageAmount, target);
		AbstractCard c;

		switch (AbstractDungeon.miscRng.random(3)) {
		case 0:
			c = new SS_Clouding(); // Get 1 Weak
			break;
		case 1:
			c = new SS_Leeching(); // Heals Boss 5(8) HP
			break;
		default:
			c = new SS_Toxin(); // Does 2(4) damage
			break;
		}
		AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(c, 1, true, true));
	}
}
