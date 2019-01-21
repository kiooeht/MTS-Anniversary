package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import theAct.events.StalkMarket;

import java.util.ArrayList;

@SpirePatch(
        clz = AbstractDungeon.class,
        method = "getEvent"
)
public class StalkMarketCanSpawnPatch {

    @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"tmp"}
    )
    public static void Insert(Random rng, ArrayList<String> tmp) {
        if (!StalkMarket.canSpawn()) {
            tmp.remove(StalkMarket.ID);
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
