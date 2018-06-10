package com.example.danco.onginfo.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Toolbar;

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

import java.util.ArrayList;
import java.util.List;

public class ListarOngsActivity extends AppCompatActivity {

    private ListView lvlistongsallongs;
    DatabaseReference firebaseDatabase;
    private FirebaseAuth autenticacao;
    private String userId;
    private List<Ong> listOng = new ArrayList<Ong>();
    private ArrayAdapter<Ong> arrayAdapterOng;
    Ong ongSelecionado;
    private Button btnlistongvoltar;
    int pessoaPerfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ongs);

        lvlistongsallongs = findViewById(R.id.lvlistongsallongs);
        btnlistongvoltar = findViewById(R.id.btnlistongvoltar);
        toolbarInfoOng = findViewById(R.id.toolbar_info_ong);




        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        ////userId = Base64Custom.codificadorBase64(autenticacao.getCurrentUser().getEmail());

        firebaseDatabase = ConfiguracaoFirebase.getFirebase();

        atualizarListView();

        lvlistongsallongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ongSelecionado = (Ong)parent.getItemAtPosition(position);
                ////Toast.makeText(ListarOngsActivity.this, "Selecionado " + ongSelecionado.getNome(), Toast.LENGTH_SHORT).show();
                abrirInfoOng(ongSelecionado);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null && extras.containsKey("pessoaPerfil")) {
            pessoaPerfil = extras.getInt("pessoaPerfil");
        }else{
            pessoaPerfil = 2;
        }


        btnlistongvoltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pessoaPerfil == 1)
                {
                    voltarPerfilPessoa();
                }else {
                    voltarTelaMenu();
                }
            }
        });

    }


    public void atualizarListView()
    {
        firebaseDatabase.child("ong").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listOng.clear();
                //Toast.makeText(ListarOngsActivity.this, "Entrei no da listview ", Toast.LENGTH_SHORT).show();
                for (DataSnapshot objSnapshot:dataSnapshot.getChildren())
                {
                    Ong ong = objSnapshot.getValue(Ong.class);
                    listOng.add(ong);
                    //Toast.makeText(ListarOngsActivity.this, "For listview " + ong.getNome(), Toast.LENGTH_SHORT).show();
                }
                arrayAdapterOng = new ArrayAdapter<Ong>(ListarOngsActivity.this,android.R.layout.simple_list_item_1,listOng);
                ///Toast.makeText(ListarOngsActivity.this, "Sair do for e to no array adapter " , Toast.LENGTH_SHORT).show();
                lvlistongsallongs.setAdapter(arrayAdapterOng);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void abrirInfoOng(Ong ongAtual)
    {
        Intent intentOng = new Intent(ListarOngsActivity.this, InfoOngAvulsoActivity.class);
        intentOng.putExtra("ongId",  ongAtual.getId());
        intentOng.putExtra("pessoaPerfil",  pessoaPerfil);
        startActivity(intentOng);
    }

    public void voltarTelaMenu()
    {
        Intent intentMenu = new Intent(ListarOngsActivity.this, StartPerfilActivity.class);
        startActivity(intentMenu);
    }
    public void voltarPerfilPessoa()
    {
        Intent intentMenu = new Intent(ListarOngsActivity.this, PerfilPessoaActivity.class);
        startActivity(intentMenu);
    }
}
