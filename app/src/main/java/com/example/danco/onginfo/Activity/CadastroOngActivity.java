package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
import com.example.danco.onginfo.Entidades.Ong;
import com.example.danco.onginfo.Entidades.Pessoa;
import com.example.danco.onginfo.Helper.Base64Custom;
import com.example.danco.onginfo.Helper.Preferencias;
import com.example.danco.onginfo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;

public class CadastroOngActivity extends AppCompatActivity {

    private EditText etxtcadnomeong;
    private EditText etxtcademailong;
    private EditText etxtcadsenhaong;
    private EditText etxtcadconfirmarsenhaong;
    private Button btncadconfirmarong;
    private Button btncadcancelarong;
    private Ong ong;
    private FirebaseAuth autenticacao;
    private EditText etxtcadcnpjong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_ong);


        etxtcadnomeong = findViewById(R.id.etxtcadnomeong);
        etxtcademailong = findViewById(R.id.etxtcademailong);
        etxtcadsenhaong = findViewById(R.id.etxtcadsenhaong);
        etxtcadconfirmarsenhaong = findViewById(R.id.etxtcadconfirmarsenhaong);
        btncadconfirmarong = findViewById(R.id.btncadconfirmarong);
        btncadcancelarong = findViewById(R.id.btncadcancelarong);
        etxtcadcnpjong = findViewById(R.id.etxtcadcnpjong);

        btncadcancelarong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelarCadastroOng = new Intent(CadastroOngActivity.this, LoginOngActivity.class);
                startActivity(intentCancelarCadastroOng);
            }
        });

        btncadconfirmarong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CadastroOngActivity.this,"Nome: " + etxtcadnomeong.getText().toString() + "Email: " + etxtcademailong.getText().toString() + "Senha: " + etxtcadsenhaong.getText().toString(), Toast.LENGTH_SHORT).show();
                if(etxtcadsenhaong.getText().toString().equals(etxtcadconfirmarsenhaong.getText().toString())){

                    ong = new Ong();
                    ong.setNome(etxtcadnomeong.getText().toString());
                    ong.setEmail(etxtcademailong.getText().toString());
                    ong.setSenha(etxtcadsenhaong.getText().toString());
                    ong.setCpf(etxtcadcnpjong.getText().toString());

                    cadastrarOng();

                }else{
                    Toast.makeText(CadastroOngActivity.this,"As senhas não são correspondentes", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void cadastrarOng(){
        Toast.makeText(CadastroOngActivity.this,"Nome: " + ong.getNome() + "Email: " + ong.getEmail() + "Senha: " + ong.getSenha(), Toast.LENGTH_SHORT).show();
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                ong.getEmail(),ong.getSenha()
        ).addOnCompleteListener(CadastroOngActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroOngActivity.this, "Usuário cadastrado com sucesse", Toast.LENGTH_SHORT).show();
                    String identificadorUsuario = Base64Custom.codificadorBase64(ong.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    ong.setId(identificadorUsuario);;
                    ong.salvar();

                    Preferencias preferencias = new Preferencias(CadastroOngActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, ong.getNome());

                    abrirLoginOng();

                }else {
                    String errorExcecao = "";

                    try{
                        throw  task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        errorExcecao = " Digite uma senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        errorExcecao = " O e-mail digitado é inválido, digite um novo e-mail";
                    }catch (FirebaseAuthUserCollisionException e){
                        errorExcecao = " Esse e-mail já está cadastrado no sistema";
                    }catch (Exception e){
                        errorExcecao = " Err ao efetuar o cadastro!";
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroOngActivity.this, "Erro: " + errorExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });

    }


    public void abrirLoginOng(){
        Intent intent = new Intent(CadastroOngActivity.this, LoginOngActivity.class);
        startActivity(intent);
    }
}
