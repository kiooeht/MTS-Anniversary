package theAct.monsters;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

public class SilentTribesmen extends AbstractMonster {
    public static final String ID = TheActMod.makeID("SilentTribesmen");
    private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final int MIN_HP_NO_ASC = 30;
    private static final int MAX_HP_NO_ASC = 37;
}
