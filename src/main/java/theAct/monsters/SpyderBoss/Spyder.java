package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.powers.GuardedPower;


public class Spyder extends AbstractMonster {
	
    public int slot;
    public int strength;
    public boolean stronger;

    public Spyder(String name, String ID, float x, float y, int slot, int strength) {
        super(name, TheActMod.makeID(ID), 1, 0.0F, 0F, slot==-1? 240F: 160F, slot==-1? 240F: 160F, TheActMod.assetPath("images/monsters/spyders/" + ID + ".png"), 
        		x + AbstractDungeon.miscRng.random(-20, 20), y + AbstractDungeon.miscRng.random(-20, 20));
        
        this.type = EnemyType.NORMAL;
        this.dialogX = 0;
        this.dialogY = 0;
        this.slot = slot==-1? 3: slot;
        this.strength = strength;
    }    
   
    @Override    
    public void usePreBattleAction() {
    	startPowers();
    }
    	
    public void startPowers() {
    	if(strength != 0)
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));
    	
    	if(slot != 0)
    		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GuardedPower(this)));
    	
    	for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
    		if(m instanceof Spyder && ((Spyder) m).slot == slot+1 && !m.hasPower(GuardedPower.POWER_ID))
    			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new GuardedPower(m)));
    		
        }
        
    }



    public void takeTurn() {
        
    }
 
 	public void breakGuard() {
 		
 	}

    protected void getMove(int num) {
    	
    }
    
    public void die() {
        this.useFastShakeAnimation(0.5F);
        super.die();
        for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
    		if(m instanceof Spyder && ((Spyder) m).slot == slot+1) {
    			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, m, GuardedPower.POWER_ID));
    			((Spyder) m).breakGuard();
    		}
        }

    }
}
