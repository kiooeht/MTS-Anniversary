package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.actions.FormationInitAction;
import theAct.powers.FormationPower;
import theAct.powers.SquadPower;


public class SpawnedSpyder extends AbstractMonster {
	
	public static float[] slotPos = new float[]{
			0, -20,
			-120, -30,
			-270, -40,
			-420, -40,
			-570, -40,
			-200, 70,
			-350, 70,
			-500, 70,
			
			200, 400,
			130, 320,
			60, 400,
			-10, 280,
			-80, 400,
			-170, 320,
			-260, 240,
			-260, 400,
			-350, 320,
			-440, 240,
			-440, 400,
			-530, 320,
			-100, 240,
			-640, 400,
			-730, 280,
			-800, 400,
			-870, 320,
			-940, 400
	};
	
    public SpyderBoss owner; 
    
    public boolean small = true;
    public int slot;
    public int strength;

    public SpawnedSpyder(String name, String ID, boolean small, int baseHP, SpyderBoss boss, int slot, int strength) {
        super(name, ID, 420, 0.0F, 0F, small?80.0F:130F, small?60.0F:100F, TheActMod.assetPath("images/monsters/spyders/" + name + ".png"), 
        		slotPos[(small?16:0) + slot*2] *1.3F + AbstractDungeon.miscRng.random(-20, 20), slotPos[(small?16:0) + slot*2 + 1] *1.2F + AbstractDungeon.miscRng.random(-10, 10));
        
        this.type = EnemyType.NORMAL;
        this.dialogX = 0;
        this.dialogY = 0;
        this.owner = boss;
        this.slot = slot;
        this.strength = strength;
        this.small = small;
                
        baseHP += AbstractDungeon.monsterHpRng.random(baseHP / 5);

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp((baseHP * 12) / 10);
        } else {
            this.setHp(baseHP);
        }
        
                
        
        	
        
        //AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new MinionPower(this)));

    }
    
    public SpawnedSpyder(String name, String ID, boolean small, int baseHP, float x, float y) {
        super(name, ID, 420, 0.0F, 0F, small?80.0F:130F, small?60.0F:100F, TheActMod.assetPath("images/monsters/spyders/" + name + ".png"), 
        		x, y);
        
        this.type = EnemyType.NORMAL;
        this.dialogX = 0;
        this.dialogY = 0;
        this.small = small;
                
        baseHP += AbstractDungeon.monsterHpRng.random(baseHP / 5);

        if (AbstractDungeon.ascensionLevel >= 7) {
            this.setHp((baseHP * 12) / 10);
        } else {
            this.setHp(baseHP);
        }
        
        
        int strength = 0;
        if (AbstractDungeon.ascensionLevel >= 2) strength++;
        if (AbstractDungeon.ascensionLevel >= 17) strength++;
        
        this.strength = strength;
    }
    
    public void usePreBattleAction() {
    	if(strength != 0)
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, strength), strength));     
        
    }



    public void takeTurn() {
        
    }

    protected void getMove(int num) {
    	
    }
    
    public void die() {
        this.useFastShakeAnimation(0.5F);
        super.die();
        if(owner != null) {
        	this.owner.resolveSpyderDeath(this);        	
        	for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
    			if(m instanceof SpawnedSpyder && m.hasPower(SquadPower.powerID))
    				((SquadPower) m.getPower(SquadPower.powerID)).spyderDeath();
    			if(m instanceof SpawnedSpyder && m.hasPower(FormationPower.powerID))
    				AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((FormationPower) m.getPower(FormationPower.powerID))));
    		}
        	AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((FormationPower) this.owner.getPower(FormationPower.powerID))));
        }

    }


}
