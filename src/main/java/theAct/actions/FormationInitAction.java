package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.core.Settings;

import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.powers.FormationPower;

public class FormationInitAction extends AbstractGameAction {
	private FormationPower pow;
	private SpyderBoss boss;
	private float startingDuration;

	public FormationInitAction(SpyderBoss boss, FormationPower pow) {
		this.pow = pow;
		this.boss = boss;
		this.duration = Settings.ACTION_DUR_FAST;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.WAIT;
	}

	@Override
	public void update() {		
		if(this.duration == this.startingDuration){
			pow.stackAmt(boss.smallSpyderAmt + boss.bigSpyderAmt);
		}
		this.tickDuration();
	}
}
