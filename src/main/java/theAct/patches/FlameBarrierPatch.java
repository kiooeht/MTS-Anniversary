package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;

@SpirePatch(
        clz = FlameBarrierPower.class,
        method = "atStartOfTurn"
)
public class FlameBarrierPatch
{
    public static SpireReturn Prefix(FlameBarrierPower __instance)
    {
        if (!(__instance.owner instanceof AbstractPlayer)) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "Flame Barrier"));
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}
