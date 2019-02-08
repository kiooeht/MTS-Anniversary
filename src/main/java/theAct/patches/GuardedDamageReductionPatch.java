package theAct.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import theAct.powers.GuardedPower;

@SpirePatch(
        clz=AbstractMonster.class,
        method="damage",
        paramtypez={DamageInfo.class}
)
public class GuardedDamageReductionPatch {

	@SpireInsertPatch(rloc = 0)
    public static void Insert(AbstractMonster __instance, DamageInfo info) {
    	if (info.output > 0 && __instance.hasPower(GuardedPower.POWER_ID)) {
            info.output /= 5;
        }
    }
}
