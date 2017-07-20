package com.fireabaseapp.wanderson_jackson.todoz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.fireabaseapp.wanderson_jackson.todoz.Adapters.RecyclerviewAdapter;
import com.fireabaseapp.wanderson_jackson.todoz.Objeto.TaskObject;
import com.fireabaseapp.wanderson_jackson.todoz.listener.ItemListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TodoListActivity extends AppCompatActivity implements View.OnClickListener , ItemListener {
    private Dialog dialogAddTarefa;
    private EditText edTarefa;
    private RecyclerView recyclerView;
    private List<TaskObject> taskObjectList;
    private LinearLayoutManager linearLayoutManager;

    //firebase database
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private DatabaseReference ref;
    private DatabaseReference deleteRef;

    FirebaseAuth auth;

    private ProgressBar progressBar;
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_list);


        //get firebase auth instance
        auth = FirebaseAuth.getInstance();

        //get current user
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(TodoListActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.task_title);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progress_todo);
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }


        database = FirebaseDatabase.getInstance();


        recyclerView  = (RecyclerView) findViewById(R.id.recycler_view_todo);

        taskObjectList = new ArrayList<>();

        linearLayoutManager = new  LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        final RecyclerviewAdapter adapter;

        adapter = new RecyclerviewAdapter(taskObjectList,this, this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarTarefaDialog();
            }
        });


        ref = database.getReference().child("TaskObject");
        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TaskObject taskObj = dataSnapshot.getValue(TaskObject.class);
                taskObjectList.add(taskObj);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d("ITEM: ", "DELETADO");

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            signOut();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void adicionarTarefaDialog(){
        dialogAddTarefa = new Dialog(this);
        dialogAddTarefa.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAddTarefa.setContentView(R.layout.dialog_todo_layout);
        dialogAddTarefa.setCanceledOnTouchOutside(false);
        edTarefa = (EditText) dialogAddTarefa.findViewById(R.id.ed_campo_tarefa);
        dialogAddTarefa.findViewById(R.id.button_cancelar_dialog).setOnClickListener(this);
        dialogAddTarefa.findViewById(R.id.btn_adicionar_tarefa_dialog).setOnClickListener(this);
        dialogAddTarefa.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_adicionar_tarefa_dialog:
                adiconarTarefa();
                dialogAddTarefa.dismiss();
                break;
            case R.id.button_cancelar_dialog:
                dialogAddTarefa.dismiss();
                break;
            default:
                break;

        }
    }

    //metodo para adicionar tarefas
    public void adiconarTarefa(){
        String tarefa = edTarefa.getText().toString();
        if (TextUtils.isEmpty(tarefa)){
            Toast.makeText(getApplicationContext(), "voce nao adicionou nada!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.texto_teste), Toast.LENGTH_LONG).show();
            reference = database.getReference().child("TaskObject").push();
            TaskObject taskObject = new TaskObject();
            taskObject.setKey(reference.getKey());
            taskObject.setTask(edTarefa.getText().toString());                                      //pega o que foi digitado no campo e transforma em strings
            reference.setValue(taskObject);

        }
    }

    //metodo para adicionar tarefas
    public void editarTarefa(TaskObject object){
        String tarefa = object.getKey();
        if (TextUtils.isEmpty(tarefa)){
            Toast.makeText(getApplicationContext(), "voce nao adicionou nada!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(),getString(R.string.texto_teste), Toast.LENGTH_LONG).show();
            reference = database.getReference().child("TaskObject").push();
            TaskObject taskObject = new TaskObject();
            taskObject.setKey(reference.getKey());
            taskObject.setTask(edTarefa.getText().toString());                                      //pega o que foi digitado no campo e transforma em strings
            reference.setValue(taskObject);

        }
    }


    //sign out method
    public void signOut() {
        auth.signOut();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    //implementação de metodo via interface, que sera utilizado em outra classe, o RecyclerAdapter
    @Override
    public void onDelete(TaskObject object) {
        //reference.child("key").removeValue();
        Toast.makeText(getApplicationContext(), "TASK: " + object.getTask(), Toast.LENGTH_SHORT).show();
        //deleteRef.child("key").removeValue();
       // taskObjectList.remove(taskObjectList);//remove do recyclerview
        //notify();
    }

    @Override
    public void onOpenDialog(TaskObject taskObject) {
        adicionarTarefaDialog();//teste method
    }


}
