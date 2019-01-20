package theAct.events;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theAct.TheActMod;
import theAct.relics.SneckoAutograph;
import theAct.relics.SpiritDisease;

public class SneckoIdol extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("SneckoIdol");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;
    private static final float DAMAGE_AMT = 0.15F;
    private static final float DAMAGE_AMT_ASC = 0.30F;
    private static final int GOLD_AMT = 120;
    private static final int GOLD_AMT_ASC = 100;
    private int screenNum = 0;
    private int damage;
    private int gold;

    public SneckoIdol() {
        super(NAME, DESCRIPTIONS[0], TheActMod.assetPath("images/events/SneckoIdol.png"));
        if (AbstractDungeon.ascensionLevel >= 15) {
            damage = (int)(AbstractDungeon.player.maxHealth * DAMAGE_AMT_ASC);
            gold = GOLD_AMT_ASC;
        } else {
            damage = (int)(AbstractDungeon.player.maxHealth * DAMAGE_AMT);
            gold = GOLD_AMT;
        }
        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1] + gold + OPTIONS[2] + damage + OPTIONS[3]);
        imageEventText.setDialogOption(OPTIONS[4]);
    }

    @Override
    protected void buttonEffect(int i) {
        switch (screenNum) {
            case 0:
                switch(i) {
                    case 0:
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.updateBodyText(DESCRIPTIONS[1]);
                        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(CardLibrary.getCopy(Shame.ID), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, new SneckoAutograph());
                        imageEventText.clearRemainingOptions();
                        break;
                    case 1:
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.updateBodyText(DESCRIPTIONS[2]);
                        AbstractDungeon.player.damage(new DamageInfo(AbstractDungeon.player, damage));
                        AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, AbstractGameAction.AttackEffect.FIRE));
                        AbstractDungeon.player.gainGold(gold);
                        imageEventText.clearRemainingOptions();
                        break;
                    case 2:
                        imageEventText.updateDialogOption(0, OPTIONS[4]);
                        imageEventText.updateBodyText(DESCRIPTIONS[3]);
                        imageEventText.clearRemainingOptions();
                        break;
                }
                break;
            case 1:
                openMap();
                break;
        }
    }
}
