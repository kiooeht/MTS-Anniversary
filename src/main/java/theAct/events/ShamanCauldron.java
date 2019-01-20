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
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theAct.TheActMod;
import theAct.patches.PotionRarityEnum;
import theAct.relics.PaperFaux;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.returnRandomPotion;
//TODO Fix the strings in events.json because some didn't work
public class ShamanCauldron extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("ShamanCauldron");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private CurrentScreen currentScreen;

    public ShamanCauldron() {
        super(NAME, DESCRIPTIONS[0], null);
        imageEventText.setDialogOption(OPTIONS[0]);
        if(AbstractDungeon.player.hasAnyPotions()){ //if the player has at least 1 potion
            imageEventText.setDialogOption(OPTIONS[1]);
        }
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
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.BROWNBREWFILL;
                        break;
                    case 1: //Lose all potions and fill all slots
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.BROWNBREWREPLACE;
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
                    fillPotionSlots();
                    imageEventText.updateBodyText(DESCRIPTIONS[4]);
                    imageEventText.updateDialogOption(0, OPTIONS[4]);
                    currentScreen = CurrentScreen.COMPLETE;
                    break;
            case BROWNBREWREPLACE: //same as BROWNBREWFILL but you replace instead
                    replacePotions();
                    imageEventText.updateBodyText(DESCRIPTIONS[4]);
                    imageEventText.updateDialogOption(0, OPTIONS[4]);
                    currentScreen = CurrentScreen.COMPLETE;
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

    //TODO: Fix this and make it replace all potion slots with the random jungle potion
    private static void replacePotions(){
        for (int x = 0; x <AbstractDungeon.player.potionSlots; x++){
            AbstractPotion p = returnRandomPotion(PotionRarityEnum.JUNGLE, false);
            //AbstractDungeon.player.potions.replaceAll(p); // how i do this :(
        }
    }

    //TODO: Fix this and make it fill all empty potion slots with the random jungle potion
    private static void fillPotionSlots(){

        AbstractPotion p = returnRandomPotion(PotionRarityEnum.JUNGLE, false);
        //for each empty potion slot do this...
        AbstractDungeon.actionManager.addToBottom(new ObtainPotionAction(p));

    }


    private enum CurrentScreen {
        MAIN,
        BROWNBREWREPLACE,
        BROWNBREWFILL,
        COMPLETE,
        END
    }
}
