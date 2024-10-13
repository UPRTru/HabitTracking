package api.repository;

import api.model.Habit;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HabitRepositoryLocal {
    private static final String PATH = "src/main/resources/";
    private static final String DB_FILE = PATH + "habits.xml";

    public HabitRepositoryLocal() {
        if (!new File(DB_FILE).exists()) {
            try {
                Files.createFile(Paths.get(DB_FILE));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Файл создан: " + DB_FILE);
        }
    }

    public long getNewHabitId() {
        long result = 0;
        try {
            List<Habit> users = getAllHabits();
            if (users.isEmpty()) {
                return result;
            } else {
                return users.get(users.size() - 1).getId() + 1;
            }
        } catch (IOException e) {
            return result;
        }
    }

    public void addHabit(Habit habit) throws IOException {
        List<Habit> habits = getAllHabits();
        habits.add(habit);
        writeHabitsToFile(habits);
    }

    public void updateHabit(Habit habit) throws IOException {
        List<Habit> habits = getAllHabits();
        int index = findIndexOfHabitById(habits, habit.getId());
        if (index >= 0) {
            habits.set(index, habit);
            writeHabitsToFile(habits);
        } else {
            throw new IllegalArgumentException("Привычка не найдена.");
        }
    }

    public void removeHabit(long id) throws IOException {
        List<Habit> habits = getAllHabits();
        int index = findIndexOfHabitById(habits, id);
        if (index >= 0) {
            habits.remove(index);
            writeHabitsToFile(habits);
        } else {
            throw new IllegalArgumentException("Привычка не найдена.");
        }
    }

    public Habit findHabitById(long id) throws IOException {
        List<Habit> habits = getAllHabits();
        return habits.stream().filter(h -> h.getId() == id).findFirst().orElse(null);
    }

    public List<Habit> getHabitsByUserId(long user_id) throws IOException {
        List<Habit> habits = getAllHabits();
        return habits.stream().filter(h -> h.getUser_id() == user_id).toList();
    }

    public List<Habit> getAllHabits() throws IOException {
        List<Habit> habits = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<habit id=") && line.endsWith("</habit>")) {
                    int startIndex = line.indexOf("id=\"") + 4;
                    int endIndex = line.indexOf("\">", startIndex);
                    long id = Long.parseLong(line.substring(startIndex, endIndex));


                    startIndex = line.indexOf("<history>") + 9;
                    endIndex = line.indexOf("</history>", startIndex);
                    String historyString = line.substring(startIndex, endIndex);

                    long user_id = 0;
                    String name = "";
                    String description = "";
                    int frequencyDay = 0;
                    int duration = 0;
                    Date startDate = null;
                    int missedDays = 0;
                    Date endDate = null;
                    Date nextExecutionDate = null;
                    int streak = 0;
                    List<Date> history = new ArrayList<>();
                    for (String entry : entries(line)) {
                        String t = entry;
                        String[] keyValue = entry.split(":");
                        if ("user_id".equalsIgnoreCase(keyValue[0])) {
                            user_id = Long.parseLong(keyValue[1]);
                        } else if ("name".equalsIgnoreCase(keyValue[0])) {
                            name = keyValue[1];
                        } else if ("description".equalsIgnoreCase(keyValue[0])) {
                            description = keyValue[1];
                        } else if ("frequencyDay".equalsIgnoreCase(keyValue[0])) {
                            frequencyDay = Integer.parseInt(keyValue[1]);
                        } else if ("duration".equalsIgnoreCase(keyValue[0])) {
                            duration = Integer.parseInt(keyValue[1]);
                        } else if ("startDate".equalsIgnoreCase(keyValue[0])) {
                            startDate = new Date(Long.parseLong(keyValue[1]));
                        } else if ("missedDays".equalsIgnoreCase(keyValue[0])) {
                            missedDays = Integer.parseInt(keyValue[1]);
                        } else if ("endDate".equalsIgnoreCase(keyValue[0])) {
                            endDate = new Date(Long.parseLong(keyValue[1]));
                        } else if ("nextExecutionDate".equalsIgnoreCase(keyValue[0])) {
                            if (!keyValue[1].equals("null")) {
                                nextExecutionDate = new Date(Long.parseLong(keyValue[1]));
                            }
                        } else if ("streak".equalsIgnoreCase(keyValue[0])) {
                            streak = Integer.parseInt(keyValue[1]);
                        } else if (historyString != null && !historyString.isEmpty() && !historyString.equals("null")) {
                            for (String entry2 : entries(historyString)) {
                                String[] keyValue2 = entry2.split(":");
                                if ("date".equalsIgnoreCase(keyValue2[0])) {
                                    history.add(new Date(Long.parseLong(keyValue2[1])));
                                }
                            }
                        }
                    }
                    Habit habit = new Habit(id, user_id, name, description, frequencyDay, duration);
                    habit.setStartDate(startDate);
                    habit.setMissedDays(missedDays);
                    habit.setEndDate(endDate);
                    habit.setStreak(streak);
                    habit.setHistory(history);
                    if (nextExecutionDate != null) {
                        habit.setNextExecutionDate(nextExecutionDate);
                    }
                    habits.add(habit);
                }
            }
        }
        return habits;
    }

    private String[] entries(String line) {
        Pattern pattern = Pattern.compile("<([^<>]+)>([^<]+)</\\1>");
        Matcher matcher = pattern.matcher(line);
        StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            sb.append(matcher.group(1)).append(":").append(matcher.group(2)).append("\n");
        }
        return sb.toString().split("\n");
    }

    private void writeHabitsToFile(List<Habit> habits) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DB_FILE)))) {
            String nextExecutionDate;
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<habits>");
            for (Habit h : habits) {
                if (h.getNextExecutionDate() == null) {
                    nextExecutionDate = "null";
                } else {
                    nextExecutionDate = h.getNextExecutionDate().getTime() + "";
                }
                writer.print("<habit id=\"");
                writer.print(h.getId());
                writer.print("\">");
                writer.print("<user_id>");
                writer.print(h.getUser_id());
                writer.print("</user_id>");
                writer.print("<name>");
                writer.print(h.getName());
                writer.print("</name>");
                writer.print("<description>");
                writer.print(h.getDescription());
                writer.print("</description>");
                writer.print("<frequencyDay>");
                writer.print(h.getFrequencyDay());
                writer.print("</frequencyDay>");
                writer.print("<duration>");
                writer.print(h.getDuration());
                writer.print("</duration>");
                writer.print("<startDate>");
                writer.print(h.getStartDate().getTime());
                writer.print("</startDate>");
                writer.print("<missedDays>");
                writer.print(h.getMissedDays());
                writer.print("</missedDays>");
                writer.print("<endDate>");
                writer.print(h.getEndDate().getTime());
                writer.print("</endDate>");
                writer.print("<nextExecutionDate>");
                writer.print(nextExecutionDate);
                writer.print("</nextExecutionDate>");
                writer.print("<streak>");
                writer.print(h.getStreak());
                writer.print("</streak>");
                writer.print("<history>");
                if (h.getHistory().size() > 0) {
                    for(Date d : h.getHistory()) {
                        writer.print("<date>");
                        writer.print(d.getTime());
                        writer.print("</date>");
                    }
                } else {
                    writer.print("null");
                }
                writer.print("</history>");
                writer.println("</habit>");
            }
            writer.println("</habits>");
        }
    }

    private int findIndexOfHabitById(List<Habit> habits, long id) {
        for (int i = 0; i < habits.size(); i++) {
            if (habits.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }
}
