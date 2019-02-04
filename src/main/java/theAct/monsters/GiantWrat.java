package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import theAct.TheActMod;

public class GiantWrat extends AbstractMonster {

    public static final String ID = TheActMod.makeID("GiantWrat");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;
    private static final float HB_X = -75.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private static final int HP_MIN = 91;
    private static final int HP_MAX = 96;
    private static final int ASC_HP_MIN = 94;
    private static final int ASC_HP_MAX = 99;
    private static final byte FAT_BURNER = 1;
    private static final byte SLAM = 2;
    private static final byte FLAIL = 3;
    private static final String FAT_BURNER_NAME = MOVES[0];
    private static final String SLAM_NAME = MOVES[1];
    private static final String FLAIL_NAME = MOVES[2];
    private static final int FAT_BURNER_STRENGTH_GAIN_AMOUNT = 3;
    private static final int ASC_FAT_BURNER_STRENGTH_GAIN_AMOUNT = 4;
    private static final int ASC2_FAT_BURNER_STRENGTH_GAIN_AMOUNT = 5;
    private static final int FAT_BURNER_BLOCK_AMOUNT = 15;
    private static final int ASC2_FAT_BURNER_BLOCK_AMOUNT = 20;
    private static final int SLAM_DAMAGE = 13;
    private static final int ASC_SLAM_DAMAGE = 14;
    private static final int SLAM_BLOCK_AMOUNT = 10;
    private static final int FLAIL_DAMAGE = 7;
    private static final int ASC_FLAIL_DAMAGE = 8;
    private static final int FLAIL_HIT_AMOUNT = 2;
    private static final int FLAIL_SELF_VULNERABLE_AMOUNT = 1;
    private int fatBurnerStrengthGainAmount;
    private int fatBurnerBlockAmount;
    private int slamDamage;
    private int slamBlockAmount;
    private int flailDamage;
    private int flailHitAmount;
    private int flailSelfVulnerableAmount;

    public GiantWrat(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            this.fatBurnerStrengthGainAmount = ASC2_FAT_BURNER_STRENGTH_GAIN_AMOUNT;
            this.fatBurnerBlockAmount = ASC2_FAT_BURNER_BLOCK_AMOUNT;
            this.slamDamage = ASC_SLAM_DAMAGE;
            this.slamBlockAmount = SLAM_BLOCK_AMOUNT;
            this.flailDamage = ASC_FLAIL_DAMAGE;
            this.flailHitAmount = FLAIL_HIT_AMOUNT;
            this.flailSelfVulnerableAmount = FLAIL_SELF_VULNERABLE_AMOUNT;
        }
        else if (AbstractDungeon.ascensionLevel >= 2) {
            this.fatBurnerStrengthGainAmount = ASC_FAT_BURNER_STRENGTH_GAIN_AMOUNT;
            this.fatBurnerBlockAmount = FAT_BURNER_BLOCK_AMOUNT;
            this.slamDamage = ASC_SLAM_DAMAGE;
            this.slamBlockAmount = SLAM_BLOCK_AMOUNT;
            this.flailDamage = ASC_FLAIL_DAMAGE;
            this.flailHitAmount = FLAIL_HIT_AMOUNT;
            this.flailSelfVulnerableAmount = FLAIL_SELF_VULNERABLE_AMOUNT;
        }
        else {
            this.fatBurnerStrengthGainAmount = FAT_BURNER_STRENGTH_GAIN_AMOUNT;
            this.fatBurnerBlockAmount = FAT_BURNER_BLOCK_AMOUNT;
            this.slamDamage = SLAM_DAMAGE;
            this.slamBlockAmount = SLAM_BLOCK_AMOUNT;
            this.flailDamage = FLAIL_DAMAGE;
            this.flailHitAmount = FLAIL_HIT_AMOUNT;
            this.flailSelfVulnerableAmount = FLAIL_SELF_VULNERABLE_AMOUNT;

        }
        this.loadAnimation(
            TheActMod.assetPath("images/monsters/Wrat/Wrat.atlas"),
            TheActMod.assetPath("images/monsters/Wrat/Wrat.json"),
            1);

        AnimationState.TrackEntry e =this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e = this.state.setAnimation(1, "footidle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        e = this.state.setAnimation(2, "tailidle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.damage.add(new DamageInfo(this, slamDamage));
        this.damage.add(new DamageInfo(this, flailDamage));
    }

    public GiantWrat() {
        this(0.0F, 0.0F);
    }

    @Override
    public void changeState(String key) {
        switch (key) {
            case "ATTACK": {
                this.state.setAnimation(1, "attack", false);
                break;
            }
        }
    }

    public void takeTurn() {
        switch (this.nextMove) {
            case FAT_BURNER: {
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new InflameEffect(this), 0.5F));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.fatBurnerStrengthGainAmount), this.fatBurnerStrengthGainAmount));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, fatBurnerBlockAmount));
                break;
            }
            case SLAM: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, slamBlockAmount));
                break;
            }
            case FLAIL: {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                for (int i = 0; i < this.flailHitAmount; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new VulnerablePower(this, this.flailSelfVulnerableAmount, true), this.flailSelfVulnerableAmount));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void getMove(int num) {
        if (lastMove(FAT_BURNER)) {
            this.setMove(SLAM_NAME, SLAM, Intent.ATTACK_DEFEND, this.damage.get(0).base);
        }
        else if (lastMove(SLAM)) {
            if (num < 50) {
                this.setMove(FLAIL_NAME, FLAIL, Intent.ATTACK, this.damage.get(1).base, this.flailHitAmount, true);
            }
            else {
                this.setMove(FAT_BURNER_NAME, FAT_BURNER, Intent.DEFEND_BUFF);
            }
        }
        else {
            this.setMove(FAT_BURNER_NAME, FAT_BURNER, Intent.DEFEND_BUFF);
        }
    }
}
