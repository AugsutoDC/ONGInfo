package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Pessoa;
import com.example.danco.onginfo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginPessoaActivity extends AppCompatActivity {

    private EditText etxtloginemailpessoa;
    private EditText etxtloginsenhapessoa;
    private Button btnlogarpessoa;
    private FirebaseAuth autenticacao;
    private Pessoa pessoas;
    private Button btnnewloginpessoa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_pessoa);

        etxtloginemailpessoa = findViewById(R.id.etxtloginemailpessoa);
        etxtloginsenhapessoa = findViewById(R.id.etxtloginsenhapessoa);
        btnlogarpessoa = findViewById(R.id.btnlogarpessoa);
        btnnewloginpessoa = findViewById(R.id.btnnewloginpessoa);


        btnnewloginpessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadastroPessoa = new Intent(LoginPessoaActivity.this, CadastroPessoaActivity.class);
                startActivity(intentCadastroPessoa);
            }
        });

        btnlogarpessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etxtloginemailpessoa.getText().toString().equals("") && !etxtloginsenhapessoa.getText().toString().equals(""))
                {
                    pessoas = new Pessoa();
                    pessoas.setEmail(etxtloginemailpessoa.getText().toString());
                    pessoas.setSenha(etxtloginsenhapessoa.getText().toString());

                    validarLogin();
                }else{
                    Toast.makeText(LoginPessoaActivity.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(pessoas.getEmail(), pessoas.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    abrirTelaListagemOng();
                    Toast.makeText(LoginPessoaActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginPessoaActivity.this, "Usuário ou senha invalidos", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    public void abrirTelaListagemOng(){
        Intent intentAbrirTelaListagemOng = new Intent(LoginPessoaActivity.this, PerfilPessoaActivity.class);
        startActivity(intentAbrirTelaListagemOng);
    }

}
