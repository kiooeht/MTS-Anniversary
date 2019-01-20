package theAct.potions;

/*
This is an event-exclusive potion.
When used, the player gains 5 Strength and 5 Poison.
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
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.ThornsPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theAct.TheActMod;
import theAct.patches.PotionRarityEnum;

//logger

//BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
public class TwistedElixir extends CustomPotion
{
    public static final Logger logger = LogManager.getLogger(TheActMod.class.getName());

    public static final String POTION_ID = "TwistedElixir";
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public TwistedElixir() {

        super(NAME, POTION_ID, PotionRarityEnum.JUNGLE, PotionSize.BOTTLE, PotionColor.ANCIENT);

        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0]);
        this.isThrown = true;
        this.targetRequired = true;
        this.tips.add(new PowerTip(this.name, this.description));
    }


    public void use(final AbstractCreature target) {

        int monsterHealth = target.currentHealth;

        if (monsterHealth < 20){
           AbstractDungeon.getCurrRoom().addGoldToRewards(5);
        }
        else if (monsterHealth >= 20 && monsterHealth < 50){
            AbstractDungeon.getCurrRoom().addGoldToRewards(monsterHealth/2);
        }
        else if (monsterHealth >= 50 && monsterHealth < 100){
            AbstractDungeon.getCurrRoom().addGoldToRewards(monsterHealth/2);
            AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion(PotionRarity.UNCOMMON, false));
        }
        else if (monsterHealth >= 100 && monsterHealth < 200){
            AbstractDungeon.getCurrRoom().addGoldToRewards(monsterHealth/2);
            AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion(PotionRarity.UNCOMMON, false));
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
        }
        else if (monsterHealth >= 200 && monsterHealth < 300){
            AbstractDungeon.getCurrRoom().addGoldToRewards(monsterHealth/2);
            AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion(PotionRarity.RARE, false));
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
        }
        else{
            AbstractDungeon.getCurrRoom().addGoldToRewards(150);
            AbstractDungeon.getCurrRoom().addPotionToRewards(AbstractDungeon.returnRandomPotion(PotionRarity.RARE, false));
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.UNCOMMON);
            AbstractDungeon.getCurrRoom().addRelicToRewards(AbstractRelic.RelicTier.COMMON);
        }

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new StrengthPower(AbstractDungeon.player, 3), 3));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, AbstractDungeon.player, new MetallicizePower(AbstractDungeon.player, 4), 4));






    }

    //com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity

    @Override
    public AbstractPotion makeCopy() {
        return new TwistedElixir();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 5;
    }

}