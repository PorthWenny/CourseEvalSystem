package guisys;

public class Question {
    private String question;
    private String type;
    private String answer = "";
    private int id;

    public Question(int id, String question, String type) {
        this.question = question;
        this.type = type;
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    
    public void setId (int id) {
    	this.id = id;
    }
    
    public int getId () {
    	return id;
    }
}
