package theAct.potions;

/*
This is an event-exclusive potion.
When used, the player gains 5 Strength and 5 Poison.
 */

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theAct.patches.PotionRarityEnum;

//BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
public class HauntedGourd extends CustomPotion
{
    public static final String POTION_ID = "HauntedGourdPotion";


    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public HauntedGourd() {

        super(NAME, POTION_ID, PotionRarityEnum.JUNGLE, PotionSize.CARD, PotionColor.FIRE);

        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0]);
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(final AbstractCreature target) {

        AbstractDungeon.actionManager.addToBottom(new DiscoveryAction(AbstractCard.CardType.ATTACK));
        AbstractDungeon.actionManager.addToBottom(new DiscoveryAction(AbstractCard.CardType.SKILL));
        AbstractDungeon.actionManager.addToBottom(new DiscoveryAction(AbstractCard.CardType.POWER));
      //ADD CURSE TO HAND
        AbstractCard c = AbstractDungeon.returnRandomCurse().makeCopy();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(c, 1));
        AbstractCard d = new Slimed();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInHandAction(d, 1));

    }

    //com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity

    @Override
    public AbstractPotion makeCopy() {
        return new HauntedGourd();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 5;
    }

}