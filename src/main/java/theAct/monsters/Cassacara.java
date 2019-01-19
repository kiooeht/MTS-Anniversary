package theAct.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import theAct.TheActMod;

public class Cassacara extends AbstractMonster {

    public static final String ID = TheActMod.makeID("Cassacara");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = 8.0F;
    private static final float HB_Y = 136.0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private static final int HP_MIN = 107;
    private static final int HP_MAX = 111;
    private static final int ASC_HP_MIN = 113;
    private static final int ASC_HP_MAX = 117;
    private static final byte BIG_BITE = 1;
    private static final byte CHEW = 2;
    private static final byte BOTTOMLESS_STOMACH = 3;
    private static final String BIG_BITE_NAME = MOVES[0];
    private static final String CHEW_NAME = MOVES[1];
    private static final String BOTTOMLESS_STOMACH_NAME = MOVES[2];
    private static final int BIG_BITE_DAMAGE = 14;
    private static final float BIG_BITE_PERCENTAGE_HP_PER_STRENGTH = 0.5F;
    private static final int ASC_BIG_BITE_DAMAGE = 16;
    private static final float ASC2_BIG_BITE_PERCENTAGE_HP_PER_STRENGTH = 0.25F;
    private static final int CHEW_DAMAGE = 9;
    private static final int CHEW_HIT_AMOUNT = 2;
    private static final int ASC_CHEW_DAMAGE = 10;
    private static final int BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT = 2;
    private static final int ASC2_BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT = 3;
    private int bigBiteDamage;
    private float bigBitePercentageHPPerStrength;
    private int chewDamage;
    private int chewHitAmount;
    private int bottomlessStomachStrengthAmount;

    public Cassacara(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, TheActMod.assetPath("/images/monsters/cassacara/placeholder.png"), x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.bigBiteDamage = ASC_BIG_BITE_DAMAGE;
            this.bigBitePercentageHPPerStrength = ASC2_BIG_BITE_PERCENTAGE_HP_PER_STRENGTH;
            this.chewDamage = ASC_CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = ASC2_BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            this.bigBiteDamage = ASC_BIG_BITE_DAMAGE;
            this.bigBitePercentageHPPerStrength = BIG_BITE_PERCENTAGE_HP_PER_STRENGTH;
            this.chewDamage = ASC_CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;
        }
        else {
            this.bigBiteDamage = BIG_BITE_DAMAGE;
            this.bigBitePercentageHPPerStrength = BIG_BITE_PERCENTAGE_HP_PER_STRENGTH;
            this.chewDamage = CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;

        }
        this.damage.add(new DamageInfo(this, bigBiteDamage));
        this.damage.add(new DamageInfo(this, chewDamage));
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case BIG_BITE: {
                // TODO: Add animations when art is in
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy()), 0.2F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                // TODO: Add actions for sacrificing carcass sacks
                break;
            }
            case CHEW: {
                // TODO: Add animations when art is in
                for (int i = 0; i < chewHitAmount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy())));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.NONE));
                }
                break;
            }
            case BOTTOMLESS_STOMACH: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.bottomlessStomachStrengthAmount), this.bottomlessStomachStrengthAmount));
                // TODO: Add actions for adding carcass sacks
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void getMove(int num) {
        if (lastMove(BIG_BITE)) {
            this.setMove(CHEW_NAME, CHEW, Intent.ATTACK, this.damage.get(1).base, this.chewHitAmount, true);
        }
        else if (lastMove(CHEW)) {
            this.setMove(BOTTOMLESS_STOMACH_NAME, BOTTOMLESS_STOMACH, Intent.BUFF);
        }
        else {
            this.setMove(BIG_BITE_NAME, BIG_BITE, Intent.ATTACK_BUFF, this.damage.get(0).base);
        }
    }
}
