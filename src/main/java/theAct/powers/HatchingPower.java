package theAct.powers;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.monsters.SpyderBoss.SpawnedSpyder;
import theAct.monsters.SpyderBoss.SpyderBoss;
import theAct.powers.abstracts.Power;

public class HatchingPower extends Power {
	public static final String NAME = "Hatching";
	public static final String powerID = TheActMod.makeID(NAME);
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(NAME);
	
	public SpyderBoss boss;

	public HatchingPower(SpawnedSpyder owner, int amount) {
		this.owner = owner;
		this.boss = owner.owner;
		this.amount = amount;
		this.type = PowerType.BUFF;
		this.name = strings.NAME;
		this.setImage(NAME + "84.png", NAME + "32.png");
		this.ID = powerID;
		this.updateDescription();
	}

	public void updateDescription() {
		this.description =	strings.DESCRIPTIONS[0] + amount + strings.DESCRIPTIONS[1];
	}	
    
   @Override
   public void onDeath() {
	   int str = 0;
	   if(owner.hasPower(StrengthPower.POWER_ID))
		   str = owner.getPower(StrengthPower.POWER_ID).amount;
	   for (int i = 0; i < amount; i++)
		   boss.spawnSmallSpyder(str);
   }

	
}