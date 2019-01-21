package theAct.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class InfiniteIntangiblePower extends Power {
	public static final String POWER_ID = TheActMod.makeID("InfiniteIntangible");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = strings.NAME;
	
	public InfiniteIntangiblePower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = strings.NAME;
		this.owner = owner;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.setImage(NAME + "84.png", NAME + "32.png");
        this.priority = 99;
		this.updateDescription();
	}
	
	@Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_INTANGIBLE", 0.05f);
    }
    
    @Override
    public float atDamageReceive(float damage, final DamageInfo.DamageType type) {
        if (damage > 1.0f) {
            damage = 1.0f;
        }
        return damage;
    }
    
    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }
      
}