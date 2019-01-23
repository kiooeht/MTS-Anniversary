package theAct.powers;

import basemod.BaseMod;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.NonStackablePower;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class DigestPower extends Power implements NonStackablePower
{

	public static final String powerID = TheActMod.makeID("DigestPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	private AbstractCard card;
	private boolean justApplied;
	private boolean shouldGiveCardBack;

	public DigestPower(AbstractCreature owner, AbstractCard card, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("digestPower84.png", "digestPower32.png");
		this.ID = powerID;
		this.card = card;
		this.justApplied = true;
		this.shouldGiveCardBack = true;
		this.updateDescription();
	}

	public void updateDescription() {
		if (this.amount == 1) {
			this.description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1]
					+ FontHelper.colorString(card.name, "y") + strings.DESCRIPTIONS[3]
					+ FontHelper.colorString(card.name, "y") + strings.DESCRIPTIONS[4];
		}
		else {
			this.description = strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[2]
					+ FontHelper.colorString(card.name, "y") + strings.DESCRIPTIONS[3]
					+ FontHelper.colorString(card.name, "y") + strings.DESCRIPTIONS[4];
		}
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if(justApplied){
			this.justApplied = false;
			return;
		}

		AbstractDungeon.actionManager.addToBottom(new VFXAction(this.owner, new ExhaustCardEffect(this.card), 1.0f));
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if (info.type == DamageInfo.DamageType.NORMAL) {
			amount--;
			if (amount == 0) {
				returnCardToHand();
			}
			updateDescription();
		}
		return super.onAttacked(info, damageAmount);
	}

	@Override
	public void onDeath() {
		returnCardToHand();
	}

	private void returnCardToHand() {
		if (this.shouldGiveCardBack) {
			this.shouldGiveCardBack = false;
			if (AbstractDungeon.player.hand.size() < BaseMod.MAX_HAND_SIZE) {
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(card.makeSameInstanceOf()));
			} else {
				AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(card.makeSameInstanceOf(), 1));
			}
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
		}
	}
}
