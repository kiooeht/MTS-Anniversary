package theAct.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.powers.abstracts.Power;

public class FragileEggsPower extends Power {
	public static final String POWER_ID = TheActMod.makeID("FragileEggs");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = strings.NAME;
	private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);
	
	private int reset = 0;

	public FragileEggsPower(AbstractCreature owner, int amount) {
		this.ID = POWER_ID;
		this.name = strings.NAME;
		this.owner = owner;
		this.amount = amount;
		this.reset = amount;
		this.type = PowerType.BUFF;
		this.setImage(IMG + "84.png", IMG + "32.png");
		this.updateDescription();
	}

	public void updateDescription() {
		this.description = strings.DESCRIPTIONS[0] + reset + strings.DESCRIPTIONS[1] + amount + strings.DESCRIPTIONS[2];
	}	
    
    @Override
    public int onAttacked(final DamageInfo info, final int damageAmount) {        
        this.flash();
        if(damageAmount > 0)
        	this.amount -= damageAmount;
        while(this.amount <= 0) {
        	this.amount += reset;
        	int str = 0;
        	if(owner.hasPower(StrengthPower.POWER_ID))
        		str = owner.getPower(StrengthPower.POWER_ID).amount;
        	((SpyderBoss)owner).spawnBigSpyder(str);
        }
        updateDescription();
        return damageAmount;
    }

	
}