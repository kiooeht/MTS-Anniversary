package theAct.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theAct.TheActMod;

public class ParSnip extends CustomRelic {

    public static final String ID = TheActMod.makeID("ParSnip");
    public static final Texture IMAGE_PATH = ImageMaster.loadImage(TheActMod.assetPath("images/relics/Parsnip.png"));
    public static final Texture IMAGE_OUTLINE_PATH = ImageMaster.loadImage(TheActMod.assetPath("images/relics/ParsnipOutline.png"));

    public ParSnip() {
        super(ID, IMAGE_PATH, IMAGE_OUTLINE_PATH, RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    public void onEquip() {
        flash();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.rarity == AbstractCard.CardRarity.BASIC && c.canUpgrade()) {
                c.upgrade();
                AbstractDungeon.player.bottledCardUpgradeCheck(c);
                AbstractDungeon.effectList.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(), MathUtils.random(0.1f, 0.9f) * Settings.WIDTH, MathUtils.random(0.2f, 0.8f) * Settings.HEIGHT));
            }
        }
    }

    public AbstractRelic makeCopy() {
        return new ParSnip();
    }
}
