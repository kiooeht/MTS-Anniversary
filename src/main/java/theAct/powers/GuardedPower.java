package theAct.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class GuardedPower extends Power {
	public static final String POWER_ID = TheActMod.makeID("Guarded");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = strings.NAME;
    private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);
	
	public GuardedPower(AbstractCreature owner, int amount) {
        this.ID = POWER_ID;
        this.name = strings.NAME;
		this.owner = owner;
		this.amount = amount;
		this.type = PowerType.BUFF;
		this.setImage(IMG + "84.png", IMG + "32.png");
        this.priority = 99;
		this.updateDescription();
	}
	
	@Override
    public void playApplyPowerSfx() {
        CardCrawlGame.sound.play("POWER_INTANGIBLE", 0.05f);
    }

	@Override
	public void onRemove() {
		CardCrawlGame.sound.play("POWER_FOCUS", 0.1f);
		CardCrawlGame.sound.play("BLOCK_BREAK", 0.05f);
	}
    
    @Override
    public float atDamageReceive(float damage, final DamageInfo.DamageType type) {
        if (type == DamageType.NORMAL) {
            return Math.max(damage * (1.0F - this.amount * 0.25F), 0.0F);
        }
        else {
            return damage;
        }
    }
    
    @Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }
      
}