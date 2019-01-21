package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theAct.TheActMod;

public class Creamberry extends CustomRelic {

    public static final String ID = TheActMod.makeID("Creamberry");
    public static final Texture IMAGE_PATH = ImageMaster.loadImage(TheActMod.assetPath("images/relics/Creamberry.png"));
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage(TheActMod.assetPath("images/relics/CreamberryOutline.png"));

    public Creamberry() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    // Effect patched.

    public AbstractRelic makeCopy() {
        return new Creamberry();
    }
}
