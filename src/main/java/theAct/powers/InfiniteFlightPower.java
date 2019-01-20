package theAct.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class InfiniteFlightPower extends Power {
	public static final String NAME = "InfiniteFlight";
	public static final String powerID = TheActMod.makeID(NAME);
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(NAME);

	public InfiniteFlightPower(AbstractCreature owner) {
		this.owner = owner;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =	strings.DESCRIPTIONS[0];
	}
	
	@Override
	public void playApplyPowerSfx() {
	        CardCrawlGame.sound.play("POWER_FLIGHT", 0.05f);
	}
	
	@Override
    public float atDamageFinalReceive(final float damage, final DamageInfo.DamageType type) {
        return this.calculateDamageTakenAmount(damage, type);
    }
    
    private float calculateDamageTakenAmount(final float damage, final DamageInfo.DamageType type) {
        if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
            return damage / 2.0f;
        }
        return damage;
    }
    
    @Override
    public int onAttacked(final DamageInfo info, final int damageAmount) {        
        this.flash();
        return damageAmount;
    }

	
}