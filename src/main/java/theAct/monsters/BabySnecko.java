package theAct.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.FastShakeAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import theAct.TheActMod;
import theAct.powers.RandomizePower;

public class BabySnecko extends AbstractMonster {

    public static final String ID = TheActMod.makeID("BabySnecko");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = -30.0F;
    private static final float HB_Y = -20.0F;
    private static final float HB_W = 135.0f;
    private static final float HB_H = 200.0f;
    private static final int HP_MIN = 25;
    private static final int HP_MAX = 30;
    private static final int ASC_HP_MIN = 28;
    private static final int ASC_HP_MAX = 33;
    private static final byte BITE = 1;
    private static final byte GLARE = 2;
    private static final String BITE_NAME = MOVES[0];
    private static final String GLARE_NAME = MOVES[1];
    private static final int CONFUSE_AMOUNT = 3;
    private static final int ASC_CONFUSE_AMOUNT = 4;
    private static final int BITE_DAMAGE = 12;
    private static final int ASC_BITE_DAMAGE = 15;
    private int biteDamage;
    private int confuseAmount;

    public int posIndex;

    public BabySnecko(final float x, final float y, final int posIndex) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.loadAnimation(
                TheActMod.assetPath("images/monsters/BabySnecko/BabySnecko.atlas"),
                TheActMod.assetPath("images/monsters/BabySnecko/BabySnecko.json"), 1f);
        final AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.type = EnemyType.NORMAL;
        this.posIndex = posIndex;
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        this.confuseAmount = CONFUSE_AMOUNT;
        this.biteDamage = BITE_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.confuseAmount = ASC_CONFUSE_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            this.biteDamage = ASC_BITE_DAMAGE;
        }
        this.damage.add(new DamageInfo(this, biteDamage));
    }

    @Override
    public void changeState(final String stateName) {
        switch (stateName) {
            case "ATTACK": {
                this.state.setAnimation(0, "boop", false);
                this.state.addAnimation(0, "idle", true, 0.0f);
                break;
            }
        }
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case BITE: {
                AbstractDungeon.actionManager.addToBottom(new AnimateFastAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy()), 0.3f));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                break;
            }
            case GLARE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SNECKO_GLARE"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5f));
                AbstractDungeon.actionManager.addToBottom(new FastShakeAction(AbstractDungeon.player, 1.0f, 1.0f));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this,  new RandomizePower(AbstractDungeon.player,this.confuseAmount),this.confuseAmount));
                break;
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void getMove(int num) {
        // glares or bites at random, can't repeat 3 times in a row
        if (num < 50 && !lastTwoMoves(GLARE)){
            this.setMove(GLARE_NAME,GLARE,Intent.DEBUFF);
        }
        else if (!lastTwoMoves(BITE)){
            this.setMove(BITE_NAME,BITE,Intent.ATTACK,damage.get(0).base);
        }
        else{
            this.setMove(GLARE_NAME,GLARE,Intent.DEBUFF);
        }
    }
}
