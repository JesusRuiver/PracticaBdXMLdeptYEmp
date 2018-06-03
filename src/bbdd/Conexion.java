package bbdd;

import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.XPathQueryService;

public class Conexion {
	
	private String driver = "org.exist.xmldb.DatabaseImpl";
    private String URI = "xmldb:exist://localhost:8081/exist/xmlrpc/db/empresa";
    private String user = "admin";
    private String pass = "admin";
    
    private XPathQueryService servicio;
    private Collection coleccion;
    
    
	public Conexion() {
		
	}
    
	public Collection conectar() {

        try {
            Class cl = Class.forName(driver);
            Database database = (Database) cl.newInstance();
            DatabaseManager.registerDatabase(database);
            coleccion = DatabaseManager.getCollection(URI, user, pass);
            
            servicio = (XPathQueryService) coleccion.getService("XPathQueryService", "1.0");
            
            return coleccion;
        } catch (XMLDBException e) {
            System.out.println("Error al inicializar la BD eXist.");
        } catch (ClassNotFoundException e) {
            System.out.println("Error en el driver.");
        } catch (InstantiationException e) {
            System.out.println("Error al instanciar la BD.");
        } catch (IllegalAccessException e) {
            System.out.println("Error al instanciar la BD.");
        }
        return null;
    }

	public XPathQueryService getServicio() {
		
		return servicio;
	}
    
	

}
