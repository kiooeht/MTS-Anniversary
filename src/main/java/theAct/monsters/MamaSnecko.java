package theAct.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import theAct.TheActMod;
import theAct.powers.RandomizePower;

public class MamaSnecko extends AbstractMonster {

    public static final String ID = TheActMod.makeID("MamaSnecko");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = -30.0F;
    private static final float HB_Y = -20.0F;
    private static final float HB_W = 310.0f;
    private static final float HB_H = 400.0f;
    private static final int HP_MIN = 130;
    private static final int HP_MAX = 140;
    private static final int ASC_HP_MIN = 140;
    private static final int ASC_HP_MAX = 150;
    private static final byte EGGS = 1;
    private static final byte GLARE = 2;
    private static final byte TAIL = 3;
    private static final byte BITE = 4;
    private static final byte FURY = 5;
    private static final String EGGS_NAME = MOVES[0];
    private static final String GLARE_NAME = MOVES[1];
    private static final String TAIL_NAME = MOVES[2];
    private static final String BITE_NAME = MOVES[3];
    private static final String FURY_NAME = MOVES[4];
    private static final int TAIL_DAMAGE = 8;
    private static final int ASC_TAIL_DAMAGE = 10;
    private static final int CONFUSE_AMOUNT = 8;
    private static final int ASC_CONFUSE_AMOUNT = 12;
    private static final int TAIL_VULN = 2;
    private static final int ASC_TAIL_WEAK = 2;
    private static final int BITE_DAMAGE = 15;
    private static final int ASC_BITE_DAMAGE = 18;
    private static final int FURY_DAMAGE = 30;
    private static final int ASC_FURY_DAMAGE = 36;
    private int confuseAmount;
    private int tailDamage;
    private int tailVuln;
    private int biteDamage;
    private int furyDamage;

    public boolean waitingForEggs = false;
    public boolean randomAction = false;
    public boolean firstTurn = true;

    public MamaSnecko() {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, 175, 0);
        this.loadAnimation(
                TheActMod.assetPath("images/monsters/MamaSnecko/skeleton.atlas"),
                TheActMod.assetPath("images/monsters/MamaSnecko/skeleton.json"),
                0.7f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Hit", "Idle", 0.1f);
        e.setTimeScale(0.8f);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        this.confuseAmount = CONFUSE_AMOUNT;
        this.tailDamage = TAIL_DAMAGE;
        this.tailVuln = TAIL_VULN;
        this.biteDamage = BITE_DAMAGE;
        this.furyDamage = FURY_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.confuseAmount = ASC_CONFUSE_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            this.biteDamage = ASC_BITE_DAMAGE;
            this.furyDamage = ASC_FURY_DAMAGE;
            this.tailDamage = ASC_TAIL_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, tailDamage));
        this.damage.add(new DamageInfo(this, biteDamage));
        this.damage.add(new DamageInfo(this, furyDamage));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case TAIL: {
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                if (AbstractDungeon.ascensionLevel >= 17) {
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, ASC_TAIL_WEAK, true), 2));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, tailVuln, true), 2));
                break;
            }
            case BITE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "Attack_2"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy()), 0.3f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case FURY: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "Attack_2"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3f));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy()), 0.3f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case GLARE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "Attack"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SNECKO_GLARE"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5f));
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(AbstractDungeon.player, 1.0f, 1.0f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new RandomizePower(AbstractDungeon.player,this.confuseAmount),this.confuseAmount));
                break;
            }
            case EGGS:{
                this.waitingForEggs = true;
                int posToAvoid = -1;
                for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != null && !m.isDying) {
                        if (m instanceof SneckoEgg) posToAvoid = ((SneckoEgg) m).posIndex;
                        if (m instanceof BabySnecko) posToAvoid = ((BabySnecko) m).posIndex;
                    }
                }
                if (posToAvoid != 0) AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new SneckoEgg(-160.0f, -20.0f, 4),true, -4));
                if (posToAvoid != 1) AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new SneckoEgg(-320.0f, 30.0f, 3),true, -3));
                if (posToAvoid != 2) AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new SneckoEgg(-480.0f, -20.0f, 2),true,-2 ));
                if (posToAvoid != 3 && posToAvoid >= 0) AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new SneckoEgg(-640.0f, 30.0f, 1),true, -1));
            //AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(this.gremlins));
               // AbstractDungeon.actionManager.addToBottom(new SummonGremlinAction(this.gremlins));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void changeState(final String stateName) {
        switch (stateName) {
            case "Attack": {
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0f);
                break;
            }
            case "Attack_2": {
                this.state.setAnimation(0, "Attack_2", false);
                this.state.addAnimation(0, "Idle", true, 0.0f);
                break;
            }
        }
    }

    public void getMove(int num) {
        // starts each combat with eggs -> glare -> tailwhip.
        // after that, does random moves between eggs (50% if space), tailwhip (20%), bite (30%).
        // if all 3 eggs are destroyed before hatching, next move is fury and subsequent moves are random.
        if (waitingForEggs && numAliveEggs() == 0){
            this.randomAction = true;
            this.waitingForEggs = false;
            AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this,"Enraged!"));
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "Attack"));
            this.setMove(FURY_NAME,FURY,Intent.ATTACK,this.damage.get(2).base);
        }
        else if (randomAction){
            if (num < 50) {
                if (numAliveMinions() <= 1 && !this.lastTwoMoves(EGGS)) {
                    this.setMove(EGGS_NAME,EGGS,Intent.UNKNOWN);
                } else {
                    this.getMove(AbstractDungeon.aiRng.random(50, 99));
                }
            }
            else if (num < 70 && !this.lastTwoMoves(TAIL)){
                this.setMove(TAIL_NAME,TAIL,Intent.ATTACK_DEBUFF,this.damage.get(0).base);
            }
            else if (!this.lastTwoMoves(BITE)){
                this.setMove(BITE_NAME,BITE,Intent.ATTACK,this.damage.get(1).base);
            }
            else{
                this.setMove(TAIL_NAME,TAIL,Intent.ATTACK_DEBUFF,this.damage.get(0).base);
            }
        }
        else{
            if (this.firstTurn){
                this.firstTurn = false;
                this.setMove(EGGS_NAME,EGGS,Intent.UNKNOWN);
            }
            else{
                if (this.lastMove(EGGS)){
                    this.setMove(GLARE_NAME,GLARE,Intent.DEBUFF);
                }
                else if (this.lastMove(GLARE)){
                    this.randomAction = true;
                    this.setMove(TAIL_NAME,TAIL,Intent.ATTACK_DEBUFF,this.damage.get(0).base);
                }
            }
        }
    }

    @Override
    public void damage(final DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0f);
        }
    }

    @Override
    public void die() {
        super.die();
        for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDead && !m.isDying) {
                if (m instanceof SneckoEgg) {
                    AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                    AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
                }
                else{
                    AbstractDungeon.actionManager.addToTop(new EscapeAction(m));
                }
            }
        }
        CardCrawlGame.sound.play("SNECKO_DEATH");
    }

    private int numAliveMinions() {
        int count = 0;
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m != this && !m.isDying) {
                ++count;
            }
        }
        return count;
    }

    private int numAliveEggs() {
        int count = 0;
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && m instanceof SneckoEgg && !m.isDying) {
                ++count;
            }
        }
        return count;
    }
}
