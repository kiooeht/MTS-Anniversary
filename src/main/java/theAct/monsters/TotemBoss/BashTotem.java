//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import theAct.TheActMod;

public class BashTotem extends AbstractTotemSpawn {
    public static final String ID = TheActMod.makeID("BashTotem");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public Integer attackDmg;

    public BashTotem(TotemBoss boss) {
        super(NAME, ID, boss);


        if (AbstractDungeon.ascensionLevel >= 19) {
            this.attackDmg = 8;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.attackDmg = 7;
        } else {
            this.attackDmg = 8;
        }

        this.intentType = Intent.ATTACK;

        this.damage.add(new DamageInfo(this, this.attackDmg));
    }



    public void takeTurn() {
        float vfxSpeed = 0.1F;
        if (Settings.FAST_MODE) {
            vfxSpeed = 0.0F;
        }

        switch(this.nextMove) {
            case 1:
               // AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX - 60.0F * Settings.scale, AbstractDungeon.player.hb.cY, false), vfxSpeed));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.NONE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num)
    {
        this.setMove((byte)1, intentType, this.attackDmg);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }




}
