package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.TheActMod;
import theAct.relics.Creamberry;
import theAct.relics.Flamango;
import theAct.relics.ParSnip;
import theAct.relics.ShellPeas;

public class StalkMarket extends AbstractImageEvent {

    public static final String ID = TheActMod.makeID("StalkMarket");
    private static final EventStrings EVENT_STRINGS = CardCrawlGame.languagePack.getEventString(ID);
    public static final String[] DESCRIPTIONS = EVENT_STRINGS.DESCRIPTIONS;
    public static final String[] OPTIONS = EVENT_STRINGS.OPTIONS;
    private static final int PAR_SNIP_COST = 90;
    private static final int FLAMANGO_COST = 135;
    private static final int SHELL_PEAS_COST = 150;
    private static final int BUSTED_BERRY_COST = 185;
    private CurScreen screen;

    private enum CurScreen {
        INTRO_1, INTRO_2, INTRO_3, INTRO_4, END
    }

    public StalkMarket(){
        // TODO: Add image
        super(EVENT_STRINGS.NAME, DESCRIPTIONS[0], null);
        this.screen = CurScreen.INTRO_1;
        this.imageEventText.setDialogOption(OPTIONS[0]);
    }

    public static boolean canSpawn() {
        if (AbstractDungeon.player.gold >= PAR_SNIP_COST) {
            return true;
        }
        else {
            return false;
        }
    }

    protected void buttonEffect(int buttonPressed) {
        switch (this.screen) {
            case INTRO_1: {
                this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                this.screen = CurScreen.INTRO_2;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[1]);
                break;
            }
            case INTRO_2: {
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.screen = CurScreen.INTRO_3;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[0]);
            }
            case INTRO_3: {
                this.imageEventText.updateBodyText(DESCRIPTIONS[3]);
                this.screen = CurScreen.INTRO_4;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[2] + PAR_SNIP_COST + OPTIONS[6], AbstractDungeon.player.gold < PAR_SNIP_COST);
                this.imageEventText.setDialogOption(OPTIONS[3] + FLAMANGO_COST + OPTIONS[6], AbstractDungeon.player.gold < FLAMANGO_COST);
                this.imageEventText.setDialogOption(OPTIONS[4] + SHELL_PEAS_COST + OPTIONS[6], AbstractDungeon.player.gold < SHELL_PEAS_COST);
                this.imageEventText.setDialogOption(OPTIONS[5] + BUSTED_BERRY_COST + OPTIONS[6], AbstractDungeon.player.gold < BUSTED_BERRY_COST);
                this.imageEventText.setDialogOption(OPTIONS[7]);
            }
            case INTRO_4: {
                switch (buttonPressed) {
                    case 0: {
                        AbstractDungeon.player.loseGold(PAR_SNIP_COST);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F,  RelicLibrary.getRelic(ParSnip.ID).makeCopy());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[4]);
                        break;
                    }
                    case 1: {
                        AbstractDungeon.player.loseGold(FLAMANGO_COST);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F,  RelicLibrary.getRelic(Flamango.ID).makeCopy());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[6]);
                        break;
                    }
                    case 2: {
                        AbstractDungeon.player.loseGold(SHELL_PEAS_COST);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F,  RelicLibrary.getRelic(ShellPeas.ID).makeCopy());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                        break;
                    }
                    case 3: {
                        AbstractDungeon.player.loseGold(BUSTED_BERRY_COST);
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH * 0.5F, Settings.HEIGHT * 0.5F,  RelicLibrary.getRelic(Creamberry.ID).makeCopy());
                        this.imageEventText.updateBodyText(DESCRIPTIONS[7]);
                        break;
                    }
                    case 4: {
                        this.imageEventText.updateBodyText(DESCRIPTIONS[8]);
                        break;
                    }
                }
                this.screen = CurScreen.END;
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[8]);
            }
            case END: {
                this.openMap();
                this.imageEventText.clearAllDialogs();
                this.imageEventText.setDialogOption(OPTIONS[8]);
            }
        }
    }
}
