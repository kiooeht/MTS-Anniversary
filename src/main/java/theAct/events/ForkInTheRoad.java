package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.events.GenericEventDialog;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.TheActMod;
import theAct.dungeons.Jungle;
import theAct.patches.GoToNextDungeonPatch;

public class ForkInTheRoad extends AbstractImageEvent
{
    public static final String ID = TheActMod.makeID("ForkInTheRoad");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final String BASE_IMG = TheActMod.assetPath("images/events/ForkInTheRoad.png");

    public ForkInTheRoad()
    {
        super(NAME, DESCRIPTIONS[0], BASE_IMG);
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
                TheActMod.wentToTheJungle = true;
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
