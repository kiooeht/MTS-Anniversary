package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;

@SpirePatch(
        clz = FlameBarrierPower.class,
        method = "atStartOfTurn"
)
public class FlameBarrierPatch
{
    public static void Replace(FlameBarrierPower __instance)
    {
        AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(__instance.owner, __instance.owner, "Flame Barrier"));
    }
}
