package theAct.events;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.AbstractImageEvent;
import com.megacrit.cardcrawl.localization.EventStrings;
import theAct.TheActMod;

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
    private static final String BASE_IMG = "images/events/spinTheWheel.jpg";

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
            String image = DESCRIPTIONS[num * 2 + 1];
            if (image.isEmpty()) {
                image = BASE_IMG;
            }
            imageEventText.loadImage(image);
            imageEventText.updateBodyText(DESCRIPTIONS[num * 2]);
        }

        void updateDialogAnswers()
        {
            imageEventText.clearAllDialogs();

            List<String> options = new ArrayList<>();
            for (int i=0; i<4; ++i) {
                options.add(OPTIONS[5 + (num-1) * 4 + i]);
            }

            Collections.shuffle(options, new Random(AbstractDungeon.eventRng.randomLong()));

            for (int i=0; i<options.size(); ++i) {
                String text = options.get(i);
                if (text.startsWith(">")) {
                    text = text.substring(1);
                    correctAnswer = i;
                }
                text = OPTIONS[i + 1] + text;
                imageEventText.updateDialogOption(i, text);
            }
        }
    }

    private List<Question> questionList;

    public GremlinQuiz()
    {
        super(NAME, DESCRIPTIONS[0], BASE_IMG);

        // Shuffle questions
        List<Question> tmpList = new ArrayList<>();
        for (int i=2; i<DESCRIPTIONS.length; i+=2) {
            tmpList.add(new Question(i/2));
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
        }
    }

    private void nextQuestion()
    {
        if (questionList.isEmpty()) {
            // Out of questions
            screenNum = 2;
            imageEventText.loadImage(BASE_IMG);
            imageEventText.updateBodyText(DESCRIPTIONS[1]);
            imageEventText.clearAllDialogs();
            return;
        }

        currQuestion = questionList.remove(0);
        currQuestion.updateBodyText();
        currQuestion.updateDialogAnswers();
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
}
