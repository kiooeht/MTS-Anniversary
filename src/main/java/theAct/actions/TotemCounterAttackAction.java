//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theAct.monsters.TotemBoss.AbstractTotemSpawn;
import theAct.monsters.TotemBoss.TotemBoss;

public class TotemCounterAttackAction extends AbstractGameAction {
    private AbstractTotemSpawn m;

    public TotemCounterAttackAction(AbstractTotemSpawn m) {
        this.m = m;
    }

    public void update() {
        if (!m.isDead && !m.isDying) {
            m.totemAttack();
        }

        this.isDone = true;


    }

}
