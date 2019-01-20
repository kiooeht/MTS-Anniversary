package theAct.monsters.MUSHROOMPOWER;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BarricadePower;
import theAct.TheActMod;
import theAct.powers.CautiousPower;
import theAct.powers.ObsessionPower;

public class MushroomYandere extends AbstractMonster {
    public static final String ID = TheActMod.makeID("MushroomYandere");
    public static final String ENCOUNTER_ID = TheActMod.makeID("MUSHROOMGANG");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final int MIN_HP = 18;
    private static final int MAX_HP = 21;
    private static final int ASC_HP_MODIFIER = 5;
    private static final int DAMAGE_AMT = 7;
    private static final int DAMAGE_AMT_ASC_MODIFIER = 2;
    private static final int OBSESSION_AMT = 2;
    private static final int OBSESSION_AMT_ASC_MODIFIER = 1;
    private int obsessionAmt;
    private int stabDmg;

    public MushroomYandere(float x, float y) {
        super(NAME, ID, MAX_HP, 0.0F, 10.0F, 280.0F, 280.0F, null, x, y);
        loadAnimation(TheActMod.assetPath("images/monsters/MUSHROOMPOWER/Yandere.atlas"), TheActMod.assetPath("images/monsters/MUSHROOMPOWER/Yandere.json"), 1.0F);
        state.setAnimation(0, "Idle", true);
        stateData.setMix("Idle", "Rawr", 0.2F);
        if (AbstractDungeon.ascensionLevel >= 17) {
            obsessionAmt = OBSESSION_AMT + OBSESSION_AMT_ASC_MODIFIER;
        } else {
            obsessionAmt = OBSESSION_AMT;
        }
        if (AbstractDungeon.ascensionLevel >= 7) {
            setHp(MIN_HP + ASC_HP_MODIFIER, MAX_HP + ASC_HP_MODIFIER);
        } else {
            setHp(MIN_HP, MAX_HP);
        }
        if (AbstractDungeon.ascensionLevel >= 2) {
            stabDmg = DAMAGE_AMT + DAMAGE_AMT_ASC_MODIFIER;
            damage.add(new DamageInfo(this, stabDmg));
        } else {
            stabDmg = DAMAGE_AMT;
            damage.add(new DamageInfo(this, stabDmg));
        }
    }

    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ObsessionPower(this, obsessionAmt)));
    }

    @Override
    public void takeTurn() {
        switch (nextMove) {
            case 0:
                state.setAnimation(0, "Rawr", false);
                state.addAnimation(0, "Idle", true, 0.0F);
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0)));
                break;
        }
        rollMove();
    }

    @Override
    protected void getMove(int i) {
        setMove((byte)0, Intent.ATTACK, damage.get(0).base);
    }
}
