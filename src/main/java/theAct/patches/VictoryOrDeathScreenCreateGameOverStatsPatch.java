package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.Exordium;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.dungeons.TheEnding;
import com.megacrit.cardcrawl.localization.ScoreBonusStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.GameOverStat;
import com.megacrit.cardcrawl.screens.VictoryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theAct.TheActMod;
import theAct.dungeons.Jungle;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class VictoryOrDeathScreenCreateGameOverStatsPatch {

    @SpirePatch(
            clz = VictoryScreen.class,
            method = "createGameOverStats"
    )
    public static class VictoryScreenPatch {

        @SpireInsertPatch(
                locator = Locator.class
        )
        public static void Insert(VictoryScreen __instance) {
            boolean act3OrHigher = CardCrawlGame.dungeon instanceof TheBeyond || CardCrawlGame.dungeon instanceof TheEnding;
            if (CardCrawlGame.dungeon instanceof Exordium || CardCrawlGame.dungeon instanceof TheCity || (act3OrHigher && !TheActMod.wentToTheJungle)) {
                return;
            }
            if (act3OrHigher) {
                __instance.stats.remove(4);
                __instance.stats.remove(3);
            }
            if (CardCrawlGame.dungeon instanceof Jungle || TheActMod.wentToTheJungle) {
                try {
                    Field elite2PointsField = VictoryScreen.class.getDeclaredField("elite2Points");
                    elite2PointsField.setAccessible(true);
                    String elite2Points = Integer.toString((int) elite2PointsField.get(null));
                    __instance.stats.add(new GameOverStat(/*Jungle Elite localized String + */"The Jungle: (" + CardCrawlGame.elites2Slain + ")", null, elite2Points)); //TODO: JUNGLE_ELITE localization
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (act3OrHigher) {
                try {
                    Field elite3PointsField = VictoryScreen.class.getDeclaredField("elite3Points");
                    elite3PointsField.setAccessible(true);
                    String elite3Points = Integer.toString((int) elite3PointsField.get(null));
                    Field localizedStringField = VictoryScreen.class.getDeclaredField("BEYOND_ELITE");
                    localizedStringField.setAccessible(true);
                    String localizedString = ((ScoreBonusStrings)localizedStringField.get(null)).NAME;
                    __instance.stats.add(new GameOverStat(localizedString + " (" + CardCrawlGame.elites2Slain + ")", null, elite3Points));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");

                return new int[] {LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[5]};
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
            boolean act3OrHigher = CardCrawlGame.dungeon instanceof TheBeyond || CardCrawlGame.dungeon instanceof TheEnding;
            if (CardCrawlGame.dungeon instanceof Exordium || CardCrawlGame.dungeon instanceof TheCity || (act3OrHigher && !TheActMod.wentToTheJungle)) {
                return;
            }
            if (act3OrHigher) {
                __instance.stats.remove(4);
                __instance.stats.remove(3);
            }
            if (CardCrawlGame.dungeon instanceof Jungle || TheActMod.wentToTheJungle) {
                try {
                    Field elite2PointsField = DeathScreen.class.getDeclaredField("elite2Points");
                    elite2PointsField.setAccessible(true);
                    String elite2Points = Integer.toString((int) elite2PointsField.get(null));
                    __instance.stats.add(new GameOverStat(/*Jungle Elite localized String + */"PLACEHOLDER ELITES: (" + CardCrawlGame.elites2Slain + ")", null, elite2Points)); //TODO: JUNGLE_ELITE localization
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (act3OrHigher) {
                try {
                    Field elite3PointsField = DeathScreen.class.getDeclaredField("elite3Points");
                    elite3PointsField.setAccessible(true);
                    String elite3Points = Integer.toString((int) elite3PointsField.get(null));
                    Field localizedStringField = DeathScreen.class.getDeclaredField("BEYOND_ELITE");
                    localizedStringField.setAccessible(true);
                    String localizedString = ((ScoreBonusStrings)localizedStringField.get(null)).NAME;
                    __instance.stats.add(new GameOverStat(localizedString + " (" + CardCrawlGame.elites3Slain + ")", null, elite3Points));
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        public static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "add");

                return new int[] {LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[5]};
            }
        }
    }
}
