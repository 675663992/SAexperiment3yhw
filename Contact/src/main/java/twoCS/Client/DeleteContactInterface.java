package twoCS.Client;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class DeleteContactInterface extends JFrame {
    private JTextField idField;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public DeleteContactInterface(String serverAddress) throws IOException {
        setTitle("Delete Contact");
        setSize(488,430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        idField = new JTextField(20);
        idField.setBounds(165,135,200,30);
        idField.setFont(new Font("楷体",Font.BOLD,18));

        JLabel ID = new JLabel("ID:");
        ID.setFont(new Font("楷体",Font.BOLD,18));
        ID.setBounds(120,135,100,35);
        add(ID);
        add(idField);

        JButton deleteButton = new JButton("Delete");
        deleteButton.setBounds(200,280,130,50);
        deleteButton.setFont(new Font("楷体",Font.BOLD,28));
        deleteButton.addActionListener(e -> {
            String id = idField.getText();
            // Send data to server to delete contact
            try {
                connectToServer(serverAddress);
                out.println("DELETE " + id);
                String response = in.readLine();
                System.out.println(response);
                if (response != null && response.contains("success")) {
                    JOptionPane.showMessageDialog(this, "Contact deleted successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete contact.");
                }
                dispose(); // Close the window after deleting
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to server.");
            }
        });

        add(deleteButton);

        setLocationRelativeTo(null);
        connectToServer(serverAddress);
    }

    private void connectToServer(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 1234);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new DeleteContactInterface("localhost").setVisible(true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
