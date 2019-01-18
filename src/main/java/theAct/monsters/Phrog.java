package theAct.monsters;

import com.megacrit.cardcrawl.actions.unique.ApplyStasisAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theAct.TheActMod;

public class Phrog extends AbstractMonster {
	/*
		Turn one he takes a card and then keeps it for 3 turns.
	 */
	public static final String ID = TheActMod.makeID("Phrog");
	private static final MonsterStrings STRINGS = CardCrawlGame.languagePack.getMonsterStrings(ID);

	private int maxHP = 73;
	private int minHP = 87;

	AbstractCard heldCard;

	public Phrog() {
		super(STRINGS.NAME, ID, 75, 0, 0, 300, 300, null, 0, 0);

		this.img = ImageMaster.loadImage(TheActMod.assetPath("/images/monsters/phrog/temp.png"));

		switch(AbstractDungeon.ascensionLevel){
			case 7:
				//hp increase
			case 2:
				//two damage increase
		}

		setHp(minHP, maxHP);
	}

	@Override
	public void takeTurn() {
		switch(this.nextMove) {
			case 0:
				AbstractDungeon.actionManager.addToBottom(new ApplyStasisAction(this));
				break;
		}
	}

	@Override
	protected void getMove(int roll) {
		if(this.heldCard == null) {
			this.setMove(STRINGS.MOVES[0], MoveBytes.LICK, Intent.MAGIC);
		}
	}

	private static class MoveBytes {
		private static final byte LICK = 0;
	}
}
