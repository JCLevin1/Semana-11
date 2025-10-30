import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Prueba extends JFrame {

    // Array fijo y ArrayList para almacenar pacientes
    private Patient[] patientsArray = new Patient[100];
    private int arrayIndex = 0;
    private ArrayList<Patient> patientList = new ArrayList<>();

    // Componentes de la UI
    private JTextField tfNombre, tfId, tfEdad;
    private JCheckBox cbConsulta, cbUrgencia, cbVacuna, cbExamenes;
    private DefaultListModel<String> listModel;
    private JList<String> jlPacientes;
    private JLabel lblPrecio;

    public Prueba() {
        initUI();
    }

    private void initUI() {
        setTitle("Registro de Pacientes");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8,8));

        // Panel izquierdo: formulario
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4,4,4,4);
        c.anchor = GridBagConstraints.WEST;

        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Nombre:"), c);
        c.gridx = 1;
        tfNombre = new JTextField(15);
        form.add(tfNombre, c);

        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("ID:"), c);
        c.gridx = 1;
        tfId = new JTextField(10);
        form.add(tfId, c);

        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Edad:"), c);
        c.gridx = 1;
        tfEdad = new JTextField(4);
        form.add(tfEdad, c);

        // Servicios
        c.gridx = 0; c.gridy = 3; c.gridwidth = 2;
        JPanel servicios = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        cbConsulta = new JCheckBox("Consulta ($50)");
        cbUrgencia = new JCheckBox("Urgencia ($100)");
        cbVacuna = new JCheckBox("Vacuna ($30)");
        cbExamenes = new JCheckBox("Exámenes ($75)");
        servicios.add(cbConsulta);
        servicios.add(cbUrgencia);
        servicios.add(cbVacuna);
        servicios.add(cbExamenes);
        form.add(servicios, c);

        // Botones
        c.gridy = 4; c.gridwidth = 1; c.gridx = 0;
        JButton btnCalcular = new JButton("Calcular precio");
        form.add(btnCalcular, c);

        c.gridx = 1;
        JButton btnRegistrar = new JButton("Registrar paciente");
        form.add(btnRegistrar, c);

        // Precio mostrado
        c.gridx = 0; c.gridy = 5; c.gridwidth = 2;
        lblPrecio = new JLabel("Precio: $0.00");
        form.add(lblPrecio, c);

        add(form, BorderLayout.WEST);

        // Panel derecho: lista de pacientes
        listModel = new DefaultListModel<>();
        jlPacientes = new JList<>(listModel);
        JScrollPane sc = new JScrollPane(jlPacientes);
        sc.setPreferredSize(new Dimension(350, 300));
        add(sc, BorderLayout.CENTER);

        // Acciones
        btnCalcular.addActionListener(e -> {
            double precio = calcularPrecioSeleccionado();
            lblPrecio.setText(String.format("Precio: $%.2f", precio));
        });

        btnRegistrar.addActionListener(e -> registrarPaciente());

        // Doble clic en la lista para ver detalles
        jlPacientes.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int i = jlPacientes.locationToIndex(e.getPoint());
                    if (i >= 0 && i < patientList.size()) {
                        Patient p = patientList.get(i);
                        JOptionPane.showMessageDialog(Prueba.this, p.details(), "Detalles del paciente", JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });

        pack();
        setLocationRelativeTo(null);
    }

    private double calcularPrecioSeleccionado() {
        double total = 0.0;
        if (cbConsulta.isSelected()) total += 50.0;
        if (cbUrgencia.isSelected()) total += 100.0;
        if (cbVacuna.isSelected()) total += 30.0;
        if (cbExamenes.isSelected()) total += 75.0;
        return total;
    }

    private void registrarPaciente() {
        String nombre = tfNombre.getText().trim();
        String id = tfId.getText().trim();
        String edadTxt = tfEdad.getText().trim();

        if (nombre.isEmpty() || id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre e ID son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int edad = 0;
        try {
            if (!edadTxt.isEmpty()) edad = Integer.parseInt(edadTxt);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Edad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        double precio = calcularPrecioSeleccionado();

        Patient p = new Patient(nombre, id, edad, cbConsulta.isSelected(), cbUrgencia.isSelected(), cbVacuna.isSelected(), cbExamenes.isSelected(), precio);

        // Añadir a ArrayList
        patientList.add(p);

        // Añadir al array si hay espacio
        if (arrayIndex < patientsArray.length) {
            patientsArray[arrayIndex++] = p;
        } else {
            JOptionPane.showMessageDialog(this, "Arreglo lleno: paciente agregado sólo al ArrayList.", "Aviso", JOptionPane.WARNING_MESSAGE);
        }

        listModel.addElement(p.summary());

        // Limpiar campos
        tfNombre.setText(""); tfId.setText(""); tfEdad.setText("");
        cbConsulta.setSelected(false); cbUrgencia.setSelected(false); cbVacuna.setSelected(false); cbExamenes.setSelected(false);
        lblPrecio.setText("Precio: $0.00");

        JOptionPane.showMessageDialog(this, String.format("Paciente registrado. Precio: $%.2f", precio));
    }

    // Clase interna Patient
    private static class Patient {
        String nombre;
        String id;
        int edad;
        boolean consulta, urgencia, vacuna, examenes;
        double precio;

        Patient(String nombre, String id, int edad, boolean consulta, boolean urgencia, boolean vacuna, boolean examenes, double precio) {
            this.nombre = nombre;
            this.id = id;
            this.edad = edad;
            this.consulta = consulta;
            this.urgencia = urgencia;
            this.vacuna = vacuna;
            this.examenes = examenes;
            this.precio = precio;
        }

        String summary() {
            return String.format("%s (ID:%s) - $%.2f", nombre, id, precio);
        }

        String details() {
            StringBuilder sb = new StringBuilder();
            sb.append("Nombre: ").append(nombre).append('\n');
            sb.append("ID: ").append(id).append('\n');
            sb.append("Edad: ").append(edad).append('\n');
            sb.append("Servicios: ");
            ArrayList<String> s = new ArrayList<>();
            if (consulta) s.add("Consulta");
            if (urgencia) s.add("Urgencia");
            if (vacuna) s.add("Vacuna");
            if (examenes) s.add("Exámenes");
            sb.append(s.isEmpty() ? "(ninguno)" : String.join(", ", s)).append('\n');
            sb.append(String.format("Precio: $%.2f", precio));
            return sb.toString();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Prueba().setVisible(true));
    }
}
