package d.dictionary.BaseFactory;


import java.sql.*;
import java.util.ArrayList;

public class DictionaryManagement extends Dictionary {
    private static final String HOST_NAME = "localhost";
    private static final String DB_NAME = "dictionary";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "1234567890";
    private static final String PORT = "3307";
    private static final String MYSQL_URL = String.format("jdbc:mysql://%s:%s/%s", HOST_NAME, PORT, DB_NAME);

    protected static Connection connection = null;

    public static void connectToDB() throws SQLException {
        connection = DriverManager.getConnection(MYSQL_URL, USERNAME, PASSWORD);
    }


    public static void AddWord() throws SQLException {
        connectToDB();
        wordList = getAllWords();
        for (Word word : wordList) {
            dictionary.addWord(word);
        }
    }

    public static void init() throws SQLException {
        try {
            connectToDB();
            Trie.searchedWords = getAllWordTargets();
            for (String word : Trie.searchedWords) {
                Trie.insert(word);
            }
            AddWord();
            System.out.println("Initialized successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Initialization failed.", e);
        }
    }

    public static String findWord(String target) {
        String SQL_QUERY = "SELECT definition FROM dictionary WHERE target = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);

            try {
                ResultSet rs = ps.executeQuery();
                try {
                    if (rs.next()) {
                        StringBuilder result = new StringBuilder();
                        String detail = rs.getString("definition");
                        detail = detail.replaceAll("<I><Q>", "");
                        detail = detail.replaceAll("</Q></I>", "");
                        String[] parts = detail.split("<br />");
                        parts[0] = parts[0].replaceAll("@", "");
                        for (String part : parts) {
                            if (part.contains("@")) {
                                result.append(part.replace("@", "")).append('\n');
                            } else if (part.contains("*")) {
                                result.append(part.substring(1).trim()).append('\n');
                            } else if (part.contains("=")) {
                                result.append(part.replace("=", "").replace("+", ":"));
                                result.append('\n');
                            } else result.append(part).append('\n');
                        }
                        return result.toString();
                    } else {
                        return "404";
                    }
                } finally {
                    close(rs);
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "Từ tìm kiếm không tồn tại hoặc đã bị xoá";
    }

    public static boolean addWord(String target, String explain) {
        String SQL_QUERY = "INSERT INTO dictionary (target, definition) VALUES (?, ?)";

        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
            ps.setString(2, explain);

            try {
                ps.executeUpdate();
            } catch (SQLIntegrityConstraintViolationException e) {
                return false;
            } finally {
                close(ps);
            }

            Trie.insert(target);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    public boolean deleteWord(String target) {
        String SQL_QUERY = "DELETE FROM dictionary WHERE target = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);

            try {
                int deletedRow = ps.executeUpdate();

                if (deletedRow == 0) {
                    return false;
                }
            } finally {
                close(ps);
            }

            Trie.delete(target);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }


    public boolean updateWord(String target, String explain) {
        String SQL_QUERY = "UPDATE dictionary SET definition = ? WHERE target = ?";

        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);
            ps.setString(1, target);
            ps.setString(2, explain);

            try {
                int updatedRow = ps.executeUpdate();

                if (updatedRow == 0) {
                    return false;
                }
            } finally {
                close(ps);
            }

            Trie.insert(target);

            return true;
        } catch (SQLException e) {
            e.printStackTrace();

            return false;
        }
    }

    private static ArrayList<Word> getWordsFromResultSet(PreparedStatement ps) throws SQLException {
        try {
            ResultSet rs = ps.executeQuery();

            try {
                ArrayList<Word> res = new ArrayList<>();

                while (rs.next()) {
                    res.add(new Word(rs.getString(2), rs.getString(3)));
                }

                return res;
            } finally {
                close(rs);
            }
        } finally {
            close(ps);
        }
    }


    public static ArrayList<Word> getAllWords() {
        String SQL_QUERY = "SELECT * FROM dictionary";

        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);

            return getWordsFromResultSet(ps);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }


    public static ArrayList<String> getAllWordTargets() {
        String SQL_QUERY = "SELECT * FROM dictionary";
        try {
            PreparedStatement ps = connection.prepareStatement(SQL_QUERY);

            try {
                ResultSet rs = ps.executeQuery();

                if (rs != null) {
                    try {
                        ArrayList<String> res = new ArrayList<>();

                        while (rs.next()) {
                            res.add(rs.getString(2));
                        }

                        return res;
                    } finally {
                        close(rs);
                    }
                } else {
                    System.out.println("ResultSet is null");
                    return new ArrayList<>();
                }
            } finally {
                close(ps);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    protected static void close(Connection connection) {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void close(PreparedStatement ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected static void close(ResultSet ps) {
        try {
            if (ps != null) {
                ps.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        close(connection);
    }

}
