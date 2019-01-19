package theAct.events;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theAct.TheActMod;
import theAct.cards.PetSnecko;

public class SneckoCultEvent extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("SneckoCultEvent");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int screenNum = 0;
    private boolean cardSelected = false;

    public SneckoCultEvent() {
        super(NAME, DESCRIPTIONS[0], "theActAssets/images/SneckoCultEvent.png");
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1], new PetSnecko());
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screenNum) {
            case 0:
                switch(i) {
                    case 0:
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        randomizeCost();
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                    case 1:
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new PetSnecko(), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        screenNum = 1;
                        break;
                    case 2:
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        screenNum = 1;
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        break;
                    default:
                        screenNum = 1;
            }
            break;
            case 1:
                openMap();
        }
    }

    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            int rng = AbstractDungeon.miscRng.random(3);
            cardSelected = true;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            c.cost = rng;
            c.costForTurn = rng;
            c.upgradedCost = true;
            c.isCostModified = true;
            AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy()));
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private void randomizeCost() {
        if (!AbstractDungeon.isScreenUp) {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[4], false, false, false, false);
        } else {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[4], false, false, false, false);
        }
    }
}
