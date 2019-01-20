package theAct.powers;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnCardDrawPower;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.actions.EnemyRandomizeCost;
import theAct.powers.abstracts.Power;

public class RandomizePower extends Power implements OnCardDrawPower {

    public static final String powerID = TheActMod.makeID("RandomizePower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);

    public RandomizePower(int stackAmount) {
        this.owner = AbstractDungeon.player;
        this.type = PowerType.DEBUFF;
        this.name = strings.NAME;
        this.setImage("digestPower84.png", "digestPower32.png");
        this.ID = powerID;
        this.amount = stackAmount;
        this.updateDescription();
        this.isTurnBased = true;
    }

    @Override
    public void onCardDraw(AbstractCard c){
            if (amount > 0) {
                if ((c.cost >= 0)) {

                    int newCost = AbstractDungeon.cardRandomRng.random(3);
                    c.superFlash(Color.LIME.cpy());

                    if (c.cost != newCost) {
                        c.costForTurn = newCost;
                        c.isCostModifiedForTurn = true;

                    }
                   reducePower(1);
                }
            }

    }

    @Override
    public void reducePower(int stackAmount) {
        if (this.amount - stackAmount <= 0) {
            this.fontScale = 8.0F;
            this.amount = 0;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this.owner,this.owner, "theJungle:RandomizePower"));

        } else {
            this.fontScale = 8.0F;
            this.amount -= stackAmount;
        }

    }

    public void updateDescription(){
        this.description =
                strings.DESCRIPTIONS[0] + strings.DESCRIPTIONS[1];
    }
}
