package theAct.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import com.megacrit.cardcrawl.vfx.combat.ExplosionSmallEffect;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class BetterExplosivePower extends Power {
	public static final String NAME = "Explosive";
	public static final String powerID = TheActMod.makeID(NAME);
	
	public int damage;

	public BetterExplosivePower(AbstractCreature owner, int timer, int damage) {
		this.owner = owner;
		this.amount = timer;
		this.damage = damage;
		this.type = PowerType.BUFF;
		this.name = ExplosivePower.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		this.updateDescription();
	}
    
    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = ExplosivePower.DESCRIPTIONS[3] + damage + ExplosivePower.DESCRIPTIONS[2];
        }
        else {
            this.description = ExplosivePower.DESCRIPTIONS[0] + this.amount + ExplosivePower.DESCRIPTIONS[1] + damage + ExplosivePower.DESCRIPTIONS[2];
        }
    }
    
    @Override
    public void duringTurn() {
        if (this.amount == 1 && !this.owner.isDying) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new ExplosionSmallEffect(this.owner.hb.cX, this.owner.hb.cY), 0.1f));
            AbstractDungeon.actionManager.addToBottom(new SuicideAction((AbstractMonster)this.owner));
            final DamageInfo damageInfo = new DamageInfo(this.owner, this.damage, DamageInfo.DamageType.THORNS);
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damageInfo, AbstractGameAction.AttackEffect.FIRE, true));
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, BetterExplosivePower.powerID, 1));
            this.updateDescription();
        }
    }

}