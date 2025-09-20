import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import javax.swing.border.*;

/**
 * BookingPage - Booking form JFrame
 * - Edit DB connection settings inside the code before running.
 */
public class BookingPage extends JFrame {
    // DB connection settings - EDIT THESE
    private static final String DB_URL = "jdbc:mysql://localhost:3306/carrental?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "pranav2006";

    private JTextField nameField, emailField, phoneField;
    private JTextArea addressArea;
    private JSpinner pickupDateSpinner, dropDateSpinner;
    private JTextField pickupLocField, dropoffLocField;
    private JComboBox<String> carCombo;
    private JLabel carPreviewLabel;

    public BookingPage() {
        setTitle("Let's find your perfect car");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(240,240,240));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("<html><span style='font-size:22pt'>Let's find your perfect car</span></html>");
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form area
        JPanel form = new JPanel();
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        form.setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8,8,8,8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Name, Email
        gc.gridx = 0; gc.gridy = 0; gc.weightx = 0.25;
        form.add(new JLabel("Name:"), gc);
        gc.gridx = 1; gc.weightx = 0.75;
        nameField = new JTextField();
        form.add(nameField, gc);

        gc.gridx = 2; gc.weightx = 0.25;
        form.add(new JLabel("Email:"), gc);
        gc.gridx = 3; gc.weightx = 0.75;
        emailField = new JTextField();
        form.add(emailField, gc);

        // Row 1: Phone
        gc.gridx = 0; gc.gridy = 1; gc.weightx = 0.25;
        form.add(new JLabel("Phone No:"), gc);
        gc.gridx = 1; gc.gridwidth = 3; gc.weightx = 0.75;
        phoneField = new JTextField();
        form.add(phoneField, gc);
        gc.gridwidth = 1;

        // Row 2: Address
        gc.gridx = 0; gc.gridy = 2;
        form.add(new JLabel("Address:"), gc);
        gc.gridx = 1; gc.gridwidth = 3;
        addressArea = new JTextArea(3, 20);
        JScrollPane sp = new JScrollPane(addressArea);
        form.add(sp, gc);
        gc.gridwidth = 1;

        // Row 3: Pickup Date & Drop Date (using JSpinner date)
        java.util.Date now = new java.util.Date();

        SpinnerDateModel pickModel = new SpinnerDateModel(now, null, null, Calendar.MINUTE);
        pickupDateSpinner = new JSpinner(pickModel);
        pickupDateSpinner.setEditor(new JSpinner.DateEditor(pickupDateSpinner, "yyyy-MM-dd HH:mm"));

        SpinnerDateModel dropModel = new SpinnerDateModel(new java.util.Date(now.getTime() + 3600*1000), null, null, Calendar.MINUTE);
        dropDateSpinner = new JSpinner(dropModel);
        dropDateSpinner.setEditor(new JSpinner.DateEditor(dropDateSpinner, "yyyy-MM-dd HH:mm"));

        gc.gridx = 0; gc.gridy = 3;
        form.add(new JLabel("Pickup Date:"), gc);
        gc.gridx = 1;
        form.add(pickupDateSpinner, gc);

        gc.gridx = 2;
        form.add(new JLabel("Drop Date:"), gc);
        gc.gridx = 3;
        form.add(dropDateSpinner, gc);

        // Row 4: Pickup location / dropoff location
        gc.gridx = 0; gc.gridy = 4;
        form.add(new JLabel("Pickup Location:"), gc);
        gc.gridx = 1;
        pickupLocField = new JTextField();
        form.add(pickupLocField, gc);

        gc.gridx = 2;
        form.add(new JLabel("Drop-off Location:"), gc);
        gc.gridx = 3;
        dropoffLocField = new JTextField();
        form.add(dropoffLocField, gc);

        // Row 5: Car selection and preview
        gc.gridx = 0; gc.gridy = 5;
        form.add(new JLabel("Select Car:"), gc);
        gc.gridx = 1;
        String[] cars = {"Mercedes C-Class", "BMW 7 Series", "Audi A8", "Toyota Alphard", "Range Rover", "Lexus LS"};
        carCombo = new JComboBox<>(cars);
        form.add(carCombo, gc);

        gc.gridx = 2; gc.gridwidth = 2;
        carPreviewLabel = new JLabel("<html><center>Car preview here</center></html>", SwingConstants.CENTER);
        carPreviewLabel.setPreferredSize(new Dimension(240, 120));
        carPreviewLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

        form.add(carPreviewLabel, gc);
        gc.gridwidth = 1;

        // Below: Submit button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Booking");
        bottom.add(submitBtn);

        // Add to frame
        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Car selection listener
        carCombo.addActionListener(e -> updateCarPreview());

        // Submit listener - store to DB
        submitBtn.addActionListener(e -> {
            try {
                submitBooking();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        updateCarPreview();
    }

    private void updateCarPreview() {
        String car = (String) carCombo.getSelectedItem();
        Map<String, String> map = new HashMap<>();
        map.put("Mercedes C-Class", "images/mercedes.jpg");
        map.put("BMW 7 Series", "images/7series.jpg");
        map.put("Audi A8", "images/audi.jpg");
        map.put("Toyota Alphard", "images/toyota.jpg");
        map.put("Range Rover", "images/range.jpg");
        map.put("Lexus LS", "images/lexus.jpg");

        String fn = map.getOrDefault(car, null);
        if (fn != null) {
            ImageIcon icon = new ImageIcon(fn);
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(240, 120, Image.SCALE_SMOOTH);
                carPreviewLabel.setText("");
                carPreviewLabel.setIcon(new ImageIcon(img));
                return;
            }
        }
        carPreviewLabel.setIcon(null);
        carPreviewLabel.setText("<html><center>No preview</center></html>");
    }

    // ----------------- INSERT INTO DATABASE -----------------
    private void submitBooking() throws Exception {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        Date pickup = (Date) pickupDateSpinner.getValue();
        Date drop = (Date) dropDateSpinner.getValue();
        String pickupLoc = pickupLocField.getText().trim();
        String dropoffLoc = dropoffLocField.getText().trim();
        String car = (String) carCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill name, email and phone.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!drop.after(pickup)) {
            JOptionPane.showMessageDialog(this, "Drop date must be after pickup date.", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Load JDBC driver
        Class.forName("com.mysql.cj.jdbc.Driver");

        String sql = "INSERT INTO bookings " +
                "(name, email, phone, address, pickup_date, drop_date, pickup_location, dropoff_location, car_model) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, phone);
            pst.setString(4, address);
            pst.setTimestamp(5, new java.sql.Timestamp(pickup.getTime()));
            pst.setTimestamp(6, new java.sql.Timestamp(drop.getTime()));
            pst.setString(7, pickupLoc);
            pst.setString(8, dropoffLoc);
            pst.setString(9, car);

            int rows = pst.executeUpdate();
            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "✅ Booking saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "⚠️ Failed to save booking.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        pickupDateSpinner.setValue(new Date());
        dropDateSpinner.setValue(new Date(System.currentTimeMillis() + 3600*1000));
        pickupLocField.setText("");
        dropoffLocField.setText("");
        carCombo.setSelectedIndex(0);
        updateCarPreview();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BookingPage bp = new BookingPage();
            bp.setVisible(true);
        });
    }
}
