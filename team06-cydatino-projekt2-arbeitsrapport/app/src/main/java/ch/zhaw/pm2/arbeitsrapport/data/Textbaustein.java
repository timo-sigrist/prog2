package ch.zhaw.pm2.arbeitsrapport.data;

public class Textbaustein {

    private int id;
    private String content;

    public Textbaustein() {

    }

    public Textbaustein(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
