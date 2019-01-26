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

    private ArrayList<Integer> choice = new ArrayList<Integer>(Arrays.asList(0,0,1,1));
    private float[] pos = new float[] {-650.0F, 300.0F, -425.0F, 220.0F, -200.0F, 250.0F};

    public SpyderBoss() {
        super(NAME, ID_WITHOUT_PREFIX, 0F, 0F, -1, 0,180.0F, -180.0F);

        this.type = EnemyType.BOSS;
       // this.loadAnimation("images/monsters/theForest/mage/skeleton.atlas", "images/monsters/theForest/mage/skeleton.json", 1.0F); //NOT DONE
        
        this.stronger = AbstractDungeon.ascensionLevel >= 19;
        
        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(200);
        } else {
            this.setHp(210);
        }      
        
        if (stronger) {
        	this.damage.add(new DamageInfo(this, 9));
			this.damage.add(new DamageInfo(this, 5));
            
        } else if (AbstractDungeon.ascensionLevel >= 4){
        	this.damage.add(new DamageInfo(this, 9));
			this.damage.add(new DamageInfo(this, 5));
        } else {
        	this.damage.add(new DamageInfo(this, 8));
			this.damage.add(new DamageInfo(this, 4));
        }

        /*
        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Idle", "Sumon", 0.1F);
        this.stateData.setMix("Sumon", "Idle", 0.1F);
        this.stateData.setMix("Hurt", "Idle", 0.1F);
        this.stateData.setMix("Idle", "Hurt", 0.1F);
        this.stateData.setMix("Attack", "Idle", 0.1F);
        e.setTime(e.getEndTime() * MathUtils.random());
        */

    }

    public void usePreBattleAction() {

        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSSSPIDER");

                     
    	//AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, stronger?30:20, true));
        //AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, 1), 1));
       // AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new MourningPower(this)));
        
    }

    public void spawnSpyders(int str) {
        if(isDying)
            return;
        
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
    	int art = 99;
        switch(this.nextMove) {

		case 0:
			int str = 0;
			if(hasPower(StrengthPower.POWER_ID)) str += getPower(StrengthPower.POWER_ID).amount;
			spawnSpyders(str);
			if (!this.hasPower(GuardedPower.POWER_ID)) AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new GuardedPower(this)));

			break;
		case 1:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1), AttackEffect.SLASH_HORIZONTAL));

			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
            break;
		case 2:
			//if(hasPower(ArtifactPower.POWER_ID))
			//	art -= getPower(ArtifactPower.POWER_ID).amount;
			//if(art > 0)
			//	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new ArtifactPower(this, art), art, true));
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(this, stronger?3:2), stronger?3:2, true));
			}

			break;

			case 3:
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(0), AttackEffect.SLASH_HEAVY));
				break;
            
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }
    

    protected void getMove(int num) {
		TheActMod.logger.info("spider boss monster count " + AbstractDungeon.getMonsters().monsters.size());

		if(this.moveHistory.isEmpty())
		{
			setMove(MoveBytes.SPIDERS, Intent.UNKNOWN);
			return;
		}
		int livingSpiders = 0;
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
			if (!m.isDying && !m.isDead && !(m instanceof SpyderBoss))
				livingSpiders++;
		}
		if (livingSpiders == 0) {
			setMove(MoveBytes.SPIDERS, Intent.UNKNOWN);
			return;
		}

		else if(lastMove(MoveBytes.SPIDERS)) {
			Integer chosenStart = AbstractDungeon.cardRng.random(2);
			switch (chosenStart) {
				case 0:
					setMove(MoveBytes.STRENGTHBUFF, Intent.BUFF);
					return;
				case 1:
					setMove(MoveBytes.STRENGTHBUFF, Intent.BUFF);
					return;
				case 2:
					setMove(MoveBytes.DAMAGEFRAIL, Intent.ATTACK_DEBUFF, damage.get(1).base);
					return;

			}
		} else
		{
			if(lastMove(MoveBytes.DAMAGE)) {
				setMove(MoveBytes.STRENGTHBUFF, Intent.BUFF);
			}
			else if(lastMove(MoveBytes.STRENGTHBUFF)) {
				setMove(MoveBytes.DAMAGEFRAIL, Intent.ATTACK_DEBUFF, damage.get(1).base);
			}
			else if(lastMove(MoveBytes.DAMAGEFRAIL)) {
			setMove(MoveBytes.DAMAGE, Intent.ATTACK, damage.get(0).base);

			}
		}

    }
    
    @Override
    public void breakGuard() {
    	//setMove((byte) 3, Intent.STUN);
    	//createIntent();
    	//AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this, this, ArtifactPower.POWER_ID));
    }

    /*
    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hurt", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }

    }
    */

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
		public static final byte STRENGTHBUFF = 2;
		public static final byte DAMAGEFRAIL = 3;
	}
}
