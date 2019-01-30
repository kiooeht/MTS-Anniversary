package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.TextAboveCreatureAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import theAct.TheActMod;
import theAct.actions.PhrogLickAction;

public class Phrog extends AbstractMonster {
	public static final String ID = TheActMod.makeID("Phrog");
	private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
	private static final int LICK_DAMAGE = 3;
	private static final int LICK_DAMAGE_ASC_MODIFIER = 1;
	private static final int LICK_DAMAGE_ASC_MODIFIER_AGAIN = 1;
	private int maxHP = 106;
	private int minHP = 103;
	private int tackleDamage = 25;
	private int lickDmg;
	private boolean offsetTurn;

	private AbstractCard card;

	public Phrog(float xOffset, float yOffset, boolean offsetTurn) {
		super(STRINGS.NAME, ID, 75, 0, 0, 300, 300, null, 0 + xOffset, 0f + yOffset);

		//this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));
		this.offsetTurn = offsetTurn;
		if (AbstractDungeon.ascensionLevel >= 7) {
			minHP += 5;
			maxHP += 5;
		}
		if (AbstractDungeon.ascensionLevel >= 2) {
			tackleDamage += 2;
			lickDmg = LICK_DAMAGE + LICK_DAMAGE_ASC_MODIFIER;
		}
		if (AbstractDungeon.ascensionLevel >= 17) {
			lickDmg = LICK_DAMAGE + LICK_DAMAGE_ASC_MODIFIER + LICK_DAMAGE_ASC_MODIFIER_AGAIN;
		}
		if (AbstractDungeon.ascensionLevel < 2) {
			lickDmg = LICK_DAMAGE;
		}
		this.damage.add(new DamageInfo(this, tackleDamage));
		damage.add(new DamageInfo(this, lickDmg));

		this.setHp(minHP, maxHP);

		this.animY += 25f;

		this.loadAnimation(
			TheActMod.assetPath("images/monsters/phrog/Phrog.atlas"),
			TheActMod.assetPath("images/monsters/phrog/Phrog.json"),
			0.75f);

		AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());

		this.type = EnemyType.ELITE;

	}

	public void usePreBattleAction() {

		//Only play if this is the slot 0 elite - prevents doubling up on music from each elite triggering it.
		if (AbstractDungeon.getMonsters().monsters.get(0) == this) {
			CardCrawlGame.music.unsilenceBGM();
			AbstractDungeon.scene.fadeOutAmbiance();
			AbstractDungeon.getCurrRoom().playBgmInstantly("JUNGLEELITE");
		}

	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;

		switch(this.nextMove) {
			case MoveBytes.LICK:
				AbstractDungeon.actionManager.addToBottom(new TalkAction(this, STRINGS.DIALOG[0], 1.0f, 2.0f));
				AbstractDungeon.actionManager.addToBottom(new PhrogLickAction(this, 3));
				AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, damage.get(1)));
				break;
			case MoveBytes.TACKLE:
				AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
				AbstractDungeon.actionManager.addToBottom(new DamageAction(p, damage.get(0), AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
				break;
			case MoveBytes.JUMP:
				AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new WeakPower(p, 2, true), 2));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(p, this, new FrailPower(p, 2, true), 2));
				break;
			case MoveBytes.CROAK:
				AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
				AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this, this, new StrengthPower(this, 5), 5));
				break;
			case MoveBytes.STUNNED:
				AbstractDungeon.actionManager.addToBottom(new TextAboveCreatureAction(this, TextAboveCreatureAction.TextType.STUNNED));
				break;
		}



		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	public void changeState(String key){
		switch (key) {
			case "ATTACK":
				this.state.setAnimation(1, "attack", false);
				break;
		}
	}

	@Override
	public void damage(DamageInfo info) {
		super.damage(info);
		if (info.owner != null && info.type != DamageInfo.DamageType.THORNS && info.output > 0) {
			this.state.setAnimation(2, "oof", false);
		}
	}

	public void setCard(AbstractCard card){
		this.card = card;
	}

	@Override
	protected void getMove(int roll) {
		if(offsetTurn && this.moveHistory.isEmpty()) {
			this.setMove(STRINGS.MOVES[1], MoveBytes.JUMP, Intent.STRONG_DEBUFF);
			return;
		}
		if(!this.lastMove(MoveBytes.LICK)) {
			this.setMove(STRINGS.MOVES[0], MoveBytes.LICK, Intent.ATTACK_DEBUFF, damage.get(1).base);
			return;
		}
		if (card != null) {
			switch (card.type) {
				case ATTACK:
					this.setMove(MoveBytes.TACKLE, Intent.ATTACK, damage.get(0).base);
					break;
				case SKILL:
					// 2 frail/weak
					this.setMove(STRINGS.MOVES[1], MoveBytes.JUMP, Intent.STRONG_DEBUFF);
					break;
				case POWER:
					this.setMove(STRINGS.MOVES[2], MoveBytes.CROAK, Intent.BUFF);
					break;
				case STATUS:
					this.setMove(MoveBytes.STUNNED, Intent.STUN);
					break;
				case CURSE:
					this.setMove(MoveBytes.STUNNED, Intent.STUN);
					break;
				default:
					TheActMod.logger.info("I love modded card types -BD");
					this.setMove(MoveBytes.STUNNED, Intent.STUN);
					break;
			}
		} else {
			this.setMove(MoveBytes.TACKLE, Intent.ATTACK, damage.get(0).base);
		}
	}

	private static class MoveBytes {
		private static final byte LICK = 0;
		private static final byte TACKLE = 1;
		private static final byte CROAK = 2;
		private static final byte JUMP = 4;
		private static final byte STUNNED = 3;
	}
}
