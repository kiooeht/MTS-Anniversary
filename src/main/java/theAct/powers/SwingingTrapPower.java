package theAct.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;

import theAct.TheActMod;

public class SwingingTrapPower extends AbstractPower {
	public static final String powerID = TheActMod.makeID("SwingingTrapPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
	private int phase;
	private int thornsAmt;
	public SwingingTrapPower(AbstractCreature owner, int thornsAmt, int phase) {
		this.owner = owner;
		this.amount = 0;
		this.phase = phase - 1;
		this.thornsAmt = thornsAmt;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.ID = powerID;
		this.cyclePhase();
		this.updateDescription();
	}
	public boolean isAttacking() {
		return this.phase == 0;
	}
    @Override
	public void updateDescription() {
		this.description = DESCRIPTIONS[1];
		if (this.phase == 0) {
			this.description = this.description + DESCRIPTIONS[2];
			this.loadRegion("strength");
		}
		else if (this.phase == 1) {
			this.description = this.description + DESCRIPTIONS[3] + this.thornsAmt + DESCRIPTIONS[4];
			this.loadRegion("thorns");
		} else if (this.phase == 2) {
			this.description = this.description + DESCRIPTIONS[5];
			this.loadRegion("weak");
		}
		this.description = this.description + DESCRIPTIONS[0];
	}
	private void cyclePhase() {
		this.phase++;
		if (this.phase > 2) {
			this.phase = 0;
		}
		this.amount = (this.phase == 1) ? this.thornsAmt : 0;
		this.updateDescription();
	}
	
    @Override
    public void onUseCard(final AbstractCard card, final UseCardAction action) {
        this.flash();
        if (this.phase == 1 && card.type == AbstractCard.CardType.ATTACK) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(this.owner, this.amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        }
        this.cyclePhase();
    }
    @Override
    public float atDamageGive(final float damage, final DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.NORMAL) {
            return damage;
        }
        if (this.phase != 0) {
        	return 0f;
        }
        return damage;
    }
}
