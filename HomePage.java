import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class HomePage extends JFrame {

    public HomePage() {
        setTitle("Premium Car Rentals");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 650);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);


        // Top bar (burgundy background) with logo and nav
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.BLACK);// burgundy
        topBar.setPreferredSize(new Dimension(0, 90));
        topBar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JLabel logo = new JLabel("CAR RENTAL");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("SansSerif", Font.BOLD, 28));
        logo.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
        topBar.add(logo, BorderLayout.WEST);

        // nav labels
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.CENTER, 24, 30));
        nav.setOpaque(false);
        String[] navItems = {"Home", "About Us", "Services", "Fleet", "Contact Us"};
        
        for (String s : navItems) {
            JLabel l = new JLabel(s);
            l.setForeground(Color.WHITE);
            l.setFont(new Font("SansSerif", Font.PLAIN, 16));
            nav.add(l);
        }
        topBar.add(nav, BorderLayout.CENTER);

        // Book Now button
        JButton bookNow = new JButton("BOOK NOW");
        bookNow.setBackground(Color.WHITE);
        bookNow.setForeground(new Color(87, 20, 31));
        bookNow.setFocusPainted(false);
        bookNow.setFont(new Font("SansSerif", Font.BOLD, 14));
        bookNow.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        topBar.add(bookNow, BorderLayout.EAST);

        add(topBar, BorderLayout.NORTH);

        // Main area: left text and right wallpaper
        JPanel main = new JPanel(new BorderLayout());
        main.setBackground(new Color(245,245,245));

        // Left promotional text
        JPanel left = new JPanel();
        left.setOpaque(false);
        left.setPreferredSize(new Dimension(540, 420));
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBorder(BorderFactory.createEmptyBorder(90, 60, 60, 60));
        JLabel heading = new JLabel("Booking");
        heading.setFont(new Font("SansSerif", Font.PLAIN, 96));
        heading.setForeground(Color.BLACK);
        left.add(heading);

        JLabel sub = new JLabel("Reserve your one-of-a-kind experience");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 16));
        sub.setForeground(Color.BLACK);
        sub.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        left.add(sub);

        main.add(left, BorderLayout.WEST);

        // Right wallpaper panel (show an image)
        JPanel right = new JPanel(new BorderLayout());
        right.setOpaque(false);
        right.setBorder(BorderFactory.createEmptyBorder(58, 20, 20, 40));
        // Load image - make sure banner.jpg is in project folder or resource path
        ImageIcon banner = null;
        try {
            banner = new ImageIcon("QQ.png"); // replace filename if needed
            Image img = banner.getImage().getScaledInstance(460, 300, Image.SCALE_SMOOTH);
            banner = new ImageIcon(img);
        } catch (Exception e) {
            // ignore, will show placeholder
        }

        JLabel imageLabel;
        if (banner != null && banner.getIconWidth() > 0) {
            imageLabel = new JLabel(banner);
        } else {
            imageLabel = new JLabel("<html><center>Wallpaper<br/>(place banner.jpg in project folder)</center></html>");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setPreferredSize(new Dimension(460, 300));
            imageLabel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        }
        right.add(imageLabel, BorderLayout.CENTER);

        main.add(right, BorderLayout.EAST);
        add(main, BorderLayout.CENTER);

        // Book Now action
        bookNow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Open BookingPage
                BookingPage booking = new BookingPage();
                booking.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        // simple look and feel
        SwingUtilities.invokeLater(() -> {
            HomePage hp = new HomePage();
            hp.setVisible(true);
        });
    }
}
