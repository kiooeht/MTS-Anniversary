package theAct.monsters;

import com.megacrit.cardcrawl.monsters.*;
import com.megacrit.cardcrawl.localization.*;
import com.megacrit.cardcrawl.dungeons.*;
import com.megacrit.cardcrawl.cards.*;
import com.badlogic.gdx.math.*;
import com.esotericsoftware.spine.*;
import com.megacrit.cardcrawl.actions.*;
import com.megacrit.cardcrawl.actions.utility.*;
import com.megacrit.cardcrawl.actions.animations.*;
import com.megacrit.cardcrawl.powers.*;

import theAct.TheActMod;
import theAct.powers.SwingingTrapPower;

import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.core.*;

public class SwingingAxe extends AbstractMonster
{
    public static final String ID = TheActMod.makeID("SwingingAxe");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    private static final int DMG = 10;
    private static final int A_2_DMG = 12;
    private int dmg;
    
    public SwingingAxe() {
        this(0.0f, 0.0f);
    }
    
    public SwingingAxe(final float x, final float y) {
        super(SwingingAxe.NAME, ID, (AbstractDungeon.ascensionLevel >= 7) ? 90 : 75, 0.0f, 10.0f, 280.0f, 280.0f, null, x, y);
        if (AbstractDungeon.ascensionLevel >= 2) {
            this.dmg = A_2_DMG;
        }
        else {
            this.dmg = DMG;
        }
        this.damage.add(new DamageInfo(this, this.dmg));
        
        this.loadAnimation(TheActMod.assetPath("images/monsters/Trap/Trap.atlas"), TheActMod.assetPath("images/monsters/Trap/Trap.json"), 1.0F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "Incoming", true);

        this.stateData.setMix("Incoming", "Swinging", 0.1f);
        this.stateData.setMix("Swinging", "Reseting", 0.6f);
        this.stateData.setMix("Reseting", "Incoming", 0.4f);
    }
    
    @Override
    public void usePreBattleAction() {
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 5)));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SwingingTrapPower(this, (AbstractDungeon.ascensionLevel >= 17) ? 4 : 3, 0)));
    }
    
    @Override
    public void takeTurn() {
        switch (this.nextMove) {
	        case 1: {
	        	if (!this.hasPower(SwingingTrapPower.powerID) || ((SwingingTrapPower)this.getPower(SwingingTrapPower.powerID)).isAttacking()) {
	        		AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_HEAVY, true));
	        	}
	            break;
	        }
	        case 2: {
	        	if (!this.hasPower(SwingingTrapPower.powerID)) {
	        		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new SwingingTrapPower(this, (AbstractDungeon.ascensionLevel >= 17) ? 4 : 3, 0)));
	        	}
	            break;
	        }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    
    @Override
    protected void getMove(final int num) {
    	if (!this.hasPower(SwingingTrapPower.powerID)) {
    		this.setMove(MOVES[0], (byte)2, Intent.BUFF);
    	}
    	this.setMove((byte)1, Intent.ATTACK, this.damage.get(0).base);
    }
    
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = SwingingAxe.monsterStrings.NAME;
        MOVES = SwingingAxe.monsterStrings.MOVES;
        DIALOG = SwingingAxe.monsterStrings.DIALOG;
    }
}