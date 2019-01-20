package theAct.events;

import java.util.Iterator;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import basemod.helpers.BaseModCardTags;
import theAct.TheActMod;
import theAct.cards.colorless.Gourd;

public class JungleGarden extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("JungleGarden");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String IMAGE = TheActMod.assetPath("images/events/JungleGarden.png");

    private int healAmt;
    private Screen screen = Screen.INTRO;
    private static enum Screen {
        INTRO,
        LEAVE;
    }

    public JungleGarden() {
        super(NAME, DESCRIPTIONS[0], IMAGE);
        healAmt = AbstractDungeon.player.maxHealth * 14 / 100;
        this.imageEventText.setDialogOption(OPTIONS[0], new Gourd());
        this.imageEventText.setDialogOption(OPTIONS[1] + healAmt + OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int buttonPressed) {
        if (this.screen == Screen.INTRO) {
            switch (buttonPressed) {
                case 0:
                    replaceDefends();
                    this.imageEventText.updateBodyText(DESCRIPTIONS[1]);
                    break;
                case 1:
                    AbstractDungeon.player.heal(healAmt);
                    this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                    break;
            }
            this.screen = Screen.LEAVE;
            this.imageEventText.clearAllDialogs();
            this.imageEventText.setDialogOption(OPTIONS[3]);
        } else {
            this.openMap();
        }
    }

    private void replaceDefends() {
        for (Iterator<AbstractCard> i = AbstractDungeon.player.masterDeck.group.iterator(); i.hasNext(); ) {
            AbstractCard e = i.next();
            if (e.hasTag(BaseModCardTags.BASIC_DEFEND)) i.remove();
        }
        for (int i = 0; i < 5; ++i) {
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Gourd(), (float)Settings.WIDTH / 2.0f, (float)Settings.HEIGHT / 2.0f));
        }
    }
}
