package com.fireabaseapp.wanderson_jackson.todoz;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import com.google.firebase.auth.FirebaseAuth;

public class EsqueciSenhaActivity extends AppCompatActivity {

    private EditText edRecuperarSenha;
    private AppCompatButton btnRecuperarSenha;
    private FirebaseAuth auth;
    private ProgressBar barraProgresso;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esqueci_senha);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        auth = FirebaseAuth.getInstance();

        barraProgresso = (ProgressBar) findViewById(R.id.progressBarEsqueciSenha);
        edRecuperarSenha = (EditText) findViewById(R.id.ed_recuperar_senha);
        btnRecuperarSenha = (AppCompatButton) findViewById(R.id.btn_recuperar_senha);
        btnRecuperarSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recuperarSenha();
            }
        });
    }

    //method forgotPassword

    public void recuperarSenha(){
        String email_recuperar = edRecuperarSenha.getText().toString().trim();
        if (TextUtils.isEmpty(email_recuperar)){
            Toast.makeText(getApplicationContext(), R.string.digite_email, Toast.LENGTH_LONG).show();
            return;
        }

        barraProgresso.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email_recuperar)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), R.string.enviamos_email, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), R.string.falha_no_envio, Toast.LENGTH_LONG).show();
                        }
                        barraProgresso.setVisibility(View.GONE);
                    }
                });
    }

}
