package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.powers.EntanglePower;
import com.megacrit.cardcrawl.vfx.combat.WebEffect;

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
    private boolean offset = false;
    
	
    public WebberSpyder(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
	}
    
    public WebberSpyder(float x, float y, boolean offset) {	
		super(NAME, ID, SMALL, BASEHP, x, y);
		this.offset = offset;
		damage.add(new DamageInfo(this, 5));
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
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WebbedPower(AbstractDungeon.player, offset?2:1), offset?2:1));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0f * Settings.scale, this.hb.cY + 10.0f * Settings.scale)));
            break;
        case 1:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 6));
        	break;        	
        case 2:
        	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new EntanglePower(AbstractDungeon.player)));
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new WebEffect(AbstractDungeon.player, this.hb.cX - 70.0f * Settings.scale, this.hb.cY + 10.0f * Settings.scale)));
        	break;
        case 3:
        	AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 18));
        	break;
        case 4:
        	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
        	break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		if(this.lastMove((byte)3)) 
			this.setMove((byte)0, Intent.DEBUFF);
		else if(this.lastMove((byte)0)) 
			if(offset)
				this.setMove((byte)4, Intent.ATTACK, this.damage.get(0).base, 2, true);
			else
				this.setMove((byte)1, Intent.DEFEND);
		else if(this.lastMove((byte)1) || this.lastMove((byte)4)) 
			this.setMove((byte)2, Intent.STRONG_DEBUFF);
		else if(this.lastMove((byte)2)) 
			this.setMove((byte)3, Intent.DEFEND);
		else
			if(offset)
				this.setMove((byte)0, Intent.ATTACK, this.damage.get(0).base, 2, true);
			else
				this.setMove((byte)3, Intent.DEFEND);
	}

}
