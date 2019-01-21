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
	public static final String POWER_ID = TheActMod.makeID("Formation");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = strings.NAME;
	private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);

	public FormationPower(AbstractCreature owner) {
		this.ID = POWER_ID;
		this.name = strings.NAME;
		this.owner = owner;
		this.amount = 0;
		this.type = PowerType.BUFF;
		this.setImage(IMG + "84.png", IMG + "32.png");
		AbstractDungeon.actionManager.addToBottom(new FormationInitAction(this));
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0];
	}
	
	public void recalculateAmt() {
		amount = ((SpawnedSpyder)owner).owner.smallSpyderAmt + ((SpawnedSpyder)owner).owner.bigSpyderAmt;
		if (amount < 6 && owner.hasPower(InfiniteIntangiblePower.POWER_ID)) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, InfiniteIntangiblePower.POWER_ID));
		} else if (amount >= 6 && !owner.hasPower(InfiniteIntangiblePower.POWER_ID)) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new InfiniteIntangiblePower(owner)));
		}
		updateDescription();
	}
}