package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.vfx.combat.WebEffect;

import theAct.TheActMod;
import theAct.powers.WebbedPower;

public class HunterSpyder extends Spyder{
	
	public static final String ID_WITHOUT_PREFIX = "HunterSpyder";
	public static final String ID = TheActMod.makeID(ID_WITHOUT_PREFIX);
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
    
    public static final int HP = 14;
    
    public HunterSpyder(float x, float y, int slot) {
    	this(x, y, slot, 0, true);
    }
    
    public HunterSpyder(float x, float y, int slot, int strength) {
    	this(x, y, slot, strength, false);
    }
    
    public HunterSpyder(float x, float y, int slot, int strength, boolean normal) {	
		super(NAME, ID_WITHOUT_PREFIX, x, y, slot, strength,x + AbstractDungeon.miscRng.random(-20, 20), y + AbstractDungeon.miscRng.random(-20, 20));

		this.stronger = AbstractDungeon.ascensionLevel >= (normal?17:19);
		
		if (AbstractDungeon.ascensionLevel >= (normal?7:9)) {
            this.setHp(HP*6/5, HP*3/2);
        } else {
            this.setHp(HP, HP*5/4);
        }      
        
        if (stronger) {
        	this.damage.add(new DamageInfo(this, 3));
        	this.damage.add(new DamageInfo(this, 7));
        	
        } else if (AbstractDungeon.ascensionLevel >= (normal?2:4)){
        	this.damage.add(new DamageInfo(this, 3));
        	this.damage.add(new DamageInfo(this, 7)); 
        	
        } else {
        	this.damage.add(new DamageInfo(this, 2));
        	this.damage.add(new DamageInfo(this, 5));
        }
	}
	
	
	@Override
	public void takeTurn(){
		switch(this.nextMove) {
		case 0:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1), AttackEffect.SLASH_HORIZONTAL, true));
            break;
		case 1:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.SLASH_VERTICAL, true));
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.SLASH_DIAGONAL, true));
            break;
		case 2:
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WebbedPower(AbstractDungeon.player, 1), 1));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0f * Settings.scale, this.hb.cY - 90.0f * Settings.scale)));
            break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		byte l = -1;
		for (byte i = 0; i < 3; i++) {
			if(lastMove(i)) {
				l = i;
				break;
			}
		}
		if(l == -1)
			l = (byte)(slot%3);
		switch(l) {
		case 0:
			setMove((byte) 1, Intent.ATTACK, damage.get(0).base, (stronger?3:2), true);	
            break;
		case 1:
			setMove((byte) 2, Intent.STRONG_DEBUFF);	
            break;
		case 2:
			setMove((byte) 0, Intent.ATTACK, damage.get(1).base);	
            break;
		}			
	}

}
