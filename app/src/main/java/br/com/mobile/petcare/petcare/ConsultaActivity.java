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

public class ConsultaActivity extends AppCompatActivity {

    ListView listView;
    FloatingActionButton fab;
    List<Cliente> clienteList;
    DAO dao;

    Toolbar toolbar;
    Toolbar searchToolbar;

    EditText searchET;

    String filtro;
    boolean filtrarNome;
    boolean vazio;
    boolean rgExists;
    boolean cpfExists;

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("FILTRO", filtro);
        outState.putBoolean("FILTRANOME", filtrarNome);


        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_paciente, menu);
        searchToolbar.inflateMenu(R.menu.menu_search_simple);
        searchToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                int id = item.getItemId();
                if (id == R.id.action_back) {
                    searchToolbar.setVisibility(View.GONE);
                    getSupportActionBar().show();
                    filtrarNome = false;

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
        setContentView(R.layout.activity_consulta);
        dao = new DAO(getThis());
        rgExists = false;
        cpfExists = false;
        searchET = (EditText) findViewById(R.id.searchET);

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

        //searchToolbar.setNavigationIcon(R.drawable.ic_action_back);

        if (savedInstanceState != null) {
            filtro = savedInstanceState.getString("FILTRO");
            filtrarNome = savedInstanceState.getBoolean("FILTRANOME");

            searchET.setText(filtro);

            if (filtrarNome) {
                searchToolbar.setVisibility(View.VISIBLE);
                getSupportActionBar().hide();
            }

            atualizaLista();
        }

        listView = (ListView) findViewById(R.id.listView);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println(clienteList.get(position).getId());


                final Cliente cliente = clienteList.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                builder.setMessage("Cliente de ID " + cliente.getId());

                builder.setNeutralButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("Deletar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dao.deleteCliente(cliente.getId());
                        atualizaLista();
                    }
                });

                builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();

                        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
                        View layout = inflater.inflate(R.layout.cadastro_cliente_layout, (ViewGroup) findViewById(R.id.scrollView));


                        final EditText nomeET;
                        final EditText idadeET;
                        final EditText telefoneET;
                        final EditText emailET;
                        final EditText rgET;
                        final EditText cpfET;

                        nomeET = (EditText) layout.findViewById(R.id.nomeET);
                        idadeET = (EditText) layout.findViewById(R.id.idadeET);
                        telefoneET = (EditText) layout.findViewById(R.id.telefoneET);
                        emailET = (EditText) layout.findViewById(R.id.emailET);
                        rgET = (EditText) layout.findViewById(R.id.rgET);
                        cpfET = (EditText) layout.findViewById(R.id.cpfET);

                        nomeET.setText(cliente.getNome());
                        idadeET.setText(cliente.getIdade() + "");
                        telefoneET.setText(cliente.getTelefone());
                        emailET.setText(cliente.getEmail() + "");
                        rgET.setText(cliente.getRg());
                        cpfET.setText(cliente.getCpf());


                        nomeET.setEnabled(false);
                        idadeET.setEnabled(false);
                        rgET.setEnabled(false);
                        cpfET.setEnabled(false);


                        AlertDialog.Builder builder = new AlertDialog.Builder(getThis());
                        builder.setView(layout);
                        builder.setPositiveButton("Alterar", new DialogInterface.OnClickListener() {
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

                        final AlertDialog dialog2 = builder.create();

                        dialog2.show();
                        dialog2.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                vazio = false;

                                System.out.println("@@@@@@@@@@@@@@@");
                                String nome = nomeET.getText().toString();
                                String idadeText = idadeET.getText().toString();
                                String telefone = telefoneET.getText().toString();
                                String email = emailET.getText().toString();
                                String rg = rgET.getText().toString();
                                String cpf = cpfET.getText().toString();


                                System.out.println(telefone);
                                System.out.println(rg);
                                System.out.println(cpf);


                                EditText[] texts = new EditText[]{nomeET, idadeET, telefoneET, emailET, rgET, cpfET};

                                for (EditText et : texts) {
                                    if (et.getText().toString().isEmpty()) {
                                        et.setError("Campo obrigatório");
                                        vazio = true;
                                    }
                                }


                                if (!vazio) {
                                    int idade = Integer.parseInt(idadeText);


                                    Cliente clienteUp = new Cliente(cliente.getId(), nome, idade, telefone, email, rg, cpf);

                                    dao.updateCliente(clienteUp);

                                    Toast.makeText(getThis(), "Alterado com sucesso", Toast.LENGTH_SHORT).show();
                                    System.out.println("insertido");

                                    atualizaLista();
                                    dialog2.dismiss();
                                }
                            }
                        });


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
                View layout = inflater.inflate(R.layout.cadastro_cliente_layout, (ViewGroup) findViewById(R.id.scrollView));


                final EditText nomeET;
                final EditText idadeET;
                final EditText telefoneET;
                final EditText emailET;
                final EditText rgET;
                final EditText cpfET;


                nomeET = (EditText) layout.findViewById(R.id.nomeET);
                idadeET = (EditText) layout.findViewById(R.id.idadeET);
                telefoneET = (EditText) layout.findViewById(R.id.telefoneET);
                emailET = (EditText) layout.findViewById(R.id.emailET);
                rgET = (EditText) layout.findViewById(R.id.rgET);
                cpfET = (EditText) layout.findViewById(R.id.cpfET);


                rgET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (new DAO(getThis()).rgExists(s.toString())) {
                            rgET.setError("RG existente");
                            rgExists = true;
                        } else {
                            rgExists = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });

                cpfET.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (new DAO(getThis()).cpfExists(s.toString())) {
                            cpfET.setError("CPF existente");
                            cpfExists = true;
                        } else {
                            cpfExists = false;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });


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
                        vazio = false;


                        String nome = nomeET.getText().toString();
                        String idadeText = idadeET.getText().toString();
                        String telefone = telefoneET.getText().toString();
                        String email = emailET.getText().toString();
                        String rg = rgET.getText().toString();
                        String cpf = cpfET.getText().toString();


                        EditText[] texts = new EditText[]{nomeET, idadeET, telefoneET, emailET, rgET, cpfET};

                        for (EditText et : texts) {
                            if (et.getText().toString().isEmpty()) {
                                et.setError("Campo obrigatório");
                                vazio = true;
                            }
                        }


                        if (!vazio && !rgExists && !cpfExists) {
                            int idade = Integer.parseInt(idadeText);


                            Cliente cliente = new Cliente(nome, idade, telefone, email, rg, cpf);


                            dao.insertCliente(cliente);

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
        clienteList = dao.selectCliente();

        List<Cliente> clientes = new ArrayList<Cliente>();


        if (filtrarNome) {
            if (filtro != null && !filtro.isEmpty()) {
                for (Cliente cliente : clienteList) {
                    String nome = cliente.getNome().toLowerCase();
                    String f = filtro.toLowerCase();
                    if (!nome.contains(f)) {
                        clientes.add(cliente);
                    }
                }
            }
        }

        clienteList = this.filtraLista(clienteList, clientes);
        clientes = new ArrayList<Cliente>();


        clienteList = this.filtraLista(clienteList, clientes);

        for (Cliente cliente : clienteList) {
            Map<String, String> datum = new HashMap<String, String>(2);
            datum.put("nome", cliente.getNome());

            datum.put("id", String.format(
                    "\nID: %d\nTelefone: %s\nEmail: %s",
                    cliente.getId(),
                    cliente.getTelefone(),
                    cliente.getEmail()));
            data.add(datum);
        }


        SimpleAdapter adapter = new SimpleAdapter(this, data, android.R.layout.simple_list_item_2, new String[]{"nome", "id"}, new int[]{android.R.id.text1, android.R.id.text2});


        listView.setAdapter(adapter);

    }

    private ConsultaActivity getThis() {
        return this;
    }


    private List<Cliente> filtraLista(List<Cliente> in, List<Cliente> filtro) {
        for (Cliente cliente : filtro) {
            if (in.contains(cliente)) {
                in.remove(cliente);
            }
        }

        return in;

    }
}
