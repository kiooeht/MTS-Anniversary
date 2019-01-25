package theAct.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;
import theAct.monsters.TotemBoss.AbstractTotemSpawn;
import theAct.powers.abstracts.Power;

public class TotemRevengeAttackPower extends Power {

	public static final String powerID = TheActMod.makeID("TotemRevengeAttackPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public TotemRevengeAttackPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("immunityPower84.png", "immunityPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		if (this.owner instanceof AbstractTotemSpawn && card.target == AbstractCard.CardTarget.ALL_ENEMY) {
			((AbstractTotemSpawn) this.owner).totemAttack();
			flash();
		}
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}


}