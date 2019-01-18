package theAct.patches;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.campfire.CampfireBurningEffect;

import java.lang.reflect.Field;

@SpirePatch(
        clz = CampfireBurningEffect.class,
        method = SpirePatch.CONSTRUCTOR
)
public class CampfireBurningEffectPatch {

    public static void Postfix(CampfireBurningEffect __instance) {
        if (CardCrawlGame.dungeon instanceof TheJungle) {
            try {
                Field colorField = AbstractGameEffect.class.getDeclaredField("color");
                colorField.setAccessible(true);
                colorField.set(__instance, new Color(1.0f, 1.0f, 1.0f, 0.0f)); //TODO: find appropriate color
                ((Color)colorField.get(__instance)).r -= 0.0f;
                ((Color)colorField.get(__instance)).g -= 0.0f; //TODO: find appropriate variance
                ((Color)colorField.get(__instance)).b -= 0.0f; //MathUtils.random(0.5f), etc
            } catch (NoSuchFieldException | IllegalAccessException E) {
                //logger info
                E.printStackTrace();
            }
        }
    }
}
