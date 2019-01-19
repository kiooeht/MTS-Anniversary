package theAct.events.buttons;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;

import java.lang.reflect.Field;

public class ImageDialogOptionButton extends LargeDialogOptionButton
{
    private Texture img;

    public ImageDialogOptionButton(int slot, String msg, String imgPath)
    {
        super(slot, msg);

        img = ImageMaster.loadImage(imgPath);
    }

    @Override
    public void renderCardPreview(SpriteBatch sb)
    {
        if (img != null && hb.hovered) {
            try {
                Field xField = LargeDialogOptionButton.class.getDeclaredField("x");
                xField.setAccessible(true);
                Field yField = LargeDialogOptionButton.class.getDeclaredField("y");
                yField.setAccessible(true);

                float x = xField.getFloat(this) - img.getWidth() / 2f;
                float y = yField.getFloat(this);

                int blendSrc = sb.getBlendSrcFunc();
                int blendDst = sb.getBlendDstFunc();
                sb.setBlendFunction(GL20.GL_ONE, GL20.GL_ZERO);
                sb.draw(
                        img,
                        x,
                        y,
                        img.getWidth() * Settings.scale,
                        img.getHeight() * Settings.scale
                );
                sb.setBlendFunction(blendSrc, blendDst);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
    }
}
