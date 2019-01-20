package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

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
		AbstractDungeon.actionManager.addToBottom(new FormationInitAction(this));
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =	strings.DESCRIPTIONS[0];
	}
	
	public void recalculateAmt() {
		amount = ((SpawnedSpyder)owner).owner.smallSpyderAmt + ((SpawnedSpyder)owner).owner.bigSpyderAmt;
		if(amount < 6 && owner.hasPower(InfiniteIntangiblePower.powerID)) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, InfiniteIntangiblePower.powerID));
		} else if(amount >= 6 && !owner.hasPower(InfiniteIntangiblePower.powerID)) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new InfiniteIntangiblePower(owner)));
		}
		updateDescription();
	}
}