package theAct.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Shovel;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import theAct.TheActMod;

public class LostInTheJungle extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("LostInTheJungle");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMAGE = null; // TheActMod.assetPath("images/events/LostInTheJungle.png"); // TODO

    private int hpLoss;
    private int goldAmt = 300;
    private Screen screen = Screen.INTRO;
    private static enum Screen {
        INTRO,
        TREE,
        CLEARING,
        LEAVE;
    }

    public LostInTheJungle() {
        super(NAME, DESCRIPTIONS[0], IMAGE);
        hpLoss = AbstractDungeon.player.relics.size() * 2;
        this.imageEventText.setDialogOption(OPTIONS[0] + hpLoss + OPTIONS[1]);
        this.imageEventText.setDialogOption(OPTIONS[2], CardLibrary.getCopy(Writhe.ID));
        this.imageEventText.setDialogOption(OPTIONS[3]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (this.screen == Screen.INTRO) {
            switch (buttonPressed) {
                case 0:
                    this.screen = Screen.TREE;
                    AbstractDungeon.player.damage(new DamageInfo(null, hpLoss, DamageInfo.DamageType.HP_LOSS));
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(OPTIONS[4]);
                    break;
                case 1:
                    AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
                    clearingScreen();
                    break;
                default:
                    this.openMap();
            }
        } else if (this.screen == Screen.TREE) {
            clearingScreen();
        } else if (this.screen == Screen.CLEARING) {
            switch (buttonPressed) {
                case 0:
                    this.screen = Screen.LEAVE;
                    AbstractDungeon.effectList.add(new RainingGoldEffect(this.goldAmt));
                    AbstractDungeon.player.gainGold(this.goldAmt);
                    this.imageEventText.updateBodyText(DESCRIPTIONS[3] + goldAmt + DESCRIPTIONS[4]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(OPTIONS[9]);
                    break;
                case 1:
                    this.screen = Screen.LEAVE;
                    AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new Shovel());
                    this.imageEventText.updateBodyText(DESCRIPTIONS[5]);
                    this.imageEventText.clearAllDialogs();
                    this.imageEventText.setDialogOption(OPTIONS[9]);
                    break;
            }
        } else {
            this.openMap();
        }
    }

    private void clearingScreen() {
        this.screen = Screen.CLEARING;
        this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
        this.imageEventText.clearAllDialogs();
        if (AbstractDungeon.player.hasRelic(Shovel.ID)) {
            this.imageEventText.setDialogOption(OPTIONS[5]);
            this.imageEventText.setDialogOption(OPTIONS[8], true);
        } else {
            this.imageEventText.setDialogOption(OPTIONS[6], true);
            this.imageEventText.setDialogOption(OPTIONS[7]);
        }
    }
}
