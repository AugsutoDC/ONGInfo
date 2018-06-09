package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.danco.onginfo.DAO.ConfiguracaoFirebase;
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

public class CadastroPessoaActivity extends AppCompatActivity {

    private EditText etxtcadnomepessoa;
    private EditText etxtcademailpessoa;
    private EditText etxtloginsenhapessoa;
    private EditText etxtcadconfirmarsenhapessoa;
    private Button btncadconfirmarpessoa;
    private Button btncadcancelarpessoa;
    private Pessoa pessoas;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_pessoa);

        etxtcadnomepessoa = findViewById(R.id.etxtcadnomepessoa);
        etxtcademailpessoa = findViewById(R.id.etxtcademailpessoa);
        etxtloginsenhapessoa = findViewById(R.id.etxtloginsenhapessoa);
        etxtcadconfirmarsenhapessoa = findViewById(R.id.etxtcadconfirmarsenhapessoa);
        btncadconfirmarpessoa = findViewById(R.id.btncadconfirmarpessoa);
        btncadcancelarpessoa = findViewById(R.id.btncadcancelarpessoa);

        btncadcancelarpessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentCancelarCadastroPessoa = new Intent(CadastroPessoaActivity.this, LoginPessoaActivity.class);
                startActivity(intentCancelarCadastroPessoa);
            }
        });

        btncadconfirmarpessoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etxtloginsenhapessoa.getText().toString().equals(etxtcadconfirmarsenhapessoa.getText().toString())){

                    pessoas = new Pessoa();
                    pessoas.setNome(etxtcadnomepessoa.getText().toString());
                    pessoas.setEmail(etxtcademailpessoa.getText().toString());
                    pessoas.setSenha(etxtloginsenhapessoa.getText().toString());

                    cadastrarPessoa();

                }else{
                    Toast.makeText(CadastroPessoaActivity.this,"As senhas não são correspondentes", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void cadastrarPessoa(){

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                pessoas.getEmail(),pessoas.getSenha()
        ).addOnCompleteListener(CadastroPessoaActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(CadastroPessoaActivity.this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    String identificadorUsuario = Base64Custom.codificadorBase64(pessoas.getEmail());
                    FirebaseUser usuarioFirebase = task.getResult().getUser();
                    pessoas.setId(identificadorUsuario);;
                    pessoas.salvar();

                    Preferencias preferencias = new Preferencias(CadastroPessoaActivity.this);
                    preferencias.salvarUsuarioPreferencias(identificadorUsuario, pessoas.getNome());

                    abrirLoginPessoa();

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
                    Toast.makeText(CadastroPessoaActivity.this, "Erro: " + errorExcecao, Toast.LENGTH_LONG).show();

                }
            }
        });

    }

    public void abrirLoginPessoa(){
        Intent intent = new Intent(CadastroPessoaActivity.this, LoginPessoaActivity.class);
        startActivity(intent);
    }
}
