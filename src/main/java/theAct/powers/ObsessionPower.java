package theAct.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class ObsessionPower extends Power {
    public static final String powerID = TheActMod.makeID("ObsessionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ObsessionPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        name = powerStrings.NAME;
        setImage("obsessionPower84.png", "obsessionPower32.png");
        ID = powerID;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
    }

    @Override
    public void onUseCard(AbstractCard c, UseCardAction a) {
        if (c.type == AbstractCard.CardType.ATTACK && a.target != owner) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, new DamageInfo(owner, amount)));
        }
    }
}
