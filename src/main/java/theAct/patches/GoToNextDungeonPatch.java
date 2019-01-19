package theAct.patches;

import basemod.CustomEventRoom;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.helpers.EventHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.ui.buttons.ProceedButton;
import javassist.CtBehavior;
import theAct.events.ForkInTheRoad;

@SpirePatch(
        clz=ProceedButton.class,
        method="goToNextDungeon"
)
public class GoToNextDungeonPatch
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static SpireReturn<Void> Insert(ProceedButton __instance, AbstractRoom room)
    {
        if (CardCrawlGame.dungeon instanceof Exordium) {
            AbstractDungeon.currMapNode.room = new ForkEventRoom();
            AbstractDungeon.getCurrRoom().onPlayerEntry();
            AbstractDungeon.rs = AbstractDungeon.RenderScene.EVENT;

            return SpireReturn.Return(null);
        }

        return SpireReturn.Continue();
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractDungeon.class, "fadeOut");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

    public static class ForkEventRoom extends EventRoom
    {
        @Override
        public void onPlayerEntry()
        {
            AbstractDungeon.overlayMenu.proceedButton.hide();
            String eventName = AbstractDungeon.eventList.remove(0);
            this.event = new ForkInTheRoad();
            this.event.onEnterRoom();
        }
    }
}
