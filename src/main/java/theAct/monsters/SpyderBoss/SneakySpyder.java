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
import com.megacrit.cardcrawl.powers.WeakPower;

import theAct.TheActMod;

public class SneakySpyder extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("SneakySpyder");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 8;
    private static final boolean SMALL = true;
    
	
    public SneakySpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
		damage.add(new DamageInfo(this, 2));
		damage.add(new DamageInfo(this, 3));
	}
    
    public SneakySpyder(float x, float y) {	
		super(NAME, ID, SMALL, BASEHP*3/2, x, y);
		damage.add(new DamageInfo(this, 3));
		damage.add(new DamageInfo(this, 4));
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
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL, true));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HORIZONTAL));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL, true));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
        	break;
        case 2:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 16));
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new WeakPower(this, 1, true), 1));
        	break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		if(this.lastMove((byte)2)) {
			if(num < 60)
				this.setMove((byte)0, Intent.ATTACK, this.damage.get(0).base, 3, true);
			else
				this.setMove((byte)1, Intent.ATTACK, this.damage.get(1).base, 2, true);				
		}else {
			if(num < 20)
				this.setMove((byte)0, Intent.ATTACK, this.damage.get(0).base, 3, true);
			else if (num < 60)
				this.setMove((byte)1, Intent.ATTACK, this.damage.get(1).base, 2, true);
			else
				this.setMove((byte)2, Intent.DEFEND_BUFF);	
		}
	}

}
