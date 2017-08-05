package com.fireabaseapp.wanderson_jackson.todoz.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fireabaseapp.wanderson_jackson.todoz.Interfaces.RecyclerViewClickListener;
import com.fireabaseapp.wanderson_jackson.todoz.Objeto.TaskObject;
import com.fireabaseapp.wanderson_jackson.todoz.R;
import com.fireabaseapp.wanderson_jackson.todoz.listener.ItemListener;

import java.util.List;

/**
 * Created by wjackson on 7/15/2017.
 */

public class RecyclerviewAdapter extends RecyclerView.Adapter<RecyclerviewAdapter.MyViewHolder> {


    private List<TaskObject> taskObjectList;
    private LayoutInflater layoutInflater;
    private ItemListener listener;                                                            // instancia do listener criado como interface para ser utilizado aqui nesta classe adapter
    private RecyclerViewClickListener recyclerViewClickListener;

    public RecyclerviewAdapter(List<TaskObject> taskObjectList, ItemListener listener, Context context) {//todos o parametros adicionados no construtor inclusive o listener
        this.taskObjectList = taskObjectList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.listener = listener;                                                                   //item construction
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.task_list_item_object, parent, false);          //setando o layout
        MyViewHolder mvh = new MyViewHolder(view);                                                  //criando o objeto da MyViewHolder
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.txtObject.setText(taskObjectList.get(position).getTask());                   //setando no holder no texto baseado na posicao


        //acao executada apartir de
        holder.cardList.setOnLongClickListener(new View.OnLongClickListener() {                 //baseado no click longo do item, vai executar de abrir menu d econtexto
            @Override
            public boolean onLongClick(final View v) {
                final TaskObject task = taskObjectList.get(position);                              //pega a posicao do task na lista de objetos
                //System.out.print("item da lista: " + p);
                //opcao utilizada no menu de contexto
                final CharSequence[] items = {"Editar", "Excluir"};

                //criacao do menu de contexto
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

                builder.setTitle(R.string.selecione_action);                                        //titulo do menu de contexto

                builder.setItems(items, new DialogInterface.OnClickListener() {                     //set o item para quando for selecionado via position chamar sus respectiva acao
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 0:
                                listener.onOpenDialog(task);//metodo para chamada de dialog teste, o correto seria abrir modal com dados preenchidos
                                Toast.makeText(v.getContext(), "Editou:  " + task.getTask(), Toast.LENGTH_SHORT).show();
                                break;
                            case 1:

                                Toast.makeText(v.getContext(), "Excluiu " + task.getTask(), Toast.LENGTH_SHORT).show();
                                listener.onDelete(task);
                                //preparar metodo que notifica a exclusao
                                break;
                            default:
                                break;
                        }

                    }
                });
                builder.show();

                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return taskObjectList.size();                                                               //pegando o tanho da lista de objeto
    }

    /*Implement clicks in this class, then verify if instance of recyclerview is null or not and
    use methods of instance*/
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {                                            //classe intenar que serve para pegar itens da view

        private TextView txtObject;
        private CardView cardList;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtObject = (TextView) itemView.findViewById(R.id.task_list_item);
            cardList = (CardView) itemView.findViewById(R.id.card_list);
        }

        @Override
        public void onClick(View v) {
            if (recyclerViewClickListener != null) {
                recyclerViewClickListener.OnClickListener(v, getAdapterPosition());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            if (recyclerViewClickListener != null) {
                recyclerViewClickListener.OnLongClickListener(v, getAdapterPosition());
                return true;
            } else {
                return false;
            }
        }
    }

    public void setRecyclerViewClickListener(RecyclerViewClickListener r) {
        this.recyclerViewClickListener = r;
    }


}
