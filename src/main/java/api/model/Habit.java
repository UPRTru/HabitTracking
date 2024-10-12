package api.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Habit {
    private final long oneDay = 86400000;
    private final long id;
    private final long user_id; //id пользователя
    private String name; //название
    private String description; //описание
    private int frequencyDay; //частота
    private int duration; //длительность
    private Date startDate; //дата начала
    private int missedDays; //пропущенные дни
    private Date endDate; //дата окончания
    private Date nextExecutionDate = null; //дата следующего выполнения
    private int streak = 0; //текущая серия выполнения привычки
    private List<Date> history = new ArrayList<>(); //история выполнения привычки

    public Habit(long id, long user_id, String name, String description, int frequencyDay, int duration) {
        this.id = id;
        this. user_id = user_id;
        this.name = name;
        this.description = description;
        this.frequencyDay = frequencyDay;
        this.duration = duration;
        startDate = new Date();
        endDate = new Date(startDate.getTime() + (oneDay * duration));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getFrequencyDay() {
        return frequencyDay;
    }

    public void setFrequencyDay(int frequencyDay) {
        if (history.size() > 0) {
            nextExecutionDate = new Date(history.get(history.size() - 1).getTime() + (oneDay * frequencyDay));
        }
        this.frequencyDay = frequencyDay;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        endDate = new Date(startDate.getTime() + (oneDay * duration));
        this.duration = duration;
    }

    public void executed() {
        int checkMissedDays = checkMissedDays();
        if (checkMissedDays > 0) {
            missedDays += checkMissedDays;
            streak = 1;
        } else {
            streak += 1;
        }
        nextExecutionDate = new Date(new Date().getTime() + (oneDay * frequencyDay));
        history.add(new Date());
    }

    public int checkMissedDays() {
        if (nextExecutionDate == null) {
            return (int) ((getNotNextExecutionDate().getTime() - new Date().getTime()) / oneDay);
        } else {
            return (int) ((nextExecutionDate.getTime() - new Date().getTime()) / oneDay);
        }
    }

    public Date getNotNextExecutionDate() {
        return new Date(startDate.getTime() + (oneDay * frequencyDay));
    }

    public long getId() {
        return id;
    }

    public long getUser_id() {
        return user_id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public int getMissedDays() {
        return missedDays;
    }

    public Date getEndDate() {
        return endDate;
    }

    public Date getNextExecutionDate() {
        return nextExecutionDate;
    }

    public boolean completed() {
        return endDate.after(new Date());
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setMissedDays(int missedDays) {
        this.missedDays = missedDays;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setNextExecutionDate(Date nextExecutionDate) {
        this.nextExecutionDate = nextExecutionDate;
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id + '\'' +
                ", название='" + name + '\'' +
                ", описание='" + description + '\'' +
                ", частота (в днях)=" + frequencyDay + '\'' +
                ", длительность=" + duration + '\'' +
                ", дата начала=" + startDate + '\'' +
                ", пропущенные дни=" + missedDays + '\'' +
                ", дата окончания=" + endDate + '\'' +
                ", дата следующего выполнения=" + nextExecutionDate + '\'' +
                ", текущая серия выполнения привычки=" + streak + '\'' +
                ", история выполнения привычки=" + getDateHistoryToString().toString() + '\'' +
                '}';
    }

    public String getDateHistoryToString() {
        StringBuilder result = new StringBuilder();
        for (Date date : history) {
            result.append(date.toString()).append("\n");
        }
        return result.toString();
    }

    public int getStreak() {
        return streak;
    }

    public List<Date> getHistory() {
        return history;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void setHistory(List<Date> history) {
        this.history = history;
    }
}
