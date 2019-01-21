package theAct.monsters.MUSHROOMPOWER;

import com.megacrit.cardcrawl.actions.animations.AnimateSlowAttackAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theAct.TheActMod;
import theAct.powers.EnergeticPower;
import theAct.powers.ProtectionPower;

public class MushroomGenki extends AbstractMonster {
    public static final String ID = TheActMod.makeID("MushroomGenki");
    public static final String ENCOUNTER_ID = TheActMod.makeID("MUSHROOMGANG");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final int MIN_HP = 30;
    private static final int MAX_HP = 37;
    private static final int ASC_HP_MODIFIER = 4;
    private static final int STR_AMT = 1;
    private static final int STR_AMT_ASC_MODIFIER = 1;
    private static final int DAMAGE_AMT = 4;
    private static final int DAMAGE_AMT_ASC_MODIFIER = 2;
    private int dmgAmt;
    private int strAmt;

    public MushroomGenki(float x, float y) {
        super(NAME, ID, MAX_HP, 0.0F, 10.0F, 160.0F, 155.0F, null, x, y);
        loadAnimation(TheActMod.assetPath("images/monsters/MUSHROOMPOWER/Kuudere.atlas"), TheActMod.assetPath("images/monsters/MUSHROOMPOWER/Kuudere.json"), 1.0F);
        state.setAnimation(0, "Idle", true);
        stateData.setMix("Idle", "Kyuuuuu", 0.2F);
        if (AbstractDungeon.ascensionLevel >= 17) {
            strAmt = STR_AMT + STR_AMT_ASC_MODIFIER;
        } else {
            strAmt = STR_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(MIN_HP + ASC_HP_MODIFIER, MAX_HP + ASC_HP_MODIFIER);
        } else {
            setHp(MIN_HP, MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            dmgAmt = DAMAGE_AMT + DAMAGE_AMT_ASC_MODIFIER;
            damage.add(new DamageInfo(this, dmgAmt));
        } else {
            dmgAmt = DAMAGE_AMT;
            damage.add(new DamageInfo(this, dmgAmt));
        }
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("MUSHROOMGANG");

        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new EnergeticPower(this, strAmt)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new AnimateSlowAttackAction(this));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0)));
                break;
            case 1:
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (m != this) {
                        state.setAnimation(0, "Kyuuuuu", false);
                        state.addAnimation(0, "Idle", true, 0.0F);
                        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, strAmt), strAmt));
                    }
                }
                break;
        }
        rollMove();
    }

    @Override
    protected void getMove(int i) {
        if (lastMove((byte)0)) {
            setMove((byte)1, Intent.BUFF);
            return;
        }
        setMove((byte)0, Intent.ATTACK, damage.get(0).base);
    }
}
