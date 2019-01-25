package theAct.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theAct.TheActMod;
import theAct.relics.PaperFaux;

public class FauxPas extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("FauxPas");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private CurrentScreen currentScreen;
    private int hpLoss;
    private int goldGain;
    private boolean isA15;

    public FauxPas() {
        super(NAME, DESCRIPTIONS[0], TheActMod.assetPath("images/events/fauxpas.png"));
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        this.currentScreen = CurrentScreen.MAIN;
        if (AbstractDungeon.ascensionLevel >= 15) {
            isA15 = true;
            hpLoss = 15;
            goldGain = 150;
        } else {
            isA15 = false;
            hpLoss = 10;
            goldGain = 200;
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch(currentScreen) {
            case MAIN:
                switch (i) {
                    case 0:
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.updateDialogOption(0, OPTIONS[8]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.TUNNEL;
                        break;
                    case 1:
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        currentScreen = CurrentScreen.COMPLETE;
                        break;
                }
                break;
            case TUNNEL:
                imageEventText.updateBodyText(DESCRIPTIONS[6]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(isA15 ? OPTIONS[6] : OPTIONS[2]);
                imageEventText.setDialogOption(OPTIONS[3], new Shame());
                imageEventText.setDialogOption(isA15 ? OPTIONS[7] : OPTIONS[4], new Normality());
                currentScreen = CurrentScreen.CLEARING;
                break;
            case CLEARING:
                switch (i) {
                    case 0:
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        imageEventText.updateDialogOption(0, OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        AbstractDungeon.player.increaseMaxHp(5, false);
                        AbstractDungeon.player.damage(new DamageInfo(null, this.hpLoss));
                        currentScreen = CurrentScreen.COMPLETE;
                        break;
                    case 1:
                        imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        imageEventText.updateDialogOption(0, OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Shame(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new PaperFaux());
                        currentScreen = CurrentScreen.COMPLETE;
                        break;
                    case 2:
                        imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        imageEventText.updateDialogOption(0, OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        AbstractDungeon.effectList.add(new RainingGoldEffect(goldGain));
                        AbstractDungeon.player.gainGold(goldGain);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
                        currentScreen = CurrentScreen.COMPLETE;
                        break;
                }
                break;
            case COMPLETE:
                openMap();
                break;
        }
    }

    private enum CurrentScreen {
        MAIN,
        TUNNEL,
        CLEARING,
        COMPLETE
    }
}
