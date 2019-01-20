package theAct.vfx;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import theAct.TheActMod;
import theAct.monsters.TotemBoss.TotemBoss;


public class TotemShadowParticle extends com.megacrit.cardcrawl.vfx.AbstractGameEffect {
    private float scale = 1F;
    private int W;
    private Texture img;
    public TotemBoss p;
    private static int xOffset = -500;
    private static int yOffset = -128;


    public TotemShadowParticle(TotemBoss p) {
        this.duration = 0.05F;
        this.img = ImageMaster.loadImage( TheActMod.assetPath("images/monsters/totemboss/totemshadow.png"));
        W = img.getWidth();
        this.p = p;
        this.renderBehind = true;


    }

    public void update() {


    }
    public void finish(){
        this.isDone = true;

    }
    public void dispose() {


    }

    public void render(SpriteBatch sb, float x, float y) {
    }


    public void render(SpriteBatch sb) {


        sb.setColor(new Color(1F, 1F, 1F, .5F));

        sb.draw(this.img, this.p.drawX + W / 2.0F + ((xOffset) * Settings.scale), this.p.drawY - W / 2.0F + ((yOffset) * Settings.scale), W / 2.0F, W / 2.0F, W, W, this.scale * Settings.scale, this.scale * Settings.scale, 0.0F, 0, 0, W, W, false, false);


    }
}


