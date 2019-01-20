package theAct.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;
import theAct.actions.RandomizeHandForTurnAction;

public class PetSnecko extends CustomCard {
    public static final String ID = TheActMod.makeID("PetSnecko");
    private static CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;
    public static final String IMG_PATH = null;
    private static final int COST = 0;
    private static final CardRarity rarity = CardRarity.SPECIAL;
    private static final CardTarget target = CardTarget.SELF;
    private static final int DRAW_AMT = 1;
    private static final int DRAW_UPGRADE = 1;

    public PetSnecko() {
        super(ID, NAME, IMG_PATH, COST, DESCRIPTION,
                CardType.SKILL, CardColor.COLORLESS,
                rarity, target);
        this.magicNumber = this.baseMagicNumber = DRAW_AMT;
    }

    @Override
    public void upgrade() {
        if (upgraded) {
            upgradeName();
            upgradeMagicNumber(DRAW_UPGRADE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        AbstractDungeon.actionManager.addToBottom(new DrawCardAction(p, magicNumber));
        AbstractDungeon.actionManager.addToBottom(new RandomizeHandForTurnAction());
    }
}
