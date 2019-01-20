package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.monsters.SpyderBoss.SpyderBoss;

public class ShyInitAction extends AbstractGameAction {
	private AbstractCreature owner;
	private SpyderBoss boss;
	private float startingDuration;

	public ShyInitAction(SpyderBoss boss, AbstractCreature owner) {
		this.owner = owner;
		this.boss = boss;
		this.duration = Settings.ACTION_DUR_FAST;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.WAIT;
	}

	@Override
	public void update() {		
		if(this.duration == this.startingDuration){
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new StrengthPower(owner, -2*(boss.smallSpyderAmt + boss.bigSpyderAmt)), -2*(boss.smallSpyderAmt + boss.bigSpyderAmt)));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DexterityPower(owner, 2*(boss.smallSpyderAmt + boss.bigSpyderAmt)), 2*(boss.smallSpyderAmt + boss.bigSpyderAmt)));
		}
		this.tickDuration();
	}
}
