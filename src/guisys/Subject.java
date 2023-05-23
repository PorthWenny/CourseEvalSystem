package guisys;

public class Subject {
    private int id;
    private String name;
    private String prof;

    public Subject(int id, String name, String prof) {
        this.id = id;
        this.name = name;
        this.prof = prof;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProf() {
        return prof;
    }

    public void setProf(String prof) {
        this.prof = prof;
    }
}
