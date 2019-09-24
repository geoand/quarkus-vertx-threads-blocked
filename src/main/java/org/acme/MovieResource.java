package org.acme;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/movie")
public class MovieResource {

    private static List<Movie> MOVIES = new ArrayList<>(100);

    static {
        for (int i = 0; i < 100; i++) {
            MOVIES.add(new Movie(i+1, "title" + i, "rating" + i, i));
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Movie> all() {
        return MOVIES;
    }
}
