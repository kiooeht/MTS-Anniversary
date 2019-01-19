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

	private int maxHP = 113;
	private int minHP = 97;
	private int tackleDamage = 25;

	private AbstractCard card;

	public Phrog() {
		super(STRINGS.NAME, ID, 75, 0, 0, 300, 300, null, 0, 0f);

		//this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));

		switch(AbstractDungeon.ascensionLevel){
			case 7:
				this.minHP += 5;
				this.maxHP += 5;
			case 2:
				this.tackleDamage += 2;
		}

		this.damage.add(new DamageInfo(this, tackleDamage));

		this.setHp(minHP, maxHP);

		this.animY += 25f;

		this.loadAnimation(
			TheActMod.assetPath("images/monsters/phrog/Phrog.atlas"),
			TheActMod.assetPath("images/monsters/phrog/Phrog.json"),
			0.75f);

		AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;

		switch(this.nextMove) {
			case MoveBytes.LICK:
				AbstractDungeon.actionManager.addToBottom(new TalkAction(this, STRINGS.DIALOG[0], 1.0f, 2.0f));
				AbstractDungeon.actionManager.addToBottom(new PhrogLickAction(this, 3));
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
		if(!this.lastMove(MoveBytes.LICK)) {
			this.setMove(STRINGS.MOVES[0], MoveBytes.LICK, Intent.MAGIC);
			return;
		}
		switch(card.type){
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
			case CURSE:
				this.setMove(MoveBytes.STUNNED, Intent.STUN);
				break;
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
