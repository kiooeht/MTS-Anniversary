package theAct.monsters;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
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
import theAct.powers.IncubationPower;

public class SneckoEgg extends AbstractMonster {
	public static final String ID = TheActMod.makeID("SneckoEgg");
	private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);
	public static final String[] MOVES = STRINGS.MOVES;

	private static final int HP_MIN = 8;
	private static final int HP_MAX = 12;
	private static final int ASC_HP_MIN = 10;
	private static final int ASC_HP_MAX = 14;
	private static final byte CRACK = 1;
	private static final byte HATCH = 2;
	private static final String CRACK_NAME = MOVES[0];
	private static final String HATCH_NAME = MOVES[1];

	private float snekX;
	private float snekY;
	public int posIndex;

	public SneckoEgg(final float x, final float y,final int posIndex) {
		super(STRINGS.NAME, ID, HP_MAX, 0, 0, 120, 150, null, x, y);
		snekX = x;
		snekY = y;
		this.posIndex = posIndex;
		//this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));

		if (AbstractDungeon.ascensionLevel >= 8) {
			setHp(ASC_HP_MIN, ASC_HP_MAX);
		}
		else {
			setHp(HP_MIN, HP_MAX);
		}

		this.animY += 25f;

		this.loadAnimation(
				TheActMod.assetPath("images/monsters/SneckoEgg/SneckoEgg.atlas"),
				TheActMod.assetPath("images/monsters/SneckoEgg/SneckoEgg.json"),
				2f);


		AnimationState.TrackEntry e = this.state.setAnimation(0, "Idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());

		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this,new IncubationPower(this,2),2));
	}

	@Override
	public void takeTurn() {
		AbstractPlayer p = AbstractDungeon.player;

		switch(this.nextMove) {
			case CRACK:
				break;

			case HATCH:
				break;
		}



		AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
	}

	public void hatch(){
		for (AbstractMonster m : AbstractDungeon.getMonsters().monsters){
			if (m instanceof MamaSnecko){
				// eggs are hatching, no longer get angry when no eggs exist
				((MamaSnecko) m).waitingForEggs = false;
			}
		}
		AbstractDungeon.actionManager.addToTop(new HideHealthBarAction(this));
		AbstractDungeon.actionManager.addToTop(new SuicideAction(this));
		AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new BabySnecko(snekX,snekY,posIndex),true));
	}

	@Override
	protected void getMove(int roll) {
		if (this.hasPower(IncubationPower.powerID) && this.getPower(IncubationPower.powerID).amount <= 2 && this.lastMove(CRACK)){
			this.setMove(HATCH_NAME,HATCH,Intent.BUFF);
		}
		else{
			this.setMove(CRACK_NAME,CRACK,Intent.STUN);
		}
	}
}
