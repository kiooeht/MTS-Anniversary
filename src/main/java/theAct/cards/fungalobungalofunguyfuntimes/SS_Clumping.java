package theAct.cards.fungalobungalofunguyfuntimes;

import basemod.abstracts.CustomCard;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;

import theAct.TheActMod;

public class SS_Clumping extends CustomCard {
    public static final String ID = TheActMod.makeID("SS_Clumping");
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String IMG_PATH = null;
    private static final int COST = 1;

    public SS_Clumping() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION, CardType.STATUS, CardColor.COLORLESS, CardRarity.SPECIAL, CardTarget.NONE);
        this.magicNumber = this.baseMagicNumber = 1;
        this.exhaust = true;
        this.isEthereal = true;
    }
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, 1));
    }
    @Override
    public void triggerWhenDrawn() {
        if (AbstractDungeon.player.hasPower("Evolve") && !AbstractDungeon.player.hasPower("No Draw")) {
            AbstractDungeon.player.getPower("Evolve").flash();
            AbstractDungeon.actionManager.addToBottom(new DrawCardAction(AbstractDungeon.player, AbstractDungeon.player.getPower("Evolve").amount));
        }
    }
    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return this.cardPlayable(m) && this.hasEnoughEnergy();
    }
    @Override
    public void upgrade() {
    }
}
