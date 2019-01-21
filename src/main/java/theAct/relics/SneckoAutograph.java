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
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.vfx.ShopSpeechBubble;
import com.megacrit.cardcrawl.vfx.SpeechTextEffect;
import theAct.TheActMod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class SneckoAutograph extends CustomRelic implements ClickableRelic {
    public static final String ID = TheActMod.makeID("SneckoAutograph");
    private static RelicStrings relicStrings = CardCrawlGame.languagePack.getRelicStrings(ID);
    public static final String NAME = relicStrings.NAME;
    public static final String[] DESCRIPTIONS = relicStrings.DESCRIPTIONS;
    private static final String IMG_PATH = TheActMod.assetPath("images/relics/SneckoAutograph.png");
    private static final String OUTLINE_PATH = TheActMod.assetPath("images/relics/SneckoAutographOutline.png");

    float shopBubbleX = MathUtils.random(660.0F, 1260.0F) * Settings.scale;
    float shopBubbleY = Settings.HEIGHT - 380.0F * Settings.scale;
    private static ArrayList<StoreRelic> relics = new ArrayList<>();
    private static boolean relicStuff = false;
    private static int rng;

    public SneckoAutograph() {
        super(ID, ImageMaster.loadImage(IMG_PATH), ImageMaster.loadImage(OUTLINE_PATH), RelicTier.SPECIAL, LandingSound.FLAT);
        getUpdatedDescription();
    }

    @Override
    public void onEquip() {
        setCounter(-4);
    }

    @Override
    public void onEnterRoom(AbstractRoom r) {
        if (r instanceof ShopRoom && counter == -4) {
            flash();
        }
    }

    @Override
    public void setCounter(int i) {
        super.setCounter(i);
        description = DESCRIPTIONS[2];
        tips.clear();
        tips.add(new PowerTip(name, description));
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom() instanceof ShopRoom && counter == -4) {
            AbstractDungeon.topLevelEffectsQueue.add(new ShopSpeechBubble(shopBubbleX, shopBubbleY, 4.0f, DESCRIPTIONS[3], false));
            AbstractDungeon.topLevelEffectsQueue.add(new SpeechTextEffect(shopBubbleX + -166.0F * Settings.scale, shopBubbleY + 126.0F * Settings.scale, 4.0F, DESCRIPTIONS[3], DialogWord.AppearEffect.BUMP_IN));
            setCounter(-2);
            description = DESCRIPTIONS[2];
            tips.clear();
            tips.add(new PowerTip(name, description));
            initializeTips();
            ShopScreen shop = AbstractDungeon.shopScreen;
            relics = (ArrayList<StoreRelic>)ReflectionHacks.getPrivate(shop, ShopScreen.class, "relics");
            rng = AbstractDungeon.miscRng.random(relics.size() - 1);
            relicStuff = true;
        }
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
        }
        return DESCRIPTIONS[0] + DESCRIPTIONS[1];
    }

    public static void iHatePostUpdate() {
        if (relicStuff) {
            relics.get(rng).isPurchased = true;
            relics.get(rng).relic.makeCopy().instantObtain(AbstractDungeon.player, AbstractDungeon.player.relics.size(), true);
            relicStuff = false;
        }
    }
}
