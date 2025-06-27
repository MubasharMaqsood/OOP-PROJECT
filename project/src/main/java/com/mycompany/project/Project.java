/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class Project extends JFrame {

    public Project() {
        setTitle("Car Rental System - Welcome");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Welcome to Our Car Rental System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel infoLabel = new JLabel("We offer top quality cars for rent at affordable prices!", SwingConstants.CENTER);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 18));
        infoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel availableLabel = new JLabel("Available Cars:", SwingConstants.CENTER);
        availableLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        availableLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel imagePanel = new JPanel(new GridLayout(2, 2, 20, 20));
        imagePanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        String[] carNames = {"Toyota Corolla", "Honda Civic", "BMW X5", "Tesla Model S"};
        String[] imagePaths = {"toyota.jpg", "honda.jpg", "bmw.jpg", "tesla.jpg"};

        for (int i = 0; i < carNames.length; i++) {
            JPanel carPanel = new JPanel();
            carPanel.setLayout(new BorderLayout());

            JLabel carLabel = new JLabel(carNames[i], SwingConstants.CENTER);
            carLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

            ImageIcon icon = new ImageIcon(imagePaths[i]);
            Image img = icon.getImage().getScaledInstance(250, 150, Image.SCALE_SMOOTH);
            JLabel imgLabel = new JLabel(new ImageIcon(img));
            imgLabel.setHorizontalAlignment(SwingConstants.CENTER);

            carPanel.add(carLabel, BorderLayout.NORTH);
            carPanel.add(imgLabel, BorderLayout.CENTER);
            imagePanel.add(carPanel);
        }

        JButton continueButton = new JButton("Continue to Booking");
        continueButton.setFont(new Font("SansSerif", Font.BOLD, 16));
        continueButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        continueButton.addActionListener(e -> {
            dispose();
            new BookingFrame();
        });

        JButton rentedBtn = new JButton("View Rented Cars");
        rentedBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        rentedBtn.addActionListener(e -> {
            dispose();
            new RentedCarsFrame();
        });

        JButton availableBtn = new JButton("View Available Cars");
        availableBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        availableBtn.addActionListener(e -> {
            dispose();
            new AvailableCarsFrame();
        });

        contentPanel.add(titleLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(infoLabel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        contentPanel.add(availableLabel);
        contentPanel.add(imagePanel);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(continueButton);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(availableBtn);
        contentPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        contentPanel.add(rentedBtn);

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        add(scrollPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Project::new);
    }
}

class BookingFrame extends JFrame implements ActionListener {
    private JTextField nameField, cnicField, daysField;
    private JComboBox<String> carBox;
    static HashMap<String, Integer> carStock = new HashMap<>() {{
        put("Toyota Corolla", 5);
        put("Honda Civic", 5);
        put("BMW X5", 5);
        put("Tesla Model S", 5);
    }};

    public BookingFrame() {
        setTitle("Car Booking Form");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(7, 2, 10, 10));

