package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.relics.Circlet;
import theAct.TheActMod;
import theAct.relics.BirthdayCake;
import theAct.relics.BirthdayCakeSlice;

public class HappyBirthday extends AbstractImageEvent
{
    public static final String ID = TheActMod.makeID("HappyBirthday");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    public static final int SLICE_MAX_HP = 5;
    public static final int CAKE_MAX_HP = 15;

    public HappyBirthday()
    {
        super(NAME, DESCRIPTIONS[0], TheActMod.assetPath("images/events/HappyBirthday.jpg"));

        imageEventText.setDialogOption(String.format(OPTIONS[0], SLICE_MAX_HP));
        imageEventText.setDialogOption(String.format(OPTIONS[1], CAKE_MAX_HP));
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0:
                String relicID = Circlet.ID;
                switch (buttonPressed) {
                    case 0:
                        if (!AbstractDungeon.player.hasRelic(BirthdayCakeSlice.ID) && !AbstractDungeon.player.hasRelic(BirthdayCake.ID)) {
                            relicID = BirthdayCakeSlice.ID;
                        }
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        break;
                    case 1:
                        if (!AbstractDungeon.player.hasRelic(BirthdayCake.ID)) {
                            relicID = BirthdayCake.ID;
                        }
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        break;
                }
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0f, Settings.HEIGHT / 2.0f,
                        RelicLibrary.getRelic(relicID).makeCopy());

                screenNum = 1;
                imageEventText.updateDialogOption(0, OPTIONS[2]);
                imageEventText.clearRemainingOptions();
                break;
            default:
                openMap();
                break;
        }
    }
}
