package theAct.patches;

import com.megacrit.cardcrawl.audio.*;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.badlogic.gdx.audio.*;
import theAct.TheActMod;

@SpirePatch(clz = TempMusic.class, method = "getSong")
public class JungleMusicPatchTemp {

    @SpirePostfixPatch
    public static Music Postfix(Music __result, TempMusic __instance, String key) {
        TheActMod.logger.info("Music patch Temp hit");
        switch (key) {
            case "JUNGLEELITE": {
                return MainMusic.newMusic(TheActMod.assetPath("audio/music/jungle_elite.ogg"));
            }
            case "BOSSTOTEM": {
                return MainMusic.newMusic(TheActMod.assetPath("audio/music/boss_totem.ogg"));
            }
            case "BOSSSPIDER": {
                return MainMusic.newMusic(TheActMod.assetPath("audio/music/boss_spider.ogg"));
            }
            case "BOSSMUSHROOM": {
                return MainMusic.newMusic(TheActMod.assetPath("audio/music/boss_mushroom.ogg"));
            }
            default: {
                return __result;
            }
        }

    }

}

