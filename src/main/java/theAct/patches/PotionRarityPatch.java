package theAct.patches;


import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;

/*
The purpose of this patch is to make sure you can't get any Jungle Event potions outside of the actual event.
Currently, the only method that does this is getRandomPotion() and getRandomPotion(RNG) but the latter is never used.
 */

@SpirePatch(
        clz=PotionHelper.class,
        method="getRandomPotion",
        paramtypez={
        }
)
public class PotionRarityPatch { //needs to be refactored with SpireInsert eventually but not for a long time.

    @SpirePostfixPatch
    public static AbstractPotion Postfix(AbstractPotion result) {
        if(result.rarity == PotionRarityEnum.JUNGLE){
            return AbstractDungeon.returnRandomPotion(true);
        }
            return result; //potion was not a jungle potion
    }


}
