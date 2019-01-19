package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import theAct.powers.RegenerativeSlime;

public class SlimyTreeVines extends AbstractMonster {
    public static final String ID = "theJungle:SlimyTreeVines";
    public static final String ENCOUNTER_NAME = ID;
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = 10.0f;
    private static final float HB_W = 210.0f;
    private static final float HB_H = 220.0f;
    // stats
    private static final int HP_MIN = 120;
    private static final int HP_MAX = 130;
    private static final int HP_MIN_A = HP_MIN + 10;
    private static final int HP_MAX_A = HP_MAX + 10;
    private int attackDmg, attackDmg2;
    private int slimedAmt = 2;
    private int healAmt = 3;
    // moves
    private static final byte TANGLE = 1;
    private static final byte ATTACK = 2;
    private static final byte ATTACK_2 = 3;

    public SlimyTreeVines() {
        this(0,0);
    }

    public SlimyTreeVines(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.loadAnimation("theActAssets/images/monsters/SlimeTreeVines/skeleton.atlas", "theActAssets/images/monsters/SlimeTreeVines/skeleton.json", 1.0f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.attackDmg = 18;
            this.attackDmg2 = 10;
        } else {
            this.attackDmg = 15;
            this.attackDmg2 = 8;
        }
        this.damage.add(new DamageInfo(this, attackDmg));
        this.damage.add(new DamageInfo(this, attackDmg2));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new RegenerativeSlime(this, healAmt)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case TANGLE: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), this.slimedAmt));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.BLUNT_LIGHT));
                break;
            }
            case ATTACK_2: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AttackEffect.BLUNT_LIGHT));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Slimed(), 1));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int num) {
        if (!this.lastMove(TANGLE) && num < 33) {
            this.setMove(MOVES[0], TANGLE, Intent.STRONG_DEBUFF);
            return;
        } else if (num < 50 && !this.lastTwoMoves(ATTACK) ) {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(ATTACK_2, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        }
    }
}