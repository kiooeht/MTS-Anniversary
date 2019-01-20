package theAct.potions;

/*
This is an event-exclusive potion.
When used, the player heals equal to the Poison/Venom it removes.
 */

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.EnvenomPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.TheActMod;
import theAct.patches.PotionRarityEnum;

//logger

//BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
public class SpyderVenom extends CustomPotion
{
    public static final Logger logger = LogManager.getLogger(TheActMod.class.getName());

    public static final String POTION_ID = "SpyderVenomPotion";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public SpyderVenom() {

        super(NAME, POTION_ID, PotionRarityEnum.JUNGLE, PotionSize.S, PotionColor.SMOKE);

        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0]);
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(final AbstractCreature target) {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player, new EnvenomPower(AbstractDungeon.player, this.potency), this.potency));
       }

    //com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity

    @Override
    public AbstractPotion makeCopy() {
        return new SpyderVenom();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 1;
    }

}