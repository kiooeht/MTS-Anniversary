package theAct.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.OnReceivePowerPower;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class SlothfulPower extends Power implements OnReceivePowerPower {
    public static final String POWER_ID = TheActMod.makeID("SlothfulPower");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);

    public SlothfulPower(AbstractCreature owner, int amount) {
        this.name = strings.NAME;
        this.ID = POWER_ID;
        this.owner = owner;
        this.amount = amount;
        this.updateDescription();
        this.loadRegion("nightmare");
        this.type = PowerType.DEBUFF;
    }

    public void updateDescription() {
        if (this.amount == 1) {
            this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
        } else {
            this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[2];
        }
    }

    @Override
    public boolean onReceivePower(AbstractPower p, AbstractCreature ptarget, AbstractCreature psource) {
        if(p.type == PowerType.BUFF && ptarget == AbstractDungeon.player) {
            flash();
            this.onSpecificTrigger();
            return false;
        }
        return true;
    }

    public void onSpecificTrigger() {
        if (this.amount <= 0) {
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this.owner, this.owner, this.ID));
        } else {
            AbstractDungeon.actionManager.addToTop(new ReducePowerAction(this.owner, this.owner, this.ID, 1));
        }

    }

}
