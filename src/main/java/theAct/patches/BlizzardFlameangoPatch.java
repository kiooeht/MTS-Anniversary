package theAct.patches;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.blue.Blizzard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theAct.monsters.Flameango;

@SpirePatch(
        clz = Blizzard.class,
        method = "use"
)
public class BlizzardFlameangoPatch
{
    public static void Prefix(Blizzard __instance, AbstractPlayer p, AbstractMonster m)
    {
        AbstractRoom currRoom =  AbstractDungeon.getCurrRoom();
        int monsters = currRoom.monsters.monsters.size();
        for(int i = 0; i < monsters; i++) {
            AbstractMonster mon = currRoom.monsters.monsters.get(i);
            if(mon.isDying || mon.isEscaping || mon.currentHealth > 0) {
                if (mon.id.equals(Flameango.ID)) {
                    AbstractDungeon.actionManager.addToBottom(new StunMonsterAction(mon, p));
                }
            }
        }
    }
}
