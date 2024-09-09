package ch.zhaw.pm2.arbeitsrapport.data;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Arbeitsrapport {

    private static AtomicInteger idCounter = new AtomicInteger();
    private int id;
    private List<Textbaustein> textbausteinList;

    public Arbeitsrapport() {
        this.id = idCounter.getAndIncrement();
        this.textbausteinList = new ArrayList<>();
    }

    public Arbeitsrapport(List<Textbaustein> textbausteinList) {
        this.id = idCounter.getAndIncrement();
        this.textbausteinList = textbausteinList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Textbaustein> getTextbausteinList() {
        return textbausteinList;
    }

    public void setTextbausteinList(List<Textbaustein> textbausteinList) {
        this.textbausteinList = textbausteinList;
    }
}
