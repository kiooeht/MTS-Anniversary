package theAct.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import theAct.TheActMod;
//import sneckomod.SneckoMod;

public class EnemyRandomizeCost
        extends AbstractGameAction
{

    public AbstractPlayer p;
    public int numOfCards;

    public EnemyRandomizeCost(AbstractPlayer p, int numOfCards)
    {
        this.p = p;
        this.numOfCards = numOfCards;
        TheActMod.logger.info("Randomizing " + this.numOfCards + "cards");

    }



    public void update()
    {

        Integer cardsRandomized = 0;
        //Randomly selects a certain number of cards within the players hand and changes their cost
        for(AbstractCard c : p.hand.group) {

                if ((c.cost >= 0)) {

                    int newCost = AbstractDungeon.cardRandomRng.random(3);
                    c.superFlash(Color.LIME.cpy());

                    if (c.cost != newCost) {
                       c.costForTurn = newCost;
                       c.isCostModifiedForTurn = true;

                    }
                    cardsRandomized++;
                    if (cardsRandomized == this.numOfCards){
                        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, TheActMod.makeID("RandomizePower"), cardsRandomized));
                        this.isDone = true;
                        return;
                    }

                }
            }

        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(AbstractDungeon.player, AbstractDungeon.player, TheActMod.makeID("RandomizePower"), cardsRandomized));
        this.isDone = true;
    }

}
