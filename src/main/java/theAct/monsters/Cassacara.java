package theAct.monsters;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import theAct.TheActMod;
import theAct.actions.CassacaraSacrificeAction;
import theAct.powers.CassacaraInfoPower;

public class Cassacara extends AbstractMonster {

    public static final String ID = TheActMod.makeID("Cassacara");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = 0.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 350.0F;
    private static final float HB_H = 350.0F;
    private static final String ANIMATION_ATLAS = TheActMod.assetPath("images/monsters/cassacara/Cassacara.atlas");
    private static final String ANIMATION_JSON =TheActMod.assetPath("images/monsters/cassacara/Cassacara.json");
    private static final int HP_MIN = 150;
    private static final int HP_MAX = 154;
    private static final int ASC_HP_MIN = 156;
    private static final int ASC_HP_MAX = 160;
    private static final byte BIG_BITE = 1;
    private static final byte CHEW = 2;
    private static final byte BOTTOMLESS_STOMACH = 3;
    private static final String BIG_BITE_NAME = MOVES[0];
    private static final String CHEW_NAME = MOVES[1];
    private static final String BOTTOMLESS_STOMACH_NAME = MOVES[2];
    private static final int BIG_BITE_DAMAGE = 10;
    private static final int BIG_BITE_STRENGTH_GAIN_PER_SACK = 3;
    private static final int ASC_BIG_BITE_DAMAGE = 12;
    private static final int ASC2_BIG_BITE_STRENGTH_GAIN_PER_SACK = 4;
    private static final int CHEW_DAMAGE = 7;
    private static final int CHEW_HIT_AMOUNT = 2;
    private static final int ASC_CHEW_DAMAGE = 8;
    private static final int BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT = 3;
    private static final int ASC2_BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT = 4;
    private int bigBiteDamage;
    private int bigBiteStrengthGainPerSack;
    private int chewDamage;
    private int chewHitAmount;
    private int bottomlessStomachStrengthAmount;
    private AbstractMonster[] carcassSacks;

    public Cassacara(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 18) {
            this.bigBiteDamage = ASC_BIG_BITE_DAMAGE;
            this.bigBiteStrengthGainPerSack = ASC2_BIG_BITE_STRENGTH_GAIN_PER_SACK;
            this.chewDamage = ASC_CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = ASC2_BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 3) {
            this.bigBiteDamage = ASC_BIG_BITE_DAMAGE;
            this.bigBiteStrengthGainPerSack = BIG_BITE_STRENGTH_GAIN_PER_SACK;
            this.chewDamage = ASC_CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;
        }
        else {
            this.bigBiteDamage = BIG_BITE_DAMAGE;
            this.bigBiteStrengthGainPerSack = BIG_BITE_STRENGTH_GAIN_PER_SACK;
            this.chewDamage = CHEW_DAMAGE;
            this.chewHitAmount = CHEW_HIT_AMOUNT;
            this.bottomlessStomachStrengthAmount = BOTTOMLESS_STOMACH_STRENGTH_GAIN_AMOUNT;

        }
        this.damage.add(new DamageInfo(this, bigBiteDamage));
        this.damage.add(new DamageInfo(this, chewDamage));
        this.carcassSacks = new AbstractMonster[2];

        this.loadAnimation(ANIMATION_ATLAS, ANIMATION_JSON, 1.0F);
        this.state.setAnimation(0, "idleLeaves", true);
        this.state.setAnimation(1, "idleLick", true);
        this.state.setAnimation(2, "IdleChomp", true);
        this.stateData.setMix("Lick", "idleLick", 0.1f);
        this.stateData.setMix("Chomp", "IdleChomp", 0.1f);
    }

    public Cassacara() {
        this(0.0F, 0.0F);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("JUNGLEELITE");
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CassacaraInfoPower(this, this.bigBiteStrengthGainPerSack), this.bottomlessStomachStrengthAmount));
    }

    @Override
    public void takeTurn() {
        switch (this.nextMove) {
            case BIG_BITE: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHOMP"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, Color.CHARTREUSE.cpy()), 0.3F));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
                for (AbstractMonster m : this.carcassSacks) {
                    if (m != null && !m.isDeadOrEscaped()) {
                        AbstractDungeon.actionManager.addToBottom(new CassacaraSacrificeAction(this, m, this.bigBiteStrengthGainPerSack));
                    }
                }
                break;
            }
            case CHEW: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "LICK"));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CHOMP"));
                for (int i = 0; i < chewHitAmount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new BiteEffect(AbstractDungeon.player.hb.cX + MathUtils.random(-50.0f, 50.0f) * Settings.scale, AbstractDungeon.player.hb.cY + MathUtils.random(-50.0f, 50.0f) * Settings.scale, Color.CHARTREUSE.cpy()), 0.2F));
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.NONE));
                }
                break;
            }
            case BOTTOMLESS_STOMACH: {
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.bottomlessStomachStrengthAmount), this.bottomlessStomachStrengthAmount));
                if (this.carcassSacks[0] == null || this.carcassSacks[0].isDeadOrEscaped()) {
                    CarcassSack sackToSpawn = new CarcassSack(-475.0F, -20.0F);
                    this.carcassSacks[0] = sackToSpawn;
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(sackToSpawn, true, 1));
                }
                if (this.carcassSacks[1] == null || this.carcassSacks[1].isDeadOrEscaped()) {
                    CarcassSack sackToSpawn = new CarcassSack(-300.0F, -45.0F);
                    this.carcassSacks[1] = sackToSpawn;
                    AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(sackToSpawn, true, 1));
                }
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void changeState(String key){
        switch (key) {
            case "LICK": {
                this.state.setAnimation(1, "Lick", false);
                this.state.addAnimation(1, "idleLick", true, 0.0F);
                break;
            }
            case "CHOMP": {
                this.state.setAnimation(2, "Chomp", false);
                this.state.addAnimation(2, "IdleChomp", true, 0.0F);
            }
        }
    }

    public void getMove(int num) {
        if (lastMove(BOTTOMLESS_STOMACH)) {
            this.setMove(CHEW_NAME, CHEW, Intent.ATTACK, this.damage.get(1).base, this.chewHitAmount, true);
        }
        else if (lastMove(CHEW)) {
            this.setMove(BIG_BITE_NAME, BIG_BITE, Intent.ATTACK_BUFF, this.damage.get(0).base);
        }
        else {
            this.setMove(BOTTOMLESS_STOMACH_NAME, BOTTOMLESS_STOMACH, Intent.BUFF);
        }
    }

    @Override
    public void die() {
        super.die();
        for (final AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
            if (!m.isDead && !m.isDying) {
                AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(m));
                AbstractDungeon.actionManager.addToTop(new SuicideAction(m));
            }
        }
    }
}
