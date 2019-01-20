package theAct.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.ShieldParticleEffect;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;
import theAct.vfx.ImmunityShieldEffect;

public class TotemHealthLinkPower extends Power {

	public static final String powerID = TheActMod.makeID("TotemHealthLinkPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);


	public TotemHealthLinkPower(AbstractCreature owner, int stackCount) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.amount = stackCount;
		this.setImage("immunityPower84.png", "immunityPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
	}


}