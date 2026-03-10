import javax.swing.*;
import java.awt.*;

public class ModuloFisica extends ModuloAsistente {
    private String registro = "Pendiente";

    public ModuloFisica() {
        super("Física");
    }

    @Override
    public void ejecutar() {
        String[] ops = { "Gravedad", "Velocidad", "Distancia", "Energía C.", "Energía P." };
        int sel = JOptionPane.showOptionDialog(null, "Seleccione análisis:", "LABORATORIO CUN",
                0, JOptionPane.PLAIN_MESSAGE, null, ops, ops[0]);

        if (sel == -1)
            return;

        try {
            double res = 0;
            String u = "", t = ops[sel], c = "#8e44ad";
            JPanel formulario = new JPanel(new GridLayout(0, 2, 10, 10));

            switch (sel) {
                case 0: // GRAVEDAD
                    JTextField tH_G = new JTextField();
                    JTextField tT_G = new JTextField();
                    formulario.add(new JLabel("Altura (m):"));
                    formulario.add(tH_G);
                    formulario.add(new JLabel("Tiempo (s):"));
                    formulario.add(tT_G);
                    if (mostrarFormulario(formulario, t)) {
                        res = (2 * Double.parseDouble(tH_G.getText()))
                                / Math.pow(Double.parseDouble(tT_G.getText()), 2);
                        u = " m/s²";
                    } else
                        return;
                    break;

                case 1: // VELOCIDAD (Vf = V0 + g*t)
                    JTextField tV0_V = new JTextField("0");
                    JTextField tT_V = new JTextField();
                    formulario.add(new JLabel("Vel. Inicial (m/s):"));
                    formulario.add(tV0_V);
                    formulario.add(new JLabel("Tiempo (s):"));
                    formulario.add(tT_V);
                    if (mostrarFormulario(formulario, t)) {
                        res = Double.parseDouble(tV0_V.getText()) + (9.81 * Double.parseDouble(tT_V.getText()));
                        u = " m/s";
                        c = "#e67e22";
                    } else
                        return;
                    break;

                case 2: // DISTANCIA (h = V0*t + 0.5*g*t²)
                    JTextField tV0_D = new JTextField("0");
                    JTextField tT_D = new JTextField();
                    formulario.add(new JLabel("Vel. Inicial (m/s):"));
                    formulario.add(tV0_D);
                    formulario.add(new JLabel("Tiempo (s):"));
                    formulario.add(tT_D);
                    if (mostrarFormulario(formulario, t)) {
                        double v0 = Double.parseDouble(tV0_D.getText());
                        double time = Double.parseDouble(tT_D.getText());
                        res = (v0 * time) + (0.5 * 9.81 * Math.pow(time, 2));
                        u = " m";
                        c = "#3498db";
                    } else
                        return;
                    break;

                case 3: // ENERGÍA CINÉTICA (Ec = 0.5 * m * v²)
                    JTextField tM_C = new JTextField();
                    JTextField tV_C = new JTextField();
                    formulario.add(new JLabel("Masa (kg):"));
                    formulario.add(tM_C);
                    formulario.add(new JLabel("Velocidad (m/s):"));
                    formulario.add(tV_C);
                    if (mostrarFormulario(formulario, t)) {
                        res = 0.5 * Double.parseDouble(tM_C.getText())
                                * Math.pow(Double.parseDouble(tV_C.getText()), 2);
                        u = " J";
                        c = "#27ae60";
                    } else
                        return;
                    break;

                case 4: // ENERGÍA POTENCIAL (Ep = m * g * h)
                    JTextField tM_P = new JTextField();
                    JTextField tH_P = new JTextField();
                    formulario.add(new JLabel("Masa (kg):"));
                    formulario.add(tM_P);
                    formulario.add(new JLabel("Altura (m):"));
                    formulario.add(tH_P);
                    if (mostrarFormulario(formulario, t)) {
                        res = Double.parseDouble(tM_P.getText()) * 9.81 * Double.parseDouble(tH_P.getText());
                        u = " J";
                        c = "#e74c3c";
                    } else
                        return;
                    break;
            }

            // Diseño de tarjeta de resultado (No plano)
            String html = "<html><body style='width:250px; font-family:sans-serif;'>" +
                    "<div style='background:" + c
                    + "; color:white; padding:12px; text-align:center; border-radius:10px 10px 0 0;'><b>"
                    + t.toUpperCase() + "</b></div>" +
                    "<div style='background:white; border:2px solid " + c
                    + "; padding:20px; text-align:center; border-radius:0 0 10px 10px;'>" +
                    "<h1 style='margin:0; color:#333;'>" + String.format("%.2f", res) + u + "</h1></div></html>";

            JOptionPane.showMessageDialog(null, html, "Análisis Físico", JOptionPane.PLAIN_MESSAGE);
            registro = t + ": " + String.format("%.2f", res) + u;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error: Ingrese valores numéricos válidos.");
        }
    }

    private boolean mostrarFormulario(JPanel panel, String titulo) {
        int result = JOptionPane.showConfirmDialog(null, panel, "Datos para " + titulo,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        return result == JOptionPane.OK_OPTION;
    }

    @Override
    public String obtenerReporte() {
        return "Física: " + registro;
    }
}