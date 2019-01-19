package theAct.actions;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
//import sneckomod.SneckoMod;

public class EnemyRandomizeCost
        extends AbstractGameAction
{
    public AbstractCard c;
    public AbstractPlayer p;
    public int numOfCards;

    private int sizeOfHand;
    private Random R = new Random();
    private int[] cardsSelected; //Holds the card's positions in the player's hand

    public EnemyRandomizeCost(AbstractPlayer p, int numOfCards)
    {
        this.p = p;
        this.numOfCards = numOfCards;
        cardsSelected = new int [this.numOfCards];
    }



    public void update()
    {
        sizeOfHand = p.hand.group.size();
        AbstractCard card;

        //Randomly selects a certain number of cards within the players hand and changes their cost
        for(int i = 0; i < numOfCards; i++) {
            if(p.hand.size() != 0) {
            int checkCard = 0;
            if(i != 0) {
                do {
                    checkCard = R.random(0, sizeOfHand-1);
                } while (contains(checkCard, i));

            }
            else
            {
                checkCard = R.random(0, sizeOfHand-1);
            }

            cardsSelected[i] = checkCard;


            this.c = p.hand.group.get(checkCard);


                //this.c = p.hand.getRandomCard(R);

                if ((this.c.cost >= 0) /*&& (!this.c.hasTag(SneckoMod.SNEKPROOF))*/) {
                    int newCost = AbstractDungeon.cardRandomRng.random(3);


                    if (this.c.cost != newCost) {
                        this.c.cost = newCost;
                        this.c.costForTurn = this.c.cost;
                        this.c.isCostModified = true;
                        this.c.superFlash(Color.LIME.cpy());
                    }
                }
            }
        }

        this.isDone = true;
    }

    //Checks if a card has been previously selected
    public boolean contains(int value, int searchMax)
    {
        for(int i = 0; i < searchMax; i++)
        {

           if(value == cardsSelected[i])
               return true;
        }

        return false;
    }
}
