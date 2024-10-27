package ThreeCS;

import java.io.*;
import java.net.*;
import java.util.List;
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

            BusinessLogic businessLogic = new BusinessLogic();

            switch (operation) {
                case "ADD":
                    if (parts.length < 4) {
                        out.println("Error: Missing parameters for ADD operation");
                        return;
                    }
                    businessLogic.addContact(parts[1], parts[2], parts[3]);
                    break;
                case "UPDATE":
                    if (parts.length < 5) {
                        out.println("Error: Missing parameters for UPDATE operation");
                        return;
                    }
                    businessLogic.updateContact(Integer.parseInt(parts[1]), parts[2], parts[3], parts[4]);
                    break;
                case "DELETE":
                    if (parts.length < 2) {
                        out.println("Error: Missing ID for DELETE operation");
                        return;
                    }
                    businessLogic.deleteContact(Integer.parseInt(parts[1]));
                    break;
                case "GET":
                    if (parts.length < 2) {
                        out.println("Error: Missing ID for GET operation");
                        return;
                    }
                    List<DataAccess.Contact> contacts = businessLogic.getAllContacts();
                    contacts.forEach(contact -> out.println(contact.getId() + ", " + contact.getName() + ", " + contact.getAddress() + ", " + contact.getPhone()));
                    break;
                case "GET_ALL":
                    List<DataAccess.Contact> allContacts = businessLogic.getAllContacts();
                    allContacts.forEach(contact -> out.println(contact.getId() + ", " + contact.getName() + ", " + contact.getAddress() + ", " + contact.getPhone()));
                    break;
                default:
                    out.println("Error: Unknown operation");
                    break;
            }
        }
    }
}
