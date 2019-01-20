package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.powers.BetterExplosivePower;
import theAct.powers.HatchingPower;

public class EggSpyder extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("EggSpyder");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 30;
    private static final boolean SMALL = false;
    
	
	public EggSpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PlatedArmorPower(this, 8), 8));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BetterExplosivePower(this, 4, 8), 4));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new HatchingPower(this, 3), 3));
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
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
            break;	
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){	
		if(this.hasPower(BetterExplosivePower.powerID) && this.getPower(BetterExplosivePower.powerID).amount <= 2)
			this.setMove((byte)2, Intent.UNKNOWN);
		else if(this.lastMove((byte)0) || this.lastMove((byte)1))
			this.setMove((byte)1, Intent.BUFF);
		else
			this.setMove((byte)0, Intent.BUFF);
	}

}
