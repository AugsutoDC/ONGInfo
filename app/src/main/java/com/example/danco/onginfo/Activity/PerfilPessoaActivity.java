package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Pessoa;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class PerfilPessoaActivity extends AppCompatActivity {

    private TextView txtperfilpessoanome;
    private TextView txtperfilpessoaemail;
    private Button btnperfilpessoaverongs;
    DatabaseReference firebaseDatabase;
    private FirebaseAuth autenticacao;
    private String userId;
    Pessoa pessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_pessoa);

        txtperfilpessoanome = findViewById(R.id.txtperfilpessoanome);
        txtperfilpessoaemail = findViewById(R.id.txtperfilpessoaemail);
        btnperfilpessoaverongs = findViewById(R.id.btnperfilpessoaverongs);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        getDadosPerfil();

        btnperfilpessoaverongs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listarOngs();
            }
        });
    }

    public void getDadosPerfil()
    {
        firebaseDatabase.child("pessoa").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pessoa = dataSnapshot.getValue(Pessoa.class);
                /////Toast.makeText(PerfilPessoaActivity.this, "Nome: " + pessoa.getNome() + "id: " + pessoa.getId(), Toast.LENGTH_SHORT).show();

                //pessoa.setId(userId);
                txtperfilpessoanome.setText(pessoa.getNome());
                txtperfilpessoaemail.setText(pessoa.getEmail());


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(PerfilPessoaActivity.this, "Erro inesperado!", Toast.LENGTH_SHORT).show();
                Intent intentLoginPessoa = new Intent(PerfilPessoaActivity.this, LoginPessoaActivity.class);
                startActivity(intentLoginPessoa);
            }
        });
    }

    public void listarOngs()
    {
        Intent intentLoginPessoa = new Intent(PerfilPessoaActivity.this, ListarOngsActivity.class);
        intentLoginPessoa.putExtra("pessoaPerfil",  1);
        startActivity(intentLoginPessoa);
    }
}
