package theAct.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.ExplosivePower;
import theAct.TheActMod;
import theAct.monsters.SneckoEgg;
import theAct.powers.abstracts.Power;

public class IncubationPower extends Power {
    public static final String powerID = TheActMod.makeID("Incubation");
    private static final PowerStrings strings = CardCrawlGame.languagePack.getPowerStrings(powerID);
    public static final String[] DESCRIPTIONS = strings.DESCRIPTIONS;

    public IncubationPower(AbstractCreature owner, int amount) {
        this.owner = owner;
        this.amount = amount;
        this.type = PowerType.BUFF;
        this.isTurnBased = true;
        this.name = strings.NAME;
        this.region48 = new TextureAtlas.AtlasRegion(new Texture(TheActMod.assetPath("images/powers/incubationPower32.png")), 0, 0, 32, 32);
        this.region128 = new TextureAtlas.AtlasRegion(new Texture(TheActMod.assetPath("images/powers/incubationPower84.png")), 0, 0, 84, 84);
        this.ID = powerID;
        this.updateDescription();
    }
    
    @Override
    public void updateDescription() {
        if (this.amount == 1) {
            this.description = IncubationPower.DESCRIPTIONS[0];
        }
        else {
            this.description = IncubationPower.DESCRIPTIONS[1] + this.amount + IncubationPower.DESCRIPTIONS[2];
        }
    }
    
    @Override
    public void duringTurn() {
        if (this.amount == 1 && !this.owner.isDying) {
            if(this.owner instanceof SneckoEgg){
                ((SneckoEgg)this.owner).hatch();
            }
        }
        else {
            AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this.owner, this.owner, IncubationPower.powerID, 1));
            this.updateDescription();
        }
    }

}