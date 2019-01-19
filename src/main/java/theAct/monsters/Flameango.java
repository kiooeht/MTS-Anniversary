package theAct.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
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

    private static final int BURNS = 1;
    private static final int ARMOR = 3;
    private static final int FLAME_DAMAGE = 6;
    private static final int PECK_DAMAGE = 4;
    private static final int PECK_HITS = 2;

    private static final int ASC_ARMOR = 1;
    private static final int ASC_DAMAGE = 1;
    private static final int ASC_BURNS = 1;

    private static final int ASC_HEALTH = 5;

    private int burnAmount; private int flameArmor; private int flameDamage;
    private int peckDamage; private int peckHits;

    public Flameango(float x)
    {
        super(NAME, ID, maxHP, 0, 0, 300, 300, null, x, 0);
        this.img = ImageMaster.loadImage(TheActMod.assetPath("images/monsters/flameango/placeholder.png"));

        this.burnAmount = BURNS;
        this.flameArmor = ARMOR;
        this.flameDamage = FLAME_DAMAGE;
        this.peckDamage = PECK_DAMAGE;
        this.peckHits = PECK_HITS;
        if (AbstractDungeon.ascensionLevel >= 2)
        {
            this.flameArmor += ASC_ARMOR;
            this.burnAmount += ASC_BURNS;
            this.peckDamage += ASC_DAMAGE;
        }

        setHp(minHP, maxHP);
        if(AbstractDungeon.ascensionLevel >= 7)
            this.maxHealth += ASC_HEALTH;

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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameBarrierPower(this, flameArmor), flameArmor));
            break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    protected void getMove(int i)
    {
        if(i < 20)
            setMove((byte)0, AbstractMonster.Intent.ATTACK, this.damage.get(0).base, this.peckHits, true);
        else if(i < 60)
            setMove(FIRE_BREATH_NAME, (byte)1, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
        else
            setMove(FLAME_ARMOR_NAME, (byte)2, Intent.BUFF);
    }

    @Override
    public void die()
    {
        super.die();
        CardCrawlGame.sound.play("BYRD_DEATH");
    }
}
