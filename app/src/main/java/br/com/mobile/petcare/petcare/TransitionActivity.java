package br.com.mobile.petcare.petcare;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import br.com.mobile.petcare.petcare.modelo.Cliente;
import br.com.mobile.petcare.petcare.modelo.DAO;
import br.com.mobile.petcare.petcare.modelo.Pet;

public class TransitionActivity extends AppCompatActivity {

    ImageButton irClienteButton;
    ImageButton irPetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transition);



        irPetButton = (ImageButton) findViewById(R.id.irPetsButton);
        irClienteButton = (ImageButton) findViewById(R.id.irClientesButton);

        irPetButton.getBackground().setColorFilter(getResources().getColor(R.color.no), PorterDuff.Mode.MULTIPLY);
        irClienteButton.getBackground().setColorFilter(getResources().getColor(R.color.no), PorterDuff.Mode.MULTIPLY);
        /*TextView teste = (TextView) findViewById(R.id.testeteste);

        List<Pet> pets = dao.selectPet();
        String s = "";

        for (Pet pet : pets){
            s += "Pet: " + pet.getNome() + "  " + pet.
        }*/

    }

    public void irClientes(View view) {
        Intent intent = new Intent(getThis(), ConsultaActivity.class);
        startActivity(intent);
    }

    public void irPets(View view) {
        Intent intent = new Intent(getThis(), ConsultaPetActivity.class);
        startActivity(intent);
    }

    private Activity getThis() {
        return this;
    }
}
