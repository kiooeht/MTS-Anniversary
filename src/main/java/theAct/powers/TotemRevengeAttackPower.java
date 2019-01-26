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

	private boolean spawnedIn;


	public TotemRevengeAttackPower(AbstractCreature owner, boolean spawnedIn) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.ID = powerID;
		this.spawnedIn = spawnedIn;
		this.updateDescription();


		if (spawnedIn) {
			this.setImage("immunityPowerInactive84.png", "immunityPowerInactive32.png");
		} else {
			this.setImage("immunityPower84.png", "immunityPower32.png");

		}
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		super.onUseCard(card, action);

		if (this.owner instanceof AbstractTotemSpawn && card.target == AbstractCard.CardTarget.ALL_ENEMY) {
			if (!spawnedIn) {
				((AbstractTotemSpawn) this.owner).totemAttack();
				flash();
			}
	}
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		super.atEndOfTurn(isPlayer);
		if (spawnedIn) {
			this.spawnedIn = false;
			this.setImage("immunityPower84.png", "immunityPower32.png");
			flash();
			this.updateDescription();
		}
	}

	public void updateDescription() {
		if (spawnedIn) {
			this.description = strings.DESCRIPTIONS[1];
		} else {
			this.description = strings.DESCRIPTIONS[0];
		}
	}


}