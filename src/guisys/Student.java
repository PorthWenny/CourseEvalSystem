package guisys;

public class Student {
    private String id;
    private String name;
    private String surname;
    private String course;
    private String level;

    public Student(String id, String name, String surname, String course, String level) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.course = course;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
