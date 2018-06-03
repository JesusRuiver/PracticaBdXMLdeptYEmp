package ventanas;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

import bbdd.Conexion;
import utilidades.ExtractXML;
import utilidades.BuildXML;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;

public class Ejercicio1PracticaCbox extends JFrame {

	private JPanel contentPane;

	private JTextField txtNumeroDept;
	private JTextField txtNombreDept;
	private JTextField txtLocalidadDept;

	JButton btnAnterior = new JButton("Anterior");
	JButton btnSiguiente = new JButton("Siguiente");
	
	JComboBox cboxDepartamento = new JComboBox();

	private Conexion miConexionXML = new Conexion();
	
	private int id;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Ejercicio1PracticaCbox frame = new Ejercicio1PracticaCbox();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Ejercicio1PracticaCbox() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 468, 332);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JButton btnAlta = new JButton("Alta");

		btnAlta.setBounds(10, 210, 89, 23);
		contentPane.add(btnAlta);

		JButton btnBaja = new JButton("Baja");

		btnBaja.setBounds(115, 210, 89, 23);
		contentPane.add(btnBaja);

		JButton btnModificacion = new JButton("Modificacion");

		btnModificacion.setBounds(214, 210, 100, 23);
		contentPane.add(btnModificacion);

		JButton btnLimpiar = new JButton("Limpiar");

		btnLimpiar.setBounds(335, 210, 89, 23);
		contentPane.add(btnLimpiar);

		txtNumeroDept = new JTextField();
		txtNumeroDept.setBounds(150, 77, 128, 20);
		contentPane.add(txtNumeroDept);
		txtNumeroDept.setColumns(10);

		txtNombreDept = new JTextField();
		txtNombreDept.setColumns(10);
		txtNombreDept.setBounds(150, 121, 170, 20);
		contentPane.add(txtNombreDept);

		txtLocalidadDept = new JTextField();
		txtLocalidadDept.setColumns(10);
		txtLocalidadDept.setBounds(150, 161, 251, 20);
		contentPane.add(txtLocalidadDept);

		JButton btnConsultar = new JButton("Consultar");
		btnConsultar.setBounds(312, 76, 89, 23);
		contentPane.add(btnConsultar);

		JLabel lbGentionDep = new JLabel("GESTI\u00D3N DE DEPARTAMENTOS");
		lbGentionDep.setBounds(34, 29, 209, 14);
		contentPane.add(lbGentionDep);

		JLabel lbNDept = new JLabel("N\u00BA Departamento:");
		lbNDept.setBounds(10, 80, 126, 14);
		contentPane.add(lbNDept);

		JLabel lbNombre = new JLabel("Nombre:");
		lbNombre.setBounds(10, 124, 89, 14);
		contentPane.add(lbNombre);

		JLabel lbLocalidad = new JLabel("Localidad:");
		lbLocalidad.setBounds(10, 164, 89, 14);
		contentPane.add(lbLocalidad);

		JButton btnAnterior = new JButton("Anterior");

		btnAnterior.setBounds(96, 244, 89, 23);
		contentPane.add(btnAnterior);

		JButton btnSiguiente = new JButton("Siguiente");

		btnSiguiente.setBounds(247, 244, 89, 23);
		contentPane.add(btnSiguiente);
		
		
		
		cboxDepartamento.setBounds(96, 49, 182, 20);
		contentPane.add(cboxDepartamento);

		miConexionXML.conectar();
		
		rellenaComboDepartamentos();
		
		cboxDepartamento.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				String depYid = cboxDepartamento.getSelectedItem().toString().trim();
				
				String [] parteId = depYid.trim().split("- ");
				
				String idStr = parteId[1];
				
				id = Integer.parseInt(idStr);
				
				System.out.println(idStr);
			}
		});

		btnConsultar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

					consutarDepartamento(id);
				
			}
		});

		btnSiguiente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (txtNumeroDept.getText().equals("")) {

					txtNumeroDept.setText("0");
					nextDepartamento();

				} else {
					nextDepartamento();
				}

			}

		});

		btnAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtNumeroDept.getText().equals("")) {

					txtNumeroDept.setText("0");
					prevDepartamento();

				} else {
					prevDepartamento();
				}

				
			}
		});

		btnModificacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				modificarDepartamento();
			}
		});

		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				limpiar();
			}
		});

		btnBaja.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				borrarDepartamento(Integer.parseInt(txtNumeroDept.getText()));

			}
		});

		btnAlta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				crearDepartamento();
			}
		});
	}
	
	private void rellenaComboDepartamentos() {
		
		try {

			ResourceSet result = miConexionXML.getServicio().query("/departamentos/DEP_ROW/concat(DNOMBRE, \" - \", DEPT_NO)");
			//ResourceSet result = miConexionXML.getServicio().query("/departamentos/DEP_ROW/DEPT_NO/text()");
			//ResourceSet result = miConexionXML.getServicio().query("/departamentos/DEP_ROW/DEPT_NO/text() | /departamentos/DEP_ROW/DNOMBRE/text()");
			
			ResourceIterator i;
			i = result.getIterator();
			
			while(i.hasMoreResources()) {
				
				Resource r = i.nextResource();
				cboxDepartamento.addItem(r.getContent());
			}
			
		} catch (XMLDBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void consutarDepartamento(int id) {
		try {

			txtNombreDept.setText("");
			txtLocalidadDept.setText("");

			// labelMensajes.setText("");

			ResourceSet result = miConexionXML.getServicio().query("/departamentos/DEP_ROW[DEPT_NO = " + id + "]");

			ResourceIterator i;
			i = result.getIterator();

			if (!i.hasMoreResources()) {
				JOptionPane.showMessageDialog(null, "El departamento con id " + id + " no existe.");
			} else {
				Resource r = i.nextResource();
				mostrarDepartamento((String) r.getContent());
			}

			// col.close();
		} catch (XMLDBException e) {
			System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
			e.printStackTrace();
		}
	}

	private void mostrarDepartamento(String DEP_ROWxml) {

		ExtractXML xml = new ExtractXML(DEP_ROWxml);

		txtNumeroDept.setText(xml.getField("DEPT_NO"));
		txtNombreDept.setText(xml.getField("DNOMBRE"));
		txtLocalidadDept.setText(xml.getField("LOC"));

		checkFirstLastDepartamento();

		// labelMensajes.setText("");
	}

	private void modificarDepartamento() {
		try {
			int id = Integer.parseInt(txtNumeroDept.getText());
			if (existsDepartamento(id)) {

				String replacement = BuildXML.createDepartamento(id, txtNombreDept.getText(),
						txtLocalidadDept.getText());
				miConexionXML.getServicio()
						.query("update replace /departamentos/DEP_ROW[DEPT_NO = " + id + "] with " + replacement);

				JOptionPane.showMessageDialog(null, "Departamento " + id + " modificado.");
			} else {
				JOptionPane.showMessageDialog(null, "El departamento " + id + " no existe. No se puede modificar");
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private void borrarDepartamento(int id) {
		try {

			if (existsDepartamento(id)) {

				miConexionXML.getServicio().query("update delete /departamentos/DEP_ROW[DEPT_NO = " + id + "]");

				JOptionPane.showMessageDialog(null, "Departamento " + id + " eliminado.");

				limpiar();
			} else {
				JOptionPane.showMessageDialog(null, "El departamento " + id + " no se puede borrar, no existe.");
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private void crearDepartamento() {
		try {
			int id = Integer.parseInt(txtNumeroDept.getText());
			if (!existsDepartamento(id)) {

				String depart = BuildXML.createDepartamento(id, txtNombreDept.getText(), txtLocalidadDept.getText());
				miConexionXML.getServicio().query("update insert " + depart + " into /departamentos");

				JOptionPane.showMessageDialog(null, "Departamento " + id + " guardado.");
			} else {
				JOptionPane.showMessageDialog(null,
						"El departamento " + id + " ya existe. No se puede volver a insertar");
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private boolean existsDepartamento(int id) {
		try {

			ResourceSet result = miConexionXML.getServicio().query("/departamentos/DEP_ROW[DEPT_NO = " + id + "]");
			ResourceIterator i = result.getIterator();
			if (!i.hasMoreResources()) {
				return false;
			} else {
				return true;
			}
		} catch (XMLDBException e) {
			System.out.println(" ERROR AL CONSULTAR DOCUMENTO.");
			e.printStackTrace();
		}
		return false;
	}

	private void nextDepartamento() {
		try {
			ResourceSet result = miConexionXML.getServicio()
					.query("(for $dep in /departamentos/DEP_ROW" + " where number($dep/DEPT_NO) > "
							+ (Integer.parseInt(txtNumeroDept.getText()) + " order by number($dep/DEPT_NO) "
									+ " return $dep)[1]"));
			ResourceIterator i;
			i = result.getIterator();
			if (i.hasMoreResources()) {
				Resource r = i.nextResource();
				mostrarDepartamento((String) r.getContent());
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private void prevDepartamento() {
		try {
			ResourceSet result = miConexionXML.getServicio()
					.query("(for $dep in /departamentos/DEP_ROW" + " where number($dep/DEPT_NO) < "
							+ (Integer.parseInt(txtNumeroDept.getText()) + " order by number($dep/DEPT_NO) descending"
									+ " return $dep)[1]"));
			ResourceIterator i;
			i = result.getIterator();
			if (i.hasMoreResources()) {
				Resource r = i.nextResource();
				mostrarDepartamento((String) r.getContent());
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private void checkFirstLastDepartamento() {

		btnAnterior.setEnabled(true);
		btnSiguiente.setEnabled(true);
		try {
			// PREV
			ResourceSet prev = miConexionXML.getServicio().query(
					"/departamentos/DEP_ROW[number(DEPT_NO) < " + (Integer.parseInt(txtNumeroDept.getText()) + "]"));
			if (prev.getSize() == 0) {
				btnAnterior.setEnabled(false);
			}

			// NEXT
			ResourceSet next = miConexionXML.getServicio().query(
					"/departamentos/DEP_ROW[number(DEPT_NO) > " + (Integer.parseInt(txtNumeroDept.getText()) + "]"));
			if (next.getSize() == 0) {
				btnSiguiente.setEnabled(false);
			}
		} catch (XMLDBException e) {
			e.printStackTrace();
		}
	}

	private void limpiar() {
		txtNumeroDept.setText("");
		txtNombreDept.setText("");
		txtLocalidadDept.setText("");
	}
}
