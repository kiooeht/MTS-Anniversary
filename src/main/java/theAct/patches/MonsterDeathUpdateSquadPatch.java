package theAct.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import theAct.actions.FormationInitAction;
import theAct.monsters.SpyderBoss.SpawnedSpyder;
import theAct.powers.FormationPower;
import theAct.powers.ShyPower;
import theAct.powers.SquadPower;

@SpirePatch(cls="com.megacrit.cardcrawl.monsters.AbstractMonster", method="die", paramtypez = {boolean.class})
public class MonsterDeathUpdateSquadPatch {

	@SpirePostfixPatch
	public static void Postfix(AbstractMonster __instance, boolean triggerRelics) {
		for(AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
			if(m instanceof SpawnedSpyder && m.hasPower(SquadPower.powerID))
				((SquadPower) m.getPower(SquadPower.powerID)).spyderDeath();
			if(m instanceof SpawnedSpyder && m.hasPower(ShyPower.powerID))
				((ShyPower) m.getPower(ShyPower.powerID)).spyderDeath();
			if(m instanceof SpawnedSpyder && m.hasPower(FormationPower.powerID))
				AbstractDungeon.actionManager.addToBottom(new FormationInitAction(((FormationPower) m.getPower(FormationPower.powerID))));
		}		
	}
	
}