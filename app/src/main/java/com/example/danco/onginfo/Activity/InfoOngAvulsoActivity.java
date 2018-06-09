package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Evento;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.ListarEventosActivity;
import com.example.danco.onginfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class InfoOngAvulsoActivity extends AppCompatActivity {

    private TextView txtinfoongnome;
    private TextView txtinfoongtelefone;
    private TextView txtinfoonglocal;
    private TextView txtinfoongsobre;
    private Button btninfoongvoltar;
    private FirebaseAuth autenticacao;
    private String userId;
    DatabaseReference firebaseDatabase;
    private Ong ong;
    private Button btninfoongeventos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_ong_avulso);

        txtinfoongnome = findViewById(R.id.txtinfoongnome);
        txtinfoongtelefone = findViewById(R.id.txtinfoongtelefone);
        txtinfoonglocal = findViewById(R.id.txtinfoonglocal);
        txtinfoongsobre = findViewById(R.id.txtinfoongsobre);
        btninfoongvoltar = findViewById(R.id.btninfoongvoltar);
        btninfoongeventos = findViewById(R.id.btninfoongeventos);

        //Pegando o firebase e o id da ong logada
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        Bundle extras = getIntent().getExtras();
        String ongId;
        if(extras != null && extras.containsKey("ongId")) {
            ongId = extras.getString("ongId");
            firebaseDatabase.child("ong").child(userId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    ong = dataSnapshot.getValue(Ong.class);
                    txtinfoongnome.setText(ong.getNome());
                    txtinfoongtelefone.setText(ong.getTelefone());
                    txtinfoonglocal.setText(ong.getLocal());
                    txtinfoongsobre.setText(ong.getSobre());

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    voltarListaOngs();
                }
            });
        }

        btninfoongvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voltarListaOngs();
            }
        });

        btninfoongeventos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verListaEventos();
            }
        });
    }

    public void voltarListaOngs()
    {
        Intent intentOng = new Intent(InfoOngAvulsoActivity.this, ListarOngsActivity.class);
        startActivity(intentOng);
    }

    public void verListaEventos()
    {
        Intent intentEvento = new Intent(InfoOngAvulsoActivity.this, ListarEventosActivity.class);
        intentEvento.putExtra("ongId",  ong.getId());
        startActivity(intentEvento);
    }
}
