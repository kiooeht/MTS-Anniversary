//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package theAct.monsters.TotemBoss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState.TrackEntry;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.*;
import com.megacrit.cardcrawl.actions.unique.IncreaseMaxHpAction;
import com.megacrit.cardcrawl.actions.utility.SFXAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.vfx.combat.HemokinesisEffect;
import com.megacrit.cardcrawl.vfx.combat.IntimidateEffect;
import theAct.TheActMod;
import theAct.actions.IncreaseMaxHPFlatAction;
import theAct.actions.SnakeMissileAction;
import theAct.actions.TotemBossMakeUntargetable;
import theAct.actions.TotemFallWaitAction;
import theAct.powers.TotemBossImmunityPower;
import theAct.powers.TotemHealthLinkPower;
import theAct.powers.TotemStrengthPower;
import theAct.vfx.TotemBossHPEffect;
import theAct.vfx.TotemShadowParticle;

import java.util.ArrayList;
import java.util.Collections;

public class TotemBoss extends AbstractMonster {
    public static final String ID = TheActMod.makeID("TotemBoss");
    private static final MonsterStrings monsterStrings;
    public static final String NAME;
    public static final String[] MOVES;
    public static final String[] DIALOG;

    private TotemShadowParticle totemShadow;
    private int healAmt;
    private int debuffAmt;
    private int strikeAmt;;
    private int superAmt;

    private int multiStrikeCount = 6;
    public boolean stopTotemFall;
    private float totemSfxTimer = 0.f;
    public int encounterSlotsUsed = 1;
    public int totemsSpawned = 0;
    public ArrayList<Integer> remainingTotems = new ArrayList<>();
    public ArrayList<Color> remainingColors = new ArrayList<>();
    public ArrayList<AbstractTotemSpawn> livingTotems = new ArrayList<>();

    private boolean firstTurn = true;

    private boolean firstTotemDrop = true;

    public TotemBoss() {
        super(NAME, ID, 50, 0.0F, -30.0F, 220.0F, 320.0F, (String)null, -20.0F, 10.0F);
        this.type = EnemyType.BOSS;
        this.loadAnimation(TheActMod.assetPath("images/monsters/totemboss/skeleton.atlas"), TheActMod.assetPath("images/monsters/totemboss/skeleton.json"), 1.0F);

        if (AbstractDungeon.ascensionLevel >= 19) {
            this.strikeAmt = 9;
            this.superAmt = 2;
            this.healAmt = 12;
            this.debuffAmt = 3;
        } else if (AbstractDungeon.ascensionLevel >= 4) {
            this.strikeAmt = 9;
            this.superAmt = 2;
            this.healAmt = 10;
            this.debuffAmt = 2;
        } else {
            this.strikeAmt = 8;
            this.superAmt = 1;
            this.healAmt = 10;
            this.debuffAmt = 2;
        }


            this.damage.add(new DamageInfo(this, this.strikeAmt));
            this.damage.add(new DamageInfo(this, this.superAmt));



        TrackEntry e = this.state.setAnimation(0, "Idle", true);
        this.stateData.setMix("Idle", "Sumon", 0.1F);
        this.stateData.setMix("Sumon", "Idle", 0.1F);
        this.stateData.setMix("Hurt", "Idle", 0.1F);
        this.stateData.setMix("Idle", "Hurt", 0.1F);
        this.stateData.setMix("Attack", "Idle", 0.1F);
        e.setTime(e.getEndTime() * MathUtils.random());

        this.powers.add(new TotemStrengthPower(this));
        //this.powers.add(new TotemBossImmunityPower(this));

        this.firstTotemDrop = true;

    }

    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("BOSSTOTEM");

        remainingTotems.add(1);
        remainingTotems.add(2);
        remainingTotems.add(3);
        remainingTotems.add(4);
        remainingTotems.add(5);
        remainingTotems.add(6);



        if (AbstractDungeon.ascensionLevel >= 9){
            remainingTotems.add(7);

        }

        Collections.shuffle(remainingTotems,AbstractDungeon.cardRng.random);



        spawnNewTotem();
        spawnNewTotem();
        spawnNewTotem();

        this.totemShadow = new TotemShadowParticle(this);

        AbstractDungeon.effectList.add(this.totemShadow);

