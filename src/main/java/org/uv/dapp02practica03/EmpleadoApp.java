package org.uv.dapp02practica03;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class EmpleadoApp {
    private JFrame frame;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtClave, txtNombre, txtDireccion, txtTelefono;
    private EmpleadoDAO empleadoDAO;

    public EmpleadoApp() {
        empleadoDAO = new EmpleadoDAO();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Gestión de Empleados");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        tableModel = new DefaultTableModel(new Object[]{"Clave", "Nombre", "Dirección", "Teléfono"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        txtClave = new JTextField(5);
        txtNombre = new JTextField(10);
        txtDireccion = new JTextField(10);
        txtTelefono = new JTextField(10);
        
        JButton btnAgregar = new JButton("Agregar");
        JButton btnEliminar = new JButton("Eliminar");
        JButton btnBuscar = new JButton("Buscar por ID");
        JButton btnMostrarTodos = new JButton("Mostrar Todos");

        panel.add(new JLabel("Clave:"));
        panel.add(txtClave);
        panel.add(new JLabel("Nombre:"));
        panel.add(txtNombre);
        panel.add(new JLabel("Dirección:"));
        panel.add(txtDireccion);
        panel.add(new JLabel("Teléfono:"));
        panel.add(txtTelefono);
        panel.add(btnAgregar);
        panel.add(btnEliminar);
        panel.add(btnBuscar);
        panel.add(btnMostrarTodos);
        
        frame.add(panel, BorderLayout.SOUTH);

        btnAgregar.addActionListener(e -> agregarEmpleado());
        btnEliminar.addActionListener(e -> eliminarEmpleado());
        btnBuscar.addActionListener(e -> buscarEmpleadoPorId());
        btnMostrarTodos.addActionListener(e -> cargarEmpleados());

        cargarEmpleados();
        frame.setVisible(true);
    }

    private void cargarEmpleados() {
        tableModel.setRowCount(0);
        List<EmpleadoPojo> empleados = empleadoDAO.buscarAll();
        for (EmpleadoPojo emp : empleados) {
            tableModel.addRow(new Object[]{emp.getClave(), emp.getNombre(), emp.getDireccion(), emp.getTelefono()});
        }
    }

    private void agregarEmpleado() {
        EmpleadoPojo nuevo = new EmpleadoPojo();
        nuevo.setNombre(txtNombre.getText());
        nuevo.setDireccion(txtDireccion.getText());
        nuevo.setTelefono(txtTelefono.getText());
        empleadoDAO.guardar(nuevo);
        cargarEmpleados();
    }

    private void eliminarEmpleado() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            Long clave = (Long) tableModel.getValueAt(selectedRow, 0);
            empleadoDAO.eliminar(clave);
            cargarEmpleados();
        }
    }

    private void buscarEmpleadoPorId() {
        try {
            Long id = Long.parseLong(txtClave.getText());
            EmpleadoPojo empleado = empleadoDAO.buscarById(id);
            tableModel.setRowCount(0);
            if (empleado != null) {
                tableModel.addRow(new Object[]{empleado.getClave(), empleado.getNombre(), empleado.getDireccion(), empleado.getTelefono()});
            } else {
                JOptionPane.showMessageDialog(frame, "Empleado no encontrado", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "ID no válido", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EmpleadoApp::new);
    }
}
