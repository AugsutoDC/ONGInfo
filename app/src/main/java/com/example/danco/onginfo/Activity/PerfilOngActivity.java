package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Evento;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.Entidades.Pessoa;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PerfilOngActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;
    private String userId;
    private Ong ong;
    private EditText etxtperfilongnome;
    private EditText etxtperfilongsobre;
    private EditText etxtperfilongtelefone;
    private EditText etxtperfilonglocal;
    private Button btnperfilongsalvar;
    private ListView lvperfilonglistview;
    private List<Evento> listEvento = new ArrayList<Evento>();
    private ArrayAdapter<Evento> arrayAdapterEvento;
    private ImageView imgperfilongaddevento;
    DatabaseReference firebaseDatabase;
    Evento eventoSelecionado;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_ong);

        etxtperfilongnome = findViewById(R.id.etxtperfilongnome);
        etxtperfilongsobre = findViewById(R.id.etxtperfilongsobre);
        etxtperfilongtelefone = findViewById(R.id.etxtperfilongtelefone);
        etxtperfilonglocal = findViewById(R.id.etxtperfilonglocal);
        btnperfilongsalvar = findViewById(R.id.btnperfilongsalvar);
        lvperfilonglistview = findViewById(R.id.lvperfilonglistview);
        imgperfilongaddevento = findViewById(R.id.imgperfilongaddevento);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        getDadosPerfil();

        lvperfilonglistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                eventoSelecionado = (Evento)parent.getItemAtPosition(position);
                abrirEditEvento(eventoSelecionado);
            }
        });
        /*
        lvperfilonglistview.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                eventoSelecionado = (Evento)parent.getItemAtPosition(position);
                Toast.makeText(PerfilOngActivity.this, "Evento selecionado: " + eventoSelecionado.getTitulo(), Toast.LENGTH_SHORT).show();
                abrirEditEvento(eventoSelecionado);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

        btnperfilongsalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                atualizarDadosPerfil();
                ///atualizarListView();
            }
        });
       //// ong.setNome(databaseReference.child("nome").getDatabase().toString());

        imgperfilongaddevento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentong = new Intent(PerfilOngActivity.this, CadastroEventoActivity.class);
                startActivity(intentong);
            }
        });

    }

    public void abrirEditEvento(Evento eventoAtual)
    {
        Intent intentEvento = new Intent(PerfilOngActivity.this, CadastroEventoActivity.class);
        intentEvento.putExtra("eventoId",  eventoAtual.getId());
        startActivity(intentEvento);
    }

    public void getDadosPerfil()
    {
        firebaseDatabase.child("ong").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ong = dataSnapshot.getValue(Ong.class);
                //Toast.makeText(PerfilOngActivity.this, "Nome: " + ong.getNome(), Toast.LENGTH_SHORT).show();

                ong.setId(userId.toString());

                etxtperfilongnome.setText(ong.getNome());
                etxtperfilongsobre.setText(ong.getSobre());
                etxtperfilongtelefone.setText(ong.getTelefone());
                etxtperfilonglocal.setText(ong.getLocal());


                atualizarListView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void atualizarDadosPerfil()
    {
        ong.setNome(etxtperfilongnome.getText().toString().trim());
        ong.setLocal(etxtperfilonglocal.getText().toString().trim());
        ong.setSobre(etxtperfilongsobre.getText().toString().trim());
        ong.setTelefone(etxtperfilongtelefone.getText().toString().trim());

        firebaseDatabase.child("ong").child(ong.getId()).setValue(ong);
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
                arrayAdapterEvento = new ArrayAdapter<Evento>(PerfilOngActivity.this,android.R.layout.simple_list_item_1,listEvento);
                lvperfilonglistview.setAdapter(arrayAdapterEvento);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
