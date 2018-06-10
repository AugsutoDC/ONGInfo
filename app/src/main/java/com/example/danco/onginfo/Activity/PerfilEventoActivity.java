package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Evento;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilEventoActivity extends AppCompatActivity {
    private TextView txtperfileventodescricao;
    private TextView txtperfileventoendereco;
    private TextView txtperfileventodata;
    private TextView txtperfileventonome;
    private TextView txtperfileventotitulo;
    private FirebaseAuth autenticacao;
    private String userId;
    DatabaseReference firebaseDatabase;
    String eventoId;
    String ongId;
    Evento evento;
    private Button btnperfileventovoltar;
    Ong ong;
    int pessoaPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_evento);

        txtperfileventonome = findViewById(R.id.txtperfileventonome);
        txtperfileventodescricao = findViewById(R.id.txtperfileventodescricao);
        txtperfileventoendereco = findViewById(R.id.txtperfileventoendereco);
        txtperfileventodata = findViewById(R.id.txtperfileventodata);
        txtperfileventotitulo = findViewById(R.id.txtperfileventotitulo);
        btnperfileventovoltar = findViewById(R.id.btnperfileventovoltar);

        //Pegando o firebase e o id da ong logada
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("eventoId")  && extras.containsKey("ongId")) {
            eventoId = extras.getString("eventoId");
            ongId = extras.getString("ongId");
            getDadosPerfil();

        }else{
            ongId = extras.getString("ongId");
            voltarListaEventos();
        }

        if(extras != null && extras.containsKey("pessoaPerfil")) {
            pessoaPerfil = extras.getInt("pessoaPerfil");
        }else{
            pessoaPerfil = 2;
        }

        btnperfileventovoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarListaEventos();
            }
        });
    }

    public void exibirEvento()
    {
        firebaseDatabase.child("eventos").child(ongId).child(eventoId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                evento = dataSnapshot.getValue(Evento.class);
                txtperfileventodata.setText(evento.getData());
                txtperfileventotitulo.setText(evento.getTitulo());
                txtperfileventoendereco.setText(evento.getEndereco());
                txtperfileventodescricao.setText(evento.getDescricao());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                voltarListaEventos();
            }
        });
    }

    public void getDadosPerfil()
    {
        firebaseDatabase.child("ong").child(ongId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ong = dataSnapshot.getValue(Ong.class);
                ///Toast.makeText(PerfilEventoActivity.this, "Nome: " + ong.getNome(), Toast.LENGTH_SHORT).show();
                txtperfileventonome.setText(ong.getNome().toUpperCase());
                exibirEvento();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void voltarListaEventos()
    {
        Intent intentEvento = new Intent(PerfilEventoActivity.this, ListarEventosActivity.class);
        intentEvento.putExtra("ongId",  ongId);
        intentEvento.putExtra("pessoaPerfil",  pessoaPerfil);
        startActivity(intentEvento);
    }
}
