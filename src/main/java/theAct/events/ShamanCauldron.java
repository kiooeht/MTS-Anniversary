package theAct.events;

import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.common.ObtainPotionAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theAct.TheActMod;
import theAct.patches.PotionRarityEnum;
import theAct.potions.*;
import theAct.relics.PaperFaux;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnRandomPotion;
public class ShamanCauldron extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("ShamanCauldron");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private CurrentScreen currentScreen;
    private static final String BASE_IMG = TheActMod.assetPath("images/events/Cauldron.png");
    private static final String BROWN_IMG = TheActMod.assetPath("images/events/Cauldron.png");

    public ShamanCauldron() {
        super(NAME, DESCRIPTIONS[0], BASE_IMG);
            imageEventText.setDialogOption(OPTIONS[0]);
            imageEventText.setDialogOption(OPTIONS[1], !AbstractDungeon.player.hasAnyPotions());
            imageEventText.setDialogOption(OPTIONS[2]);

        this.currentScreen = CurrentScreen.MAIN;
    }

    @Override
    protected void buttonEffect(int i) {
        switch(currentScreen) {
            case MAIN:
                switch (i) {
                    case 0: //Take damage and fill empty potion slots
                        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 8));
                        CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.LOW, ScreenShake.ShakeDur.SHORT, false);
                        CardCrawlGame.sound.play("BLUNT_FAST");
                        AbstractDungeon.player.damage(new DamageInfo(null, 8));
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.BROWNBREWFILL;
                        imageEventText.loadImage(TheActMod.assetPath("images/events/CauldronBrown.png"));
                        break;
                    case 1: //Lose all potions and fill all slots
                        for (int x = 0; x < AbstractDungeon.player.potionSlots; x++){
                            AbstractDungeon.player.potions.set(x, new PotionSlot(x));
                        }
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.BROWNBREWREPLACE;
                        imageEventText.loadImage(TheActMod.assetPath("images/events/CauldronBrown.png"));
                        break;
                    case 2: //nope out
                        imageEventText.clearRemainingOptions();
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        currentScreen = CurrentScreen.END;
                        break;
                }
                break;
            case BROWNBREWFILL: //fill

                int rando2 = (int)(Math.random() * (6) + 1); //1-6
                for (int x = 0; x < AbstractDungeon.player.potionSlots; x++){
                    AbstractPotion p = this.getJunglePotion(rando2);
                    AbstractDungeon.player.obtainPotion(p);
                }

                imageEventText.updateBodyText(DESCRIPTIONS[4]);
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                currentScreen = CurrentScreen.END;
                break;
            case BROWNBREWREPLACE:

                int rando = (int)(Math.random() * (6) + 1); //1-6
                for (int x = 0; x < AbstractDungeon.player.potionSlots; x++){
                    AbstractPotion p = this.getJunglePotion(rando);
                    AbstractDungeon.player.obtainPotion(p);
                }

                imageEventText.updateBodyText(DESCRIPTIONS[4]);
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                currentScreen = CurrentScreen.END;

                break;
            case COMPLETE: //done here
                imageEventText.updateBodyText(DESCRIPTIONS[4]);
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                currentScreen = CurrentScreen.END;
                break;
            default:
                openMap();
                break;
        }
    }


    private static void replacePotions(){
        for (int x = 0; x < AbstractDungeon.player.potionSlots; x++){
            AbstractDungeon.player.potions.set(x, new PotionSlot(x));
        }
    }

    private void fillPotionSlots(){
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 10));

        AbstractPotion p = getJunglePotion((int)(Math.random() * (6) + 1));

        int availableSlots = AbstractDungeon.player.potionSlots;
        for (int x = 0; x < availableSlots; x++){
            AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 1));
            AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(p));
        }
    }

    private AbstractPotion getJunglePotion(int number){
        AbstractDungeon.actionManager.addToBottom(new LoseHPAction(AbstractDungeon.player, AbstractDungeon.player, 3));

        switch (number){
            case 1:
                return new Antidote();
            case 2:
                return new HauntedGourd();
            case 3:
                return new JungleJuice();
            case 4:
                return new SneckoExtract();
            case 5:
                return new TwistedElixir();
            case 6:
                return new SpyderVenom();
            default:
                return new TwistedElixir();
        }
    }


    private enum CurrentScreen {
        MAIN,
        BROWNBREWREPLACE,
        BROWNBREWFILL,
        COMPLETE,
        END
    }
}
