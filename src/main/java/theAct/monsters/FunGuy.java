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
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.SporeCloudPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import theAct.TheActMod;
import theAct.powers.FungalInfectionPower;
import theAct.powers.InfectiousSporesPower;

public class FunGuy extends AbstractMonster {
	public static final String ID = TheActMod.makeID("FunGuy");
	private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
	

	public static final int CHMP_DMG = 12;
	public static final int A_CHMP_DMG = 15;
	public static final int BURST_DMG = 2;
	public static final int A_BURST_DMG = 3;
	public static final int BURST_AMT = 2;
	public static final int A_BURST_AMT = 2;
	public static final int ASS_DMG = 8;
	public static final int A_ASS_DMG = 10;
	public static final int INF_AMT = 3;
	public static final int A_INF_AMT = 4;

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

	public FunGuy() {
		super(STRINGS.NAME, ID, (AbstractDungeon.ascensionLevel >= 9) ? 282 : 300, 0, 0, 300, 300, null, 150f, -50f);
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
		

		this.damage.add(new DamageInfo(this, chompDamage));
		this.damage.add(new DamageInfo(this, burstDamage));
		this.damage.add(new DamageInfo(this, assymDamage));
		this.damage.add(new DamageInfo(this, assymDamage+(AbstractDungeon.ascensionLevel >= 9 ? 2 : 0)));
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
        
		this.spawnTheBeasts(AbstractDungeon.ascensionLevel >= 4 ? 2 : 1, AbstractDungeon.ascensionLevel >= 19 ? 2 : 0);
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;

		switch(this.nextMove) {
		case CHOMP:
			AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
			break;
		case INFECTION:
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FungalInfectionPower(AbstractDungeon.player, this.infectionAmount), this.infectionAmount));
			break;
		case BURST:
			for (int i=0; i<this.burstAmount; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(1), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
			}
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, 1, true), 1));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, 1, true), 1));
			this.burstAmount++;
			break;
		case ASS:
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
		case GROWTH:
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 2), 2));
			AbstractDungeon.actionManager.addToBottom(new GainBlockAction(this, this, 8));
			for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
				if (m != null && m != this && !m.isDeadOrEscaped()) {
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new StrengthPower(m, 1), 1));
					AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m, this, new SporeCloudPower(m, 1), 1));
				}
			}
			break;
		case SPREAD:
			this.spawnTheBeasts(1, AbstractDungeon.ascensionLevel >= 19 ? 2 : 1);
			break;
		}

		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}
	
	@Override
	protected void getMove(int roll) {
		if (this.lastMove(ASS)) {
			this.setMoveNow(SPREAD);
			return;
		}
		if (!this.lastMove(INFECTION) && !AbstractDungeon.player.hasPower(FungalInfectionPower.powerID) && roll % 2 == 1) {
			this.setMoveNow(INFECTION);
			return;
		}
		if (roll < 33) {
			if (this.lastMove(CHOMP)) {
				this.setMoveNow(GROWTH);
				return;
			} else {
				this.setMoveNow(CHOMP);
				return;
			}
		} else if (roll < 67) {
			if (this.lastMove(BURST)) {
				this.setMoveNow(GROWTH);
				return;
			} else {
				this.setMoveNow(BURST);
				return;
			}
		} else {
			if (this.lastMove(SPREAD) || this.lastMove(ASS)) {
				this.setMoveNow(GROWTH);
				return;
			} else {
				this.setMoveNow(ASS);
				return;
			}
		}
	}
	//handy function to quickly set the next turn
	private void setMoveNow(byte nextTurn) {
		switch (nextTurn) {
            case CHOMP: {
            	this.setMove(nextTurn, Intent.ATTACK, this.damage.get(0).base);
            	return;
			}
            case INFECTION: {
            	this.setMove(nextTurn, Intent.STRONG_DEBUFF);
            	return;
			}
			case SPREAD: {
				this.setMove(nextTurn, Intent.UNKNOWN);
            	return;
			}
			case BURST: {
				this.setMove(nextTurn, Intent.ATTACK_DEBUFF, this.damage.get(1).base, this.burstAmount, true);
            	return;		
			}
			case GROWTH: {
				this.setMove(nextTurn, Intent.DEFEND_BUFF);
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
