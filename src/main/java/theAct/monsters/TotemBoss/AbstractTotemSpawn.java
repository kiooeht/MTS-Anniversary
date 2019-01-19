//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.GoldenSlashEffect;
import theAct.TheActMod;


public class AbstractTotemSpawn extends AbstractMonster {
    private TotemBoss owner;

    public Integer baseHP = 30;
    public Integer HPAscBuffed = 5;



    public Intent intentType = Intent.BUFF;


    public AbstractTotemSpawn(String name, String ID, TotemBoss boss) {
        super(name, ID, 420, 0.0F, 0F, 100.0F, 150.0F, TheActMod.assetPath("images/monsters/phtotem.png"), -90.0F, 0.0F);



        this.type = EnemyType.BOSS;
        this.dialogX = -100.0F * Settings.scale;
        this.dialogY = 10.0F * Settings.scale;
        this.owner = boss;
        this.drawY = 1000F * Settings.scale;
        this.drawX = Settings.WIDTH * 0.6F;

        if (AbstractDungeon.ascensionLevel >= 9) {
            this.setHp(baseHP + HPAscBuffed);
        } else {
            this.setHp(baseHP);
        }

    }



    public void takeTurn() {
        float vfxSpeed = 0.1F;
        if (Settings.FAST_MODE) {
            vfxSpeed = 0.0F;
        }

        switch(this.nextMove) {
            case 1:
               // AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.4F));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(new GoldenSlashEffect(AbstractDungeon.player.hb.cX - 60.0F * Settings.scale, AbstractDungeon.player.hb.cY, false), vfxSpeed));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, (DamageInfo)this.damage.get(0), AttackEffect.NONE));
                break;
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        boolean shouldFall = false;
        Float Y = 0F;
        for (AbstractTotemSpawn totem : owner.livingTotems){
            if (!totem.isDead && !totem.isDying && totem != null) {
                if (totem.drawY < this.drawY) {
                    //this totem is above another totem
                    if (totem.drawY > Y) {
                        //If the new totem being checked is above the previous, set this to the new highest totem BELOW this totem
                        Y = totem.drawY;
                    }

                }
            }
        }

        if (Y == 0F){
           // TheActMod.logger.info(this.id + " drawY " + this.drawY + " vs floorY " + AbstractDungeon.floorY);

            // This is the lowest totem.  If it is not at the floor, it needs to fall.
            if (this.drawY > AbstractDungeon.floorY){
                shouldFall = true;
            }
        } else {
            // check the totem below this one.  If any totem is greater than a difference away, this totem needs to drop.


                   // TheActMod.logger.info(this.id + " drawY " + this.drawY + " vs Y " + Y);

                    if (this.drawY > Y) {
                      //  TheActMod.logger.info(this.id + " difference: " + (this.drawY - Y));

                        if (this.drawY - Y > 240F) {
                            shouldFall = true;
                        }
                    }


        }
        TheActMod.logger.info(this.owner.stopTotemFall);
        if (shouldFall && !this.owner.stopTotemFall){
           // TheActMod.logger.info(this.id + "is falling");
            this.drawY = this.drawY - 30 * Settings.scale;
        }
    }


    /*
    public void changeState(String key) {
        byte var3 = -1;
        switch(key.hashCode()) {
            case 1941037640:
                if (key.equals("ATTACK")) {
                    var3 = 0;
                }
            default:
                switch(var3) {
                    case 0:
                        this.state.setAnimation(0, "Attack", false);
                        this.state.addAnimation(0, "Idle", true, 0.0F);
                    default:
                }
        }
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hit", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }

    }
    */




    protected void getMove(int num)
    {
        this.setMove((byte)1, intentType);
    }

    public void die() {
        this.useFastShakeAnimation(1.0F);
        CardCrawlGame.screenShake.rumble(1.0F);
        super.die();
        this.owner.resolveTotemDeath(this);


    }


}
