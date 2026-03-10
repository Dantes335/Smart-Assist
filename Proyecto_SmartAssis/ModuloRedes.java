import javax.swing.*;
import java.awt.*;

public class ModuloRedes extends ModuloAsistente {
    private String registro = "Sin actividad";

    public ModuloRedes() {
        super("Redes");
    }

    @Override
    public void ejecutar() {
        // --- FORMULARIO DE INGRESO ESTILIZADO ---
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setOpaque(false);

        JTextField txtVlan = new JTextField("10");
        JTextField txtIp = new JTextField("192.168.1.0");
        JTextField txtPref = new JTextField("24");

        // Personalización de campos para que coincidan con el estilo global
        configurarCampo(txtVlan);
        configurarCampo(txtIp);
        configurarCampo(txtPref);

        formulario.add(new JLabel("ID VLAN:"));
        formulario.add(txtVlan);
        formulario.add(new JLabel("IP de Red:"));
        formulario.add(txtIp);
        formulario.add(new JLabel("Prefijo (CIDR):"));
        formulario.add(txtPref);

        int result = JOptionPane.showConfirmDialog(null, formulario, "Ingeniería de Redes - CUN",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String ipIngresada = txtIp.getText().trim();
                int prefijo = Integer.parseInt(txtPref.getText().trim());

                // 1. VALIDACIÓN DE OCTETOS E IP
                if (!validarIP(ipIngresada)) {
                    JOptionPane.showMessageDialog(null,
                            "Error: La IP " + ipIngresada + " no es válida (Octetos 0-255).", "Error de Red", 0);
                    return;
                }

                // 2. VALIDACIÓN DE PREFIJO
                if (prefijo < 8 || prefijo > 30) {
                    JOptionPane.showMessageDialog(null, "Error: Prefijo fuera de rango académico (Use 8 a 30).",
                            "Error de Prefijo", 0);
                    return;
                }

                // 3. CÁLCULO DE HOSTS REALES (2^(32-n) - 2)
                int hostsUtilizables = (int) Math.pow(2, (32 - prefijo)) - 2;

                // 4. LÓGICA DE MÁSCARA Y GATEWAY
                String baseIp = ipIngresada.substring(0, ipIngresada.lastIndexOf('.'));
                String gateway = baseIp + ".1";
                String mask = calcularMascara(prefijo);

                // --- INTERFAZ DE RESULTADO TIPO TERMINAL ---
                String msg = "<html><body style='width:350px; font-family:sans-serif; background-color:#1e1e2e; padding:10px;'>"
                        +
                        "<div style='background-color:#44475a; color:#50fa7b; padding:8px; text-align:center; border-radius:10px 10px 0 0;'>"
                        +
                        "<b>>_ CONSOLE_REPORT</b></div>" +
                        "<div style='background-color:#282a36; color:#f8f8f2; padding:15px; border:2px solid #44475a; border-radius:0 0 10px 10px; font-family:monospace;'>"
                        +
                        "<span style='color:#bd93f9;'>SUBRED:</span> " + ipIngresada + "/" + prefijo + "<br>" +
                        "<span style='color:#bd93f9;'>MÁSCARA:</span> " + mask + "<br>" +
                        "<span style='color:#bd93f9;'>HOSTS:</span> <b style='color:#50fa7b;'>" + hostsUtilizables
                        + " utilizables</b><br>" +
                        "<hr style='border:0.5px solid #444;'>" +
                        "<b style='color:#f1fa8c;'>Cisco IOS Config:</b><br>" +
                        "<span style='color:#6272a4;'># conf t</span><br>" +
                        "<span style='color:#ff79c6;'>interface g0/0/1." + txtVlan.getText() + "</span><br>" +
                        "<span style='color:#ff79c6;'>encapsulation dot1Q " + txtVlan.getText() + "</span><br>" +
                        "<span style='color:#ff79c6;'>ip address " + gateway + " " + mask + "</span><br>" +
                        "<span style='color:#6272a4;'># exit</span>" +
                        "</div></body></html>";

                JOptionPane.showMessageDialog(null, msg, "Resultado de Operaciones", JOptionPane.PLAIN_MESSAGE);
                this.registro = "VLAN " + txtVlan.getText() + " | Hosts: " + hostsUtilizables;

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese solo números enteros en VLAN y Prefijo.");
            }
        }
    }

    // Método para validar que cada octeto de la IP esté entre 0 y 255
    private boolean validarIP(String ip) {
        String[] octetos = ip.split("\\.");
        if (octetos.length != 4)
            return false;
        try {
            for (String octeto : octetos) {
                int val = Integer.parseInt(octeto);
                if (val < 0 || val > 255)
                    return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Método para devolver la máscara de subred según el prefijo común
    private String calcularMascara(int p) {
        if (p >= 24)
            return "255.255.255." + (256 - (int) Math.pow(2, 32 - p));
        if (p >= 16)
            return "255.255." + (256 - (int) Math.pow(2, 24 - p)) + ".0";
        return "255." + (256 - (int) Math.pow(2, 16 - p)) + ".0.0";
    }

    private void configurarCampo(JTextField f) {
        f.setBackground(new Color(40, 42, 54));
        f.setForeground(new Color(0, 255, 200));
        f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createLineBorder(new Color(68, 71, 90)));
    }

    @Override
    public String obtenerReporte() {
        return "Módulo Redes: " + registro;
    }
}