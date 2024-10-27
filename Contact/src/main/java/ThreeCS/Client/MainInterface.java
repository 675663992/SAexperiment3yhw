package ThreeCS.Client;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class MainInterface extends JFrame {
    public MainInterface() {
        setTitle("Contact Manager - Main Interface");
        setSize(488,430);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JButton addContactButton = new JButton("Add Contact");
        addContactButton.setBounds(90,45,300,40);
        addContactButton.setFont(new Font("楷体",Font.BOLD,28));
        addContactButton.addActionListener(e -> {
            new AddContactInterface("localhost").setVisible(true);
        });

        JButton viewContactButton = new JButton("View Contact");
        viewContactButton.setBounds(90,110,300,45);
        viewContactButton.setFont(new Font("楷体",Font.BOLD,28));
        viewContactButton.addActionListener(e -> {
            new ViewContactInterface().setVisible(true);
        });

        JButton editContactButton = new JButton("Edit Contact");
        editContactButton.setBounds(90,175,300,45);
        editContactButton.setFont(new Font("楷体",Font.BOLD,28));
        editContactButton.addActionListener(e -> {
            try {
                new EditContactInterface("localhost").setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        JButton deleteContactButton = new JButton("Delete Contact");
        deleteContactButton.setBounds(90,240,300,45);
        deleteContactButton.setFont(new Font("楷体",Font.BOLD,28));
        deleteContactButton.addActionListener(e -> {
            try {
                new DeleteContactInterface("localhost").setVisible(true);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(addContactButton);
        add(viewContactButton);
        add(editContactButton);
        add(deleteContactButton);

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainInterface().setVisible(true);
        });
    }
}
