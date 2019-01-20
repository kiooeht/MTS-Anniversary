package theAct.relics;

import basemod.ReflectionHacks;
import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.mod.stslib.relics.ClickableRelic;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import theAct.TheActMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SneckoAutograph extends CustomRelic implements ClickableRelic {
    public static final String ID = TheActMod.makeID("SneckoAutograph");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    float shopBubbleX = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
    float shopBubbleY = Settings.HEIGHT - 380.0F * Settings.scale;
    private static ArrayList<StoreRelic> relics = new ArrayList<>();
    private static boolean removeRelic = false;
    private static int rng;

    public SneckoAutograph() {
        super(ID, ImageMaster.loadImage(TheActMod.assetPath("images/relics/SneckoAutograph.png")), RelicTier.SPECIAL, LandingSound.FLAT);
        counter = -4;
        getUpdatedDescription();
    }

    @Override
    public void onEnterRoom(AbstractRoom r) {
        if (r instanceof ShopRoom) {
            flash();
        }
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom() instanceof ShopRoom && counter == -4) {
            AbstractDungeon.topLevelEffectsQueue.add(new ShopSpeechBubble(shopBubbleX, shopBubbleY, DESCRIPTIONS[3], true));
            counter = -2;
            ShopScreen shop = AbstractDungeon.shopScreen;
            relics = (ArrayList<StoreRelic>)ReflectionHacks.getPrivate(shop, ShopScreen.class, "relics");
            rng = AbstractDungeon.miscRng.random(relics.size() - 1);
            removeRelic = true;
            relics.get(rng).relic.instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
        }
        getUpdatedDescription();
    }


    @Override
    public AbstractRelic makeCopy() {
        return new SneckoAutograph();
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter == -2) {
            return DESCRIPTIONS[2];
        } else {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        }
    }

    public static void iHatePostUpdate() {
        if (removeRelic) {
            relics.get(rng).isPurchased = true;
            removeRelic = false;
        }
    }
}
