package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;
import theAct.events.HappyBirthday;

public class BirthdayCakeSlice extends CustomRelic
{
    public static final String ID = TheActMod.makeID("BirthdayCakeSlice");
    public static final String IMG_PATH = TheActMod.assetPath("images/relics/birthdayCakeSlice.png");
    public static final String OUTLINE_PATH = TheActMod.assetPath("images/relics/birthdayCakeSliceOutline.png");

    public BirthdayCakeSlice()
    {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription()
    {
        return DESCRIPTIONS[0] + HappyBirthday.SLICE_MAX_HP + LocalizedStrings.PERIOD;
    }

    @Override
    public void onEquip()
    {
        AbstractDungeon.player.increaseMaxHp(HappyBirthday.SLICE_MAX_HP, true);
    }

    @Override
    public AbstractRelic makeCopy()
    {
        return new BirthdayCakeSlice();
    }
}
