package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Evento;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.SimpleDateFormat;

public class CadastroEventoActivity extends AppCompatActivity {

    private EditText etxtcadeventodata;
    private EditText etxtcadeventotitulo;
    private EditText etxtcadeventoendereco;
    private EditText etxtcadeventodescricao;
    private Button btncadeventovoltar;
    private Button btncadeventosalvar;
    private Evento evento;
    private FirebaseAuth autenticacao;
    private String userId;
    DatabaseReference firebaseDatabase;
    ConstraintLayout cadeventolayer;
    private ImageButton ibtncadeventoremover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_evento);

        etxtcadeventodata = findViewById(R.id.etxtcadeventodata);
        etxtcadeventotitulo = findViewById(R.id.etxtcadeventotitulo);
        etxtcadeventoendereco = findViewById(R.id.etxtcadeventoendereco);
        etxtcadeventodescricao = findViewById(R.id.etxtcadeventodescricao);
        btncadeventovoltar = findViewById(R.id.btncadeventovoltar);
        btncadeventosalvar = findViewById(R.id.btncadeventosalvar);
        cadeventolayer = findViewById(R.id.cadeventolayer);
        ibtncadeventoremover = findViewById(R.id.ibtncadeventoremover);

        cadeventolayer.setVisibility(View.GONE);

        //Pegando o firebase e o id da ong logada
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        Bundle extras = getIntent().getExtras();
        String eventoId;
        if(extras != null && extras.containsKey("eventoId")) {
            eventoId = extras.getString("eventoId");
            firebaseDatabase.child("eventos").child(userId).child(eventoId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    evento = dataSnapshot.getValue(Evento.class);
                    etxtcadeventodata.setText(evento.getData());
                    etxtcadeventotitulo.setText(evento.getTitulo());
                    etxtcadeventoendereco.setText(evento.getEndereco());
                    etxtcadeventodescricao.setText(evento.getDescricao());

                    cadeventolayer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        
        //voltar para a tela de perfil da ong
        btncadeventovoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retornarPerfil();
            }
        });

        //salvar evento
        btncadeventosalvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                evento = new Evento();
                salvarEvento();
                retornarPerfil();
            }
        });

        ibtncadeventoremover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(CadastroEventoActivity.this, "Entrei no evento de click ", Toast.LENGTH_SHORT).show();
                removerEvento();
            }
        });
    }

    public void salvarEvento()
    {
        evento.setOngId(userId);
        evento.setData(etxtcadeventodata.getText().toString().trim());
        evento.setTitulo(etxtcadeventotitulo.getText().toString().trim());
        evento.setEndereco(etxtcadeventoendereco.getText().toString().trim());
        evento.setDescricao(etxtcadeventodescricao.getText().toString().trim());

        Bundle extras = getIntent().getExtras();
        String eventoId;
        if(extras != null && extras.containsKey("eventoId")) {
            eventoId = extras.getString("eventoId");
        }else{
            eventoId = gerarEventoId();
        }
        evento.setId(eventoId);

        firebaseDatabase.child("eventos").child(evento.getOngId()).child(evento.getId()).setValue(evento);
    }

    public void retornarPerfil()
    {
        Intent intentong = new Intent(CadastroEventoActivity.this, PerfilOngActivity.class);
        startActivity(intentong);
    }

    public String gerarEventoId()
    {
        SimpleDateFormat idEvento = new SimpleDateFormat("ddMMyyyyHHmmss");
        String eventoId = idEvento.toString();
        eventoId = eventoId.replace("/","");
        eventoId = eventoId.replace(":","");
        eventoId = eventoId.replace(" ","");
        eventoId = Base64Custom.codificadorBase64(eventoId);

        return eventoId;
    }

    public void removerEvento()
    {
        evento.setOngId(userId);
        firebaseDatabase.child("eventos").child(evento.getOngId()).child(evento.getId()).removeValue();
        retornarPerfil();
    }
}
