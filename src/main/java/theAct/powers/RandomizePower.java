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
    private String[] DESCRIPTIONS = strings.DESCRIPTIONS;
    private int cardsRandomizedThisTurn;

    public RandomizePower(AbstractCreature p, int stackAmount) {
        this.owner = p;
        this.type = PowerType.DEBUFF;
        this.name = strings.NAME;
        this.setImage("stackingConfusePower84.png", "stackingConfusePower32.png");
        this.ID = powerID;
        this.amount = stackAmount;
        this.updateDescription();
        this.isTurnBased = true;
        this.cardsRandomizedThisTurn = 0;
    }

    @Override
    public void updateDescription(){
        if (this.amount == 1) {
            this.description = DESCRIPTIONS[0];
        }
        else {
            this.description = DESCRIPTIONS[1] + this.amount + DESCRIPTIONS[2];
        }
    }

    @Override
    public void onCardDraw(AbstractCard c){
        if (c.cost >= 0 && this.cardsRandomizedThisTurn < 2) {
            int newCost = AbstractDungeon.cardRandomRng.random(3);
            c.superFlash(Color.LIME.cpy());
            if (c.cost != newCost) {
                c.costForTurn = newCost;
                c.isCostModifiedForTurn = true;
            }
            this.cardsRandomizedThisTurn++;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        super.atEndOfTurn(isPlayer);
        cardsRandomizedThisTurn = 0;
    }

    @Override
    public void atStartOfTurnPostDraw() {
        super.atStartOfTurnPostDraw();
        reducePower(1);
        this.flash();
    }

    @Override
    public void reducePower(int stackAmount) {
        if (this.amount - stackAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount=0;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner, this.owner, this));

        } else {
            this.fontScale = 8.0F;
            this.amount -= stackAmount;
        }
        updateDescription();
    }


}
