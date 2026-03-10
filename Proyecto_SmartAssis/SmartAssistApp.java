import javax.swing.*;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SmartAssistApp extends JFrame {
    private ArrayList<IComando> historial = new ArrayList<>();

    public SmartAssistApp() {
        // --- CONFIGURACIÓN DE VENTANA ---
        setTitle("SMART-ASSIST V3.1 - Harrison Fitzgeralt");
        setSize(600, 700);
        setMinimumSize(new Dimension(500, 600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(18, 18, 24)); // Fondo negro profundo suave
        setLayout(new BorderLayout());

        // --- HEADER ---
        JPanel pnlHeader = new JPanel(new GridLayout(2, 1));
        pnlHeader.setBackground(new Color(30, 31, 40));
        pnlHeader.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        JLabel lblTitulo = new JLabel("SISTEMA INTEGRADO DE INGENIERÍA", SwingConstants.CENTER);
        lblTitulo.setForeground(new Color(0, 212, 255)); // Azul neón suave
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel lblUni = new JLabel("Corporación Unificada Nacional - CUN", SwingConstants.CENTER);
        lblUni.setForeground(new Color(200, 200, 200));
        lblUni.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        pnlHeader.add(lblTitulo);
        pnlHeader.add(lblUni);
        add(pnlHeader, BorderLayout.NORTH);

        // --- CONTENEDOR CENTRAL CON RESTRICCIÓN DE TAMAÑO ---
        // Esto evita que los botones crezcan demasiado al maximizar
        JPanel pnlCenterWrapper = new JPanel(new GridBagLayout());
        pnlCenterWrapper.setOpaque(false);

        JPanel pnlBotones = new JPanel(new GridLayout(5, 1, 15, 15));
        pnlBotones.setOpaque(false);
        pnlBotones.setPreferredSize(new Dimension(350, 450)); // Tamaño fijo ideal para los botones

        // Colores pastel/mate más elegantes
        BotonGamer b1 = new BotonGamer("📊 MÓDULO FÍSICA", new Color(110, 80, 180));
        BotonGamer b2 = new BotonGamer("🌐 MÓDULO REDES", new Color(45, 160, 145));
        BotonGamer b3 = new BotonGamer("💾 EXPORTAR DATA", new Color(70, 85, 100));
        BotonGamer b4 = new BotonGamer("ℹ️ ACERCA DE", new Color(55, 130, 200));
        BotonGamer b5 = new BotonGamer("❌ SALIR", new Color(190, 65, 65));

        b1.addActionListener(e -> {
            ModuloFisica m = new ModuloFisica();
            m.ejecutar();
            historial.add(m);
        });
        b2.addActionListener(e -> {
            ModuloRedes m = new ModuloRedes();
            m.ejecutar();
            historial.add(m);
        });
        b3.addActionListener(e -> guardarReporte());
        b4.addActionListener(e -> mostrarCreditos());
        b5.addActionListener(e -> {
            if (JOptionPane.showConfirmDialog(this, "¿Cerrar sistema?", "Confirmar", 0) == 0)
                System.exit(0);
        });

        pnlBotones.add(b1);
        pnlBotones.add(b2);
        pnlBotones.add(b3);
        pnlBotones.add(b4);
        pnlBotones.add(b5);
        pnlCenterWrapper.add(pnlBotones); // El GridBagLayout mantendrá el panel centrado y con su tamaño
        add(pnlCenterWrapper, BorderLayout.CENTER);

        // --- FOOTER ---
        JPanel pnlStatus = new JPanel(new BorderLayout());
        pnlStatus.setBackground(new Color(25, 25, 30));
        pnlStatus.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        JLabel lblUser = new JLabel("Harrison Fitzgeralt | Estudiante CUN");
        lblUser.setForeground(new Color(120, 120, 130));
        lblUser.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JLabel lblFecha = new JLabel(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        lblFecha.setForeground(new Color(120, 120, 130));

        pnlStatus.add(lblUser, BorderLayout.WEST);
        pnlStatus.add(lblFecha, BorderLayout.EAST);
        add(pnlStatus, BorderLayout.SOUTH);
    }

    private void mostrarCreditos() {
        String msg = "<html><center><p style='color:#ffffff;'>SmartAssist v3.1<br>Harrison Fitzgeralt<br>CUN 2026</p></center></html>";
        JOptionPane.showMessageDialog(this, msg, "Créditos", JOptionPane.PLAIN_MESSAGE);
    }

    private void guardarReporte() {
        try (PrintWriter w = new PrintWriter("reporte_final.txt")) {
            w.println("=== REPORTE GENERADO POR HARRISON FITZGERALT ===");
            for (IComando c : historial)
                w.println(c.obtenerReporte());
            JOptionPane.showMessageDialog(this, "Reporte exportado.");
        } catch (Exception ex) {
        }
    }

    class BotonGamer extends JButton {
        private Color colorBase;

        public BotonGamer(String t, Color c) {
            super(t);
            this.colorBase = c;
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // Sombra suave
            g2.setColor(new Color(0, 0, 0, 60));
            g2.fill(new RoundRectangle2D.Double(2, 4, getWidth() - 4, getHeight() - 4, 25, 25));
            // Fondo
            g2.setColor(colorBase);
            g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight() - 4, 25, 25));
            g2.dispose();
            super.paintComponent(g);
        }
    }

    public static void main(String[] args) {
        // --- ESTILO GLOBAL DE COMPONENTES ---
        UIManager.put("OptionPane.background", new Color(30, 31, 40));
        UIManager.put("Panel.background", new Color(30, 31, 40));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("Button.background", new Color(60, 65, 80));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("TextField.background", new Color(40, 42, 54));
        UIManager.put("TextField.foreground", new Color(0, 255, 200));
        UIManager.put("TextField.border", BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 100)),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        UIManager.put("Label.foreground", Color.WHITE);

        SwingUtilities.invokeLater(() -> new SmartAssistApp().setVisible(true));
    }
}