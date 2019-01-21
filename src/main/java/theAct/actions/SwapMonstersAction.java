package theAct.actions;

import java.util.Collections;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class SwapMonstersAction extends AbstractGameAction {

    public SwapMonstersAction() {       
    }

    @Override
    public void update() {
    	if(AbstractDungeon.getMonsters().monsters.size() > 1)
    		Collections.swap(AbstractDungeon.getMonsters().monsters, AbstractDungeon.getMonsters().monsters.size()-1, AbstractDungeon.getMonsters().monsters.size()-2);
    	isDone = true;
    }
}
