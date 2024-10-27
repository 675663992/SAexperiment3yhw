package ThreeCS.Client;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ViewContactInterface extends JFrame {
    private JTable table;
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public ViewContactInterface() {
        setTitle("View All Contacts");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Name");
        model.addColumn("Address");
        model.addColumn("Phone");
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        JButton viewAllButton = new JButton("View All Contacts");
        viewAllButton.addActionListener(e -> {
            if (socket == null || socket.isClosed()) {
                connectToServer();
            }
            out.println("GET_ALL");
        });

        panel.add(viewAllButton);
        add(panel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        connectToServer();
    }

    private void connectToServer() {
        try {
            socket = new Socket("localhost", 1234);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            readResponses();
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to connect to server.");
        }
    }

    private void readResponses() {
        new Thread(() -> {
            try {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.equals("BEGIN_LIST")) {
                        ((DefaultTableModel) table.getModel()).setRowCount(0);
                    } else if (line.equals("END_LIST")) {
                        break;
                    } else {
                        String[] data = line.split(", ");
                        DefaultTableModel model = (DefaultTableModel) table.getModel();
                        model.addRow(new Object[]{data[0], data[1], data[2], data[3]});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ViewContactInterface().setVisible(true);
        });
    }
}
