package theAct.events;

import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theAct.TheActMod;
import theAct.relics.SneckoAutograph;
import theAct.relics.SpiritDisease;

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
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CardLibrary.getCopy(Regret.ID), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new SneckoAutograph());
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1:
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 2:
                        imageEventText.updateDialogOption(0, OPTIONS[3]);
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
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
