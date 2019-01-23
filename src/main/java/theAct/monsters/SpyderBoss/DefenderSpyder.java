package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import theAct.TheActMod;

public class DefenderSpyder extends Spyder{
	
	public static final String ID_WITHOUT_PREFIX = "DefenderSpyder";
	public static final String ID = TheActMod.makeID(ID_WITHOUT_PREFIX);
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    
    public static final int HP = 12;
    
    public DefenderSpyder(float x, float y, int slot) {
    	this(x, y, slot, 0, true);
    }
    
    public DefenderSpyder(float x, float y, int slot, int strength) {
    	this(x, y, slot, strength, false);
    }
    
	public DefenderSpyder(float x, float y, int slot, int strength, boolean normal) {	
		super(NAME, ID_WITHOUT_PREFIX, x, y, slot, strength,x + AbstractDungeon.miscRng.random(-20, 20), y + AbstractDungeon.miscRng.random(-20, 20), normal);
		
		this.stronger = AbstractDungeon.ascensionLevel >= (normal?17:19);
		
		if (AbstractDungeon.ascensionLevel >= (normal?7:9)) {
			this.setHp(HP*6/5, HP*3/2);
        } else {
            this.setHp(HP, HP*5/4);
        }      
        
        if (stronger) {
        	this.damage.add(new DamageInfo(this, 5));
        	this.damage.add(new DamageInfo(this, 3));
            
        } else if (AbstractDungeon.ascensionLevel >= (normal?2:4)){
            this.damage.add(new DamageInfo(this, 5));
        	this.damage.add(new DamageInfo(this, 3));
            
        } else {
        	this.damage.add(new DamageInfo(this, 4));
        	this.damage.add(new DamageInfo(this, 2));
        }
	}
	
	
	@Override
	public void takeTurn(){
		switch(this.nextMove) {
		case 0:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1), AttackEffect.BLUNT_LIGHT, true));
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, stronger?19:16, true));
            break;
		case 1:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.BLUNT_LIGHT, true));
            break;
		case 2:
            for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, stronger?8:6, true));
            	if(stronger)
            		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new BlurPower(m, 2), 2, true));
            }
            if(!stronger)
            	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new BlurPower(this, 2), 2, true));
            break;
		case 3:
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1, true));
            if(stronger)
            	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1, true));
            break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		byte l = -1;
		for (byte i = 0; i < 4; i++) {
			if(lastMove(i)) {
				l = i;
				break;
			}
		}
		if(l == -1)
			l = (byte) slot;
		switch(l) {
		case 0:
			setMove((byte) 1, Intent.ATTACK, damage.get(0).base);	
            break;
		case 1:
			setMove((byte) 2, Intent.DEFEND_BUFF);	
            break;
		case 2:
			setMove((byte) 3, Intent.DEBUFF);	
            break;
		case 3:
			setMove((byte) 0, Intent.ATTACK_DEFEND, damage.get(1).base);	
            break;
		}
	}

}
