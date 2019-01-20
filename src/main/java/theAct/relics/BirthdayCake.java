package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.cards.curses.Necronomicurse;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theAct.TheActMod;
import theAct.cards.curses.EternalShame;
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
    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(HappyBirthday.CAKE_MAX_HP, true);

        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new EternalShame(), Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
        UnlockTracker.markCardAsSeen(EternalShame.ID);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BirthdayCake();
    }
}
