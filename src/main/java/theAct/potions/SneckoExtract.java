package theAct.potions;

/*
This is an event-exclusive potion.
When used, the player gains 5 Strength and 5 Poison.
 */

import basemod.abstracts.CustomPotion;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.actions.defect.IncreaseMaxOrbAction;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.red.Berserk;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.orbs.Dark;
import com.megacrit.cardcrawl.orbs.Frost;
import com.megacrit.cardcrawl.orbs.Lightning;
import com.megacrit.cardcrawl.orbs.Plasma;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.powers.*;
import theAct.patches.PotionRarityEnum;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.player;

//BaseMod.addPotion(JungleJuice.class, Color.GREEN, Color.GRAY, Color.BLACK, JungleJuice.POTION_ID);
public class SneckoExtract extends CustomPotion
{
    public static final String POTION_ID = "SneckoExtractPotion";


    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public SneckoExtract() {

        super(NAME, POTION_ID, PotionRarityEnum.JUNGLE, PotionSize.SNECKO, PotionColor.SNECKO);

        this.potency = getPotency();
        this.description = (DESCRIPTIONS[0]);
        this.isThrown = false;
        this.targetRequired = false;
        this.tips.add(new PowerTip(this.name, this.description));
    }

    public void use(final AbstractCreature target) {

        for(int z = 0; z < 10; z++) {

            //------------------Common powers---------------------------
            int newPotency = 0;

            //STR
            if (random100() < 75) {
                newPotency = maybeNegative(potencyModify((2)));
                if (newPotency < 0) //To make it usable lol
                    newPotency++;
                if (newPotency != 0)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StrengthPower(player, newPotency), newPotency));
            }
            //DEX
            if (random100() < 75) {
                newPotency = maybeNegative(potencyModify((2)));
                if (newPotency < 0) //To make it usable lol
                    newPotency++;
                if (newPotency != 0)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DexterityPower(player, newPotency), newPotency));
            }
            //FOC
            if (random100() < 75) {
                newPotency = maybeNegative(potencyModify((2)));
                if (newPotency < 0) //To make it usable lol
                    newPotency++;
                if (newPotency < -2)
                    newPotency = -2; //Negative 3 focus is too harsh
                if (newPotency != 0)
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new FocusPower(player, newPotency), newPotency));
            }


            //------------------Uncommon powers---------------------------
            //Regen
            if (random100() < 10) {
                newPotency = potencyModify(3);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new RegenPower(player, newPotency), newPotency));
            }
            //PlatedArmor
            if (random100() < 10) {
                newPotency = potencyModify(3);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new PlatedArmorPower(player, newPotency), newPotency));
            }
            //Weakness
            if (random100() < 10) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new WeakPower(player, newPotency, false), newPotency));
            }
            //Vulnerable
            if (random100() < 10) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new VulnerablePower(player, newPotency, false), newPotency));
            }
            //Frailty
            if (random100() < 10) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new FrailPower(player, newPotency, false), newPotency));
            }
            //Heal
            if (random100() < 10) {
                newPotency = potencyModify(8 * potency);
                AbstractDungeon.actionManager.addToBottom(new HealAction(AbstractDungeon.player, AbstractDungeon.player, newPotency));
            }
            //Damage
            if (random100() < 10) {
                newPotency = potencyModify(4 * potency);
                AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, newPotency));
            }
            //GainBlock
            if (random100() < 10) {
                newPotency = potencyModify(8 * potency);
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, newPotency));
            }
            //LoseBlock
            if (random100() < 10) {
                newPotency = potencyModify(8 * potency);
                AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(AbstractDungeon.player, AbstractDungeon.player, newPotency));
            }
            //Thorns
            if (random100() < this.potency) {
                newPotency = potencyModify(3);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ThornsPower(player, newPotency), newPotency));
            }
            //Poison
            if (random100() < this.potency) {
                newPotency = potencyModify(4);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new PoisonPower(player, player, newPotency), newPotency));
            }

            //------------------Rare powers---------------------------

            //Barricade
            if (random100() < this.potency) {
                if (!player.hasPower("Barricade")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BarricadePower(player)));
                }
            }
            //Beserk
            if (random100() < this.potency) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new VulnerablePower(player, this.potency, false), this.potency));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BerserkPower(Berserk.NAME, AbstractDungeon.player, 1), 1));
            }
            //Brutality
            if (random100() < this.potency) {
                if (!player.hasPower("Brutality")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BrutalityPower(player, 1), 1));
                }
            }
            //Rage
            if (random100() < this.potency) {
                if (!player.hasPower("Rage")) {
                    newPotency = potencyModify(3);
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new RagePower(player, newPotency), newPotency));
                }
            }
            //Combust
            if (random100() < this.potency) {
                newPotency = potencyModify(5);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new CombustPower(player, 1, newPotency), newPotency));
            }
            //Corruption
            if (random100() < this.potency) {
                if (!player.hasPower("Corruption")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new CorruptionPower(player)));
                }
            }
            //Demonform
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DemonFormPower(player, newPotency), newPotency));
            }
            //Evolve
            if (random100() < this.potency) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new EvolvePower(player, 1), 1));
            }

            //Double Tap
            if (random100() < this.potency) {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DoubleTapPower(player, 1), 1));
            }

            //FeelNoPain
            if (random100() < this.potency) {
                newPotency = potencyModify(4);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new FeelNoPainPower(player, newPotency), newPotency));
            }

            //FireBreathing
            if (random100() < this.potency) {
                newPotency = potencyModify(1);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new FireBreathingPower(player, newPotency), newPotency));
            }

            //Juggernaut
            if (random100() < this.potency) {
                newPotency = potencyModify(3);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new JuggernautPower(player, newPotency), newPotency));
            }

            //Metallicize
            if (random100() < this.potency) {
                newPotency = potencyModify(2) + 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new MetallicizePower(player, newPotency), newPotency));
            }
            //Rupture
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new MetallicizePower(player, newPotency), newPotency));
            }

            //GRRRREEEEEEEEENNNNNNNN CHARACTER
            //AThousandCuts
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ThousandCutsPower(player, newPotency), newPotency));
            }
            //Accuracy
            if (random100() < this.potency) {
                newPotency = potencyModify(3) + 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new AccuracyPower(player, newPotency), newPotency));
            }
            //After Image
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new AfterImagePower(player, newPotency), newPotency));
            }
            //Envenom
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new EnvenomPower(player, newPotency), newPotency));
            }
            //InfiniteBlade
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new InfiniteBladesPower(player, newPotency), newPotency));
            }
            //NoxiousFumes
            if (random100() < this.potency) {
                newPotency = potencyModify(2);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new NoxiousFumesPower(player, newPotency), newPotency));
            }
            //ToolsOfTheTrade
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ToolsOfTheTradePower(player, newPotency), newPotency));
            }
            //WellLaidPlans
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new RetainCardPower(player, newPotency), newPotency));
            }

            //BBBBBLLLLLLLLLLLLLUUUUUUUUUUUEEEEEEEEEEEEE CHARACTER

            //BiasedCognition
            if (random100() < this.potency) {
                newPotency = potencyModify(4) + 3;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new FocusPower(player, newPotency), newPotency));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BiasPower(player, 1), 1));
            }
            //Buffer
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new BufferPower(player, newPotency), newPotency));
            }
            //Capacitor
            if (random100() < this.potency) {
                newPotency = potencyModify(1);
                AbstractDungeon.actionManager.addToBottom(new IncreaseMaxOrbAction(newPotency));
            }
            //Creative AI
            if (random100() < this.potency) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new CreativeAIPower(player, newPotency), newPotency));
            }
            //EchoForm
            if (random100() < this.potency) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new EchoPower(player, newPotency), newPotency));
            }
            //Electrodynamics
            if (random100() < this.potency) {
                if (!player.hasPower("Electrodynamics")) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ElectroPower(player)));
                }
            }
            //Heatsink
            if (random100() < this.potency) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new HeatsinkPower(player, newPotency), newPotency));
            }
            //HelloWorld
            if (random100() < this.potency) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new HelloPower(player, newPotency), newPotency));
            }
            //Loop
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new LoopPower(player, newPotency), newPotency));
            }
            //MachineLearning
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DrawPower(player, newPotency), newPotency));
            }
            //AntiMachineLearning
            if (random100() < this.potency) {
                newPotency = 1;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new DrawReductionPower(player, newPotency), newPotency));
            }
            //SelfRepair
            if (random100() < this.potency) {
                newPotency = potencyModify(8) + 3;
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new RepairPower(player, newPotency), newPotency));
            }
            //StaticDischarge
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StaticDischargePower(player, newPotency), newPotency));
            }
            //Storm
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new StormPower(player, newPotency), newPotency));
            }
            //------Orbs-------
            if (random100() < this.potency * 5) {
                newPotency = potencyModify(0);
                for (int x = 0; x < newPotency; x++) {
                    AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Dark()));
                }
            }
            if (random100() < this.potency * 5) {
                newPotency = potencyModify(0);
                for (int x = 0; x < newPotency; x++) {
                    AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Lightning()));
                }
            }
            if (random100() < this.potency * 5) {
                newPotency = potencyModify(0);
                for (int x = 0; x < newPotency; x++) {
                    AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Frost()));
                }
            }
            if (random100() < this.potency * 5) {
                AbstractDungeon.actionManager.addToBottom(new ChannelAction(new Plasma()));
            }


            //------------------------ARTIFACT AND INTANGIBILITY HERE---------------------
            if (random100() < this.potency * 2) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new ArtifactPower(player, newPotency), newPotency));
            }
            if (random100() < this.potency) {
                newPotency = potencyModify(0);
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, player, new IntangiblePlayerPower(player, newPotency), newPotency));
            }

            //BrewMorePotions
            if (random100() < this.potency) {
                for (int i = 0; i < AbstractDungeon.player.potionSlots; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(true)));
                }
            }
            //GetThisBack
            if (random100() < 2) {
                AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(AbstractDungeon.returnRandomPotion(PotionRarity.RARE, true)));
            }

        }
    }









    @Override
    public AbstractPotion makeCopy() {
        return new SneckoExtract();
    }

    @Override
    public int getPotency(final int ascensionLevel) {
        return 2;
    }

    private int random100(){
        int retVal = (int)(Math.random() * (50 * this.potency) + 1); // 1-100
        return retVal;
    }

    private int potencyModify(int max){
        int retVal = (int)(Math.random() * (max + this.potency)) + 1; // 1-2(potency) + max. so if max is 10 then it will be 1-12. if max is 0 then it's 1-2
        return retVal;
    }

    private int maybeNegative(int num){
        if (Math.random() < 0.5){
            num *= -1;
        }

        return num;
    }

}