        initialMaxHPBoost();
        //this.halfDead = true;

    }

    public void spawnNewTotem() {

        if (remainingTotems.size() <= 0){
            TheActMod.logger.info("This shouldn't happen!");
        } else {
            AbstractTotemSpawn m = null;
            Integer chosen = remainingTotems.get(AbstractDungeon.cardRng.random(0, remainingTotems.size() - 1));
            TheActMod.logger.info("Choosing Totem");
            Boolean post3 = false;
            if (this.totemsSpawned >= 3) post3 = true;
            switch (chosen) {
                case 1:
                    TheActMod.logger.info("Bash Totem Picked");
                    m = new BashTotem(this, post3);
                    break;
                case 2:
                    TheActMod.logger.info("Double Strike Totem Picked");
                    m = new DoubleStrikeTotem(this, post3);
                    break;
                case 3:
                    m = new AttackAndShieldTotem(this, post3);
                    TheActMod.logger.info("Attack/Block Totem Picked");
                    break;
                case 4:
                    m = new ShieldOtherTotem(this, post3);
                    TheActMod.logger.info("Shield Totem Picked");
                    break;
                case 5:
                    m = new BuffTotem(this, post3);
                    TheActMod.logger.info("Buff Totem Picked");
                    break;
                case 6:
                    m = new DebuffTotem(this, post3);
                    TheActMod.logger.info("Debuff Totem Picked");
                    break;
                case 7:
                    m = new ConfuseTotem(this, post3);
                    TheActMod.logger.info("Confuse Totem Picked - This should only happen with Bosses are Tougher ascension.");
                    break;
            }


            remainingTotems.remove(chosen);
            remainingColors.remove(chosen);
            livingTotems.add(m);
            this.encounterSlotsUsed++;
            TheActMod.logger.info("Spawning Monster");

            AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(m,false,-99));
            this.totemsSpawned++;

        }
    }

    public void resolveTotemDeath(AbstractTotemSpawn m){
        this.stopTotemFall = true;
        livingTotems.remove(m);
        AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(this, this, new StrengthPower(this, 1), 1));

        if (this.getPower(TotemBossImmunityPower.powerID).amount == 1){
            ((TotemBossImmunityPower) this.getPower(TotemBossImmunityPower.powerID)).unremovable = false;
        }
        AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(this, this, this.getPower(TotemBossImmunityPower.powerID), 1));

        for (AbstractMonster m2 : livingTotems) {

            if (!m2.isDying) {
                //Buff living totems before spawning new ones
                this.getPower(TotemStrengthPower.powerID).flashWithoutSound();
                AbstractDungeon.actionManager.addToTop(new ApplyPowerAction(m2, this, new StrengthPower(m2, 1), 1));
            }
        }


        if (remainingTotems.size() == 0 && livingTotems.size() == 0){
            //Totems are all dead - remove immunity and the totem shadow
            this.totemShadow.isDone = true;
            this.halfDead = false;
            AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(this,this, TotemBossImmunityPower.powerID));

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

    public void update() {
        super.update();
        if (totemSfxTimer > 0) {
            totemSfxTimer -= Gdx.graphics.getDeltaTime();
        }
    }

    public void totemStoppedFalling() {
        if (totemSfxTimer <= 0.f) { // Don't overlap effects
            totemSfxTimer = 1.0f;
            CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.MED, true);
            CardCrawlGame.sound.playAV(TheActMod.makeID("totemSmash"), 0.1f, 2.0f);
        }
    }

    public void initialMaxHPBoost(){
        float yOffset = -100 * Settings.scale;
        float xOffset = -150 * Settings.scale;
        TheActMod.logger.info("Totem boss max HP buff hit");

        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        AbstractDungeon.actionManager.addToBottom(new WaitAction(0.1F));
        for (int i = 0; i < livingTotems.size(); i++) {
            AbstractDungeon.actionManager.addToBottom(new VFXAction(new TotemBossHPEffect(this.hb.cX + xOffset, this.hb.cY + yOffset, this.hb.cX, this.hb.cY, this.livingTotems.get(i).totemLivingColor), 0.25F));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this, new TotemBossImmunityPower(this),1));
            yOffset += 200 * Settings.scale;
        }
        for (int i = 0; i < remainingTotems.size(); i++) {
            Color particleColor = Color.BLACK;
            switch (remainingTotems.get(i)) {
                case 1:
                    particleColor = BashTotem.totemColor;
                    break;
                case 2:
                    particleColor = DoubleStrikeTotem.totemColor;
                    break;
                case 3:
                    particleColor = AttackAndShieldTotem.totemColor;
                    break;
                case 4:
                    particleColor = ShieldOtherTotem.totemColor;
                    break;
                case 5:
                    particleColor = BuffTotem.totemColor;
                    break;
                case 6:
                    particleColor = DebuffTotem.totemColor;
                    break;
                case 7:
                    particleColor = ConfuseTotem.totemColor;
                    break;
            }

            AbstractDungeon.actionManager.addToBottom(new VFXAction(new TotemBossHPEffect(this.hb.cX + xOffset, this.hb.cY + yOffset, this.hb.cX, this.hb.cY, particleColor), 0.25F));
            AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(this,this, new TotemBossImmunityPower(this),1));
            yOffset += 200 * Settings.scale;
        }

    }

    public void takeTurn() {
        switch(this.nextMove) {
            case 0:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SUMMON"));

                if (livingTotems.size() > 0) {
                    if (livingTotems.size() > 0) {
                        Integer lowestHealth = 0;
                        AbstractMonster bestTarget = null;
                        for (AbstractMonster m : livingTotems) {
                            if (m.currentHealth > lowestHealth){
                                lowestHealth = m.currentHealth;
                                bestTarget = m;
                            }
                        }

                        if (bestTarget != null) {
                            AbstractDungeon.actionManager.addToBottom(new WaitAction(0.3F));
                            AbstractDungeon.actionManager.addToBottom(new HealAction(bestTarget, this, this.healAmt));
                        }
                    }
                }
                break;
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                AbstractDungeon.actionManager.addToBottom(new DamageAction(AbstractDungeon.player, this.damage.get(0), AttackEffect.SLASH_HORIZONTAL));
                for (AbstractTotemSpawn t : livingTotems){
                    AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(t, this, new StrengthPower(t, 2), 2));
                }

                break;

            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "SUMMON"));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new FrailPower(AbstractDungeon.player, this.debuffAmt, true), this.debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(AbstractDungeon.player, this, new WeakPower(AbstractDungeon.player, this.debuffAmt, true), this.debuffAmt));
                AbstractDungeon.actionManager.addToBottom(new SFXAction("MONSTER_SNECKO_GLARE"));
                AbstractDungeon.actionManager.addToBottom(new VFXAction(this, new IntimidateEffect(this.hb.cX, this.hb.cY), 0.5F));

                break;

            case 3:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
                float projectileDelay = Interpolation.linear.apply(1.0F/3.0F, 0.05F, Math.min(((float)this.multiStrikeCount)/100.0F, 1.0F));
                AbstractDungeon.actionManager.addToBottom(new SnakeMissileAction(AbstractDungeon.player,this, this.damage.get(1), this.multiStrikeCount, projectileDelay, Color.GREEN.cpy()));

                break;

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
        this.remainingTotems.clear();
        for (AbstractTotemSpawn t:livingTotems) {
            AbstractDungeon.actionManager.addToBottom(new DamageAction(t, new DamageInfo(t,999), AttackEffect.FIRE));
        }
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();


        AbstractDungeon.scene.fadeInAmbiance();
        this.onBossVictoryLogic();

    }


    @Override
    public void getMove(int roll)
    {
        if(firstTurn)
        {
            firstTurn = false;
            this.setMove(MoveBytes.HEALTOTEM, Intent.BUFF);
            return;
        }
        else
        {
            if(lastMove(MoveBytes.SUPERATTACK)) {
                this.setMove(MoveBytes.HEALTOTEM, Intent.BUFF);
            }
            else if(lastMove(MoveBytes.HEALTOTEM)){
                this.setMove(MoveBytes.DAMAGEANDBUFF, Intent.ATTACK_BUFF, this.damage.get(0).base);
            }
            else if(lastMove(MoveBytes.DAMAGEANDBUFF)){
                this.setMove(MoveBytes.DEBUFF, Intent.STRONG_DEBUFF);
            }
            else if(lastMove(MoveBytes.DEBUFF)){
                this.setMove(MoveBytes.SUPERATTACK, Intent.ATTACK, this.damage.get(1).base, this.multiStrikeCount, true);
            }

            //SAFEGUARD in case something causes the immunity to not wear off, try again every turn
            if (remainingTotems.size() == 0 && livingTotems.size() == 0) {
                //Totems are all dead - remove immunity and the totem shadow
                AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(this,this, TotemBossImmunityPower.powerID));
            }

        }
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

    public static class MoveBytes {
        public static final byte HEALTOTEM = 0;
        public static final byte DAMAGEANDBUFF = 1;
        public static final byte DEBUFF = 2;
        public static final byte SUPERATTACK = 3;
    }

    static {
        monsterStrings = CardCrawlGame.languagePack.getMonsterStrings(ID);
        NAME = monsterStrings.NAME;
        MOVES = monsterStrings.MOVES;
        DIALOG = monsterStrings.DIALOG;

    }
}
