package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.DexterityPower;

import theAct.TheActMod;
import theAct.actions.GainDexBlockAction;
import theAct.powers.ShyPower;

public class ShySpyder extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("ShySpyder");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 7;
    private static final boolean SMALL = true;
    
    private boolean firstTurn = true;
    
	
	public ShySpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
		damage.add(new DamageInfo(this, 10));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ShyPower(this)));
		AbstractDungeon.actionManager.addToBottom(new GainDexBlockAction(this));
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
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
    		AbstractDungeon.actionManager.addToBottom(new GainDexBlockAction(this));
        	break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		if(firstTurn)
			this.setMove((byte)1, Intent.ATTACK_DEFEND, this.damage.get(0).base);
		else
			if(this.hasPower(DexterityPower.POWER_ID) && this.getPower(DexterityPower.POWER_ID).amount > 0)
				this.setMove((byte)1, Intent.ATTACK_DEFEND, this.damage.get(0).base);
			else
				this.setMove((byte)0, Intent.ATTACK, this.damage.get(0).base);
		firstTurn = false;
	}

}
