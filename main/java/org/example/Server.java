package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class Server extends JFrame {
    private static final int WINDOW_WIDTH = 500;
    private static final int WINDOW_HEIGHT = 500;
    private static final int WINDOW_POSX = 200;
    private static final int WINDOW_POSY = 300;
    public static final String LOG_PATH = "src/main/java/org/example/log.txt";

    List<Client> clientList;

    JButton btnStart, btnStop;
    JTextArea log;
    boolean process;

    public Server(){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(WINDOW_POSX, WINDOW_POSY);
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setResizable(false);
        setTitle("SERVER");
        createPanel();
        setVisible(true);
        clientList = new ArrayList<>();
    }

    public boolean connectUser(Client client){
        if (!process){
            return false;
        }
        clientList.add(client);
        return true;
    }

    public String getLog() {
        return readLog();
    }

    public void disconnectUser(Client client){
        clientList.remove(client);
        if (client != null){
            client.disconnectFromServer();
        }
    }

    public void message(String text){
        if (!process){
            return;
        }
        text += "";
        appendLog(text);
        answerAll(text);
        saveInLog(text);
    }

    private void answerAll(String text){
        for (Client client: clientList){
            client.answer(text);
        }
    }

    private void saveInLog(String text){
        try (FileWriter writer = new FileWriter(LOG_PATH, true)){
            writer.write(text);
            writer.write("\n");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private String readLog(){
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader reader = new FileReader(LOG_PATH);){
            int c;
            while ((c = reader.read()) != -1){
                stringBuilder.append((char) c);
            }
            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
            return stringBuilder.toString();
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private void appendLog(String text){
        log.append(text + "\n");
    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createButtons(), BorderLayout.NORTH);
    }

    private Component createButtons() {
        JPanel panel = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("START");
        btnStop = new JButton("STOP");

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (process){
                    appendLog("Server is already work ");
                } else {
                    process = true;
                    appendLog("Start server");
                }
            }
        });

        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!process){
                    appendLog("Server is already stop");
                } else {
                    process = false;
                    while (!clientList.isEmpty()){
                        disconnectUser(clientList.get(clientList.size()-1));
                    }
                    appendLog("Server stopped");
                }
            }
        });

        panel.add(btnStart);
        panel.add(btnStop);
        return panel;
    }
}