package theAct.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.actions.unique.VampireDamageAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.FungiBeast;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import theAct.TheActMod;
import theAct.powers.FungalInfectionPower;
import theAct.powers.InfectiousSporesPower;
import theAct.powers.PuffballPower;
import theAct.cards.fungalobungalofunguyfuntimes.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FunGuy extends AbstractMonster {
	public static final String ID = TheActMod.makeID("FunGuy");
	private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
	
	public static final int CHMP_DMG = 12;
	public static final int A_CHMP_DMG = 16;
	public static final int BURST_DMG = 4;
	public static final int A_BURST_DMG = 6;
	public static final int BURST_AMT = 2;
	public static final int A_BURST_AMT = 2;
	public static final int ASS_DMG = 2;
	public static final int A_ASS_DMG = 2;
	public static final int INF_AMT = 2;
	public static final int A_INF_AMT = 3;

    private static final byte CHOMP = 1;
    private static final byte INFECTION = 2;
    private static final byte SPREAD = 3;
    private static final byte BURST = 4;
    private static final byte GROWTH = 5;
    private static final byte ASS = 6;

	private int chompDamage;
	private int burstDamage;
	private int assymDamage;
	private int burstAmount;
	private int infectionAmount;

	private AbstractCard card;
    public static final Logger logger = LogManager.getLogger("fffffffff");

	public FunGuy() {
		super(STRINGS.NAME, ID, 360, 0, 0, 300, 400, null, -50f, -50f);
		this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/funguy/placeholder.png"));
		this.type = AbstractMonster.EnemyType.BOSS;
		if (AbstractDungeon.ascensionLevel >= 4){
			this.chompDamage = A_CHMP_DMG;
			this.burstDamage = A_BURST_DMG;
			this.assymDamage = A_ASS_DMG;
			this.burstAmount = A_BURST_AMT;
		} else {
			this.chompDamage = CHMP_DMG;
			this.burstDamage = BURST_DMG;
			this.assymDamage = ASS_DMG;
			this.burstAmount = BURST_AMT;
		}
		if (AbstractDungeon.ascensionLevel >= 19){
			this.infectionAmount = A_INF_AMT;
		} else {
			this.infectionAmount = INF_AMT;
		}

	    if (AbstractDungeon.ascensionLevel >= 9) {
	      setHp(380);
	    }

		this.damage.add(new DamageInfo(this, chompDamage));
		this.damage.add(new DamageInfo(this, burstDamage));
		this.damage.add(new DamageInfo(this, assymDamage));
	}
	
	private void spawnTheBeasts(int cloud, int infection) {
		AbstractMonster m = new FungiBeast(-300, 0);
		AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true));
		if (cloud > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SporeCloudPower(m, cloud)));
		}
		if (infection > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new InfectiousSporesPower(m, infection)));
		}
		m = new FungiBeast(-550, 0);
		AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m, true));
		if (cloud > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new SporeCloudPower(m, cloud)));
		}
		if (infection > 0) {
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, m, new InfectiousSporesPower(m, infection)));
		}
	}
	
	@Override
    public void usePreBattleAction() {
		CardCrawlGame.music.unsilenceBGM();
		AbstractDungeon.scene.fadeOutAmbiance();
		AbstractDungeon.getCurrRoom().playBgmInstantly("BOSSTOTEM");
        UnlockTracker.markBossAsSeen(ID);
        
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new PuffballPower(this), 1));
		// this.spawnTheBeasts(AbstractDungeon.ascensionLevel >= 4 ? 2 : 1, AbstractDungeon.ascensionLevel >= 19 ? 2 : 0);
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;

		switch(this.nextMove) {
		case CHOMP: // Deal 18(24) Damage
			AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			break;
		case INFECTION: // Apply 2 Vuln, Gain 14 Block
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new VulnerablePower(AbstractDungeon.player, this.infectionAmount, true), this.infectionAmount));
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 14));
			break;
		case BURST: // Deal 6(8)x2 damage, scales up the number of attacks for later
			for (int i=0; i<this.burstAmount; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
			}
			this.burstAmount++;
			break;
		case ASS: // Eat the minions, Absorb their remaining HP 
			AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(AbstractDungeon.player, this.damage.get(2), AbstractGameAction.AttackEffect.POISON));
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (m != null && m != this && !m.isDeadOrEscaped()) {
					AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4f));
					if (m.hasPower(SporeCloudPower.POWER_ID)) {
						AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, this, SporeCloudPower.POWER_ID));
					}
					if (m.hasPower(InfectiousSporesPower.powerID)) {
						AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(m, this, InfectiousSporesPower.powerID));
					}
					AbstractDungeon.actionManager.addToBottom(new VampireDamageAction(m, this.damage.get(3), AbstractGameAction.AttackEffect.POISON));
					AbstractDungeon.actionManager.addToBottom(new SuicideAction(m));
				}
			}
			break;
		case GROWTH: // Gain 4 Strength, Upgrade all the spores
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, this.infectionAmount), this.infectionAmount));

		    for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
		      if ((c instanceof SS_Toxin || c instanceof SS_Clouding || c instanceof SS_Leeching)) {
		        c.upgrade();
		      }
		    }
		    for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
		      if ((c instanceof SS_Toxin || c instanceof SS_Clouding || c instanceof SS_Leeching)) {
		        c.upgrade();
		      }
		    }

  			break;
		case SPREAD: // Multi-hit for 3x5 and Block. Primarily gives spores, but is very scary after the 3(4) str buff.
			for (int i=0; i<4; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(2), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
			}
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 9));
			break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	// How I want it to act... 
	//
	// High Level:
	//	 Fills your deck with spores
	//		How many spores?
	//			Sentries does 3(4.5) a turn
	//			Slime Boss does 1(1.66) a turn
	//			Slavers do 1(2) a turn
	//			Stabby Book does 2.33(3) a turn
	//		About 3(4) a turn
	//
	//	 Does a lot of damage
	//		How much damage?
	//			Sentries does 13.5(15) a turn
	//			Slime Boss does 11.66(12.66) a turn
	//			Slavers do 26(32) a turn
	//			Stabby Book does 21(24) a turn
	//		About 20(25) a turn
	//
	//   Occasionally debuffs you
	//		What debuff should it use?
	//			Vuln is thematic, considering small parasites
	//			Block would be nice, maybe the BLOCK_DEBUFF intent
	//			

	@Override
	protected void getMove(int roll) {

		//	 At half health it'll buff itself and the spores
		//		All spores get upgraded
		//		It gains a bunch of strength
	    if ((this.currentHealth < this.maxHealth / 2) && (!this.moveHistory.contains(GROWTH))) {
			this.setMoveNow(GROWTH);
	      	return;
	    }

		// Start with this, and then every four turns (five turns on A19+). You'll be vuln for all but one of the break turns.
        if (this.moveHistory.isEmpty() || (AbstractDungeon.actionManager.turn % (this.infectionAmount+2)) == this.infectionAmount+1) {
			this.setMoveNow(INFECTION);
			return;
        }

        // When we're not making them vulnerable, we're attacking, which also gives them spores.
        // Random between Chomp, Burst, and Spread. Can't do the same one twice.
   		if (roll < 33 && !this.lastMove(BURST)) {
			this.setMoveNow(BURST);
			return;
		}
   		else if (roll > 66 && !this.lastMove(CHOMP)) {
			this.setMoveNow(CHOMP);
			return;
		}
   		else if (roll > 33 && roll < 66 && !this.lastMove(SPREAD)) {
			this.setMoveNow(SPREAD);
			return;
		}
		this.rollMove();
	}

	//handy function to quickly set the next turn
	private void setMoveNow(byte nextTurn) {
		switch (nextTurn) {
            case CHOMP: {
            	this.setMove(nextTurn, Intent.ATTACK, this.damage.get(0).base);
            	return;
			}
            case INFECTION: {
            	this.setMove(nextTurn, Intent.DEFEND_DEBUFF);
            	return;
			}
			case SPREAD: {
				this.setMove(nextTurn, Intent.ATTACK_DEFEND, this.damage.get(2).base, 5, true);
            	return;
			}
			case BURST: {
				this.setMove(nextTurn, Intent.ATTACK, this.damage.get(1).base, this.burstAmount, true);
            	return;		
			}
			case GROWTH: {
				this.setMove(nextTurn, Intent.BUFF);
            	return;
			}
			case ASS: {
				this.setMove(nextTurn, Intent.ATTACK_BUFF, this.damage.get(2).base);
            	return;
			}
			default: {
				this.setMove(nextTurn, Intent.NONE);	
            	return;	
			}
		}
	}

	public void die() {

		this.useFastShakeAnimation(5.0F);
		CardCrawlGame.screenShake.rumble(4.0F);
		for (AbstractMonster m: AbstractDungeon.getMonsters().monsters) {
            AbstractDungeon.actionManager.addToBottom(new EscapeAction(m));
        }
		super.die();

		AbstractDungeon.scene.fadeInAmbiance();
		this.onBossVictoryLogic();

	}
	
}
