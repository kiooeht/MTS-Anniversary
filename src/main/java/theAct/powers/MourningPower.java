package theAct.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;

import theAct.TheActMod;
import theAct.powers.abstracts.Power;

public class MourningPower extends Power {
	public static final String POWER_ID = TheActMod.makeID("Mourning");
	private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = strings.NAME;
    private static final String IMG = POWER_ID.substring(POWER_ID.indexOf(":")+1);
	
	public MourningPower(AbstractCreature owner) {
        this.ID = POWER_ID;
        this.name = strings.NAME;
		this.owner = owner;
		this.amount = -1;
		this.type = PowerType.BUFF;
		this.setImage(IMG + "84.png", IMG + "32.png");
		this.updateDescription();
	}
	
	@Override
    public void updateDescription() {
        this.description = strings.DESCRIPTIONS[0];
    }
      
}