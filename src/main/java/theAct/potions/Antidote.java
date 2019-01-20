package theAct.potions;

/*
This is an event-exclusive potion.
When used, the player heals equal to the Poison/Venom it removes.
 */

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

//logger
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.TheActMod;
import theAct.patches.PotionRarityEnum;

//BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
public class Antidote extends CustomPotion
{
    public static final Logger logger = LogManager.getLogger(TheActMod.class.getName());

    public static final String POTION_ID = "AntidotePotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public Antidote() {

        super(NAME, POTION_ID, PotionRarityEnum.JUNGLE, PotionSize.S, PotionColor.STRENGTH);

        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0]);
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(final AbstractCreature target) {

           int healAmount = 0;

        try {
            if (AbstractDungeon.player.hasPower("Poison")) {
                healAmount += AbstractDungeon.player.getPower("Poison").amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Poison"));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.potency));
            }
            if (AbstractDungeon.player.hasPower("Vulnerable")) {
                healAmount += AbstractDungeon.player.getPower("Vulnerable").amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Vulnerable"));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.potency));
            }
            if (AbstractDungeon.player.hasPower("Weakened")) {
                healAmount += AbstractDungeon.player.getPower("Weakened").amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Weakened"));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.potency));
            }
            if (AbstractDungeon.player.hasPower("Frail")) {
                healAmount += AbstractDungeon.player.getPower("Frail").amount;
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, AbstractDungeon.player, "Frail"));
                AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(this.potency));
            }
        }
        catch(Exception e) {
            logger.info("Something went wrong :O");
        }

        if(healAmount != 0)
            AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, healAmount));

       }

    //com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity

    @Override
    public AbstractPotion makeCopy() {
        return new Antidote();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 5;
    }

}