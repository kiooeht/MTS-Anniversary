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
import com.megacrit.cardcrawl.powers.MinionPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.InflameEffect;
import theAct.TheActMod;
import theAct.actions.TotemFallWaitAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class TotemBoss extends AbstractMonster {
    public static final String ID = TheActMod.makeID("TotemBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;


    private int healAmt;
    private boolean totems;
    public boolean stopTotemFall;
    public int encounterSlotsUsed = 1;
    public ArrayList<Integer> remainingTotems = new ArrayList<>();
    public ArrayList<AbstractTotemSpawn> livingTotems = new ArrayList<>();

    private boolean firstMove = true;

    public TotemBoss() {
        super(NAME, "Reptomancer", AbstractDungeon.monsterHpRng.random(180, 190), 0.0F, -30.0F, 220.0F, 320.0F, (String)null, -20.0F, 10.0F);
        this.type = EnemyType.ELITE;
        this.loadAnimation("images/monsters/theForest/mage/skeleton.atlas", "images/monsters/theForest/mage/skeleton.json", 1.0F);
        if (AbstractDungeon.ascensionLevel >= 8) {
            this.setHp(190, 200);
        } else {
            this.setHp(180, 190);
        }

        if (AbstractDungeon.ascensionLevel >= 3) {
            this.damage.add(new DamageInfo(this, 16));
            this.damage.add(new DamageInfo(this, 34));
        } else {
            this.damage.add(new DamageInfo(this, 13));
            this.damage.add(new DamageInfo(this, 30));
        }

        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Idle", "Sumon", 0.1F);
        this.stateData.setMix("Sumon", "Idle", 0.1F);
        this.stateData.setMix("Hurt", "Idle", 0.1F);
        this.stateData.setMix("Idle", "Hurt", 0.1F);
        this.stateData.setMix("Attack", "Idle", 0.1F);
        e.setTime(e.getEndTime() * MathUtils.random());
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


        if (remainingTotems.size() == 0 && livingTotems.size() == 0){
            die();
        } else if (remainingTotems.size() > 0) {

            spawnNewTotem();


        }
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new TotemFallWaitAction(this));
    }


    public void takeTurn() {
        switch(this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                AbstractDungeon.actionManager.addToBottom(new HealAction((livingTotems.get(AbstractDungeon.cardRng.random(livingTotems.size() - 1))), this, this.healAmt));
        }

        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private boolean canSpawn() {
        int aliveCount = 0;
        Iterator var2 = AbstractDungeon.getMonsters().monsters.iterator();

        while(var2.hasNext()) {
            AbstractMonster m = (AbstractMonster)var2.next();
            if (m != this && !m.isDying) {
                ++aliveCount;
            }
        }

        if (aliveCount > 3) {
            return false;
        } else {
            return true;
        }
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
        /*
        if (MathUtils.randomBoolean()) {
            CardCrawlGame.sound.play("VO_CHAMP_3A");
        } else {
            CardCrawlGame.sound.play("VO_CHAMP_3B");
        }
        */

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
