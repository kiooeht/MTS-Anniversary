package theAct.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnCardDrawPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class RandomizePower extends Power implements OnCardDrawPower {

    public static final String powerID = TheActMod.makeID("RandomizePower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    private int cardsRandomizedThisTurn = 0;
    private String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public RandomizePower(AbstractCreature p, int stackAmount) {
        this.owner = p;
        this.type = PowerType.DEBUFF;
        this.name = strings.NAME;
        this.setImage("stackingConfusePower84.png", "stackingConfusePower32.png");
        this.ID = powerID;
        this.amount = stackAmount;
        this.updateDescription();
        this.isTurnBased = true;
    }

    @Override
    public void updateDescription(){
        if (this.amount == 0) {
            this.description = DESCRIPTIONS[0];
        }
        else if (this.amount == 1) {
            this.description = DESCRIPTIONS[1];
        }
        else {
            this.description = DESCRIPTIONS[2] + this.amount + DESCRIPTIONS[3];
        }
        if (this.cardsRandomizedThisTurn == 1) {
            this.description += DESCRIPTIONS[4] + this.cardsRandomizedThisTurn + DESCRIPTIONS[5];
        }
        else if (cardsRandomizedThisTurn > 1) {
            this.description += DESCRIPTIONS[4] + this.cardsRandomizedThisTurn + DESCRIPTIONS[6];
        }
    }

    @Override
    public void onCardDraw(AbstractCard c){
        if (amount > 0 && c.cost >= 0) {
            int newCost = AbstractDungeon.cardRandomRng.random(3);
            this.cardsRandomizedThisTurn++;
            c.superFlash(Color.LIME.cpy());
            if (c.cost != newCost) {
                c.costForTurn = newCost;
                c.isCostModifiedForTurn = true;
            }
            reducePower(1);
        }
    }

    @Override
    public void reducePower(int stackAmount) {
        if (this.amount - stackAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount = 0;

        } else {
            this.fontScale = 8.0F;
            this.amount -= stackAmount;
        }

    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (this.amount == 0) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));
        }
    }
}
