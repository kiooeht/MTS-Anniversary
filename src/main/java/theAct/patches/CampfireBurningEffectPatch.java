package theAct.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect;
import theAct.TheActMod;
import theAct.dungeons.Jungle;

import java.lang.reflect.Field;

@SpirePatch(
        clz = CampfireBurningEffect.class,
        method = SpirePatch.CONSTRUCTOR
)
public class CampfireBurningEffectPatch {

    public static void Postfix(CampfireBurningEffect __instance) {
        if (CardCrawlGame.dungeon instanceof Jungle) {
            //TheActMod.logger.info("CampfireBurningEffect patch reached for Jungle");
            try {
                Field colorField = AbstractGameEffect.class.getDeclaredField("color");
                colorField.setAccessible(true);
                colorField.set(__instance, new Color(MathUtils.random(0.4f, 0.6f), MathUtils.random(0.3f, 0.6f), MathUtils.random(0.0f, 0.2f), 0.0f));
                float shift = MathUtils.random(-0.5f, 0.4f);
                ((Color)colorField.get(__instance)).r += shift;
                ((Color)colorField.get(__instance)).g -= shift;
            } catch (NoSuchFieldException | IllegalAccessException E) {
                E.printStackTrace();
            }
        }
    }
}
