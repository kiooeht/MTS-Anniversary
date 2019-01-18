package conspire.patches;

import java.util.ArrayList;
import java.util.HashMap;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;

public class GetDungeonPatch {
    public interface AbstractDungeonBuilder {
        public AbstractDungeon build(AbstractPlayer p, ArrayList<String> theList);
        public AbstractDungeon build(AbstractPlayer p, SaveFile save);
    }
    // Note: this should at some point be moved to BaseMod
    public static HashMap<String,AbstractDungeonBuilder> customDungeons = new HashMap<>();

    public static void addDungeon(String name, AbstractDungeonBuilder builder) {
        customDungeons.put(name,builder);
    }

    @SpirePatch(clz=CardCrawlGame.class, method="getDungeon", paramtypez={String.class, AbstractPlayer.class})
    public static class getDungeon1 {
        public static AbstractDungeon Postfix(CardCrawlGame self, String key, AbstractPlayer p, AbstractDungeon dungeon) {
            if (dungeon == null) {
                AbstractDungeonBuilder builder = customDungeons.get(key);
                if (builder != null) {
                    dungeon = builder.build(p, AbstractDungeon.specialOneTimeEventList);
                }
            }
            return dungeon;
        }
    }

    @SpirePatch(clz=CardCrawlGame.class, method="getDungeon", paramtypez={String.class, AbstractPlayer.class, SaveFile.class})
    public static class getDungeon2 {
        public static AbstractDungeon Postfix(CardCrawlGame self, String key, AbstractPlayer p, SaveFile save, AbstractDungeon dungeon) {
            if (dungeon == null) {
                AbstractDungeonBuilder builder = customDungeons.get(key);
                if (builder != null) {
                    dungeon = builder.build(p, save);
                }
            }
            return dungeon;
        }
    }
}
