package com.hls;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class menu extends JFrame implements ActionListener {

    private final JPanel root;
    private TextField account;
    private JPasswordField passwd;

    public menu() {
        this.setTitle("Menu");
        this.setSize(500, 500);
        root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        this.add(root);

        show_login();
//        show_demo();

        this.setVisible(true);
    }


    private void show_demo() {
        JPanel root = new JPanel();
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        this.add(root);

        JPanel jPanel = new JPanel();
        root.add(jPanel);
        jPanel.add(new JLabel("今天早上吃："));
        List<String> s = new ArrayList<>();
        s.add("豆浆");
        s.add("油条");
        s.add("稀饭");
        s.add("面包");
        s.add("包子");
        JComboBox<String> stringJComboBox = new JComboBox<>(s.stream().toArray(String[]::new));
        jPanel.add(stringJComboBox);
        JPanel jPanel1 = new JPanel();
        root.add(jPanel1);
        TextField textField = new TextField("选择的是：", 15);
        jPanel1.add(textField);
        stringJComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String string = stringJComboBox.getSelectedItem().toString();
                textField.setText("选择的是：" + string);
            }
        });

    }

    private void show_diolog() {
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "Welcome to HLS");
            }

            @Override
            public void windowClosing(WindowEvent e) {
                JOptionPane.showMessageDialog(null, "Goodbye");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
    }

    private void show_login() {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        root.add(jPanel);
        Font timesRoman = new Font("TimesRoman", Font.PLAIN, 20);
        JLabel jLabel = new JLabel("Login");
        jLabel.setFont(timesRoman);
        jPanel.add(jLabel);

        JPanel panel = new JPanel();
        panel.add(new JLabel("account："));
        account = new TextField(15);
        panel.add(account);
        jPanel.add(panel);
        panel = new JPanel();
        panel.add(new JLabel("password："));
        passwd = new JPasswordField(15);
        panel.add(passwd);
        jPanel.add(panel);
        panel = new JPanel();
        Button submit = new Button("Submit");
        submit.addActionListener(this);
        panel.add(submit);
        Button cancel = new Button("Cancel");
        cancel.addActionListener(this);
        panel.add(cancel);
        jPanel.add(panel);
    }

    private void show_choice() {
        JPanel panel = new JPanel();
        this.add(panel);

        ButtonGroup group = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            JRadioButton admin = new JRadioButton("admin");
            group.add(admin);
            panel.add(admin);
        }

        JPanel panel1 = new JPanel();
        this.add(panel1);
        for (int i = 0; i < 5; i++) {
            JCheckBox admin = new JCheckBox("admin");
            panel1.add(admin);
        }


        JPanel panel2 = new JPanel();
        this.add(panel2);
        for (int i = 0; i < 5; i++) {
            Button button = new Button("button" + i);
            button.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "123");
            });
            panel2.add(button);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Submit")) {
            String string = Arrays.toString(passwd.getPassword());
            string = string.replaceAll("\\[", "")
                    .replaceAll("]", "")
                    .replaceAll(",", "")
                    .replaceAll(" ", "");
            System.out.println(string);
            if (account.getText().equals("admin") && string.equals("admin")) {
                JOptionPane.showMessageDialog(null, "login success");
                return;
            }
            JOptionPane.showMessageDialog(null, "login failure");
        } else if (e.getActionCommand().equals("Cancel")) {
            JOptionPane.showMessageDialog(null, "login cancel");
        }
    }
}
