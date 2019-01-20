package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;

import theAct.TheActMod;
import theAct.powers.WebbedPower;

public class WebberSpyder extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("WebberSpyder");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 23;
    private static final boolean SMALL = false;
    
	
    public WebberSpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
	}
    
    public WebberSpyder(float x, float y) {	
		super(NAME, ID, SMALL, BASEHP, x, y);
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
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WebbedPower(AbstractDungeon.player, 1), 1));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 6));
        	break;        	
        case 2:
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player)));
        	break;
        case 3:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 17));
        	break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		if(this.lastMove((byte)3)) 
			this.setMove((byte)0, Intent.DEBUFF);
		else if(this.lastMove((byte)0)) 
			this.setMove((byte)1, Intent.DEFEND);
		else if(this.lastMove((byte)1)) 
			this.setMove((byte)2, Intent.STRONG_DEBUFF);
		else
			this.setMove((byte)3, Intent.DEFEND);
	}

}
