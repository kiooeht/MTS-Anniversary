package theAct.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.powers.abstracts.Power;

public class FragileEggsPower extends Power {
	public static final String NAME = "FragileEggs";
	public static final String powerID = TheActMod.makeID(NAME);
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(NAME);
	
	private int reset = 0;

	public FragileEggsPower(AbstractCreature owner, int amount) {
		this.owner = owner;
		this.amount = amount;
		this.reset = amount;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =	strings.DESCRIPTIONS[0] + reset + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
	}	
    
    @Override
    public int onAttacked(final DamageInfo info, final int damageAmount) {        
        this.flash();
        if(damageAmount > 0)
        	this.amount -= damageAmount;
        while(this.amount <= 0) {
        	this.amount += reset;
        	((SpyderBoss)owner).spawnBigSpyder();
        }
        updateDescription();
        return damageAmount;
    }

	
}