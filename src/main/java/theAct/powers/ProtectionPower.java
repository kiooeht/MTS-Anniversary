package theAct.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class ProtectionPower extends Power {
    public static final String powerID = TheActMod.makeID("ProtectionPower");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    private static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    public ProtectionPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        type = PowerType.BUFF;
        name = powerStrings.NAME;
        setImage("protectionPower84.png", "protectionPower32.png");
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
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m != owner) {
                    AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, owner, amount));
                }
            }
        }
        if (c.type == AbstractCard.CardType.ATTACK && a.target == null) {
            int aliveCount = 0;
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (!m.isDying && !m.isEscaping) {
                    aliveCount++;
                }
            }
            if (aliveCount < 2) {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != owner) {
                        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, owner, amount));
                    }
                }
            }
        }
    }
}
