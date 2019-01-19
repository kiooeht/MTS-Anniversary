//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import theAct.monsters.TotemBoss.TotemBoss;

public class TotemFallWaitAction extends AbstractGameAction {
    private TotemBoss m;

    public TotemFallWaitAction(TotemBoss m) {
        this.m = m;
    }

    public void update() {

        m.stopTotemFall = false;
        this.isDone = true;


    }

}
