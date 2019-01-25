package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
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
    public static final String[] DIALOG = MONSTER_STRINGS.DIALOG;

    private static final String WHIP_NAME = MOVES[0];
    private static final String CONFUSE_START_NAME = MOVES[1];
    private static final String TACKLE_NAME = MOVES[2];

    private int HP_MIN = 50;
    private int  HP_MAX = 56;
    private static final float HB_X = 0F;
    private static final float HB_Y = 0F;
    private static final float HB_W = 320.0F;
    private static final float HB_H = 240.0F;
    private int WHIP_DAMAGE = 17;
    private int TACKLE_DAMAGE = 10;
    private int CARDS_CONFUSED = 1;


    private boolean talky;
    //private int numOfCards = 3;



    public SneckoCultist(int xOffset, int yOffset) {
        this(xOffset, yOffset, false);
    }

    public SneckoCultist(int xOffset, int yOffset, boolean talk)
    {
        super(MONSTER_STRINGS.NAME, ID, 56, HB_X, HB_Y, HB_W, HB_H, TheActMod.assetPath("/images/monsters/sneckoCultist/placeholder.png"), 0 + xOffset, 0f + yOffset);
        this.type = EnemyType.NORMAL;
        this.loadAnimation(TheActMod.assetPath("images/monsters/sneckoCultist/SneckoCultist.atlas"), TheActMod.assetPath("images/monsters/sneckoCultist/SneckoCultist.json"), 1.0F);

        this.talky = talk;
        this.dialogX = -50.0f * Settings.scale;
        this.dialogY = 50.0f * Settings.scale;

        this.damage.add(new DamageInfo(this, WHIP_DAMAGE));
        this.damage.add(new DamageInfo(this,TACKLE_DAMAGE));

        if(AbstractDungeon.ascensionLevel >= 19)
        {
            HP_MIN += 6;
            HP_MAX += 6;
            CARDS_CONFUSED = 3;
            WHIP_DAMAGE = 20;

        }else if(AbstractDungeon.ascensionLevel >= 4)
        {
            HP_MIN += 4;
            HP_MAX += 4;
            CARDS_CONFUSED = 1;
            WHIP_DAMAGE = 20;
        }

        this.setHp(HP_MIN, HP_MAX);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    @Override
    public void takeTurn()
    {
        AbstractPlayer player = AbstractDungeon.player;
        switch(this.nextMove)
        {
            case MoveBytes.WHIP:
            {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(player, damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
                break;
            }
            case MoveBytes.CONFUSE_START:
            {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, this, new RandomizePower(player,3),3));
                if (this.talky) {
                    int r = MathUtils.random(1);
                    if (r == 0) {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction(TheActMod.makeID("sneckoCultist1"),0.1f));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new SFXAction(TheActMod.makeID("sneckoCultist2"),0.1f));
                    }
                    r = MathUtils.random(1);
                    if (r == 0) {
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[0], 1.0f, 2.0f));
                    } else {
                        AbstractDungeon.actionManager.addToBottom(new TalkAction(this, DIALOG[1], 1.0f, 2.0f));
                    }
                }
                break;
            }
            case MoveBytes.TACKLE:
            {
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(player, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(player, this, new RandomizePower(player,CARDS_CONFUSED),CARDS_CONFUSED));
            }

        }


        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void changeState(String key){
        switch (key) {
            case "ATTACK":
                this.state.setAnimation(1, "whip", false);
                this.state.addAnimation(1, "whip", false,0.0f);
                break;
        }
    }

    @Override
    public void getMove(int roll)
    {
        if(this.moveHistory.isEmpty())
        {
            this.setMove(CONFUSE_START_NAME, MoveBytes.CONFUSE_START, Intent.DEBUFF);
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