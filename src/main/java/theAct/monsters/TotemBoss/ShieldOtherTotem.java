//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import theAct.TheActMod;
import theAct.powers.BlockFromStrengthPower;

public class ShieldOtherTotem extends AbstractTotemSpawn {
    public static final String ID = TheActMod.makeID("ShieldOtherTotem");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    public Integer secondaryEffect;
    public static Color totemColor = Color.CYAN;

    public ShieldOtherTotem(TotemBoss boss, boolean spawnedIn) {
        super(NAME, ID, boss, TheActMod.assetPath("images/monsters/totemboss/totemcyan.png"), spawnedIn);
        this.loadAnimation(TheActMod.assetPath("images/monsters/totemboss/cyan/Totem.atlas"), TheActMod.assetPath("images/monsters/totemboss/cyan/Totem.json"), 1.0F);

        AnimationState.TrackEntry e = this.state.setAnimation(0, "idle", true);
        e.setTime(e.getEndTime() * MathUtils.random());

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.secondaryEffect = 12;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.secondaryEffect = 10;
        } else {
            this.secondaryEffect = 8;
        }

        this.intentType = Intent.DEFEND;
        this.powers.add(new BlockFromStrengthPower(this));

        this.totemLivingColor = totemColor;


    }


    @Override
    public void totemAttack() {
        // AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.25F));
        AbstractDungeon.actionManager.addToBottom(new VFXAction(new BorderFlashEffect(Color.CYAN)));

        Integer blockBonus = 0;
        if (this.hasPower(StrengthPower.POWER_ID)){
            blockBonus = this.getPower(StrengthPower.POWER_ID).amount;
        }
        for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {

            if (!m.isDying && !(m instanceof TotemBoss) && m!=this) {
                AbstractDungeon.actionManager.addToBottom(new GainBlockAction(m, this, this.secondaryEffect + blockBonus));
            }
        }
    }

    public void getUniqueTotemMove() {this.setMove((byte)1, intentType);
    }
    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }




}
