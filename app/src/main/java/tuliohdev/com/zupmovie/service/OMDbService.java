package tuliohdev.com.zupmovie.service;

import org.androidannotations.annotations.rest.Accept;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.MediaType;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;

import tuliohdev.com.zupmovie.model.Movie;

/**
 * Created by tulio on 1/26/16.
 */
@Rest(rootUrl = "http://www.omdbapi.com",
        converters = { GsonHttpMessageConverter.class, StringHttpMessageConverter.class     })
@Accept(MediaType.APPLICATION_JSON)
public interface OMDbService  extends RestClientErrorHandling {

    @Get("/?t={movieName}&r=json")
    Movie buscarFilmePorNome(String movieName);
}
