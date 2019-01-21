package theAct.patches;

import com.badlogic.gdx.audio.Music;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.audio.MainMusic;
import theAct.TheActMod;

@SpirePatch(clz = MainMusic.class, method = "getSong")
public class JungleMusicPatchMain {

    @SpirePostfixPatch
    public static Music Postfix(Music __result, MainMusic __instance, String key) {
        TheActMod.logger.info("Music patch Main hit");

        switch (key) {
            case "JUNGLEMAIN": {
                return MainMusic.newMusic(TheActMod.assetPath("audio/music/jungle_main.ogg"));
            }
            default: {
                return __result;
            }
        }

    }

}