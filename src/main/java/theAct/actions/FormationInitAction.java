package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;

import theAct.powers.FormationPower;

public class FormationInitAction extends AbstractGameAction {
	private FormationPower pow;

	public FormationInitAction(FormationPower pow) {
		this.pow = pow;
		this.actionType = ActionType.WAIT;
	}

	@Override
	public void update() {			
		pow.recalculateAmt();
		this.isDone = true;
	}
}
