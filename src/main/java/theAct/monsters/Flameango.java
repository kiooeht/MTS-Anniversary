package theAct.monsters;

import basemod.interfaces.OnStartBattleSubscriber;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FlameBarrierPower;
import com.megacrit.cardcrawl.relics.Mango;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.vfx.SpeechBubble;
import theAct.TheActMod;
import java.util.Random;

public class Flameango extends AbstractMonster
{
    public static final String ID = TheActMod.makeID("Flameango");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    private static final String NAME = MONSTER_STRINGS.NAME;
    private static final String FIRE_BREATH_NAME = MONSTER_STRINGS.MOVES[0];
    private static final String FLAME_ARMOR_NAME = MONSTER_STRINGS.MOVES[1];
    private static final String FLAVOR_TOWN = MONSTER_STRINGS.DIALOG[0];

    private static final int minHP = 65;
    private static final int maxHP = 70;

    private static final int BURNS = 1;
    private static final int ARMOR = 3;
    private static final int FLAME_DAMAGE = 8;
    private static final int PECK_DAMAGE = 4;
    private static final int PECK_HITS = 3;

    private static final int ASC_ARMOR = 1;
    private static final int ASC_DAMAGE = 1;
    private static final int ASC_BURNS = 1;

    private static final int ASC_HEALTH = 5;

    private int burnAmount; private int flameArmor; private int flameDamage;
    private int peckDamage; private int peckHits;

    private boolean firstTurn = true;

    public Flameango(int x)
    {
        super(NAME, ID, maxHP, 0, 0, 300, 300, null, x, 0);

        this.loadAnimation(
                TheActMod.assetPath("images/monsters/flamaengo/Flamaengo.atlas"),
                TheActMod.assetPath("images/monsters/flamaengo/Flamaengo.json"),
                1);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

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

        this.dialogX = (-50.0F * Settings.scale);
        this.dialogY = (50.0F * Settings.scale);
    }

    @Override
    public void takeTurn()
    {
        if(this.firstTurn)
        {
            if(AbstractDungeon.player.hasRelic(Mango.ID))
            {
                AbstractDungeon.actionManager.addToBottom(new TalkAction(this, FLAVOR_TOWN, 0.5f, 2.0f));
            }
            firstTurn = false;
        }
        AbstractPlayer p = AbstractDungeon.player;

        switch (this.nextMove)
        {
        case 0:
            for(int i = 0; i < peckHits; i++) {
                AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            }
            AbstractDungeon.actionManager.addToTop(new ChangeStateAction(this, "ATTACK"));
            break;
        case 1:
            AbstractDungeon.actionManager.addToBottom(new DamageAction(p, this.damage.get(1), AbstractGameAction.AttackEffect.FIRE));
            AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new Burn(), burnAmount));
            AbstractDungeon.actionManager.addToTop(new ChangeStateAction(this, "ATTACK"));
            break;

        case 2:
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FlameBarrierPower(this, flameArmor), flameArmor));
            break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void changeState(String key){
    switch (key) {
        case "ATTACK":
            this.state.setAnimation(1, "rawr", false);
            break;
    }
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
