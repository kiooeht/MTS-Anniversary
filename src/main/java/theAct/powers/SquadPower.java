package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.actions.SquadInitAction;
import theAct.monsters.SpyderBoss.SpawnedSpyder;
import theAct.powers.abstracts.Power;

public class SquadPower extends Power {
	public static final String POWER_ID = TheActMod.makeID("Squad");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = strings.NAME;

	public SquadPower(AbstractCreature owner) {
		this.ID = POWER_ID;
		this.name = strings.NAME;
		this.owner = owner;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.setImage(NAME + "84.png", NAME + "32.png");
		AbstractDungeon.actionManager.addToBottom(new SquadInitAction(((SpawnedSpyder)owner).owner, owner));
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}

	public void spyderDeath() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -1), -1, true));
	}
	
	public void spyderSpawn() {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1), 1, true));
	}
}