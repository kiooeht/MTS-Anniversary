package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import theAct.TheActMod;
import theAct.actions.FormationInitAction;
import theAct.monsters.SpyderBoss.SpawnedSpyder;
import theAct.powers.abstracts.Power;

public class FormationPower extends Power {
	public static final String NAME = "Formation";
	public static final String powerID = TheActMod.makeID(NAME);
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(NAME);

	public FormationPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = 0;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((SpawnedSpyder)owner).owner, this));
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =	strings.DESCRIPTIONS[0];
	}

	public void spyderDeath() {
		amount--;
		if(amount == 5)
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, IntangiblePower.POWER_ID));
		updateDescription();
	}
	
	public void spyderSpawn() {
		amount++;
		if(amount == 6)
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new IntangiblePower(owner, 99), 99));
		updateDescription();
	}
	
	public void stackAmt(int amt) {
		for(int i = 0; i < amt; i++)
			spyderSpawn();
		for(int i = 0; i > amt; i--)
			spyderDeath();
	}
}