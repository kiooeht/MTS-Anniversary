package theAct.powers;

import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;
import theAct.monsters.JungleHunters;
import theAct.powers.abstracts.Power;

public class CautiousPower extends Power {
    public static final String powerID = TheActMod.makeID("CautiousPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    private int startAmt;

    public CautiousPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        startAmt = amount;
        type = PowerType.BUFF;
        name = powerStrings.NAME;
        setImage("cautiousPower84.png", "cautiousPower32.png");
        ID = powerID;
        updateDescription();
    }

    public void updateDescription() {
        if (this.amount == 0) {
            this.description = powerStrings.DESCRIPTIONS[3];
        } else if (this.amount == 1) {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[1];

        } else {
            this.description = powerStrings.DESCRIPTIONS[0] + this.amount + powerStrings.DESCRIPTIONS[2];
        }
    }

    public void atStartOfTurn() {
        amount = startAmt;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damage) {
        if (info.type == DamageInfo.DamageType.NORMAL) {
            amount--;
            this.updateDescription();
            if (amount == 0 && owner instanceof JungleHunters) {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction((AbstractMonster) owner, JungleHunters.STUNNED));
            }
        }
        return damage;
    }
}
