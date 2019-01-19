package theAct.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import theAct.TheActMod;
import java.util.Random;

public class Flameango extends AbstractMonster
{
    public static final String ID = TheActMod.makeID("Flameango");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = MONSTER_STRINGS.NAME;
    private static final String FIRE_BREATH_NAME = MONSTER_STRINGS.MOVES[0];
    private static final String FLAME_ARMOR_NAME = MONSTER_STRINGS.MOVES[1];

    private static final int minHP = 25;
    private static final int maxHP = 35;

    private static final int burnAmount = 1;
    private static final int flameArmor = 3;
    private static final int flameDamage = 6;
    private static final int peckDamage = 5;
    private static final int peckHits = 2;

    public Flameango()
    {
        super(NAME, ID, maxHP, 0, 0, 300, 300, null, 0, 0);
        this.img = ImageMaster.loadImage("theActAssets/images/flameango/placeholder.png");
        setHp(minHP, maxHP);

        this.damage.add(new DamageInfo(this, peckDamage));
        this.damage.add(new DamageInfo(this, flameDamage));
    }

    @Override
    public void takeTurn()
    {
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
        case 0:
            for(int i = 0; i < peckHits; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
            break;
        case 1:
            AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), burnAmount));
            break;

        case 2:
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameBarrierPower(this, flameArmor)));
            break;
        }
    }

    @Override
    protected void getMove(int i)
    {
        if(i < 40)
            setMove((byte)0, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, this.peckHits, true);
        else if(i < 70)
            setMove(FIRE_BREATH_NAME, (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        else
            setMove(FLAME_ARMOR_NAME, (byte)2, Intent.BUFF);
    }
}
