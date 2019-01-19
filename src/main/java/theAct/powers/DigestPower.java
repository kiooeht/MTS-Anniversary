package theAct.powers;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class DigestPower extends Power {

	public static final String powerID = TheActMod.makeID("DigestPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

	private AbstractCard card;

	public DigestPower(AbstractCreature owner, AbstractCard card, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("digestPower84.png", "digestPower32.png");
		this.ID = powerID;
		this.card = card;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0] +
			card.name +
			strings.DESCRIPTIONS[1] +
			amount +
			strings.DESCRIPTIONS[2];
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		amount--;
		if(amount <= 0) {
			AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(this.card.makeSameInstanceOf()));
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		}
		updateDescription();
		return super.onAttacked(info, damageAmount);
	}
}