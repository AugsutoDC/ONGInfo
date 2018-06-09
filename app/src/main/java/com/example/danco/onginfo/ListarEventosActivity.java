package com.example.danco.onginfo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.danco.onginfo.Activity.CadastroEventoActivity;
import com.example.danco.onginfo.Activity.InfoOngAvulsoActivity;
import com.example.danco.onginfo.Activity.PerfilOngActivity;
import com.example.danco.onginfo.Activity.TelaPrincipalActivity;
import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Evento;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListarEventosActivity extends AppCompatActivity {

    private TextView txtlisteventosnomeong;
    private Button btnlisteventosvoltar;
    private ListView lvlisteventos;
    private FirebaseAuth autenticacao;
    private String userId;
    DatabaseReference firebaseDatabase;
    Ong ong;
    private List<Evento> listEvento = new ArrayList<Evento>();
    private ArrayAdapter<Evento> arrayAdapterEvento;
    Evento eventoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_eventos);

        lvlisteventos = findViewById(R.id.lvlisteventos);
        btnlisteventosvoltar = findViewById(R.id.btnlisteventosvoltar);
        txtlisteventosnomeong = findViewById(R.id.txtlisteventosnomedaong);

        //Pegando o firebase e o id da ong logada
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        Bundle extras = getIntent().getExtras();
        String ongId;
        if(extras != null && extras.containsKey("ongId")) {
            ongId = extras.getString("ongId");

            getDadosPerfil(ongId);
        }else{
            voltarInfoOng();
        }

        btnlisteventosvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarInfoOng();
            }
        });

        lvlisteventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventoSelecionado = (Evento)parent.getItemAtPosition(position);
                abrirEditEvento(eventoSelecionado);
            }
        });

    }

    public void getDadosPerfil(String ongId)
    {
        firebaseDatabase.child("ong").child(ongId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ong = dataSnapshot.getValue(Ong.class);
                //Toast.makeText(PerfilOngActivity.this, "Nome: " + ong.getNome(), Toast.LENGTH_SHORT).show();
                txtlisteventosnomeong.setText(ong.getNome().toUpperCase());
                atualizarListView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarListView()
    {
        firebaseDatabase.child("eventos").child(ong.getId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listEvento.clear();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren())
                {
                    Evento evento = objSnapshot.getValue(Evento.class);
                    listEvento.add(evento);
                }
                arrayAdapterEvento = new ArrayAdapter<Evento>(ListarEventosActivity.this,android.R.layout.simple_list_item_1,listEvento);
                lvlisteventos.setAdapter(arrayAdapterEvento);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void voltarInfoOng()
    {
        Intent intentEvento = new Intent(ListarEventosActivity.this, InfoOngAvulsoActivity.class);
        intentEvento.putExtra("ongId",  ong.getId());
        startActivity(intentEvento);
    }

    public void abrirEditEvento(Evento eventoAtual)
    {
        Intent intentEvento = new Intent(ListarEventosActivity.this, PerfilEventoActivity.class);
        intentEvento.putExtra("eventoId",  eventoAtual.getId());
        intentEvento.putExtra("ongId",  ong.getId());
        startActivity(intentEvento);
    }
}
