package api.repository;

import api.model.Role;
import api.model.User;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserRepositoryLocal {
    private static final String PATH = "src/main/resources/";
    private static final String DB_FILE = PATH + "users.xml";

    public UserRepositoryLocal() {
        if (!new File(DB_FILE).exists()) {
            try {
            Files.createFile(Paths.get(DB_FILE));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Файл создан: " + DB_FILE);
        }
    }

    public long getNewUserId() {
        long result = 0;
        try {
            List<User> users = getAllUsers();
            if (users.isEmpty()) {
                return result;
            } else {
                return users.get(users.size() - 1).getId() + 1;
            }
        } catch (IOException e) {
            return result;
        }
    }

    public void addUser(User user) throws IOException {
        List<User> users = getAllUsers();
        users.add(user);
        writeUsersToFile(users);
    }

    public void EditProfile(User user) throws IOException {
        List<User> users = getAllUsers();
        int index = findIndexOfUserById(users, user.getId());
        if (index >= 0) {
            users.set(index, user);
            writeUsersToFile(users);
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public void removeUser(long id) throws IOException {
        List<User> users = getAllUsers();
        int index = findIndexOfUserById(users, id);
        if (index >= 0) {
            users.remove(index);
            writeUsersToFile(users);
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public void blockUser(long id) throws IOException {
        User user = findUserById(id);
        if (user != null) {
            EditProfile(Role.addBlockedRole(user));
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public void unblockUser(long id) throws IOException {
        User user = findUserById(id);
        if (user != null) {
            EditProfile(Role.addUserRole(user));
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public void roleAdmin(long id) throws IOException {
        User user = findUserById(id);
        if (user != null) {
            EditProfile(Role.addAdminRole(user));
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public void roleUser(long id) throws IOException {
        User user = findUserById(id);
        if (user != null) {
            EditProfile(Role.addUserRole(user));
        } else {
            throw new IllegalArgumentException("Пользователь не найден.");
        }
    }

    public User findUserByEmail(String email) throws IOException {
        List<User> users = getAllUsers();
        return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
    }

    public User findUserById(long id) throws IOException {
        List<User> users = getAllUsers();
        return users.stream().filter(u -> u.getId() == id).findFirst().orElse(null);
    }

    public List<User> getAllUsers() throws IOException {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(DB_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("<user id=") && line.endsWith("</user>")) {
                    int startIndex = line.indexOf("id=\"") + 4;
                    int endIndex = line.indexOf("\">", startIndex);
                    long id = Long.parseLong(line.substring(startIndex, endIndex));

                    String email = "";
                    String password = "";
                    String firstName = "";
                    String lastName = "";
                    String role = "";

                    Pattern pattern = Pattern.compile("<([^<>]+)>([^<]+)</\\1>");
                    Matcher matcher = pattern.matcher(line);
                    StringBuilder sb = new StringBuilder();
                    while (matcher.find()) {
                        sb.append(matcher.group(1)).append(":").append(matcher.group(2)).append("\n");
                    }
                    String[] entries = sb.toString().split("\n");
                    for (String entry : entries) {
                        String[] keyValue = entry.split(":");
                        if ("id".equalsIgnoreCase(keyValue[0])) {
                            id = Long.parseLong(keyValue[1]);
                        } else if ("email".equalsIgnoreCase(keyValue[0])) {
                            email = keyValue[1];
                        } else if ("password".equalsIgnoreCase(keyValue[0])) {
                            password = keyValue[1];
                        } else if ("firstName".equalsIgnoreCase(keyValue[0])) {
                            firstName = keyValue[1];
                        } else if ("lastName".equalsIgnoreCase(keyValue[0])) {
                            lastName = keyValue[1];
                        } else if ("role".equalsIgnoreCase(keyValue[0])) {
                            role = keyValue[1];
                        }
                    }
                    User user = new User(id, email, password, firstName, lastName);
                    user.setRole(role);
                    users.add(user);
                }
            }
        }
        return users;
    }

    private int findIndexOfUserById(List<User> users, long id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    private void writeUsersToFile(List<User> users) throws IOException {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(DB_FILE)))) {
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<users>");
            for (User u : users) {
                writer.print("<user id=\"");
                writer.print(u.getId());
                writer.print("\">");
                writer.print("<email>");
                writer.print(u.getEmail());
                writer.print("</email>");
                writer.print("<password>");
                writer.print(u.getPassword());
                writer.print("</password>");
                writer.print("<firstName>");
                writer.print(u.getFirstName());
                writer.print("</firstName>");
                writer.print("<lastName>");
                writer.print(u.getLastName());
                writer.print("</lastName>");
                writer.print("<role>");
                writer.print(u.getRole());
                writer.print("</role>");
                writer.println("</user>");
            }
            writer.println("</users>");
        }
    }
}
