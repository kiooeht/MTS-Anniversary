package theAct.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

public class SilentTribesmen extends AbstractMonster {
    public static final String ID = TheActMod.makeID("SilentTribesmen");
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    private static final int MIN_HP = 30;
    private static final int MAX_HP = 37;
    private static final int ASC_HP_MODIFIER = 5;

    public SilentTribesmen(float x, float y) {
        super(NAME, ID, MAX_HP, 0.0F, 10.0F, 280.0F, 280.0F, null, x, y);
        this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));
        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp(MIN_HP + ASC_HP_MODIFIER, MAX_HP + ASC_HP_MODIFIER);
        } else {
            this.setHp(MIN_HP, MAX_HP);
        }
    }
}
