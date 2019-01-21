//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theAct.monsters.TotemBoss.TotemBoss;

public class TotemBossMakeUntargetable extends AbstractGameAction {
    private TotemBoss m;

    public TotemBossMakeUntargetable(TotemBoss m) {
        this.m = m;
    }

    public void update() {

        m.isDying = true;
        this.isDone = true;


    }

}
