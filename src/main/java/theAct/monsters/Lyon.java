package theAct.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.animations.ShoutAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConstrictedPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import com.megacrit.cardcrawl.vfx.combat.ShockWaveEffect;

import theAct.powers.PridePower;

public class Lyon extends AbstractMonster {
    public static final String ID = "theJungle:Lyon";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    // location
    private static final float HB_X = 0.0f;
    private static final float HB_Y = 0.0f;
    private static final float HB_W = 330.0f;
    private static final float HB_H = 280.0f;
    // stats
    private static final int HP_MIN = 95;
    private static final int HP_MAX = 100;
    private static final int HP_MIN_A = HP_MIN + 6;
    private static final int HP_MAX_A = HP_MAX + 6;
    private int attackDmg, attackDmg2;
    private int debuffAmt;
    private static final int ATTACK_TIMES = 2;
    private static final int DEBUFF_AMT = 3;
    private static final int DEBUFF_AMT_ASC_MODIFIER = 2;
    private static final int ATTACK_DMG = 18;
    private static final int ATTACK_DMG_ASC_MODIFIER = 2;
    private static final int POUNCE_DMG = 5;
    private static final int POUNCE_DMG_ASC_MODIFIER = 1;
    // moves
    private static final byte ROAR = 1;
    private static final byte ATTACK = 2;
    private static final byte POUNCE = 3;
    private boolean doneRoar = false;

    public Lyon() {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, 0, 0);
        this.loadAnimation("theActAssets/images/monsters/lyon/Lyon.atlas", "theActAssets/images/monsters/lyon/Lyon.json", 1.25f);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.dialogX = -160.0f * Settings.scale;
        this.dialogY = 40.0f * Settings.scale;
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(HP_MIN_A, HP_MAX_A);
        } else {
            this.setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            attackDmg = ATTACK_DMG + ATTACK_DMG_ASC_MODIFIER;
            attackDmg2 = POUNCE_DMG + POUNCE_DMG_ASC_MODIFIER;
        } else {
            attackDmg = ATTACK_DMG;
            attackDmg2 = POUNCE_DMG;
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            debuffAmt = DEBUFF_AMT + DEBUFF_AMT_ASC_MODIFIER;
        } else {
            debuffAmt = DEBUFF_AMT;
        }
        this.damage.add(new DamageInfo(this, attackDmg));
        this.damage.add(new DamageInfo(this, attackDmg2));
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PridePower(this, 1)));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case ROAR: {
                doneRoar = true;
                AbstractDungeon.actionManager.addToBottom(new SFXAction("INTIMIDATE"));
                AbstractDungeon.actionManager.addToBottom(new ShoutAction(this, DIALOG[0], 1.0f, 2.0f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new ShockWaveEffect(this.hb.cX, this.hb.cY, Color.YELLOW, ShockWaveEffect.ShockWaveType.CHAOTIC), 1.25f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, debuffAmt, true), debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, debuffAmt, true)));
                break;
            }
            case POUNCE: {
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.BLUNT_HEAVY));
                break;
            }
            case ATTACK: {
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                for (int i = 0 ; i < ATTACK_TIMES ; ++i) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AttackEffect.SLASH_DIAGONAL));
                }
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
        if (!doneRoar) {
            this.setMove(MOVES[0], ROAR, Intent.STRONG_DEBUFF);
        } else if (num < 50 && !this.lastTwoMoves(POUNCE) || this.lastTwoMoves(ATTACK)) {
            this.setMove(MOVES[1], POUNCE, Intent.ATTACK, this.damage.get(0).base);
        } else {
            this.setMove(ATTACK, Intent.ATTACK, this.damage.get(1).base, ATTACK_TIMES, true);
        }
    }
}