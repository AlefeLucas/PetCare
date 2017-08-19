package br.com.mobile.petcare.petcare;

import android.app.AlertDialog;

import android.content.DialogInterface;
import android.content.res.ColorStateList;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.mobile.petcare.petcare.modelo.Cliente;
import br.com.mobile.petcare.petcare.modelo.DAO;
import br.com.mobile.petcare.petcare.modelo.Pet;

public class ConsultaPetActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fab;
    List<Pet> petList;
    DAO dao;

    Toolbar toolbar;
    Toolbar searchToolbar;
    Toolbar advancedToolbar;

    EditText searchET;
    Spinner donoSearchSpinner;

    String filtro;
    boolean filtrarNome;
    boolean filtrarDono;

    List<Cliente> donos;

    ArrayAdapter<String> spinnerArrayAdaper;

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("FILTRO", filtro);
        outState.putBoolean("FILTRANOME", filtrarNome);
        outState.putBoolean("FILTRADONO", filtrarDono);
        outState.putInt("SELECTEDINDEX", donoSearchSpinner.getSelectedItemPosition());


        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paciente, menu);
        searchToolbar.inflateMenu(R.menu.menu_search);
        searchToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.action_back) {
                    advancedToolbar.setVisibility(View.GONE);
                    searchToolbar.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    filtrarNome = false;
                    filtrarDono = false;
                } else if (id == R.id.action_adv) {
                    if (advancedToolbar.getVisibility() == View.GONE) {
                        advancedToolbar.setVisibility(View.VISIBLE);
                        filtrarDono = true;
                    } else {
                        advancedToolbar.setVisibility(View.GONE);
                        filtrarDono = false;
                    }
                }
                atualizaLista();
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_search) {
            filtrarNome = true;
            searchToolbar.setVisibility(View.VISIBLE);
            getSupportActionBar().hide();
            atualizaLista();
        }


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta_pet);
        dao = new DAO(getThis());

        searchET = (EditText) findViewById(R.id.searchET);
        donoSearchSpinner = (Spinner) findViewById(R.id.spinnerDonoSearch);

        donos = dao.selectCliente();
        String[] nomeDonos = new String[donos.size()];
        for (int x = 0; x < nomeDonos.length; x++) {
            nomeDonos[x] = donos.get(x).getNome();
        }

        spinnerArrayAdaper = new ArrayAdapter<String>(getThis(), android.R.layout.simple_spinner_item, nomeDonos);

        donoSearchSpinner.setAdapter(spinnerArrayAdaper);

        donoSearchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                atualizaLista();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        searchET.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtro = s.toString();
                atualizaLista();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchToolbar = (Toolbar) findViewById(R.id.toolbarSearch);
        advancedToolbar = (Toolbar) findViewById(R.id.toolbarAdvanced);

        //searchToolbar.setNavigationIcon(R.drawable.ic_action_back);

        if (savedInstanceState != null) {
            filtro = savedInstanceState.getString("FILTRO");
            filtrarNome = savedInstanceState.getBoolean("FILTRANOME");
            filtrarDono = savedInstanceState.getBoolean("FILTRADONO");
            donoSearchSpinner.setSelection(savedInstanceState.getInt("SELECTEDINDEX"));


            searchET.setText(filtro);

            if (filtrarNome) {
                searchToolbar.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();
            }

            if (filtrarDono) {
                advancedToolbar.setVisibility(View.VISIBLE);
            }

            atualizaLista();
        }

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(petList.get(position).getId());


                final Pet pet = petList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                builder.setMessage("Pet de ID " + pet.getId());

                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dao.deletePet(pet.getId());
                        atualizaLista();
                    }
                });


                AlertDialog alert = builder.create();
                alert.show();

                return false;
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                View layout = inflater.inflate(R.layout.cadastro_pet_layout, (ViewGroup) findViewById(R.id.scrollView));


                final EditText nomeET;
                final EditText idadeET;
                final Spinner donoSpinner;
                final EditText especieET;
                final EditText racaET;


                nomeET = (EditText) layout.findViewById(R.id.nomeET);
                idadeET = (EditText) layout.findViewById(R.id.idadeET);
                especieET = (EditText) layout.findViewById(R.id.especieET);
                racaET = (EditText) layout.findViewById(R.id.racaET);
                donoSpinner = (Spinner) layout.findViewById(R.id.spinnerDono);

                donoSpinner.setAdapter(spinnerArrayAdaper);

                AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                builder.setView(layout);
                builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                final AlertDialog dialog = builder.create();

                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean vazio = false;


                        String nome = nomeET.getText().toString();
                        String idadeText = idadeET.getText().toString();
                        String especie = especieET.getText().toString();
                        String raca = racaET.getText().toString();
                        int indexSpinner = donoSpinner.getSelectedItemPosition();

                        Cliente cliente = donos.get(indexSpinner);
                        int idDono = cliente.getId();




                        EditText[] texts = new EditText[]{nomeET, idadeET, especieET, racaET};

                        for (EditText et : texts) {
                            if (et.getText().toString().isEmpty()) {
                                et.setError("Campo obrigatório");
                                vazio = true;
                            }
                        }

                        if (!vazio) {
                            int idade = Integer.parseInt(idadeText);


                            Pet pet = new Pet(nome, idade, idDono, especie, raca);


                            dao.insertPet(pet);

                            Toast.makeText(getThis(), "Inserido com sucesso", Toast.LENGTH_SHORT).show();
                            System.out.println("insertido");

                            atualizaLista();
                            dialog.dismiss();
                        }
                    }
                });


                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.fab)));
        fab.setRippleColor(getResources().getColor(R.color.pressed));


    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizaLista();

    }

    public void atualizaLista() {
        listView = (ListView) findViewById(R.id.listView);

        List<Map<String, String>> data = new ArrayList<Map<String, String>>();
        petList = dao.selectPet();

        List<Pet> pets = new ArrayList<Pet>();


        if (filtrarNome) {
            if (filtro != null && !filtro.isEmpty()) {
                for (Pet pet : petList) {
                    String nome = pet.getNome().toLowerCase();
                    String f = filtro.toLowerCase();
                    if (!nome.contains(f)) {
                        pets.add(pet);
                    }
                }
            }
        }

        petList = this.filtraLista(petList, pets);
        pets = new ArrayList<Pet>();


        if (filtrarDono) {
            int indexSpinner = donoSearchSpinner.getSelectedItemPosition();

            Cliente cliente = donos.get(indexSpinner);
            int id = cliente.getId();

            for (Pet pet : petList) {
                if (pet.getIdDono() != id) {
                    pets.add(pet);
                }
            }

        }

        petList = this.filtraLista(petList, pets);

        for (Pet pet : petList) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("nome", pet.getNome());

            datum.put("id", String.format(
                    "\nID: %d\nDono: %s\nEspecie: %s\n" +
                            "Raça: %s",
                    pet.getId(),
                    dao.getNomeDono(pet.getIdDono()),
                    pet.getEspecie(),
                    pet.getRaca()));
            data.add(datum);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"nome", "id"}, new int[]{android.R.id.text1, android.R.id.text2});


        listView.setAdapter(adapter);

    }

    private ConsultaPetActivity getThis() {
        return this;
    }


    private List<Pet> filtraLista(List<Pet> in, List<Pet> filtro) {
        for (Pet pet : filtro) {
            if (in.contains(pet)) {
                in.remove(pet);
            }
        }

        return in;

    }
}
