package com.fireabaseapp.wanderson_jackson.todoz;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

//LOGIN
public class LoginActivity extends AppCompatActivity {
                                                                                                    //Atributos de view
    private EditText edEmail;
    private EditText edSenha;
    FloatingActionButton fabCriarConta;
    private AppCompatButton btnLogin;
    private AppCompatButton btnEsqueciSenha;
    private ProgressBar progress_bar_login;

    FirebaseAuth auth;                                                                              //firebase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();                                                          //get firebase instance

        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, LoginActivity.class));
            finish();
        }

        setContentView(R.layout.actvity_login);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.btn_login);
        setSupportActionBar(toolbar);

        edEmail = (EditText) findViewById(R.id.ed_email_login);
        edSenha = (EditText) findViewById(R.id.ed_senha_login);
        btnLogin = (AppCompatButton) findViewById(R.id.btn_login);
        btnEsqueciSenha = (AppCompatButton) findViewById(R.id.btn_esqueci_senha);
        progress_bar_login = (ProgressBar) findViewById(R.id.progress_bar_login);

        auth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginConta();                                                                       //method that todo: loggin user
            }
        });



        btnEsqueciSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {                                                  //opens the todo: @EsqueciSenhaActivity
                startActivity(new Intent(LoginActivity.this, EsqueciSenhaActivity.class));
            }
        });

        fabCriarConta = (FloatingActionButton) findViewById(R.id.fab);                              //opens the todo: create account activity
        fabCriarConta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(LoginActivity.this, CriarContaActivity.class));
            }
        });
    }

    public void loginConta(){
        String email = edEmail.getText().toString();
        final String senha = edSenha.getText().toString();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), R.string.digite_email, Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(senha)){
            Toast.makeText(getApplicationContext(), R.string.informe_senha, Toast.LENGTH_LONG).show();
            return;
        }
        progress_bar_login.setVisibility(View.VISIBLE);
        auth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress_bar_login.setVisibility(View.GONE);
                        if(!task.isSuccessful()){
                            if (edSenha.length() < 6){
                                edSenha.setError(getString(R.string.minimo_senha));
                            }else{
                                Toast.makeText(LoginActivity.this, getString(R.string.falha_auth), Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Intent intent = new Intent(LoginActivity.this, TodoListActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }



}
