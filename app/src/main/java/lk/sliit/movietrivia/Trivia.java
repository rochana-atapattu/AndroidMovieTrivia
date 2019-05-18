package lk.sliit.movietrivia;

public class Trivia {

    private String mQuestion;
    private String mCategory;
    private String mDifficulty;
    private String mType;
    private String mCorrectAnswer;
    private String mIncorrectAnswer1;
    private String mIncorrectAnswer2;
    private String mIncorrectAnswer3;

    public Trivia(String mQuestion, String mCategory, String mDifficulty, String mType, String mCorrectAnswer, String mIncorrectAnswer1, String mIncorrectAnswer2, String mIncorrectAnswer3) {
        this.mQuestion = mQuestion;
        this.mCategory = mCategory;
        this.mDifficulty = mDifficulty;
        this.mType = mType;
        this.mCorrectAnswer = mCorrectAnswer;
        this.mIncorrectAnswer1 = mIncorrectAnswer1;
        this.mIncorrectAnswer2 = mIncorrectAnswer2;
        this.mIncorrectAnswer3 = mIncorrectAnswer3;
    }

    public String getmQuestion() {
        return mQuestion;
    }

    public void setmQuestion(String mQuestion) {
        this.mQuestion = mQuestion;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmDifficulty() {
        return mDifficulty;
    }

    public void setmDifficulty(String mDifficulty) {
        this.mDifficulty = mDifficulty;
    }

    public String getmType() {
        return mType;
    }

    public void setmType(String mType) {
        this.mType = mType;
    }

    public String getmCorrectAnswer() {
        return mCorrectAnswer;
    }

    public void setmCorrectAnswer(String mCorrectAnswer) {
        this.mCorrectAnswer = mCorrectAnswer;
    }

    public String getmIncorrectAnswer1() {
        return mIncorrectAnswer1;
    }

    public void setmIncorrectAnswer1(String mIncorrectAnswer1) {
        this.mIncorrectAnswer1 = mIncorrectAnswer1;
    }

    public String getmIncorrectAnswer2() {
        return mIncorrectAnswer2;
    }

    public void setmIncorrectAnswer2(String mIncorrectAnswer2) {
        this.mIncorrectAnswer2 = mIncorrectAnswer2;
    }

    public String getmIncorrectAnswer3() {
        return mIncorrectAnswer3;
    }

    public void setmIncorrectAnswer3(String mIncorrectAnswer3) {
        this.mIncorrectAnswer3 = mIncorrectAnswer3;
    }
}
