package theAct.events;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.TheActMod;
import theAct.relics.WildMango;
import theAct.relics.WildPear;
import theAct.relics.WildStrawberry;

public class ExcessResources extends AbstractImageEvent {
    public static final String ID = TheActMod.makeID("ExcessResources");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private State state;
    private int chosen_option = 0;
    private static final int HEAL_AMOUNT = 2;

    public enum State{
        CHOOSING,
        CHOOSING_AGAIN,
        LEAVING
    }

    public ExcessResources(){
        super(NAME, DESCRIPTIONS[0], "theActAssets/images/events/ER/start.jpg");

        imageEventText.setDialogOption(OPTIONS[0]);
        imageEventText.setDialogOption(OPTIONS[1]);

        state = State.CHOOSING;
    }

    @Override
    protected void buttonEffect(int pressedButton) {
        switch(state) {
            case CHOOSING:
                switch (pressedButton) {
                    //Feast
                    case 0:
                        chosen_option = 1;
                        break;
                    //Refuse
                    case 1:
                        chosen_option = 2;
                        if (AbstractDungeon.ascensionLevel >= 15) {
                            AbstractDungeon.player.heal((HEAL_AMOUNT-1), true);
                        } else {
                            AbstractDungeon.player.heal(HEAL_AMOUNT, true);
                        }
                        state = State.LEAVING;
                        break;
                }
                imageEventText.updateBodyText(DESCRIPTIONS[chosen_option]);
                imageEventText.clearAllDialogs();
                if(chosen_option == 1) {
                    imageEventText.setDialogOption(OPTIONS[3]);
                    imageEventText.setDialogOption(OPTIONS[4]);
                    imageEventText.setDialogOption(OPTIONS[5]);
                } else {
                    imageEventText.setDialogOption(OPTIONS[2]);
                    break;
                }
                state = State.CHOOSING_AGAIN;
                break;
            case CHOOSING_AGAIN:
                switch (pressedButton) {
                    //Strawberry
                    case 0:
                        //imageEventText.loadImage("theActAssets/images/events/ER/Option2_1.jpg");
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new WildStrawberry());
                        chosen_option = 3;
                        break;
                    //Pear
                    case 1:
                        //imageEventText.loadImage("theActAssets/images/events/ER/Option2_2.jpg");
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new WildPear());
                        chosen_option = 4;
                        break;
                    //Mango
                    case 2:
                        //imageEventText.loadImage("theActAssets/images/events/ER/Option2_3.jpg");
                        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(this.drawX, this.drawY, new WildMango());
                        chosen_option = 5;
                        break;
                }
                CardCrawlGame.sound.play("EVENT_VAMP_BITE");
                imageEventText.updateBodyText(DESCRIPTIONS[chosen_option]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(OPTIONS[2]);
                state = State.LEAVING;
                break;
            case LEAVING:
                openMap();
                break;
        }
    }
}