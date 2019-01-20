package theAct.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public class SnakeMissileTrailEffect extends AbstractGameEffect {
    private static final float EFFECT_DUR = 0.4f;
    private float x;
    private float y;
    private static TextureAtlas.AtlasRegion img;
    private Color effectColor;

    public SnakeMissileTrailEffect(float x, float y, Color effectColor) {
        if (SnakeMissileTrailEffect.img == null) {
            SnakeMissileTrailEffect.img = ImageMaster.vfxAtlas.findRegion("combat/blurDot");
        }
        this.renderBehind = false;
        this.duration = EFFECT_DUR;
        this.startingDuration = EFFECT_DUR;
        this.x = x - SnakeMissileTrailEffect.img.packedWidth / 2.0f;
        this.y = y - SnakeMissileTrailEffect.img.packedHeight / 2.0f;
        this.rotation = 0.0f;
        this.scale = 0.01f;
        this.effectColor = Color.GREEN;
    }

    @Override
    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration < 0.2f) {
            this.scale = this.duration * Settings.scale * 11.0f;
        }
        else {
            this.scale = (this.duration - 0.2f) * Settings.scale * 11.0f;
        }
        if (this.duration < 0.0f) {
            this.isDone = true;
        }
    }

    @Override
    public void render(final SpriteBatch sb) {
        Color tmp = effectColor.cpy();
        tmp.set(tmp.r, tmp.g, tmp.b, 1.0F - (0.8f - (this.duration * 2)));
        sb.setColor(tmp);
        sb.draw(SnakeMissileTrailEffect.img, this.x, this.y, SnakeMissileTrailEffect.img.packedWidth / 2.0f, SnakeMissileTrailEffect.img.packedHeight / 2.0f, SnakeMissileTrailEffect.img.packedWidth, SnakeMissileTrailEffect.img.packedHeight, this.scale, this.scale, this.rotation);
        sb.setBlendFunction(770, 1);
        sb.setColor(new Color(0.0F, 0.0F, 1.0F, 1.0F - (0.8f - (this.duration * 2))));
        sb.draw(SnakeMissileTrailEffect.img, this.x, this.y, SnakeMissileTrailEffect.img.packedWidth / 2.0f, SnakeMissileTrailEffect.img.packedHeight / 2.0f, SnakeMissileTrailEffect.img.packedWidth, SnakeMissileTrailEffect.img.packedHeight, this.scale * 1.7f, this.scale * 1.7f, this.rotation);
        sb.setBlendFunction(770, 771);
    }

    static {
        SnakeMissileTrailEffect.img = null;
    }

    @Override
    public void dispose() {
    }
}