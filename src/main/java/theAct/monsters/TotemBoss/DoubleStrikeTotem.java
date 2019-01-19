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

public class DoubleStrikeTotem extends AbstractTotemSpawn {
    public static final String ID = TheActMod.makeID("DoubleStrikeTotem");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    public Integer attackDmg;

    public Integer secondaryEffect;

    public DoubleStrikeTotem(TotemBoss boss) {
        super(NAME, ID, boss);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.attackDmg = 4;
            this.secondaryEffect = 3;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.attackDmg = 3;
            this.secondaryEffect = 2;
        } else {
            this.attackDmg = 3;
            this.secondaryEffect = 2;
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
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX - 60.0F * Settings.scale, AbstractDungeon.player.hb.cY, false), vfxSpeed));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.NONE));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.NONE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }


    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }

    protected void getMove(int num)
    {
        this.setMove((byte)1, Intent.ATTACK, this.attackDmg, 2, true);
    }




}
