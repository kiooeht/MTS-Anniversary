package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import theAct.TheActMod;
import theAct.dungeons.Jungle;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class AbstractRoomUpdateIncrementElitesPatch {

    @SpireInsertPatch(
            locator = Locator.class
    )
    public static void Insert(AbstractRoom __instance) {
        if (CardCrawlGame.dungeon instanceof Jungle) {
            ++CardCrawlGame.elites2Slain;
            TheActMod.logger.info("Jungle elites: " + CardCrawlGame.elites2Slain);
        }
    }

    public static class Locator extends SpireInsertLocator {
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(CardCrawlGame.class, "dungeon");

            return new int[] {LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<>(), finalMatcher)[1]};
        }
    }
}
