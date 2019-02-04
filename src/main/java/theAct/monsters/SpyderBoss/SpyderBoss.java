package theAct.monsters.SpyderBoss;

import java.util.ArrayList;
import java.util.Arrays;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.powers.GuardedPower;

public class SpyderBoss extends Spyder {

    public static final String ID_WITHOUT_PREFIX = "QueenSpyder";
	public static final String ID = TheActMod.makeID(ID_WITHOUT_PREFIX);
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
    public static final String NAME = monsterStrings.NAME;
	private static final int HP = 190;
	private static final int ASC_HP = 200;
	private static final int DAMAGE_DAMAGE = 8; // Is there a better name for this?
	private static final int ASC_DAMAGE_DAMAGE = 9;
	private static final int STRENGTH_GAIN_AMOUNT = 2;
	private static final int ASC2_STRENGTH_GAIN_AMOUNT = 3;
	private static final int DAMAGE_FRAIL_DAMAGE = 4;
	private static final int DAMAGE_FRAIL_FRAIL_AMOUNT = 1;
	private static final int ASC_DAMAGE_FRAIL_DAMAGE = 5;
	private int damageFrailFrailAmount;
	private int strengthAmount;

    private ArrayList<Integer> choice = new ArrayList<Integer>(Arrays.asList(0,0,1,1));
    private float[] pos = new float[] {-650.0F, 300.0F, -425.0F, 220.0F, -200.0F, 250.0F};

    public SpyderBoss() {

        super(NAME, ID_WITHOUT_PREFIX, 0F, 0F, -1, 0,180.0F, -180.0F);
        this.type = EnemyType.BOSS;
        this.stronger = AbstractDungeon.ascensionLevel >= 19;
        
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(ASC_HP);
        }
        else {
            this.setHp(HP);
        }

        if (this.stronger) {
        	this.damageFrailFrailAmount = DAMAGE_FRAIL_FRAIL_AMOUNT;
        	this.strengthAmount = ASC2_STRENGTH_GAIN_AMOUNT;
			this.damage.add(new DamageInfo(this, ASC_DAMAGE_DAMAGE));
			this.damage.add(new DamageInfo(this, ASC_DAMAGE_FRAIL_DAMAGE));
		}
        else if (AbstractDungeon.ascensionLevel >= 4){
			this.damageFrailFrailAmount = DAMAGE_FRAIL_FRAIL_AMOUNT;
			this.strengthAmount = STRENGTH_GAIN_AMOUNT;
			this.damage.add(new DamageInfo(this, ASC_DAMAGE_DAMAGE));
			this.damage.add(new DamageInfo(this, ASC_DAMAGE_FRAIL_DAMAGE));
        } else {
			this.damageFrailFrailAmount = DAMAGE_FRAIL_FRAIL_AMOUNT;
			this.strengthAmount = STRENGTH_GAIN_AMOUNT;
        	this.damage.add(new DamageInfo(this, DAMAGE_DAMAGE));
			this.damage.add(new DamageInfo(this, DAMAGE_FRAIL_DAMAGE));
        }
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSSSPIDER");
    }

    public void spawnSpyders(int str) {
        if(this.isDying) {
			return;
		}
        
        ArrayList<Integer> c = (ArrayList<Integer>)choice.clone();
        
        for(int i = 2; i >=0; i--) {
        	boolean space = true;
        	for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
        		if(m instanceof Spyder && ((Spyder) m).slot == i && !m.isDeadOrEscaped()) {
        			space = false;
        			break;
        		}
            }
        	if(!space)
        		continue;
        	
        	Spyder m = null;
        	int r = AbstractDungeon.monsterRng.random(c.size() - 1);
        	int chosen = c.get(r);
        	c.remove(r);
        	
        	switch(chosen) { 	
	    	case 0:
	    		m = new HunterSpyder(pos[i*2], pos[i*2+1], i, str);
	    		break;	    	
	    	case 1:
	    		m = new DefenderSpyder(pos[i*2], pos[i*2+1], i, str);
	    		break;	    	
        	}        	
        	
        	AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true, 0));
        	m.startPowers();
        	
        }
    }
    
    public void takeTurn() {
        switch(this.nextMove) {
			case MoveBytes.SPIDERS: {
				int str = 0;
				if (this.hasPower(StrengthPower.POWER_ID)) {
					str = this.getPower(StrengthPower.POWER_ID).amount;
				}
				spawnSpyders(str);
				if (!this.hasPower(GuardedPower.POWER_ID)) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GuardedPower(this)));
				}
				break;
			}
			case MoveBytes.DAMAGE: {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.SLASH_HEAVY));
				break;
			}
			case MoveBytes.STRENGTH_BUFF: {
				for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
					if (!m.isDeadOrEscaped()) {
						AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(this, this.strengthAmount), this.strengthAmount, true));
					}
				}
				break;
			}
			case MoveBytes.DAMAGE_FRAIL: {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1), AttackEffect.SLASH_HORIZONTAL));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, damageFrailFrailAmount, true), damageFrailFrailAmount));
				break;
			}
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    protected void getMove(int num) {
		TheActMod.logger.info("spider boss monster count " + AbstractDungeon.getMonsters().monsters.size());

		boolean spidersStillAlive = false;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
			if (!m.isDeadOrEscaped() && !(m instanceof SpyderBoss)) {
				spidersStillAlive = true;
			}
		}
		if (!spidersStillAlive) {
			setMove(MoveBytes.SPIDERS, Intent.UNKNOWN);
		}
		else if(lastMove(MoveBytes.DAMAGE)) {
			setMove(MoveBytes.STRENGTH_BUFF, Intent.BUFF);
		}
		else if(lastMove(MoveBytes.STRENGTH_BUFF)) {
			setMove(MoveBytes.DAMAGE_FRAIL, Intent.ATTACK_DEBUFF, damage.get(1).base);
		}
		else if(lastMove(MoveBytes.DAMAGE_FRAIL)) {
			setMove(MoveBytes.DAMAGE, Intent.ATTACK, damage.get(0).base);
		}
		else {
			if (num % 3 == 0) {
				setMove(MoveBytes.STRENGTH_BUFF, Intent.BUFF);
			}
			else if (num % 3 == 1) {
				setMove(MoveBytes.DAMAGE_FRAIL, Intent.ATTACK_DEBUFF, damage.get(1).base);
			}
			else {
				setMove(MoveBytes.DAMAGE, Intent.ATTACK, damage.get(0).base);
			}
		}
    }
    
    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        for (final AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
            if (m != null && !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }

        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();

    }

	public static class MoveBytes {
		public static final byte SPIDERS = 0;
		public static final byte DAMAGE = 1;
		public static final byte STRENGTH_BUFF = 2;
		public static final byte DAMAGE_FRAIL = 3;
	}
}
