package theAct.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.HealthBarRenderPower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.monsters.TotemBoss.AbstractTotemSpawn;
import theAct.powers.abstracts.Power;
import theAct.vfx.ImmunityShieldEffect;

public class TotemBossImmunityPower extends Power implements HealthBarRenderPower {

	public static final String powerID = TheActMod.makeID("TotemBossImmunityPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	public boolean unremovable;

	public TotemBossImmunityPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.DEBUFF;
		this.name = strings.NAME;
		this.setImage("protectionPower84.png", "protectionPower32.png");
		this.ID = powerID;
		this.updateDescription();
		this.unremovable = true;
		this.amount = 1;
	}

	@Override
	public int onLoseHp(int damageAmount) {
		AbstractDungeon.actionManager.addToTop(new VFXAction(this.owner, new ImmunityShieldEffect(this.owner.hb.cX,this.owner.hb.cY),0.10F));
		return 0;
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		AbstractDungeon.actionManager.addToTop(new VFXAction(this.owner, new ImmunityShieldEffect(this.owner.hb.cX,this.owner.hb.cY),0.10F));
		return super.onAttacked(info, 0);
	}

	@Override
	public void reducePower(int reduceAmount) {
		if (this.amount - reduceAmount <= 0) {
			this.fontScale = 8.0F;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		} else {
			this.fontScale = 8.0F;
			this.amount -= reduceAmount;
		}
	}

	@Override
	public void onRemove()
	{
		if (this.amount > 1 && !this.unremovable) {
			// Add a copy, only one will be removed
			owner.powers.add(0, this);
			// Cancel the removal text effect
			AbstractDungeon.effectList.remove(AbstractDungeon.effectList.size() - 1);
		}
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

	@Override
	public int getHealthBarAmount() {
		return this.owner.currentHealth;
	}

	@Override
	public Color getColor() {
		return Color.ORANGE;
	}

}