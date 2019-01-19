package theAct.powers;

import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ExhaustCardEffect;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class BlockFromStrengthPower extends Power {

	public static final String powerID = TheActMod.makeID("BlockFromStrengthPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);



	public BlockFromStrengthPower(AbstractCreature owner) {
		this.owner = owner;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage("digestPower84.png", "digestPower32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =
			strings.DESCRIPTIONS[0];
	}

}