//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import com.megacrit.cardcrawl.vfx.combat.SmallLaserEffect;
import theAct.TheActMod;
import theAct.vfx.TotemBeamEffect;

import java.util.Iterator;

public class AttackAndShieldTotem extends AbstractTotemSpawn {
    public static final String ID = TheActMod.makeID("AttackAndShieldTotem");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public Integer attackDmg;
    public Integer secondaryEffect;

    public static Color totemColor = Color.PURPLE;

    public AttackAndShieldTotem(TotemBoss boss, boolean spawnedIn) {
        super(NAME, ID, boss, TheActMod.assetPath("images/monsters/totemboss/totemorange.png"), spawnedIn);
        this.loadAnimation(TheActMod.assetPath("images/monsters/totemboss/purple/Totem.atlas"), TheActMod.assetPath("images/monsters/totemboss/purple/Totem.json"), 1.0F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.attackDmg = 5;
            this.secondaryEffect = 4;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.attackDmg = 5;
            this.secondaryEffect = 3;
        } else {
            this.attackDmg = 4;
            this.secondaryEffect = 3;
        }

        this.totemLivingColor = totemColor;

        this.damage.add(new DamageInfo(this, this.attackDmg));

        this.intentType = Intent.ATTACK_DEFEND;

    }



    @Override
    public void totemAttack() {
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F)); AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.PURPLE)));
        AbstractDungeon.actionManager.addToBottom(new SFXAction("ATTACK_MAGIC_BEAM_SHORT", 0.5F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new TotemBeamEffect(this.hb.cX + beamOffsetX, this.hb.cY + beamOffsetY, AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.PURPLE.cpy(), this.hb.cX + beamOffsetX2, this.hb.cY + beamOffsetY2), 0.1F));
        AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.FIRE));

        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {

            if (!m.isDying && !(m instanceof TotemBoss) && m!=this) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.secondaryEffect));
            }
        }
    }

    public void getUniqueTotemMove() {

            this.setMove((byte) 1, Intent.ATTACK_DEFEND, this.attackDmg);

    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }




}
