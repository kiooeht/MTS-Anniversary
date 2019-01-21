package theAct.events;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.RainingGoldEffect;
import theAct.TheActMod;
import theAct.events.buttons.ImageDialogOptionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GremlinQuiz extends AbstractImageEvent
{
    public static final String ID = TheActMod.makeID("GremlinQuiz");
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);
    private static final String NAME = eventStrings.NAME;
    private static final String[] DESCRIPTIONS = eventStrings.DESCRIPTIONS;
    private static final String[] OPTIONS = eventStrings.OPTIONS;

    private static final int QUESTION_COUNT = 3;
    private static final int PRIZE_GOLD = 50;
    private static final float PRIZE_DAMAGE = 0.07f;
    private static final float A_PRIZE_DAMAGE = 0.10f;
    private static final String BASE_IMG = TheActMod.assetPath("images/events/GremlinQuiz.png");

    private static final int START_QUESTION_INDEX = 5;
    private static final int START_ABCD_INDEX = 5;
    private static final int START_ANSWER_INDEX = START_ABCD_INDEX + 4;
    private static final int NUM_ANSWERS = 4;

    private float hpLossPercent;

    private Question currQuestion = null;
    private int correctCount = 0;
    private int incorrectCount = 0;

    private class Question
    {
        int num;
        int correctAnswer = -1;

        Question(int num)
        {
            this.num = num;
        }

        void updateBodyText()
        {
            String image = DESCRIPTIONS[START_QUESTION_INDEX + num * 2 + 1];
            if (image.isEmpty()) {
                image = BASE_IMG;
            }
            imageEventText.loadImage(image);
            imageEventText.updateBodyText(DESCRIPTIONS[START_QUESTION_INDEX + num * 2]);
        }

        void updateDialogAnswers()
        {
            imageEventText.clearAllDialogs();

            List<String> options = new ArrayList<>();
            for (int i=0; i<4; ++i) {
                options.add(OPTIONS[START_ANSWER_INDEX + num * NUM_ANSWERS + i]);
            }

            Collections.shuffle(options, new Random(AbstractDungeon.eventRng.randomLong()));

            for (int i=0; i<options.size(); ++i) {
                String text = options.get(i);
                if (text.startsWith(">")) {
                    text = text.substring(1);
                    correctAnswer = i;
                }
                if (text.startsWith("img:")) {
                    String imgPath = text.substring(4);
                    text = OPTIONS[i + START_ABCD_INDEX];
                    updateImgDialogOption(i, text, imgPath);
                } else {
                    text = OPTIONS[i + START_ABCD_INDEX] + text;
                    imageEventText.updateDialogOption(i, text);
                }
            }
        }
    }

    private List<Question> questionList;

    public GremlinQuiz()
    {
        super(NAME, DESCRIPTIONS[0], BASE_IMG);

        hpLossPercent = PRIZE_DAMAGE;
        if (AbstractDungeon.ascensionLevel >= 15) {
            hpLossPercent = A_PRIZE_DAMAGE;
        }

        // Shuffle questions
        List<Question> tmpList = new ArrayList<>();
        int qNum = 0;
        for (int i=START_QUESTION_INDEX; i<DESCRIPTIONS.length; i+=2) {
            tmpList.add(new Question(qNum));
            ++qNum;
        }
        Collections.shuffle(tmpList, new Random(AbstractDungeon.eventRng.randomLong()));

        // Pick 3
        questionList = new ArrayList<>();
        int questionCount = tmpList.size();
        for (int i=0; i<Math.min(QUESTION_COUNT, questionCount); ++i) {
            questionList.add(tmpList.remove(0));
        }

        imageEventText.setDialogOption(OPTIONS[0]);
    }

    @Override
    protected void buttonEffect(int buttonPressed)
    {
        switch (screenNum) {
            case 0: // Intro
                screenNum = 1;
                nextQuestion();
                break;
            case 1: // Questions
                checkAnswer(buttonPressed);
                nextQuestion();
                break;
            case 2: // Prize
                int gold = getGoldPrize();
                if (gold > 0) {
                    AbstractDungeon.effectList.add(new RainingGoldEffect(gold));
                    AbstractDungeon.player.gainGold(gold);
                }
                int damage = getDamagePrize();
                if (damage > 0) {
                    CardCrawlGame.sound.play("ATTACK_DAGGER_6");
                    CardCrawlGame.sound.play("BLOOD_SPLAT");
                    AbstractDungeon.player.damage(new DamageInfo(null, damage, DamageInfo.DamageType.HP_LOSS));
                }

                screenNum = 3;
                imageEventText.updateDialogOption(0, OPTIONS[4]);
                imageEventText.clearRemainingOptions();
                break;
            case 3: // Done
                openMap();
                break;
        }
    }

    private void nextQuestion()
    {
        if (questionList.isEmpty()) {
            // Out of questions
            screenNum = 2;
            imageEventText.loadImage(BASE_IMG);

            String body = String.format(DESCRIPTIONS[1], correctCount, correctCount+incorrectCount);
            String prize = OPTIONS[1];
            if (incorrectCount == 0) {
                body += DESCRIPTIONS[2];
            } else if (correctCount == 0) {
                body += DESCRIPTIONS[3];
            } else {
                body += DESCRIPTIONS[4];
            }
            if (correctCount > 0) {
                prize += String.format(OPTIONS[2], getGoldPrize());
            }
            if (incorrectCount > 0) {
                prize += String.format(OPTIONS[3], getDamagePrize());
            }

            imageEventText.updateBodyText(body);
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(prize);
            return;
        }

        currQuestion = questionList.remove(0);
        currQuestion.updateBodyText();
        currQuestion.updateDialogAnswers();
    }

    private int getGoldPrize()
    {
        return correctCount * PRIZE_GOLD;
    }

    private int getDamagePrize()
    {
        return (int)(incorrectCount * (AbstractDungeon.player.maxHealth * hpLossPercent));
    }

    private void checkAnswer(int buttonPressed)
    {
        if (currQuestion == null) {
            return;
        }

        if (currQuestion.correctAnswer == -1) {
            System.out.println("ERROR: QUESTION HAS NO CORRECT ANSWER wat");
        }
        if (buttonPressed == currQuestion.correctAnswer) {
            ++correctCount;
            System.out.println("CORRECT!");
        } else {
            ++incorrectCount;
            System.out.println("WRONG!");
        }
    }

    private void updateImgDialogOption(int slot, String text, String imgPath)
    {
        imageEventText.updateDialogOption(slot, text);
        imageEventText.optionList.set(slot, new ImageDialogOptionButton(slot, text, imgPath));
    }
}
