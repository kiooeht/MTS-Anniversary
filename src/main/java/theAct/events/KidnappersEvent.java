package theAct.events;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.MonsterHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theAct.TheActMod;

public class KidnappersEvent extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("Kidnappers");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private int screenNum = 0;
    private int HEALTH_LOSS;
    private boolean cardSelected = false;
    private static int GOLD_AMT;

    public KidnappersEvent() {
        super(NAME, DESCRIPTIONS[0], TheActMod.assetPath("images/events/KidnappersEvent.png"));
        imageEventText.setDialogOption(OPTIONS[0]);
        if (AbstractDungeon.ascensionLevel >= 15) {
            HEALTH_LOSS = 15;
        } else {
            HEALTH_LOSS = 10;
        }
        if (AbstractDungeon.ascensionLevel >= 15) {
            GOLD_AMT = 100;
        } else {
            GOLD_AMT = 50;
        }
    }

    @Override
    protected void buttonEffect(int i) {
        switch(screenNum) {
            case 0:
                switch(i) {
                    case 0:
                        imageEventText.loadImage(TheActMod.assetPath("images/events/KidnappersEventTwo.png"));
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        imageEventText.setDialogOption(OPTIONS[1] + HEALTH_LOSS + OPTIONS[2]);
                        imageEventText.setDialogOption(OPTIONS[3]);
                        imageEventText.setDialogOption(OPTIONS[4], AbstractDungeon.player.gold >= GOLD_AMT);
                        screenNum = 1;
                        break;
                }
                break;
            case 1:
                switch(i) {
                    case 0:
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, HEALTH_LOSS));
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                        removeCard();
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.setDialogOption(OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        screenNum = 2;
                        break;
                    case 1:
                        AbstractDungeon.getCurrRoom().monsters = MonsterHelper.getEncounter("KidnapperSilents");
                        enterCombatFromImage();
                        AbstractDungeon.lastCombatMetricKey = "Colosseum Nobs";
                    case 2:
                        AbstractDungeon.player.loseGold(GOLD_AMT);
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        imageEventText.setDialogOption(OPTIONS[5]);
                        imageEventText.clearRemainingOptions();
                        screenNum = 2;
                }
                break;
            case 2:
                openMap();
                break;
        }
    }

    @Override
    public void update() {
        super.update();
        if (!cardSelected && !AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            cardSelected = true;
            AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
            AbstractDungeon.topLevelEffectsQueue.add(new PurgeCardEffect(c, Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
        }
    }

    private void removeCard() {
        if (!AbstractDungeon.isScreenUp) {
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[6], false, false, false, false);
        } else {
            AbstractDungeon.dynamicBanner.hide();
            AbstractDungeon.previousScreen = AbstractDungeon.screen;
            AbstractDungeon.gridSelectScreen.open(AbstractDungeon.player.masterDeck, 1, OPTIONS[6], false, false, false, false);
        }
    }
}
