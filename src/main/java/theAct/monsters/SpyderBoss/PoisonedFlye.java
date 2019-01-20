package theAct.monsters.SpyderBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PoisonPower;

import theAct.TheActMod;
import theAct.powers.InfiniteFlightPower;

public class PoisonedFlye extends SpawnedSpyder{
	
	public static final String ID = TheActMod.makeID("PoisonedFlye");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;
    
    private static final int BASEHP = 10;
    private static final boolean SMALL = true;
    
	
	public PoisonedFlye(SpyderBoss boss, int slot, int strength) {	
		super(NAME, ID, SMALL, BASEHP, boss, slot, strength);
		damage.add(new DamageInfo(this, 2));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PoisonPower(this, this, AbstractDungeon.ascensionLevel >= 19?4:5), AbstractDungeon.ascensionLevel >= 19?6:7));
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new InfiniteFlightPower(this)));
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
            AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
            for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                if (m instanceof SpawnedSpyder && !(m instanceof PoisonedFlye) && !m.isDying && !m.isEscaping && m.currentHealth < m.maxHealth) {
                    AbstractDungeon.actionManager.addToTop(new HealAction(m, this, AbstractDungeon.ascensionLevel >= 19?3:2));
                }
            }
            break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	public void getMove(int num){
		this.setMove((byte)0, Intent.ATTACK_BUFF, this.damage.get(0).base);		
	}

}
