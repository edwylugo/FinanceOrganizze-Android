package edwy.lugo.organizze.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import edwy.lugo.organizze.R;
import edwy.lugo.organizze.config.ConfiguracaoFirebase;
import edwy.lugo.organizze.helper.Base64Custom;
import edwy.lugo.organizze.helper.DateCustom;
import edwy.lugo.organizze.model.Movimentacao;
import edwy.lugo.organizze.model.Usuario;

public class ReceitasActivity extends AppCompatActivity {

    private TextInputEditText campoData, campoCategoria, campoDescricao;
    private EditText campoValor;
    private Movimentacao movimentacao;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Double receitaTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receitas);


        campoValor = findViewById(R.id.editValor);
        campoData = findViewById(R.id.editData);
        campoCategoria = findViewById(R.id.editCategoria);
        campoDescricao = findViewById(R.id.editDescricao);

        //Preenche o campo data com a date atual
        campoData.setText(DateCustom.dataAtual());

        recuperarReceitaTotal();

    }

    public void salvarReceita(View view) {

        if (validarCamposReceita()){
            movimentacao = new Movimentacao();
            String data = campoData.getText().toString();
            Double valorRecuperado = Double.parseDouble(campoValor.getText().toString());

            movimentacao.setValor(valorRecuperado);
            movimentacao.setCategoria(campoCategoria.getText().toString());
            movimentacao.setDescricao(campoDescricao.getText().toString());
            movimentacao.setData(data);
            movimentacao.setTipo("r");


            Double receitaAtualizada = receitaTotal + valorRecuperado;
            atualizarReceita(receitaAtualizada);


            movimentacao.salvar(data);
            finish();
        }
    }

    public Boolean validarCamposReceita() {

        String textoValor = campoValor.getText().toString();
        String textoData = campoData.getText().toString();
        String textoCategoria = campoCategoria.getText().toString();
        String textoDescricao = campoDescricao.getText().toString();

        if ( !textoValor.isEmpty()){

            if ( !textoData.isEmpty()){

                if ( !textoCategoria.isEmpty()){

                    if ( !textoDescricao.isEmpty()){

                        return true;

                    } else {
                        Toast.makeText(ReceitasActivity.this, "Descrição não foi preenchido!", Toast.LENGTH_SHORT).show();
                        return false;

                    }

                } else {
                    Toast.makeText(ReceitasActivity.this, "Categoria não foi preenchido!", Toast.LENGTH_SHORT).show();
                    return false;

                }

            } else {
                Toast.makeText(ReceitasActivity.this, "Data não foi preenchido!", Toast.LENGTH_SHORT).show();
                return false;

            }

        } else {
            Toast.makeText(ReceitasActivity.this, "Valor não foi preenchido!", Toast.LENGTH_SHORT).show();
            return false;

        }

    }

    public void recuperarReceitaTotal(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);

        usuarioRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Usuario usuario = dataSnapshot.getValue(Usuario.class);
                receitaTotal= usuario.getReceitaTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void atualizarReceita(Double receita){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.codificarBase64(emailUsuario);
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(idUsuario);


        usuarioRef.child("receitaTotal").setValue(receita);


    }
}
