package theAct.events;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.DialogWord;
import theAct.TheActMod;

public class River extends AbstractImageEvent
{
    private enum CUR_SCREEN { MAIN, COMPLETE}

    public static final String ID = TheActMod.makeID("River");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final int REMOVE_CARDS = 3;

    private int cardsToRemove = REMOVE_CARDS;

    private CUR_SCREEN screen;

    public River()
    {
        super(NAME, DESCRIPTIONS[0], null);
        if(AbstractDungeon.player.masterDeck.group.size() < REMOVE_CARDS)
            cardsToRemove = AbstractDungeon.player.masterDeck.group.size();
        imageEventText.setDialogOption(OPTIONS[0] + cardsToRemove + OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
        screen = CUR_SCREEN.MAIN;
    }

    public void update ()
    {
        super.update();
        if (!AbstractDungeon.isScreenUp &&  !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            CardCrawlGame.sound.play("CARD_EXHAUST");
            for(int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractDungeon.topLevelEffects.add(new com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect(
                     AbstractDungeon.gridSelectScreen.selectedCards.get(i),
                  com.megacrit.cardcrawl.core.Settings.WIDTH / 2 + (200.0f * (i - 1) * Settings.scale),
                  com.megacrit.cardcrawl.core.Settings.HEIGHT / 2));
                AbstractDungeon.player.masterDeck.removeCard(AbstractDungeon.gridSelectScreen.selectedCards.get(i));
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    @Override
    protected void buttonEffect(int i)
    {
        switch (screen) {
        case MAIN:
            switch (i) {
            case 0:
                this.imageEventText.updateBodyText(DESCRIPTIONS[1], DialogWord.AppearEffect.GROW_IN);
                if(cardsToRemove > 0)
                    AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck.getPurgeableCards(),
                        cardsToRemove, OPTIONS[3] + cardsToRemove + OPTIONS[4], false, false, false, true);
                this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                this.imageEventText.clearRemainingOptions();
                this.screen = CUR_SCREEN.COMPLETE;
                break;
            case 1:
                this.imageEventText.updateBodyText(DESCRIPTIONS[2]);
                this.imageEventText.updateDialogOption(0, OPTIONS[2]);
                this.imageEventText.clearRemainingOptions();
                this.screen = CUR_SCREEN.COMPLETE;
                break;
            }
            break;
        case COMPLETE:
            openMap();
        }
    }
}