        add(new JLabel("Customer Name:"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("CNIC (12345-1234567-1):"));
        cnicField = new JTextField();
        add(cnicField);

        add(new JLabel("Select Car:"));
        carBox = new JComboBox<>(carStock.keySet().toArray(new String[0]));
        add(carBox);

        add(new JLabel("Number of Days:"));
        daysField = new JTextField();
        add(daysField);

        JButton bookBtn = new JButton("Book Now");
        bookBtn.addActionListener(this);
        add(bookBtn);

        JButton backBtn = new JButton("Back to Home");
        backBtn.addActionListener(e -> {
            dispose();
            new Project();
        });
        add(backBtn);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String name = nameField.getText().trim();
        String cnic = cnicField.getText().trim();
        String car = (String) carBox.getSelectedItem();
        int rate;

        switch (car) {
            case "Toyota Corolla" -> rate = 50;
            case "Honda Civic" -> rate = 70;
            case "BMW X5" -> rate = 90;
            case "Tesla Model S" -> rate = 120;
            default -> rate = 0;
        }

        if (name.isEmpty() || cnic.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name and CNIC are required.", "Missing Info", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (!cnic.matches("\\d{5}-\\d{7}-\\d")) {
            JOptionPane.showMessageDialog(this, "CNIC format invalid.", "Format Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int days = Integer.parseInt(daysField.getText());
            if (days <= 0) throw new NumberFormatException();

            if (carStock.get(car) <= 0) {
                JOptionPane.showMessageDialog(this, "This car is currently not available.", "Unavailable", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int total = rate * days;

            String receipt = """
                    ====== Car Rental Receipt ======
                    Customer Name: %s
                    CNIC: %s
                    Date: %s
                    -----------------------------
                    Car Rented: %s
                    Rental Days: %d
                    Rate per Day: $%d
                    Total Amount: $%d
                    Thank you for your business!
                    =================================
                    
                    """.formatted(
                    name,
                    cnic,
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    car,
                    days,
                    rate,
                    total
            );

            String fileName = name.replaceAll("\\s+", "_") + "_receipt.txt";
            try (FileWriter writer = new FileWriter(fileName)) {
                writer.write(receipt);
            }

            carStock.put(car, carStock.get(car) - 1);

            JOptionPane.showMessageDialog(this, "Booking successful!\nReceipt saved as " + fileName);
            dispose();
            new Project();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid number of days.", "Input Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "File error during receipt save.", "File Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

class RentedCarsFrame extends JFrame {
    public RentedCarsFrame() {
        setTitle("Rented Cars");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        String[] carNames = {"Toyota Corolla", "Honda Civic", "BMW X5", "Tesla Model S"};
        String[] imagePaths = {"toyota.jpg", "honda.jpg", "bmw.jpg", "tesla.jpg"};

        for (int i = 0; i < carNames.length; i++) {
            int rented = 5 - BookingFrame.carStock.get(carNames[i]);
            if (rented > 0) {
                JPanel carPanel = new JPanel(new BorderLayout());
                JLabel carLabel = new JLabel(carNames[i] + " rented: " + rented, SwingConstants.CENTER);
                carLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

                ImageIcon icon = new ImageIcon(imagePaths[i]);
                Image img = icon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));

                carPanel.add(carLabel, BorderLayout.NORTH);
                carPanel.add(imgLabel, BorderLayout.CENTER);
                panel.add(carPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);

        JButton back = new JButton("Back to Home");
        back.addActionListener(e -> {
            dispose();
            new Project();
        });

        add(scrollPane, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
        setVisible(true);
    }
}

class AvailableCarsFrame extends JFrame {
    public AvailableCarsFrame() {
        setTitle("Available Cars");
        setSize(600, 500);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        String[] carNames = {"Toyota Corolla", "Honda Civic", "BMW X5", "Tesla Model S"};
        String[] imagePaths = {"toyota.jpg", "honda.jpg", "bmw.jpg", "tesla.jpg"};

        for (int i = 0; i < carNames.length; i++) {
            int available = BookingFrame.carStock.get(carNames[i]);
            if (available > 0) {
                JPanel carPanel = new JPanel(new BorderLayout());
                JLabel carLabel = new JLabel(carNames[i] + " available: " + available, SwingConstants.CENTER);
                carLabel.setFont(new Font("SansSerif", Font.BOLD, 14));

                ImageIcon icon = new ImageIcon(imagePaths[i]);
                Image img = icon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img));

                carPanel.add(carLabel, BorderLayout.NORTH);
                carPanel.add(imgLabel, BorderLayout.CENTER);
                panel.add(carPanel);
            }
        }

        JScrollPane scrollPane = new JScrollPane(panel);

        JButton back = new JButton("Back to Home");
        back.addActionListener(e -> {
            dispose();
            new Project();
        });

        add(scrollPane, BorderLayout.CENTER);
        add(back, BorderLayout.SOUTH);
        setVisible(true);
    }
}