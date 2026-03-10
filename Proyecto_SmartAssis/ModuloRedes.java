import javax.swing.*;
import java.awt.*;

public class ModuloRedes extends ModuloAsistente {
    private String registro = "Sin actividad";

    public ModuloRedes() {
        super("Redes");
    }

    @Override
    public void ejecutar() {
        // --- FORMULARIO TÉCNICO ---
        JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));
        formulario.setOpaque(false);

        JTextField txtIp = new JTextField("192.168.10.0");
        JTextField txtPref = new JTextField("26"); // Cambiamos a 26 para mostrar segmentación

        configurarCampo(txtIp);
        configurarCampo(txtPref);

        formulario.add(new JLabel("Dirección IP Base:"));
        formulario.add(txtIp);
        formulario.add(new JLabel("Prefijo CIDR (8-30):"));
        formulario.add(txtPref);

        int result = JOptionPane.showConfirmDialog(null, formulario, "INGENIERÍA DE REDES - CUN",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            try {
                String ip = txtIp.getText().trim();
                int p = Integer.parseInt(txtPref.getText().trim());

                // 1. VALIDACIONES DE INGENIERÍA
                if (!validarIP(ip) || p < 8 || p > 30) {
                    JOptionPane.showMessageDialog(null, "Error: IP o Prefijo fuera de rango técnico.");
                    return;
                }

                // 2. LÓGICA DE BITS (MATEMÁTICA PURA)
                int totalHosts = (int) Math.pow(2, (32 - p)) - 2;
                String baseIp = ip.substring(0, ip.lastIndexOf('.'));
                
                // Definición de jerarquía técnica
                String networkId = baseIp + ".0";
                String gateway = baseIp + ".1"; // Primer host
                String lastHost = baseIp + "." + (totalHosts);
                String broadcast = baseIp + "." + (totalHosts + 1);
                String mascara = calcularMascara(p);

                // 3. REPORTE PROFESIONAL (Visualización mejorada)
                String msg = "<html><body style='width:380px; font-family:monospace; background-color:#1e1e2e; padding:12px;'>"
                        + "<div style='color:#50fa7b; border-bottom:1px solid #50fa7b; padding-bottom:5px; margin-bottom:10px;'>"
                        + "<b>[ REPORTE TÉCNICO DE SEGMENTACIÓN ]</b></div>"
                        + "<div style='color:#f8f8f2; line-height:1.5;'>"
                        + "<span style='color:#bd93f9;'>IP BASE:</span> " + ip + " /" + p + "<br>"
                        + "<span style='color:#bd93f9;'>MÁSCARA:</span> " + mascara + "<br><br>"
                        + "<b style='color:#f1fa8c;'>ESTRUCTURA DEL SEGMENTO:</b><br>"
                        + "• NETWORK ID:    " + networkId + "<br>"
                        + "• GATEWAY (GW):  " + gateway + "<br>"
                        + "• RANGO HOSTS:   " + gateway + " - " + lastHost + "<br>"
                        + "• BROADCAST:     " + broadcast + "<br>"
                        + "<hr style='border:0.5px dashed #444;'>"
                        + "<span style='color:#50fa7b;'>CAPACIDAD TOTAL:</span> " + totalHosts + " nodos útiles<br>"
                        + "</div></body></html>";

                JOptionPane.showMessageDialog(null, msg, "Resultado de Ingeniería", JOptionPane.PLAIN_MESSAGE);
                
                // Esto es lo que se guardará en el TXT cuando des clic en "Exportar Data"
                this.registro = String.format("Red: %s/%d | GW: %s | Hosts: %d", ip, p, gateway, totalHosts);

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Error: Ingrese valores numéricos válidos.");
            }
        }
    }

    // Método para validar que la IP tenga sentido (0-255)
    private boolean validarIP(String ip) {
        try {
            String[] parts = ip.split("\\.");
            if (parts.length != 4) return false;
            for (String s : parts) {
                int i = Integer.parseInt(s);
                if (i < 0 || i > 255) return false;
            }
            return true;
        } catch (Exception e) { return false; }
    }

    // Calcula la máscara decimal basada en el prefijo CIDR
    private String calcularMascara(int p) {
        if (p >= 24) return "255.255.255." + (256 - (int) Math.pow(2, 32 - p));
        if (p >= 16) return "255.255." + (256 - (int) Math.pow(2, 24 - p)) + ".0";
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
        return "Módulo Redes -> " + registro;
    }
}
