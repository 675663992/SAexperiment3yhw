package twoCS.Client;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.*;

public class EditContactInterface extends JFrame {
    private JTextField idField;
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public EditContactInterface(String serverAddress) throws IOException {
        setTitle("Edit Contact");
        setSize(488,430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        idField = new JTextField();
        idField.setBounds(165,55,200,30);
        idField.setFont(new Font("楷体",Font.BOLD,18));

        nameField = new JTextField(20);
        nameField.setBounds(165,115,200,30);
        nameField.setFont(new Font("楷体",Font.BOLD,18));

        addressField = new JTextField(20);
        addressField.setBounds(165,175,200,30);
        addressField.setFont(new Font("楷体",Font.BOLD,18));

        phoneField = new JTextField(20);
        phoneField.setBounds(165,235,200,30);
        phoneField.setFont(new Font("楷体",Font.BOLD,18));

        JLabel ID = new JLabel("Card ID:");
        ID.setFont(new Font("楷体",Font.BOLD,18));
        ID.setBounds(80,55,150,35);

        JLabel Name = new JLabel("Name:");
        Name.setFont(new Font("楷体",Font.BOLD,18));
        Name.setBounds(110,115,150,35);

        JLabel Address = new JLabel("Address:");
        Address.setFont(new Font("楷体",Font.BOLD,18));
        Address.setBounds(80,175,150,35);

        JLabel Phone = new JLabel("Phone:");
        Phone.setFont(new Font("楷体",Font.BOLD,18));
        Phone.setBounds(100,235,150,35);

        add(ID);
        add(idField);
        add(Name);
        add(nameField);
        add(Address);
        add(addressField);
        add(Phone);
        add(phoneField);

        JButton editButton = new JButton("Edit");
        editButton.setBounds(200,280,100,50);
        editButton.setFont(new Font("楷体",Font.BOLD,28));
        editButton.addActionListener(e -> {
            String contactId = idField.getText();
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            // Send data to server to edit contact
            try {
                connectToServer(serverAddress);
                out.println("UPDATE " + contactId + " " + name + " " + address + " " + phone);
                String response = in.readLine();
                System.out.println("Server Response: " + response);
                if (response != null && response.contains("success")) {
                    JOptionPane.showMessageDialog(this, "Contact updated successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update contact: " + response);
                }
                dispose(); // Close the window after editing
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to server: " + ex.getMessage());
            }
        });

        add(editButton);

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
                new EditContactInterface("localhost").setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Failed to create Edit Contact Interface: " + e.getMessage());
            }
        });
    }
}
