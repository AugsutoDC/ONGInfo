package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginOngActivity extends AppCompatActivity {

    private EditText etxtloginemailong;
    private EditText etxtloginsenhaong;
    private Button btnlogarong;
    private FirebaseAuth autenticacao;
    private Ong ong;
    private Button btnnewloginong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_ong);

        etxtloginemailong = findViewById(R.id.etxtloginemailong);
        etxtloginsenhaong = findViewById(R.id.etxtloginsenhaong);
        btnlogarong = findViewById(R.id.btnlogarong);
        btnnewloginong = findViewById(R.id.btnnewloginong);

        btnnewloginong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCadastroPessoa = new Intent(LoginOngActivity.this, CadastroOngActivity.class);
                startActivity(intentCadastroPessoa);
            }
        });

        btnlogarong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etxtloginemailong.getText().toString().equals("") && !etxtloginsenhaong.getText().toString().equals(""))
                {
                    ong = new Ong();
                    ong.setEmail(etxtloginemailong.getText().toString());
                    ong.setSenha(etxtloginsenhaong.getText().toString());

                    validarLogin();
                }else{
                    Toast.makeText(LoginOngActivity.this, "Preencha os campos de e-mail e senha!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void validarLogin() {

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.signInWithEmailAndPassword(ong.getEmail(), ong.getSenha()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    abrirPerfilOng();
                    Toast.makeText(LoginOngActivity.this, "Login efetuado com sucesso", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(LoginOngActivity.this, "Usuário ou senha invalidos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void abrirPerfilOng(){
        Intent intentAbrirTelaListagemOng = new Intent(LoginOngActivity.this, PerfilOngActivity.class);
        startActivity(intentAbrirTelaListagemOng);
    }
}
