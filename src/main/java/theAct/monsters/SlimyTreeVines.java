package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateShakeAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.AcidSlime_M;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.RegenPower;

import theAct.powers.RegenerativeSlimePower;

public class SlimyTreeVines extends AbstractMonster {
    public static final String ID = "theJungle:SlimyTreeVines";
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = 220.0f;
    private static final float HB_W = 330.0f;
    private static final float HB_H = 320.0f;
    // stats
    private static final int HP_MIN = 115;
    private static final int HP_MAX = 120;
    private static final int HP_MIN_A = HP_MIN + 6;
    private static final int HP_MAX_A = HP_MAX + 6;
    private int attackDmg, attackDmg2, attackDmg3;
    private int slimedAmt = 3;
    private int healAmt = 3;
    private int constrictDmg;
    private int playerRegen = 3;
    private int splitThreshold = 45;
    // moves
    private static final byte TANGLE = 1;
    private static final byte ATTACK = 2;
    private static final byte ATTACK_2 = 3;
    private static final byte ATTACK_3 = 4;
    private static final byte SPLIT = 5;
    private boolean doneTangle = false;
    private boolean doneSplit = false;

    public SlimyTreeVines() {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, 0, 0);
        this.loadAnimation("theActAssets/images/monsters/SlimyTreeVines/skeleton.atlas", "theActAssets/images/monsters/SlimyTreeVines/skeleton.json", 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 15;
            this.attackDmg2 = 10;
            this.attackDmg3 = 13;
            this.constrictDmg = 4;
        } else {
            this.attackDmg = 12;
            this.attackDmg2 = 8;
            this.attackDmg3 = 10;
            this.constrictDmg = 3;
        }
        this.damage.add(new DamageInfo(this, attackDmg));
        this.damage.add(new DamageInfo(this, attackDmg2));
        this.damage.add(new DamageInfo(this, attackDmg3));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerativeSlimePower(this, healAmt)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TANGLE: {
                doneTangle = true;
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.slimedAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new ConstrictedPower(AbstractDungeon.player, this, this.constrictDmg)));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RegenPower(AbstractDungeon.player, this.playerRegen)));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.BLUNT_HEAVY));
                break;
            }
            case ATTACK_2: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 2));
                break;
            }
            case ATTACK_3: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AttackEffect.BLUNT_HEAVY));
                break;
            }
            case SPLIT: {
                doneSplit = true;
                this.name = DIALOG[0];
                AbstractDungeon.actionManager.addToBottom(new AnimateShakeAction(this, 1.0f, 0.1f));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(1.0f));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("SLIME_SPLIT"));
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new AcidSlime_M(-254.0f, MathUtils.random(-4.0f, 4.0f)), false));
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, RegenerativeSlimePower.POWER_ID));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void die() {
        super.die();
        if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(AbstractDungeon.player, this, ConstrictedPower.POWER_ID));
        }
    }

    @Override
    protected void getMove(int num) {
        if (!doneTangle && num < 75) {
            this.setMove(MOVES[0], TANGLE, Intent.STRONG_DEBUFF);
        } else if (!doneSplit && this.currentHealth < splitThreshold) {
            this.setMove(MOVES[1], SPLIT, Intent.UNKNOWN);
        } else if (doneSplit) {
            this.setMove(ATTACK_3, Intent.ATTACK, this.damage.get(2).base);
        } else if (num < 50 && !this.lastTwoMoves(ATTACK) || this.lastMove(ATTACK_2)) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(ATTACK_2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
    }
}