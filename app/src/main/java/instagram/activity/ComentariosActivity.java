package instagram.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.instagram.cursoandroid.jamiltondamasceno.instagram.R;

import java.util.ArrayList;
import java.util.List;

import instagram.adapter.AdapterComentario;
import instagram.helper.ConfiguracaoFirebase;
import instagram.helper.UsuarioFirebase;
import instagram.model.Comentario;
import instagram.model.Usuario;

public class ComentariosActivity extends AppCompatActivity {

    private EditText editComentario;
    private RecyclerView recyclerComentarios;

    private String idPostagem;
    private Usuario usuario;
    private AdapterComentario adapterComentario;
    private List<Comentario> listaComentarios = new ArrayList<>();

    private DatabaseReference firebaserRef;
    private DatabaseReference comentariosRef;
    private ValueEventListener valueEventListenerComentarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);


        editComentario = findViewById(R.id.editComentario);
        recyclerComentarios = findViewById(R.id.recyclerComentarios);


        usuario = UsuarioFirebase.getDadosUsuarioLogado();
        firebaserRef = ConfiguracaoFirebase.getFirebase();


        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Comentários");
        setSupportActionBar( toolbar );

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);


        adapterComentario = new AdapterComentario(listaComentarios, getApplicationContext() );
        recyclerComentarios.setHasFixedSize( true );
        recyclerComentarios.setLayoutManager(new LinearLayoutManager(this));
        recyclerComentarios.setAdapter( adapterComentario );


        Bundle bundle = getIntent().getExtras();
        if( bundle != null ){
            idPostagem = bundle.getString("idPostagem");
        }

    }

    private void recuperarComentarios(){

        comentariosRef = firebaserRef.child("comentarios")
                .child( idPostagem );
        valueEventListenerComentarios = comentariosRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listaComentarios.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    listaComentarios.add( ds.getValue(Comentario.class) );
                }
                adapterComentario.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarComentarios();
    }

    @Override
    protected void onStop() {
        super.onStop();
        comentariosRef.removeEventListener( valueEventListenerComentarios );
    }

    public  void salvarComentario(View view){

        String textoComentario = editComentario.getText().toString();
        if( textoComentario != null && !textoComentario.equals("") ){

            Comentario comentario = new Comentario();
            comentario.setIdPostagem( idPostagem );
            comentario.setIdUsuario( usuario.getId() );
            comentario.setNomeUsuario( usuario.getNome() );
            comentario.setCaminhoFoto( usuario.getCaminhoFoto() );
            comentario.setComentario( textoComentario );
            if(comentario.salvar()){
                Toast.makeText(this,
                        "Comentário salvo com sucesso!",
                        Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(this,
                    "Insira o comentário antes de salvar!",
                    Toast.LENGTH_SHORT).show();
        }


        editComentario.setText("");

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
