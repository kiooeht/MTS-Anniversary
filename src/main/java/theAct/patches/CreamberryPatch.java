package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import theAct.relics.Creamberry;

import java.util.ArrayList;

public class CreamberryPatch {

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="getRewardCards"
    )
    public static class RewardCardsPatch {

        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result) {
            if (AbstractDungeon.player.hasRelic(Creamberry.ID)) {
                for (AbstractCard c : __result) {
                    if ((c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE) && c.canUpgrade()) {
                        c.upgrade();
                    }
                }
            }
            return __result;
        }
    }

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="getColorlessRewardCards"
    )
    public static class ColorlessRewardCardsPatch {

        public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> __result) {
            if (AbstractDungeon.player.hasRelic(Creamberry.ID)) {
                for (AbstractCard c : __result) {
                    if ((c.rarity == AbstractCard.CardRarity.UNCOMMON || c.rarity == AbstractCard.CardRarity.RARE) && c.canUpgrade()) {
                        c.upgrade();
                    }
                }
            }
            return __result;
        }
    }
}
