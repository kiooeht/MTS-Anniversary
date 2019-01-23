package theAct.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

public class CarcassSack extends AbstractMonster {

    public static final String ID = TheActMod.makeID("CarcassSack");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    private static final float HB_X = 0.0F;
    private static final float HB_Y = 0.0F;
    private static final float HB_W = 150.0F;
    private static final float HB_H = 100.0F;
    private static final String ANIMATION_ATLAS = TheActMod.assetPath("images/monsters/cassacara/Cassacara.atlas");
    private static final String ANIMATION_JSON = TheActMod.assetPath("images/monsters/cassacara/Cassacara.json");
    private static final int HP_MIN = 25;
    private static final int HP_MAX = 27;
    private static final int ASC_HP_MIN = 27;
    private static final int ASC_HP_MAX = 29;
    private static final byte ITS_DINNER_TIME = 1;

    public CarcassSack(float x, float y) {
        super(NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, null, x, y);
        this.type = EnemyType.ELITE;
        if (AbstractDungeon.ascensionLevel >= 8) {
            setHp(ASC_HP_MIN, ASC_HP_MAX);
        }
        else {
            setHp(HP_MIN, HP_MAX);
        }
        this.loadAnimation(ANIMATION_ATLAS, ANIMATION_JSON, 1.0F);
        this.state.setAnimation(0, "idleLeaves", true);
        this.state.setAnimation(1, "TinyBulb", true);
    }

    public void takeTurn() {

    }

    public void getMove(int num) {
        this.setMove(ITS_DINNER_TIME, Intent.STUN);
    }
}