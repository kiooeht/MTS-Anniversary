package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;

import java.util.ArrayList;

public class VictoryOrDeathScreenCreateGameOverStatsPatch { //TODO: victory/death screen will still score as "the city" if you die in a later act after going through jungle

    @SpirePatch(
            clz = VictoryScreen.class,
            method = "createGameOverStats"
    )
    public static class VictoryScreenPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(VictoryScreen __instance) {
            if (CardCrawlGame.dungeon instanceof TheJungle/* || WentThroughJungleBoolean*/) {
                try {
                    String elite2Points = Integer.toString((int) VictoryScreen.class.getDeclaredField("elite2Points").get(null));
                    __instance.stats.add(new GameOverStat(VictoryScreen.JUNGLE_ELITE.NAME + " (" + CardCrawlGame.elites2Slain + ")", null, elite2Points)); //TODO: JUNGLE_ELITE localization
                } catch (NoSuchFieldException | IllegalAccessException E) {
                    E.printStackTrace();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "dungeon");

                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = DeathScreen.class,
            method = "createGameOverStats"
    )
    public static class DeathScreenPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(DeathScreen __instance) {
            if (CardCrawlGame.dungeon instanceof TheJungle/* || WentThroughJungleBoolean*/) {
                try {
                    String elite2Points = Integer.toString((int) DeathScreen.class.getDeclaredField("elite2Points").get(null));
                    __instance.stats.add(new GameOverStat(DeathScreen.JUNGLE_ELITE.NAME + " (" + CardCrawlGame.elites2Slain + ")", null, elite2Points)); //TODO: JUNGLE_ELITE localization
                } catch (NoSuchFieldException | IllegalAccessException E) {
                    E.printStackTrace();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "dungeon");

                return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher);
            }
        }
    }
}
