package theAct.vfx;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import theAct.TheActMod;
import theAct.monsters.SpyderBoss.Spyder;


public class SpyderWebParticle extends com.megacrit.cardcrawl.vfx.AbstractGameEffect {
    private float scale = 1F;
    private Texture img;
    public Spyder p;
    private float opacity = 0.6f;


    public SpyderWebParticle(Spyder p) {
        this.duration = 0.05F;
        this.img = ImageMaster.loadImage( TheActMod.assetPath("images/monsters/spyders/webline.png"));
        this.p = p;
        this.renderBehind = true;
    }

    public void update() {
    	if((p == null || p.isDeadOrEscaped()) && opacity > 0)
    		opacity -= 0.003 * Gdx.graphics.getRawDeltaTime();
    	if(opacity < 0)
    		this.finish();
    }
    
    public void finish(){
        this.isDone = true;
    }
    public void dispose() {


    }

    public void render(SpriteBatch sb, float x, float y) {
    }


    public void render(SpriteBatch sb) {


    	sb.setColor(new Color(1F, 1F, 1F, opacity));

        sb.draw(this.img, this.p.hb.cX - this.img.getWidth() * Settings.scale * 0.45f, this.p.hb.cY + this.img.getHeight() * Settings.scale * 0.05f,
        		this.img.getWidth() * Settings.scale *scale, this.img.getHeight()* Settings.scale *scale,
        		0, 0,
        		this.img.getWidth(), this.img.getHeight(),
        		false, false);


    }
}


