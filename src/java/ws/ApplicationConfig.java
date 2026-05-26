/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ws;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author alasn
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method.
     * It is automatically populated with
     * all resources defined in the project.
     * If required, comment out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(ws.AdministradorWS.class);
        resources.add(ws.AlimentoWS.class);
        resources.add(ws.AutenticarWS.class);
        resources.add(ws.DietaAlimentoWS.class);
        resources.add(ws.DietaWS.class);
        resources.add(ws.DomicilioWS.class);
        resources.add(ws.MedicoWS.class);
        resources.add(ws.PacienteWS.class);
    }
    
}
