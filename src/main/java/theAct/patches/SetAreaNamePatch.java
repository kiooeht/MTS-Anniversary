package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import com.megacrit.cardcrawl.screens.DungeonTransitionScreen;
import javassist.CtBehavior;
import theAct.dungeons.Jungle;

@SpirePatch(
        clz=DungeonTransitionScreen.class,
        method="setAreaName"
)
public class SetAreaNamePatch
{
    @SpireInsertPatch(
            locator=Locator.class
    )
    public static void Insert(DungeonTransitionScreen __instance, String key)
    {
        if (Jungle.ID.equals(key)) {
            __instance.levelName = Jungle.NAME;
            __instance.levelNum = Jungle.NUM;
        }
    }

    private static class Locator extends SpireInsertLocator
    {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception
        {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractDungeon.class, "name");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}
