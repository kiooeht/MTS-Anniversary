package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.DexterityPower;

public class GainDexBlockAction extends AbstractGameAction {
	private AbstractCreature owner;
	private float startingDuration;

	public GainDexBlockAction(AbstractCreature owner) {
		this.owner = owner;
		this.duration = Settings.ACTION_DUR_FAST;
		this.startingDuration = Settings.ACTION_DUR_FAST;
		this.actionType = ActionType.WAIT;
	}

	@Override
	public void update() {		
		if(this.duration == this.startingDuration){
			if(owner.hasPower(DexterityPower.POWER_ID) && owner.getPower(DexterityPower.POWER_ID).amount > 0)
				AbstractDungeon.actionManager.addToTop(new GainBlockAction(owner, owner, owner.getPower(DexterityPower.POWER_ID).amount));
		}
		this.tickDuration();
	}
}
