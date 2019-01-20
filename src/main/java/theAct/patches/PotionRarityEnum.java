package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.potions.AbstractPotion;

//The purpose of this addition is to make sure you can't get Jungle-specific potions outside of Shaman Brewing Event
public class PotionRarityEnum {
    @SpireEnum
    public static AbstractPotion.PotionRarity JUNGLE;
}