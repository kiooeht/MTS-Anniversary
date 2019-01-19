package theAct.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theAct.monsters.Phrog;
import theAct.powers.DigestPower;

public class PhrogLickAction extends AbstractGameAction {
	private Phrog owner;
	private AbstractCard card;
	private float startingDuration;
	private CardGroup group;
	private int hits;

	public PhrogLickAction(Phrog owner, int hits) {
		this.card = null;
		this.owner = owner;
		this.duration = Settings.ACTION_DUR_LONG;
		this.startingDuration = Settings.ACTION_DUR_LONG;
		this.actionType = ActionType.WAIT;
		this.group = AbstractDungeon.player.drawPile;
		this.hits = hits;
	}

	@Override
	public void update() {
		if(this.group.isEmpty()) {
			this.group = AbstractDungeon.player.discardPile;
			if(this.group.isEmpty()){
				this.group = AbstractDungeon.player.hand;
			}
		}
		if(this.duration == this.startingDuration){
			this.card = group.getRandomCard(false, AbstractCard.CardRarity.RARE);
			if(this.card == null) {
				this.card = group.getRandomCard(false, AbstractCard.CardRarity.UNCOMMON);
				if(this.card == null) {
					this.card = group.getRandomCard(false, AbstractCard.CardRarity.COMMON);
					if(this.card == null) {
						this.card = group.getRandomCard(false);
					}
				}
			}
			group.removeCard(this.card);
			AbstractDungeon.player.limbo.addToBottom(this.card);
			this.card.setAngle(0.0f);
			this.card.targetDrawScale = 0.75f;
			this.card.target_x = Settings.WIDTH / 2.0f;
			this.card.target_y = Settings.HEIGHT / 2.0f;
			this.card.lighten(false);
			this.card.unfadeOut();
			this.card.unhover();
			this.card.untip();
			this.card.stopGlowing();
		}
		this.tickDuration();
		if(this.isDone && this.card != null){
			AbstractDungeon.actionManager.addToBottom(new ShowCardAction(this.card));
			AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(owner, owner, new DigestPower(owner, card, hits), hits));
		}
	}
}
