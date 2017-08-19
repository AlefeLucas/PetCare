package br.com.mobile.petcare.petcare;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import br.com.mobile.petcare.petcare.modelo.DAO;
import br.com.mobile.petcare.petcare.modelo.Usuario;

public class NewUserActivity extends AppCompatActivity {

    Button cadastroButton;
    EditText emailET;
    EditText senhaET;
    EditText confirmET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        cadastroButton = (Button) findViewById(R.id.buttonCadastrarUsuario);
        emailET = (EditText) findViewById(R.id.editTextEmailCadastro);
        senhaET = (EditText) findViewById(R.id.editTextSenhaCadastro);
        confirmET = (EditText) findViewById(R.id.editTextConfirmarSenha);
        cadastroButton.getBackground().setColorFilter(getResources().getColor(R.color.buttons), PorterDuff.Mode.MULTIPLY);

        emailET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(new DAO(getThis()).emailExists(s.toString() + "@petcare.com")){
                    emailET.setError("Email existente");
                    cadastroButton.setEnabled(false);
                } else {
                    cadastroButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        cadastroButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText[] texts = new EditText[]{emailET, senhaET, confirmET};

                boolean vazio = false;

                for (EditText et : texts) {
                    if (et.getText().toString().isEmpty()) {
                        et.setError("Campo obrigatório");
                        vazio = true;
                    }
                }


                if (!vazio) {
                    String email = emailET.getText().toString();
                    String senha = senhaET.getText().toString();
                    String confirm = confirmET.getText().toString();

                    if (!senha.equals(confirm)) {
                        senhaET.setError("Senhas diferentes");
                        confirmET.setError("Senhas diferentes");
                    } else {
                        email += "@petcare.com";
                        Usuario usuario = new Usuario(email, senha);

                        DAO dao = new DAO(getThis());

                        dao.insertUsuario(usuario);

                        Toast.makeText(getThis(), "Usuário cadastrado com sucesso", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });

    }

    private Activity getThis(){
        return this;
    }

}
