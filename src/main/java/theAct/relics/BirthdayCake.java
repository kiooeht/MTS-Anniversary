package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.cards.curses.Shame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import theAct.TheActMod;
import theAct.events.HappyBirthday;

public class BirthdayCake extends CustomRelic
{
    public static final String ID = TheActMod.makeID("BirthdayCake");
    public static final String IMG_PATH = TheActMod.assetPath("images/relics/birthdayCake.png");
    public static final String OUTLINE_PATH = TheActMod.assetPath("images/relics/birthdayCakeOutline.png");

    public BirthdayCake()
    {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HappyBirthday.CAKE_MAX_HP + DESCRIPTIONS[1];
    }

    @Override
    public int getPrice()
    {
        // Stop this from being sold via ShopMod
        return 0;
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(HappyBirthday.CAKE_MAX_HP, true);
    }

    @Override
    public void atBattleStart()
    {
        flash();
        AbstractDungeon.actionManager.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Shame(), 1, true, true));
        UnlockTracker.markCardAsSeen(Shame.ID);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BirthdayCake();
    }
}
