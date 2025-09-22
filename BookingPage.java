import javax.swing.*;
import java.awt.*;
import java.text.*;
import java.util.*;

/**
 * BookingPage - Booking form JFrame (No DB Connection)
 */
public class BookingPage extends JFrame {

    private JTextField nameField, emailField, phoneField;
    private JTextArea addressArea;
    private JSpinner pickupDateSpinner, dropDateSpinner;
    private JTextField pickupLocField, dropoffLocField;
    private JComboBox<String> carCombo;
    private JPanel carPreviewPanel; 
    private JLabel priceLabel;   // NEW: to show price

    private final SimpleDateFormat dbDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // Car daily rents
    private final Map<String, Integer> carPrices = Map.of(
            "Mercedes C-Class", 100,
            "BMW 7 Series", 150,
            "Audi A8", 140,
            "Toyota Alphard", 120,
            "Range Rover", 200,
            "Lexus LS", 160
    );

    public BookingPage() {
        setTitle("Let's find your perfect car");
        setSize(800, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(240, 240, 240));
        header.setBorder(BorderFactory.createEmptyBorder(14, 20, 14, 20));
        JLabel title = new JLabel("<html><span style='font-size:22pt'>Let's find your perfect car</span></html>");
        header.add(title, BorderLayout.WEST);
        add(header, BorderLayout.NORTH);

        // Form area
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createEmptyBorder(16, 24, 24, 24));
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(8, 8, 8, 8);
        gc.anchor = GridBagConstraints.WEST;
        gc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Name, Email
        gc.gridx = 0; gc.gridy = 0;
        form.add(new JLabel("Name:"), gc);
        gc.gridx = 1; gc.weightx = 0.5;
        nameField = new JTextField();
        form.add(nameField, gc);

        gc.gridx = 2;
        form.add(new JLabel("Email:"), gc);
        gc.gridx = 3;
        emailField = new JTextField();
        form.add(emailField, gc);

        // Row 1: Phone
        gc.gridx = 0; gc.gridy = 1;
        form.add(new JLabel("Phone No:"), gc);
        gc.gridx = 1; gc.gridwidth = 3;
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

        // Row 3: Pickup Date & Drop Date
        java.util.Date now = new java.util.Date();

        SpinnerDateModel pickModel = new SpinnerDateModel(now, null, null, Calendar.MINUTE);
        pickupDateSpinner = new JSpinner(pickModel);
        pickupDateSpinner.setEditor(new JSpinner.DateEditor(pickupDateSpinner, "yyyy-MM-dd HH:mm"));

        SpinnerDateModel dropModel = new SpinnerDateModel(
                new java.util.Date(now.getTime() + 3600 * 1000), null, null, Calendar.MINUTE);
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

        // Row 4: Pickup/Dropoff Location
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

        // Row 5: Car selection + preview
        gc.gridx = 0; gc.gridy = 5;
        form.add(new JLabel("Select Car:"), gc);
        gc.gridx = 1;
        String[] cars = {"Mercedes C-Class", "BMW 7 Series", "Audi A8", "Toyota Alphard", "Range Rover", "Lexus LS"};
        carCombo = new JComboBox<>(cars);
        form.add(carCombo, gc);

        gc.gridx = 2; gc.gridwidth = 2;
        carPreviewPanel = new JPanel();
        carPreviewPanel.setPreferredSize(new Dimension(240, 180));
        carPreviewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        carPreviewPanel.setLayout(new BorderLayout());

        // Price label under car preview
        priceLabel = new JLabel("Price: $0", SwingConstants.CENTER);
        priceLabel.setFont(new Font("Arial", Font.BOLD, 14));

        carPreviewPanel.add(priceLabel, BorderLayout.SOUTH);
        form.add(carPreviewPanel, gc);
        gc.gridwidth = 1;

        // Bottom: Submit button
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton submitBtn = new JButton("Submit Booking");
        bottom.add(submitBtn);

        add(form, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);

        // Listeners
        carCombo.addActionListener(e -> updateCarPreview());
        pickupDateSpinner.addChangeListener(e -> updateCarPreview());
        dropDateSpinner.addChangeListener(e -> updateCarPreview());
        submitBtn.addActionListener(e -> submitBooking());

        updateCarPreview();
    }

    // Show preview + price
    private void updateCarPreview() {
        String car = (String) carCombo.getSelectedItem();

        // Car images
        Map<String, String> map = new HashMap<>();
        map.put("Mercedes C-Class", "images/mercedes.jpg");
        map.put("BMW 7 Series", "images/7series.jpg");
        map.put("Audi A8", "images/audi.jpg");
        map.put("Toyota Alphard", "images/toyota.jpg");
        map.put("Range Rover", "images/range.jpg");
        map.put("Lexus LS", "images/lexus.jpg");

        String fn = map.getOrDefault(car, null);

        carPreviewPanel.removeAll();
        carPreviewPanel.setLayout(new BorderLayout());

        if (fn != null) {
            ImageIcon icon = new ImageIcon(fn);
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(240, 100, Image.SCALE_SMOOTH);
                JLabel imgLabel = new JLabel(new ImageIcon(img), SwingConstants.CENTER);
                JLabel nameLabel = new JLabel(car, SwingConstants.CENTER);
                nameLabel.setFont(new Font("Arial", Font.BOLD, 14));

                carPreviewPanel.add(imgLabel, BorderLayout.CENTER);
                carPreviewPanel.add(nameLabel, BorderLayout.NORTH);
            }
        }

        // Calculate price = rent × days
        int dailyRent = carPrices.getOrDefault(car, 0);
        java.util.Date pickup = (java.util.Date) pickupDateSpinner.getValue();
        java.util.Date drop = (java.util.Date) dropDateSpinner.getValue();

        long diff = Math.max(1, (drop.getTime() - pickup.getTime()) / (1000 * 60 * 60 * 24)); // at least 1 day
        int totalPrice = dailyRent * (int) diff;

        priceLabel.setText("Price: $" + totalPrice);
        carPreviewPanel.add(priceLabel, BorderLayout.SOUTH);

        carPreviewPanel.revalidate();
        carPreviewPanel.repaint();
    }

    // Just show confirmation + Payment Page (NO DB save)
    private void submitBooking() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String phone = phoneField.getText().trim();
        String address = addressArea.getText().trim();
        java.util.Date pickup = (java.util.Date) pickupDateSpinner.getValue();
        java.util.Date drop = (java.util.Date) dropDateSpinner.getValue();

        String pickupLoc = pickupLocField.getText().trim();
        String dropoffLoc = dropoffLocField.getText().trim();
        String car = (String) carCombo.getSelectedItem();

        if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Please fill name, email and phone.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!drop.after(pickup)) {
            JOptionPane.showMessageDialog(this,
                    "Drop date must be after pickup date.",
                    "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Booking saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Open Payment Page
        PaymentPage payment = new PaymentPage(
                name, email, phone, car, pickup, drop, pickupLoc, dropoffLoc
        );
        payment.setVisible(true);
        this.dispose();
        clearForm();
    }

    private void clearForm() {
        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        addressArea.setText("");
        pickupDateSpinner.setValue(new java.util.Date());
        dropDateSpinner.setValue(new java.util.Date(System.currentTimeMillis() + 3600 * 1000));
        pickupLocField.setText("");
        dropoffLocField.setText("");
        carCombo.setSelectedIndex(0);
        updateCarPreview();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BookingPage().setVisible(true));
    }
}
