package twoCS;

import java.io.*;
import java.net.*;
import java.sql.*;

public class Server {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/ContactDB";
    private static final String USER = "root";
    private static final String PASS = "yhw20031231!";

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(1234)) {
            System.out.println("Server is running and waiting for client connection...");

            while (true) {
                Socket socket = serverSocket.accept();
                new ClientHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    processCommand(inputLine);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void processCommand(String command) {
            String[] parts = command.split(" ", 5);
            String operation = parts[0];

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                switch (operation) {
                    case "ADD":
                        if (parts.length < 4) {
                            out.println("Error: Missing parameters for ADD operation");
                            return;
                        }
                        addContact(conn, parts[1], parts[2], parts[3]);
                        break;
                    case "UPDATE":
                        if (parts.length < 4) {
                            out.println("Error: Missing parameters for UPDATE operation");
                            return;
                        }
                        updateContact(conn, Integer.parseInt(parts[1]), parts[2], parts[3], parts[4]);
                        break;
                    case "DELETE":
                        if (parts.length < 2) {
                            out.println("Error: Missing ID for DELETE operation");
                            return;
                        }
                        deleteContact(conn, Integer.parseInt(parts[1]));
                        break;
                    case "GET":
                        if (parts.length < 2) {
                            out.println("Error: Missing ID for GET operation");
                            return;
                        }
                        getContact(conn, Integer.parseInt(parts[1]));
                        break;
                    case "GET_ALL":
                        getAllContacts(conn);
                        break;
                    default:
                        out.println("Error: Unknown operation");
                        break;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                out.println("Database error: " + e.getMessage());
            }
        }

        private void addContact(Connection conn, String name, String address, String phone) throws SQLException {
            String sql = "INSERT INTO contacts (name, address, phone) VALUES (?, ?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.setString(2, address);
                stmt.setString(3, phone);
                int affectedRows = stmt.executeUpdate();
                out.println("Contact added successfully: " + affectedRows);
            }
        }

        private void updateContact(Connection conn, int id, String name, String address, String phone) throws SQLException {
            System.out.println(name);
            System.out.println(address);
            System.out.println(phone);
            System.out.println(id);
            String sql = "SELECT * FROM contacts WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String updateSql = "UPDATE contacts SET name = ?, address = ?, phone = ? WHERE id = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                            updateStmt.setString(1, name);
                            updateStmt.setString(2, address);
                            updateStmt.setString(3, phone);
                            updateStmt.setInt(4, id);
                            int affectedRows = updateStmt.executeUpdate();
                            out.println("Contact updated successfully: " + affectedRows);
                        }
                    } else {
                        out.println("Contact not found.");
                    }
                }
            }
        }

        private void deleteContact(Connection conn, int id) throws SQLException {
            String sql = "SELECT * FROM contacts WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String deleteSql = "DELETE FROM contacts WHERE id = ?";
                        try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                            deleteStmt.setInt(1, id);
                            int affectedRows = deleteStmt.executeUpdate();
                            out.println("Contact deleted successfully: " + affectedRows);
                        }
                    } else {
                        out.println("Contact not found.");
                    }
                }
            }
        }

        private void getContact(Connection conn, int id) throws SQLException {
            String sql = "SELECT * FROM contacts WHERE id = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        out.println("Contact found: " + rs.getString("name") + ", " + rs.getString("address") + ", " + rs.getString("phone"));
                    } else {
                        out.println("Contact not found.");
                    }
                }
            }
        }

        private void getAllContacts(Connection conn) throws SQLException {
            String sql = "SELECT * FROM contacts";
            try (PreparedStatement stmt = conn.prepareStatement(sql);
                 ResultSet rs = stmt.executeQuery()) {
                out.println("BEGIN_LIST"); // 标记列表开始
                while (rs.next()) {
                    out.println(rs.getInt("id") + ", " + rs.getString("name") + ", " + rs.getString("address") + ", " + rs.getString("phone"));
                }
                out.println("END_LIST"); // 标记列表结束
            }
        }
    }
}
