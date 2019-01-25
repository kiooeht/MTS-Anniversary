package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theAct.TheActMod;
import theAct.monsters.TotemBoss.AbstractTotemSpawn;
import theAct.powers.abstracts.Power;

public class MamaSneckoRevengePower extends Power {

	public static final String powerID = TheActMod.makeID("MamaSneckoRevengePower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public MamaSneckoRevengePower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("totemRevengePower84.png", "totemRevengePower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		super.atEndOfTurn(isPlayer);
		if (this.owner.hasPower(StrengthPower.POWER_ID)){
			if (this.owner.getPower(StrengthPower.POWER_ID).amount > 0){
				AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner,this.owner,StrengthPower.POWER_ID,2));
			}
		}

	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

}