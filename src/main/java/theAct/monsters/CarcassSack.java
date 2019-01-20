package theAct.monsters;

import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

public class CarcassSack extends AbstractMonster {

    public static final String ID = TheActMod.makeID("CarcassSack");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    private static final float HB_X = 8.0F;
    private static final float HB_Y = 136.0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private static final int HP_MIN = 18;
    private static final int HP_MAX = 20;
    private static final int ASC_HP_MIN = 20;
    private static final int ASC_HP_MAX = 22;
    private static final byte ITS_DINNER_TIME = 1;

    public CarcassSack(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, TheActMod.assetPath("/images/monsters/cassacara/placeholder.png"), x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
    }

    public void takeTurn() {

    }

    public void getMove(int num) {
        this.setMove(ITS_DINNER_TIME, Intent.UNKNOWN);
    }
}