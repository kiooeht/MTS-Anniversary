//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.AnimateFastAttackAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.SpawnDaggerAction;
import com.megacrit.cardcrawl.actions.utility.HideHealthBarAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.EnemyType;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import theAct.TheActMod;
import theAct.actions.TotemFallWaitAction;
import theAct.powers.ImmunityPower;
import theAct.powers.TotemFleePower;
import theAct.powers.TotemStrengthPower;
import theAct.vfx.TotemShadowParticle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TotemBoss extends AbstractMonster {
    public static final String ID = TheActMod.makeID("TotemBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private TotemShadowParticle totemShadow;
    private int healAmt;
    public boolean stopTotemFall;
    public int encounterSlotsUsed = 1;
    public ArrayList<Integer> remainingTotems = new ArrayList<>();
    public ArrayList<AbstractTotemSpawn> livingTotems = new ArrayList<>();



    public TotemBoss() {
        super(NAME, "Reptomancer", 10, 0.0F, -30.0F, 220.0F, 320.0F, (String)null, -20.0F, 10.0F);
        this.type = EnemyType.ELITE;
        this.loadAnimation(TheActMod.assetPath("images/monsters/totemboss/skeleton.atlas"), TheActMod.assetPath("images/monsters/totemboss/skeleton.json"), 1.0F);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.healAmt = 3;
        } else {
            this.healAmt = 2;
        }


        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Idle", "Sumon", 0.1F);
        this.stateData.setMix("Sumon", "Idle", 0.1F);
        this.stateData.setMix("Hurt", "Idle", 0.1F);
        this.stateData.setMix("Idle", "Hurt", 0.1F);
        this.stateData.setMix("Attack", "Idle", 0.1F);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.powers.add(new TotemStrengthPower(this));
        this.powers.add(new ImmunityPower(this));


    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSS_CITY");

        remainingTotems.add(1);
        remainingTotems.add(2);
        remainingTotems.add(3);
        remainingTotems.add(4);
        remainingTotems.add(5);
        remainingTotems.add(6);

        Collections.shuffle(remainingTotems);

        spawnNewTotem();
        spawnNewTotem();
        spawnNewTotem();

        this.totemShadow = new TotemShadowParticle(this);

        AbstractDungeon.effectList.add(this.totemShadow);

    }

    public void spawnNewTotem() {

        if (remainingTotems.size() <= 0){
            TheActMod.logger.info("This shouldn't happen!");
        } else {
            AbstractTotemSpawn m = null;
            Integer chosen = remainingTotems.get(AbstractDungeon.cardRng.random(0, remainingTotems.size() - 1));
            TheActMod.logger.info("Choosing Totem");
            switch (chosen) {
                case 1:
                    TheActMod.logger.info("Bash Totem Picked");
                    m = new BashTotem(this);
                    break;
                case 2:
                    TheActMod.logger.info("Double Strike Totem Picked");
                    m = new DoubleStrikeTotem(this);
                    break;
                case 3:
                    m = new AttackAndShieldTotem(this);
                    TheActMod.logger.info("Attack/Block Totem Picked");
                    break;
                case 4:
                    m = new ShieldOtherTotem(this);
                    TheActMod.logger.info("Shield Totem Picked");
                    break;
                case 5:
                    m = new BuffTotem(this);
                    TheActMod.logger.info("Buff Totem Picked");
                    break;
                case 6:
                    m = new DebuffTotem(this);
                    TheActMod.logger.info("Debuff Totem Picked");
                    break;
            }


            remainingTotems.remove(chosen);
            livingTotems.add(m);
            this.encounterSlotsUsed++;
            TheActMod.logger.info("Spawning Monster");

            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m,false,-99));

        }
    }

    public void resolveTotemDeath(AbstractTotemSpawn m){
        this.stopTotemFall = true;
        livingTotems.remove(m);
        for (AbstractMonster m2 : livingTotems) {

            if (!m2.isDying) {
                //Buff living totems before spawning new ones
                this.getPower(TotemStrengthPower.powerID).flashWithoutSound();
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(m2, this, new StrengthPower(m2, 1), 1));
            }
        }


        if (remainingTotems.size() == 0 && livingTotems.size() == 0){
            //Totems are all dead - remove immunity and the totem shadow
            this.totemShadow.isDone = true;
            AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this,this,ImmunityPower.powerID));

        } else if (remainingTotems.size() > 0) {
            //Spawn a new totem above if there are still any left in the array to kill
            spawnNewTotem();

        }
        //Wait for a bit (even on fast mode) so the previous totem's death animation finishes before they all fall, and gives time for the new one's visuals to be drawn in above the screen
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new TotemFallWaitAction(this));

    }


    public void takeTurn() {
        switch(this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));

                if (livingTotems.size() > 0) {
                    AbstractMonster m = livingTotems.get(AbstractDungeon.cardRng.random(livingTotems.size() - 1));

                    AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                    AbstractDungeon.actionManager.addToBottom(new HealAction((livingTotems.get(AbstractDungeon.cardRng.random(livingTotems.size() - 1))), this, this.healAmt));
                }
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void damage(DamageInfo info) {
        super.damage(info);
        if (info.owner != null && info.type != DamageType.THORNS && info.output > 0) {
            this.state.setAnimation(0, "Hurt", false);
            this.state.addAnimation(0, "Idle", true, 0.0F);
        }

    }

    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();

        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();

    }

    protected void getMove(int num) {
        this.setMove((byte) 0, Intent.BUFF);

    }



    public void changeState(String key) {
        byte var3 = -1;
        switch(key.hashCode()) {
            case -1837878047:
                if (key.equals("SUMMON")) {
                    var3 = 1;
                }
                break;
            case 1941037640:
                if (key.equals("ATTACK")) {
                    var3 = 0;
                }
        }

        switch(var3) {
            case 0:
                this.state.setAnimation(0, "Attack", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
                break;
            case 1:
                this.state.setAnimation(0, "Sumon", false);
                this.state.addAnimation(0, "Idle", true, 0.0F);
        }

    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }
}
