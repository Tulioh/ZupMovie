package tuliohdev.com.zupmovie.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.rest.RestService;

import tuliohdev.com.zupmovie.R;
import tuliohdev.com.zupmovie.adapter.MovieListAdapter;
import tuliohdev.com.zupmovie.database.MovieDAO;
import tuliohdev.com.zupmovie.helper.MoviePosterHelper;
import tuliohdev.com.zupmovie.model.Movie;
import tuliohdev.com.zupmovie.service.ErrorRest;
import tuliohdev.com.zupmovie.service.OMDbService;

@EActivity(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    private MovieDAO movieDAO;
    private ProgressDialog progressDialog;
    private MoviePosterHelper moviePosterHelper;
    private Movie filmeSelecionado;

    @ViewById
    ListView movieList;

    @RestService
    OMDbService omDbService;

    @Bean
    ErrorRest errorRest;

    @AfterInject
    void afterInject() {
        errorRest = ErrorRest.criaErrorRestDefault(getApplicationContext());
        omDbService.setRestErrorHandler(errorRest);
    }

    @AfterViews
    void afterViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        movieDAO = new MovieDAO(getApplicationContext());
        moviePosterHelper = new MoviePosterHelper(getApplicationContext());

        MovieListAdapter adapter = new MovieListAdapter(this,
                movieDAO.obterTodosFilmes() );
        movieList = (ListView) findViewById(R.id.movieList);
        movieList.setAdapter(adapter);
        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Movie movie = (Movie) movieList.getAdapter().getItem(position);
                Intent intent = new Intent(getBaseContext(), MovieDetailActivity_.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
        registerForContextMenu(movieList);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Salvando");
        progressDialog.setMessage("Aguarde...");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewMovieDialog();
            }
        });
    }

    private void showNewMovieDialog() {
        LayoutInflater li = LayoutInflater.from(MainActivity.this);
        View promptsView = li.inflate(R.layout.dialog_new_movie, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.nomeDoFilmeText);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Salvar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                final String nomeFilme = userInput.getText().toString();
                                progressDialog.show();
                                adicionarNovoFilmeEAtualizarLista(nomeFilme);
                            }
                        })
                .setNegativeButton("Cancelar",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        alertDialogBuilder.create().show();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        filmeSelecionado = (Movie) movieList.getItemAtPosition( info.position );
        menu.setHeaderTitle(filmeSelecionado.getTitle());
        menu.add(0, v.getId(), 0, "Excluir");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        movieDAO.deletar(filmeSelecionado.getImdbID());

        atualizarListaDeFilmes();

        mostrarToast("Filme excluido com sucesso!");

        return true;
    }

    @Background
    void adicionarNovoFilmeEAtualizarLista(String nome) {
        Movie movie = omDbService.buscarFilmePorNome(nome);

        if(!movie.isResponseOk()) {
            mostrarToast("Ops.. não foi encontrado nenhum filme com esse nome");
            return;
        }

        String id = movie.getImdbID();

        if(movieDAO.filmeExiste(id)) {
            mostrarToast("Ops.. esse filme já está cadastrado");
        } else {
            movie = moviePosterHelper.salvarImagemPoster(movie);
            movieDAO.inserir(movie);
            atualizarListaDeFilmes();
        }
    }

    @UiThread
    void atualizarListaDeFilmes() {
        MovieListAdapter adapter = (MovieListAdapter) movieList.getAdapter();
        adapter.updateMovieList(movieDAO.obterTodosFilmes());
        adapter.notifyDataSetChanged();
        progressDialog.dismiss();
    }

    @UiThread
    void mostrarToast(String mensagem) {
        progressDialog.dismiss();
        Toast.makeText(getApplicationContext(),
                mensagem,
                Toast.LENGTH_LONG).show();
    }
}
