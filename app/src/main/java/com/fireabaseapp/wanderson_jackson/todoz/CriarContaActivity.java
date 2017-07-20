package com.fireabaseapp.wanderson_jackson.todoz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class CriarContaActivity extends AppCompatActivity {


    private EditText edEmail;                                                              //ui atributes
    private EditText edSenha;
    private AppCompatButton btnCriarConta;
    private AppCompatButton  btnLog;
    private ProgressBar progressBar;

    private FirebaseAuth auth;                                                                      //firebase atribute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_criar_conta);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);                                     //toolbars
        toolbar.setTitle(R.string.tela_criarconta);
        setSupportActionBar(toolbar);
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException exception){
            exception.printStackTrace();
        }


        auth = FirebaseAuth.getInstance();

        edEmail = (EditText) findViewById(R.id.ed_email_cadastro);
        edSenha = (EditText) findViewById(R.id.ed_senha_cadastro);
        btnCriarConta = (AppCompatButton) findViewById(R.id.btn_criar_conta_cadastro);
        btnLog= (AppCompatButton) findViewById(R.id.btn_login_open);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar_CriarConta);



        btnCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                criarConta();                                                                       //creates account for user based on the @criarConta method
            }
        });



        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                          //abrir tela de login
                startActivity(new Intent(CriarContaActivity.this, LoginActivity.class));
            }
        });

    }

   public void criarConta(){

       String email = edEmail.getText().toString().trim();
       String senha = edSenha.getText().toString().trim();

       if(TextUtils.isEmpty(email)){
           Toast.makeText(getApplicationContext(), R.string.digite_email, Toast.LENGTH_SHORT).show();
           return;
       }
       if (TextUtils.isEmpty(senha)){                                                               //em caso de campo de senha de senha vazio
          Toast.makeText(getApplicationContext(), R.string.informe_senha, Toast.LENGTH_SHORT).show();
       }
       if (senha.length()<6 ){                                                                      //informa quando a senha e muito curto
           Toast.makeText(getApplicationContext(), R.string.informe_senha_6, Toast.LENGTH_SHORT).show();
       }

       progressBar.setVisibility(View.VISIBLE);

       auth.createUserWithEmailAndPassword(email, senha)
               .addOnCompleteListener(CriarContaActivity.this, new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       Toast.makeText(CriarContaActivity.this,"Sua conta foi criada com sucesso! " + task.isSuccessful(), Toast.LENGTH_SHORT ).show();
                       progressBar.setVisibility(View.GONE);
                       if (!task.isSuccessful()){
                           Toast.makeText(CriarContaActivity.this, " Autenticação falhou "+ task.getException(), Toast.LENGTH_SHORT).show();
                       }else{
                           startActivity(new Intent(CriarContaActivity.this, LoginActivity.class));
                           finish();
                       }
                   }
               });
   }

   @Override
   protected void onResume(){
       super.onResume();
       progressBar.setVisibility(View.GONE);
   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }




}
