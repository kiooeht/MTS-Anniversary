package theAct.monsters.SpyderBoss;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

import java.util.ArrayList;

public class SpyderBoss extends AbstractMonster {
    public static final String ID = TheActMod.makeID("SpyderNest");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private int smallSpyders = 0;
    public ArrayList<AbstractMonster> spyders = new ArrayList<AbstractMonster>();

    public SpyderBoss() {
        super(NAME, ID, AbstractDungeon.monsterHpRng.random(135, 150), 0.0F, -30.0F, 220.0F, 320.0F, (String)null, -20.0F, 10.0F);
        this.type = EnemyType.BOSS;
        this.loadAnimation("images/monsters/theForest/mage/skeleton.atlas", "images/monsters/theForest/mage/skeleton.json", 1.0F); //NOT DONE
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(155, 170);
        } else {
            this.setHp(135, 150);
        }      
        
        if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 18));
        } else {
            this.damage.add(new DamageInfo(this, 14));
        }

        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Idle", "Sumon", 0.1F);
        this.stateData.setMix("Sumon", "Idle", 0.1F);
        this.stateData.setMix("Hurt", "Idle", 0.1F);
        this.stateData.setMix("Idle", "Hurt", 0.1F);
        this.stateData.setMix("Attack", "Idle", 0.1F);
        e.setTime(e.getEndTime() * MathUtils.random());
    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");
        /*
        spawnBigSpyder();
        spawnSmallSpyder();
        spawnSmallSpyder();
        spawnSmallSpyder();
        */
    }

    public void spawnBigSpyder() {
    	
    	int chosen = AbstractDungeon.monsterRng.random(3);
    	AbstractMonster m = null;
    	switch(chosen) {
	    	case 0:
	    		m = null;
	    		break;
	    	
	    	case 1:
	    		m = null;
	    		break;
	    	
	    	case 2:
	    		m = null;
	    		break;
	    	
	    	case 3:
	    		m = null;
	    		break;
	    	
    	}

        spyders.add(m);
        TheActMod.logger.info("Spawning Monster");

        AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m,true,-99));        
    }

    public void resolveSpyderDeath(AbstractMonster m){
    	if(m.id.contains("Small"))
    		smallSpyders--;
        spyders.remove(m);
        
    }


    public void takeTurn() {
        switch(this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                break;
            case 1:
            	//spawn
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

        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();

    }

    protected void getMove(int num) {
    	if(smallSpyders < 10)
    		this.setMove((byte)1, Intent.UNKNOWN);
    	else
    		this.setMove((byte) 0, Intent.ATTACK);
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }
}
