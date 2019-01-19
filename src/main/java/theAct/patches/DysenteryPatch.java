package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.screens.DeathScreen;
import theAct.TheActMod;
import theAct.relics.SpiritDisease;

@SpirePatch(
        clz = DeathScreen.class,
        method = "getDeathText"
)
public class DysenteryPatch
{
    public static SpireReturn<String> Prefix(DeathScreen __instance)
    {
        if(AbstractDungeon.player.hasRelic(SpiritDisease.ID)) {
            if(!AbstractDungeon.player.getRelic(SpiritDisease.ID).usedUp) {
                return SpireReturn.Return(CardCrawlGame.languagePack.getUIString(TheActMod.makeID("DysenteryEasterEgg")).TEXT[0]);
            }
        }
        return SpireReturn.Continue();
    }
}
