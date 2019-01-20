package theAct.powers;

import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.powers.AbstractPower;

import theAct.TheActMod;

import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.core.*;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.*;

public class InfectiousSporesPower extends AbstractPower
{
	public static final String powerID = TheActMod.makeID("InfectiousSporesPower");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    
    public InfectiousSporesPower(final AbstractCreature owner, final int vulnAmt) {
        this.name = strings.NAME;
        this.ID = powerID;
        this.owner = owner;
        this.amount = vulnAmt;
        this.updateDescription();
        this.loadRegion("sporeCloud");
    }
    
    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0] + this.amount + strings.DESCRIPTIONS[1];
    }
    
    @Override
    public void onDeath() {
        if (AbstractDungeon.getCurrRoom().isBattleEnding()) {
            return;
        }
        CardCrawlGame.sound.play("SPORE_CLOUD_RELEASE");
        this.flashWithoutSound();
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(AbstractDungeon.player, null, new FungalInfectionPower(AbstractDungeon.player, this.amount), this.amount));
    }
}
