package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.TheActMod;

public class SneckoIdol extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("SneckoIdol");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int screenNum = 0;

    public SneckoIdol() {
        super(NAME, DESCRIPTIONS[0], TheActMod.assetPath("images/events/SneckoIdol.png"));
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);
        imageEventText.setDialogOption(OPTIONS[2]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch(i) {
                    case 0:
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1:
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 2:
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                openMap();
                break;
        }
    }
}
