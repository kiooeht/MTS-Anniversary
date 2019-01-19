package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import theAct.TheActMod;
import theAct.dungeons.Jungle;

import java.lang.reflect.Field;

import static com.megacrit.cardcrawl.rooms.AbstractRoom.RoomPhase.COMBAT;

@SpirePatch(
        clz = AbstractRoom.class,
        method = "update"
)
public class AbstractRoomUpdateIncrementElitesPatch {

    public static void Postfix(AbstractRoom __instance) {
        try {
            Field endBattleTimerField = AbstractRoom.class.getDeclaredField("endBattleTimer");
            endBattleTimerField.setAccessible(true);
            float endBattleTimer = (float) endBattleTimerField.get(__instance);
            if (__instance.phase == COMBAT
                    && __instance.isBattleOver
                    && AbstractDungeon.actionManager.actions.isEmpty()
                    && endBattleTimer == 0.0f
                    && __instance instanceof MonsterRoomElite
                    && !AbstractDungeon.loading_post_combat
                    && CardCrawlGame.dungeon instanceof Jungle
            ) {
                ++CardCrawlGame.elites2Slain;
                TheActMod.logger.info("Jungle elites: " + CardCrawlGame.elites2Slain);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
