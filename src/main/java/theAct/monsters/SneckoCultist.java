package theAct.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;
import theAct.actions.EnemyRandomizeCost;
import theAct.powers.RandomizePower;

public class SneckoCultist extends AbstractMonster {
    public static final String ID = TheActMod.makeID("SneckoCultist");
    private static final MonsterStrings MONSTER_STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = MONSTER_STRINGS.NAME;
    public static final String[] MOVES = MONSTER_STRINGS.MOVES;

    private static final String WHIP_NAME = MOVES[0];
    private static final String CONFUSE_START_NAME = MOVES[1];
    private static final String TACKLE_NAME = MOVES[2];

    private static final int HP_MIN = 50;
    private static final int HP_MAX = 56;
    private static final float HB_X = 0F;
    private static final float HB_Y = 0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private static final int A_HP_MIN = 113;
    private static final int A_HP_MAX = 117;
    private static final int WHIP_DAMAGE = 17;
    private static final int TACKLE_DAMAGE = 10;


    private boolean firstTurn = true;
    //private int numOfCards = 3;



    public SneckoCultist(int xOffset, int yOffset)
    {
        super(MONSTER_STRINGS.NAME, ID, HP_MAX, HB_X, HB_Y, HB_W, HB_H, TheActMod.assetPath("/images/monsters/sneckoCultist/placeholder.png"), 0 + xOffset, 0f + yOffset);
        this.type = EnemyType.NORMAL;

        this.damage.add(new DamageInfo(this, WHIP_DAMAGE));
        this.damage.add(new DamageInfo(this,TACKLE_DAMAGE));
    }

    @Override
    public void takeTurn()
    {
        AbstractPlayer player = AbstractDungeon.player;
        switch(this.nextMove)
        {
            case MoveBytes.WHIP:
            {
              //TODO - Animation once art is in
                AbstractDungeon.actionManager.addToBottom(new DamageAction(player, damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            }
            case MoveBytes.CONFUSE_START:
            {
                //TODO - Animation once art is in
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, this, new RandomizePower(3),3));
                break;
            }
            case MoveBytes.TACKLE:
            {
                //TODO - Animation once art is in
                AbstractDungeon.actionManager.addToBottom(new DamageAction(player, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, this, new RandomizePower(1),1));
            }

        }


        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void getMove(int roll)
    {
        if(firstTurn)
        {
            firstTurn = false;
            this.setMove(CONFUSE_START_NAME, MoveBytes.CONFUSE_START, Intent.MAGIC);
            return;
        }
        else
        {
            if(lastMove(MoveBytes.CONFUSE_START) || lastMove(MoveBytes.TACKLE)) {
                this.setMove(WHIP_NAME, MoveBytes.WHIP, Intent.ATTACK, this.damage.get(0).base);
            }
            else if(lastMove(MoveBytes.WHIP)){
                this.setMove(TACKLE_NAME, MoveBytes.TACKLE, Intent.ATTACK_DEBUFF, this.damage.get(1).base);
            }
        }
    }

    public static class MoveBytes {
        public static final byte WHIP = 0;
        public static final byte CONFUSE_START = 1;
        public static final byte TACKLE = 2;
    }
}
