package theAct.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class TotemBossImmunityPower extends Power implements HealthBarRenderPower {

	public static final String powerID = TheActMod.makeID("TotemBossImmunityPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public TotemBossImmunityPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("immunityPower84.png", "immunityPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	@Override
	public int onLoseHp(int damageAmount) {
		return 0;
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		return super.onAttacked(info, 0);
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

	@Override
	public int getHealthBarAmount() {
		return this.amount;
	}

	@Override
	public Color getColor() {
		return Color.ORANGE;
	}

}