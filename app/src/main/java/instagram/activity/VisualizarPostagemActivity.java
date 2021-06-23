package instagram.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.instagram.cursoandroid.jamiltondamasceno.instagram.R;

import de.hdodenhof.circleimageview.CircleImageView;
import instagram.model.Postagem;
import instagram.model.Usuario;

public class VisualizarPostagemActivity extends AppCompatActivity {

    private TextView textPerfilPostagem,textQtdCurtidasPostagem,
            textDescricaoPostagem,textVisualizarComentariosPostagem;
    private ImageView imagePostagemSelecionada;
    private CircleImageView imagePerfilPostagem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_postagem);


        inicializarComponentes();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Visualizar postagem");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);


        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){

            Postagem postagem = (Postagem) bundle.getSerializable("postagem");
            Usuario usuario = (Usuario) bundle.getSerializable("usuario");


            Uri uri = Uri.parse( usuario.getCaminhoFoto() );
            Glide.with(VisualizarPostagemActivity.this)
                    .load( uri )
                    .into( imagePerfilPostagem );
            textPerfilPostagem.setText( usuario.getNome() );


            Uri uriPostagem = Uri.parse( postagem.getCaminhoFoto() );
            Glide.with(VisualizarPostagemActivity.this)
                    .load( uriPostagem )
                    .into( imagePostagemSelecionada );
            textDescricaoPostagem.setText( postagem.getDescricao() );

        }

    }

    private void inicializarComponentes(){
        textPerfilPostagem = findViewById(R.id.textPerfilPostagem);
        textQtdCurtidasPostagem = findViewById(R.id.textQtdCurtidasPostagem);
        textDescricaoPostagem = findViewById(R.id.textDescricaoPostagem);
        imagePostagemSelecionada = findViewById(R.id.imagePostagemSelecionada);
        imagePerfilPostagem = findViewById(R.id.imagePerfilPostagem);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }

}
