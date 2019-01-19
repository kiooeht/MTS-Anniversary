package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theAct.relics.PaperFaux;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = SpirePatch.CONSTRUCTOR,
        paramtypez = {AbstractCreature.class, AbstractCreature.class, AbstractPower.class, int.class, boolean.class, AbstractGameAction.AttackEffect.class}
)
public class PaperFauxApplyPowerActionPatch {

    public static void Postfix(ApplyPowerAction __instance, AbstractCreature target, AbstractCreature source, AbstractPower powerToApply, int stackAmount, boolean isFast, AbstractGameAction.AttackEffect effect) {
        if (AbstractDungeon.player.hasRelic(PaperFaux.ID)) {
            if (source != null && source.isPlayer && target != source && (powerToApply.ID.equals(WeakPower.POWER_ID) || powerToApply.ID.equals(VulnerablePower.POWER_ID))) {
                AbstractDungeon.player.getRelic(PaperFaux.ID).flash();
                __instance.amount += PaperFaux.EXTRA_AMOUNT;
                powerToApply.amount += PaperFaux.EXTRA_AMOUNT;
            }
        }
    }
}
