package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.BlurPower;

import theAct.TheActMod;
import theAct.powers.FormationPower;

public class DefenderSpyder extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("DefenderSpyder");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 23;
    private static final boolean SMALL = false;
    
	
	public DefenderSpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
		damage.add(new DamageInfo(this, 4));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FormationPower(this)));
	}
	
	static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }
	
	@Override
	public void takeTurn(){
		switch(this.nextMove) {
        case 0:
            for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, 4));
            }
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BlurPower(this, 1), 1));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 15));
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(AbstractDungeon.getRandomMonster(), this, 15));
        	break;
        case 2:
        	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
        	break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){		
		if(num < 40)
			this.setMove((byte)0, Intent.DEFEND_BUFF);
		else if (num < 70)
			this.setMove((byte)1, Intent.DEFEND);
		else
			this.setMove((byte)2, Intent.ATTACK, this.damage.get(0).base);		
	}

}
