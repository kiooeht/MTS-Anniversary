package theAct.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theAct.TheActMod;
import theAct.actions.EnemyRandomizeCost;
import theAct.powers.abstracts.Power;

public class RandomizePower extends Power {

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
                strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
    }
}
