package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.vfx.combat.ClashEffect;
import com.megacrit.cardcrawl.vfx.combat.CleaveEffect;
import com.megacrit.cardcrawl.vfx.combat.ThrowDaggerEffect;
import theAct.TheActMod;
import theAct.powers.CautiousPower;

public class JungleHunters extends AbstractMonster {
    public static final String ID = TheActMod.makeID("JungleHunters");
    public static final String ENCOUNTER_ID = TheActMod.makeID("JungleHuntersEncounter");
    public static final String EVENT_ID = TheActMod.makeID("JungleHuntersEvent");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final int MIN_HP = 52;
    private static final int MAX_HP = 61;
    private static final int ASC_HP_MODIFIER = 5;
    private static final int START_BLOCK_AMT = 11;
    private static final int START_BLOCK_ASC_MODIFIER = 5;
    private static final int BLOCK_MOVE_AMT = 16;
    private static final int BLOCK_MOVE_ASC_MODIFIER = 5;
    private static final int STUN_AMT = 4;
    private static final int STUN_AMT_ASC_MODIFIER = 1;
    private static final int MED_ATK_DMG = 10;
    private static final int MED_ATK_DMG_ASC_MODIFIER = 2;
    private static final int BIG_ATK_DMG = 3;
    private static final int BIG_ATK_DMG_TIMES = 5;
    private static final int BIG_ATK_DMG_TIMES_ASC_MODIFIER = 1;
    private static final int DEX_LOSS_AMT = 1;
    private static final int DEX_LOSS_AMT_ASC_MODIFIER = 1;
    private int blockAmt;
    private int blockMoveAmt;
    private int stunAmt;
    private int bigAtkAmt;
    private int dexLossAmt;
    public static final String STUNNED = "STUNNED";
    public static final String NOTSTUNNED = "NOTSTUNNED";
    private boolean isFirstTurn = true;
    private boolean doBigAttack = false;

    public JungleHunters(float x, float y) {
        super(NAME, ID, MAX_HP, 0.0F, 10.0F, 280.0F, 280.0F, null, x, y);

        //this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));

        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(MIN_HP + ASC_HP_MODIFIER, MAX_HP + ASC_HP_MODIFIER);
        } else {
            setHp(MIN_HP, MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 17) {
            blockAmt = START_BLOCK_AMT + START_BLOCK_ASC_MODIFIER;
            blockMoveAmt = BLOCK_MOVE_AMT + BLOCK_MOVE_ASC_MODIFIER;
            stunAmt = STUN_AMT + STUN_AMT_ASC_MODIFIER;
            dexLossAmt = DEX_LOSS_AMT + DEX_LOSS_AMT_ASC_MODIFIER;
        } else {
            blockAmt = START_BLOCK_AMT;
            blockMoveAmt = BLOCK_MOVE_AMT;
            stunAmt = STUN_AMT;
            dexLossAmt = DEX_LOSS_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            damage.add(new DamageInfo(this, MED_ATK_DMG + MED_ATK_DMG_ASC_MODIFIER));
            bigAtkAmt = BIG_ATK_DMG_TIMES + BIG_ATK_DMG_TIMES_ASC_MODIFIER;
        } else {
            damage.add(new DamageInfo(this, MED_ATK_DMG));
            bigAtkAmt = BIG_ATK_DMG_TIMES;
        }
        damage.add(new DamageInfo(this, BIG_ATK_DMG));

        this.animY += 25f;

        this.loadAnimation(
                TheActMod.assetPath("images/monsters/hunters/JungleHunter.atlas"),
                TheActMod.assetPath("images/monsters/hunters/JungleHunter.json"),
                0.75f);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Attack", true);
        e.setTime(e.getEndTime() * MathUtils.random());

    }

    @Override
    public void changeState(String state) {
        switch (state) {
            case NOTSTUNNED:
                break;
            case STUNNED:
                setMove((byte)4, Intent.STUN);
                createIntent();
                break;
            case "ATTACK":
                this.state.setAnimation(1, "Idle", false);
                break;
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, blockAmt));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BarricadePower(this)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new CautiousPower(this, stunAmt)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, blockMoveAmt));
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0)));

                AbstractDungeon.actionManager.addToBottom(new com.megacrit.cardcrawl.actions.utility.SFXAction("ATTACK_HEAVY"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new ClashEffect(AbstractDungeon.player.drawX,AbstractDungeon.player.drawY)));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new DexterityPower(AbstractDungeon.player, -dexLossAmt), -dexLossAmt));
                break;
            case 3:
                for (int i = 0; i < bigAtkAmt; i++) {
                    AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1)));
                    AbstractDungeon.actionManager.addToBottom(new VFXAction(new ThrowDaggerEffect(AbstractDungeon.player.drawX,AbstractDungeon.player.drawY)));
                }
                break;
            case 4:
                AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.STUNNED));
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, NOTSTUNNED));
                break;
        }
        rollMove();
    }

    @Override
    protected void getMove(int i) {
        if (isFirstTurn) {
            setMove((byte)0, Intent.DEFEND);
            isFirstTurn = false;
            return;
        }
        if (lastMove((byte)0) || lastMove((byte)2)) {
            setMove((byte)3, Intent.ATTACK, damage.get(1).base, bigAtkAmt, true);
            return;
        }
        if (!lastMove((byte)1)) {
            setMove((byte)1, Intent.ATTACK, damage.get(0).base);
        } else {
            if (i < 50) {
                setMove((byte)0, Intent.DEFEND);
            } else {
                setMove((byte)2, Intent.DEBUFF);
            }
        }
    }

}
