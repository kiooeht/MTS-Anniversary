package theAct.actions;

import basemod.ReflectionHacks;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theAct.vfx.SnakeMissileEffect;


public class SnakeMissileAction extends AbstractGameAction {
    private DamageInfo info;
    private int damageCount = 0;
    public boolean doDamage = false;
    private int projectileCount;
    private int projectilesFired;
    private float projectileTimer = 0.0f;
    private float projectileDelay;
    private Color effectColor;
    private AbstractCreature source;

    public SnakeMissileAction(AbstractCreature target, AbstractCreature source, DamageInfo info, int projectileCount, float projectileDelay, Color effectColor) {
        this.setValues(target, this.info = info);
        this.actionType = ActionType.DAMAGE;
        this.projectileCount = projectileCount;
        this.projectileDelay = projectileDelay;
        this.effectColor = effectColor;
        this.source = source;
    }

    @Override
    public void update() {
        projectileTimer -= Gdx.graphics.getDeltaTime();
        if (this.shouldCancelAction() && this.info.type != DamageInfo.DamageType.THORNS) {
            this.isDone = true;
            return;
        }
        if (projectilesFired < projectileCount && projectileTimer <= 0.0f) {
            AbstractDungeon.effectList.add(
                    new SnakeMissileEffect(source.drawX * Settings.scale, source.drawY * Settings.scale, target.hb.cX, target.hb.cY, this, effectColor.cpy())
            );
            projectilesFired++;
            projectileTimer = projectileDelay;
        }
        if (doDamage && damageCount < projectileCount) {
            damageCount++;
            doDamage = false;
            FlashAtkImgEffect coloredPoison = new FlashAtkImgEffect(this.target.hb.cX, this.target.hb.cY, AttackEffect.POISON);
            ReflectionHacks.setPrivateInherited(coloredPoison, FlashAtkImgEffect.class, "color", effectColor.cpy());
            AbstractDungeon.effectList.add(coloredPoison);
            this.target.tint.color = effectColor.cpy();
            this.target.tint.changeColor(Color.WHITE.cpy());
            this.target.damage(this.info);
            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }
        }
        if (damageCount == projectileCount || target.isDying) {
            this.isDone = true;
        }
    }
}