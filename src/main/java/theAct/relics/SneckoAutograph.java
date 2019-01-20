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
    private ArrayList<AbstractCard> coloredCards = new ArrayList<>();
    private ArrayList<AbstractCard> colorlessCards = new ArrayList<>();
    private ArrayList<StorePotion> potions = new ArrayList<>();
    private ArrayList<StoreRelic> relics = new ArrayList<>();

    public SneckoAutograph() {
        super(ID, ImageMaster.loadImage(TheActMod.assetPath("images/relics/SneckoAutograph.png")), RelicTier.SPECIAL, LandingSound.FLAT);
        counter = 1;
    }

    @Override
    public void onEnterRoom(AbstractRoom r) {
        if (r instanceof ShopRoom) {
            flash();
        }
    }

    @Override
    public void onRightClick() {
        if (AbstractDungeon.getCurrRoom() instanceof ShopRoom && counter > 0) {
            AbstractDungeon.topLevelEffectsQueue.add(new ShopSpeechBubble(shopBubbleX, shopBubbleX, DESCRIPTIONS[3], true));
            counter = -2;
            ShopScreen shop = AbstractDungeon.shopScreen;
            coloredCards = (ArrayList<AbstractCard>)ReflectionHacks.getPrivate(shop, ShopScreen.class, "coloredCards");
            colorlessCards = (ArrayList<AbstractCard>)ReflectionHacks.getPrivate(shop, ShopScreen.class, "colorlessCards");
            potions = (ArrayList<StorePotion>) ReflectionHacks.getPrivate(shop, ShopScreen.class, "potions");
            relics = (ArrayList<StoreRelic>)ReflectionHacks.getPrivate(shop, ShopScreen.class, "relics");
            for (AbstractCard c : coloredCards) {
                c.price = 0;
            }
            for (AbstractCard c: colorlessCards) {
                c.price = 0;
            }
            for (StorePotion p : potions) {
                p.price = 0;
            }
            for (StoreRelic r : relics) {
                r.price = 0;
            }
        }
    }

    @Override
    public void onSpendGold() {
        if (!coloredCards.isEmpty()) {
            for (AbstractCard c : coloredCards) {
                try {
                    Method setPrice = ShopScreen.class.getDeclaredMethod("setPrice", AbstractCard.class);
                    setPrice.setAccessible(true);
                    setPrice.invoke(AbstractDungeon.shopScreen, c);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!colorlessCards.isEmpty()) {
            for (AbstractCard c : colorlessCards) {
                try {
                    Method setPrice = ShopScreen.class.getDeclaredMethod("setPrice", AbstractCard.class);
                    setPrice.setAccessible(true);
                    setPrice.invoke(AbstractDungeon.shopScreen, c);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!potions.isEmpty()) {
            for (StorePotion p : potions) {
                p.price = p.potion.getPrice();
            }
        }
        if (!relics.isEmpty()) {
            for (StoreRelic r : relics) {
                r.price = r.relic.getPrice();
            }
        }
        colorlessCards.clear();
        coloredCards.clear();
        potions.clear();
        relics.clear();
        getUpdatedDescription();
    }

    @Override
    public AbstractRelic makeCopy() {
        return new SneckoAutograph();
    }

    @Override
    public String getUpdatedDescription()
    {
        if (counter > 0) {
            return DESCRIPTIONS[0] + DESCRIPTIONS[1];
        } else {
            return DESCRIPTIONS[2];
        }
    }
}
