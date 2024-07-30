package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Client extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_POSX = 800;
    private static final int WINDOW_POSY = 300;

    private Server server;
    private boolean connected;
    private String name;

    JTextArea log;
    JTextField login, message;
    JPasswordField password;
    JButton btnLogin, btnSend;
    JPanel loginPanel;

    public Client(Server server) {
        this.server = server;

        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setTitle("CLIENT");
        createPanel();
        setVisible(true);
    }

    public void answer(String text) {
        appendLog(text);
    }

    private void connectToServer() {
        if (server.connectUser(this)) {
            appendLog("Connection success\n");
            loginPanel.setVisible(false);
            connected = true;
            name = login.getText();
            String log = server.getLog();
            if (log != null) {
                appendLog(log);
            }
        } else {
            appendLog("Connection failed");
        }
    }

    public void disconnectFromServer() {
        if (connected) {
            loginPanel.setVisible(true);
            connected = false;
            server.disconnectUser(this);
            appendLog("Connection lost");
        }
    }

    public void message() {
        if (connected) {
            String text = message.getText();
            if (!text.equals("")) {
                server.message(name + ": " + text);
                message.setText("");
            }
        } else {
            appendLog("No connection");
        }

    }

    private void appendLog(String text) {
        log.append(text + "\n");
    }

    private void createPanel() {
        add(createLoginPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createMessageField(), BorderLayout.SOUTH);
    }

    private Component createLoginPanel() {
        loginPanel = new JPanel(new GridLayout(1, 3));
        login = new JTextField("Pavel");
        password = new JPasswordField("1234");
        btnLogin = new JButton("LOGIN");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });


        loginPanel.add(login);
        loginPanel.add(password);
        loginPanel.add(btnLogin);

        return loginPanel;
    }

    private Component createLog() {
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createMessageField() {
        JPanel panel = new JPanel(new BorderLayout());
        message = new JTextField();
        message.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });
        btnSend = new JButton("SEND");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(message);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }
}
