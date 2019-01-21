package theAct.patches;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.cards.blue.ColdSnap;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.monsters.Flameango;

@SpirePatch(
        clz = ColdSnap.class,
        method = "use"
)
public class ColdSnapFlameangoPatch
{
    public static void Prefix(ColdSnap __instance, AbstractPlayer p, AbstractMonster m)
    {
        if(m.id.equals(Flameango.ID))
        {
            AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(m, p));
        }
    }
}

