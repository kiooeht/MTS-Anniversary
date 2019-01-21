package theAct.monsters.SpyderBoss;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import theAct.TheActMod;
import theAct.actions.FormationInitAction;
import theAct.powers.FormationPower;
import theAct.powers.FragileEggsPower;
import theAct.powers.SquadPower;

public class SpyderBoss extends AbstractMonster {
    public static final String ID = TheActMod.makeID("SpyderNest");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public int smallSpyderAmt = 0;
    public int bigSpyderAmt = 0;
    public AbstractMonster[] smallSpyders = new AbstractMonster[18];
    public AbstractMonster[] bigSpyders = new AbstractMonster[8];
    private ArrayList<Integer> bigChoice = new ArrayList<Integer>(Arrays.asList(0,0,0,0,1,1,1,1,2,2,2,2));
    public int turnAmt = 0;

    public SpyderBoss() {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(135, 149), 0.0F, 330.0F, 220.0F, 320.0F, TheActMod.assetPath("images/monsters/spyders/SpyderBoss.png"), 140.0F, -220.0F);
        this.type = EnemyType.BOSS;
       // this.loadAnimation("images/monsters/theForest/mage/skeleton.atlas", "images/monsters/theForest/mage/skeleton.json", 1.0F); //NOT DONE
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(155, 170);
        } else {
            this.setHp(135, 149);
        }      
        
        if (AbstractDungeon.ascensionLevel >= 4) {
            this.damage.add(new DamageInfo(this, 12));
        } else {
            this.damage.add(new DamageInfo(this, 9));
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

        FormationPower formation = new FormationPower(this);
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, formation));
        AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new FragileEggsPower(this, 25), 25));
        
        if (AbstractDungeon.ascensionLevel >= 4)
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));


        spawnBigSpyder(0, formation);
        if (AbstractDungeon.ascensionLevel >= 19)
        	spawnBigSpyder(0, formation);
        
        for (int i = 0; i < 3; i++)
        	spawnSmallSpyder(0, formation);

        
    }

    public void spawnBigSpyder(int str) {
        spawnBigSpyder(str, null);
    }

    public void spawnBigSpyder(int str, FormationPower formationPower) {

        if(isDying || bigSpyderAmt >= bigSpyders.length || bigChoice.size() == 0)
            return;

        int i = 0;
        while(i < bigSpyders.length) {
            if(bigSpyders[i] == null)
                break;
            i++;
        }
		
		int r = AbstractDungeon.monsterRng.random(bigChoice.size() - 1);
    	int chosen = bigChoice.get(r);
    	bigChoice.remove(r);
    	AbstractMonster m = null;
    	switch(chosen) {
	    	case 0:
	    		m = new BasherSpyder(this, i, str);
	    		break;	    	
	    	case 1:
	    		m = new WebberSpyder(this, i, str);
	    		break;	    	
	    	case 2:
	    		m = new DefenderSpyder(this, i, str);
	    		break;	    	
    	}    	

        TheActMod.logger.info("Spawning Monster");
        bigSpyderAmt++;
        bigSpyders[i] = m;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true));

        for(AbstractMonster n : AbstractDungeon.getMonsters().monsters) {
    		if(n instanceof SpawnedSpyder && n.hasPower(SquadPower.POWER_ID))
    			((SquadPower) n.getPower(SquadPower.POWER_ID)).spyderSpawn();
			if(n instanceof SpawnedSpyder && n.hasPower(FormationPower.POWER_ID))
				AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((FormationPower) n.getPower(FormationPower.POWER_ID))));
    	}

        if (formationPower == null) {
            formationPower = (FormationPower) getPower(FormationPower.POWER_ID);
        }
        AbstractDungeon.actionManager.addToBottom(new FormationInitAction(formationPower));
    }

    public void spawnSmallSpyder(int str) {
        spawnSmallSpyder(str, null);
    }

    public void spawnSmallSpyder(int str, FormationPower formationPower) {

        if(isDying || smallSpyderAmt >= smallSpyders.length)
            return;

        int i = -1;
        while(true) {
            i = AbstractDungeon.miscRng.random(smallSpyderAmt + 4);
            if(i < smallSpyders.length && smallSpyders[i] == null)
                break;
        }

        int chosen = AbstractDungeon.monsterRng.random(1);
        AbstractMonster m = null;
        switch(chosen) {
        case 0:
            m = new SneakySpyder(this, i, str);
            break;
        case 1:
            m = new FatSpyder(this, i, str);
            break;
        }

        TheActMod.logger.info("Spawning Monster");
        smallSpyderAmt++;
        smallSpyders[i] = m;
        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m,true));

        for(AbstractMonster n : AbstractDungeon.getMonsters().monsters) {
            if(n instanceof SpawnedSpyder && n.hasPower(SquadPower.POWER_ID))
                ((SquadPower) n.getPower(SquadPower.POWER_ID)).spyderSpawn();
            if(n instanceof SpawnedSpyder && n.hasPower(FormationPower.POWER_ID))
                AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((FormationPower) n.getPower(FormationPower.POWER_ID))));
        }

        if (formationPower == null) {
            formationPower = (FormationPower) getPower(FormationPower.POWER_ID);
        }
        AbstractDungeon.actionManager.addToBottom(new FormationInitAction(formationPower));
    }

    public void resolveSpyderDeath(SpawnedSpyder m){
    	if(m.small) {
    		smallSpyderAmt--;
    		smallSpyders[m.slot] = null;
    	}else {
    		bigSpyderAmt--;
    		bigSpyders[m.slot] = null;    		
    	}
    }


    public void takeTurn() {
        switch(this.nextMove) {
            case 0:
            	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
            	AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AbstractGameAction.AttackEffect.BLUNT_LIGHT, true));
            	AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));
                break;
            case 1:
            	int str = 0;
            	if(hasPower(StrengthPower.POWER_ID))
            		str = getPower(StrengthPower.POWER_ID).amount;
            	spawnSmallSpyder(str);
            	spawnSmallSpyder(str);
            	if (AbstractDungeon.ascensionLevel >= 19)
                	spawnSmallSpyder(str);
            	break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hurt", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }

    }

    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        
        for (final AbstractMonster m : bigSpyders) {
            if (m != null && !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }
        
        for (final AbstractMonster m : smallSpyders) {
            if (m != null && !m.isDying) {
                AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
            }
        }

        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();

    }

    protected void getMove(int num) {
    	turnAmt++;
    	if(turnAmt >= 4) {
    		turnAmt -= 4;
    		this.setMove((byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base, 2, true);
    	}else {
    		if(smallSpyderAmt < 12)
    			this.setMove((byte)1, Intent.UNKNOWN);
    		else
    			this.setMove((byte) 0, Intent.ATTACK_BUFF, this.damage.get(0).base, 2, true);
    	}
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }
}
