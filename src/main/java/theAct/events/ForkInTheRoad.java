package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.dungeons.Jungle;
import theAct.patches.GoToNextDungeonPatch;

public class ForkInTheRoad extends AbstractImageEvent
{
    public static final String ID = "theAct:ForkInTheRoad";
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    public ForkInTheRoad()
    {
        super(NAME, DESCRIPTIONS[0], null);
        imageEventText.setDialogOption(OPTIONS[1] + TheCity.NAME + OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[2] + Jungle.NAME + OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (buttonPressed) {
            case 0:
                CardCrawlGame.nextDungeon = TheCity.ID;
                break;
            case 1:
                CardCrawlGame.nextDungeon = Jungle.ID;
                break;
        }

        AbstractDungeon.rs = AbstractDungeon.RenderScene.NORMAL;
        if (AbstractDungeon.currMapNode.room instanceof GoToNextDungeonPatch.ForkEventRoom) {
            AbstractDungeon.currMapNode.room = ((GoToNextDungeonPatch.ForkEventRoom) AbstractDungeon.currMapNode.room).originalRoom;
        }
        GenericEventDialog.hide();

        AbstractDungeon.fadeOut();
        AbstractDungeon.isDungeonBeaten = true;
    }
}
