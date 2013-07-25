package org.keycloak.ui.example;


import java.net.URI;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@ApplicationPath("ui/api")
@Path("")
public class Admin extends javax.ws.rs.core.Application {

    private static Map<String, Application> applications = new HashMap<String, Application>();

    private static Map<String, Realm> realms = new HashMap<String, Realm>();

    private static Map<UserId, User> users = new HashMap<UserId, User>();

    @DELETE
    @Path("applications/{id}")
    public void deleteApplication(@PathParam("id") String id) {
        applications.remove(id);
    }

    @DELETE
    @Path("realms/{id}")
    public void deleteRealm(@PathParam("id") String id) {
        realms.remove(id);
        users.remove(id);
    }

    @GET
    @Path("applications/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Application getApplication(@PathParam("id") String id) {
        return applications.get(id);
    }

    @GET
    @Path("applications")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Application> getApplications() {
        return new LinkedList<Application>(applications.values());
    }

    @GET
    @Path("realms/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Realm getRealm(@PathParam("id") String id) {
        return realms.get(id);
    }


    @GET
    @Path("realms")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Realm> getRealms() {
        return new LinkedList<Realm>(realms.values());
    }

    @POST
    @Path("applications")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Application application) {
        String id = UUID.randomUUID().toString();
        application.setId(id);
        applications.put(id, application);
        return Response.created(URI.create("/applications/" + id)).build();
    }

    @POST
    @Path("realms")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Realm realm) {
        String id = UUID.randomUUID().toString();
        realm.setId(id);
        realms.put(id, realm);
        return Response.created(URI.create("/realms/" + id)).build();
    }

    @PUT
    @Path("applications/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(@PathParam("id") String id, Application application) {
        applications.put(id, application);
    }

    @PUT
    @Path("realms/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(@PathParam("id") String id, Realm realm) {
        realms.put(id, realm);
    }

    @GET
    @Path("realms/{realm}/users/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser(@PathParam("realm") String realm, @PathParam("id") String id) {
        return users.get(new UserId(realm, id));
    }

    @GET
    @Path("realms/{realm}/users")
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(@PathParam("realm") String realm) {
        LinkedList<User> list = new LinkedList<User>();
        for (Entry<UserId, User> e : users.entrySet()) {
            if (e.getKey().getRealm().equals(realm)) {
                list.add(e.getValue());
            }
        }
        return list;
    }

    @PUT
    @Path("realms/{realm}/users/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void save(@PathParam("realm") String realm, @PathParam("id") String id, User user) {
        users.put(new UserId(realm, id), user);
    }

    @DELETE
    @Path("realms/{realm}/users/{id}")
    public void deleteUser(@PathParam("realm") String realm, @PathParam("id") String id) {
        users.remove(new UserId(realm, id));
    }

}