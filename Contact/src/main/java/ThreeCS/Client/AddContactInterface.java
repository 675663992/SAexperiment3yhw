package ThreeCS.Client;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class AddContactInterface extends JFrame {
    private JTextField nameField;
    private JTextField addressField;
    private JTextField phoneField;
    private Socket socket;
    private PrintWriter out;

    public AddContactInterface(String serverAddress) {
        setTitle("Add Contact");
        setSize(488,430);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel Name = new JLabel("Name:");
        Name.setFont(new Font("楷体",Font.BOLD,18));
        Name.setBounds(110,55,100,35);

        JLabel Address = new JLabel("Address:");
        Address.setFont(new Font("楷体",Font.BOLD,18));
        Address.setBounds(80,115,100,35);

        JLabel Phone = new JLabel("Phone:");
        Phone.setFont(new Font("楷体",Font.BOLD,18));
        Phone.setBounds(100,175,100,35);

        nameField = new JTextField();
        nameField.setBounds(165,55,200,30);
        nameField.setFont(new Font("楷体",Font.BOLD,18));

        addressField = new JTextField();
        addressField.setBounds(165,115,200,30);
        addressField.setFont(new Font("楷体",Font.BOLD,18));

        phoneField = new JTextField();
        phoneField.setBounds(165,175,200,30);
        phoneField.setFont(new Font("楷体",Font.BOLD,18));

        add(Name);
        add(nameField);
        add(Address);
        add(addressField);
        add(Phone);
        add(phoneField);

        JButton addButton = new JButton("Add");
        addButton.setBounds(200,280,100,50);
        addButton.setFont(new Font("楷体",Font.BOLD,28));
        addButton.addActionListener(e -> {
            String name = nameField.getText();
            String address = addressField.getText();
            String phone = phoneField.getText();
            // Send data to server to add contact
            try {
                connectToServer(serverAddress);
                out.println("ADD " + name + " " + address + " " + phone);
                String response = in.readLine();
                System.out.println(response);
                if (response != null && response.contains("success")) {
                    JOptionPane.showMessageDialog(this, "Contact added successfully.");
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add contact.");
                }
                dispose(); // Close the window after adding
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error connecting to server.");
            }
        });

        add(addButton);

        setLocationRelativeTo(null);
    }

    private BufferedReader in;
    private void connectToServer(String serverAddress) throws IOException {
        socket = new Socket(serverAddress, 1234);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AddContactInterface("localhost").setVisible(true);
        });
    }
}
