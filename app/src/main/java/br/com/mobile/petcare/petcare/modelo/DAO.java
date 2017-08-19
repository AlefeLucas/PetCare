package br.com.mobile.petcare.petcare.modelo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class DAO {

    private SQLiteDatabase db;

    public DAO(Context context) {
        BdConnect conecta = new BdConnect(context);
        db = conecta.getWritableDatabase();
    }

    public void insertUsuario(Usuario usuario) {

        ContentValues values = new ContentValues();

        values.put("email", usuario.getEmail().toLowerCase());
        values.put("senha", usuario.getSenha());

        db.insert("usuario", null, values);

    }

    public List<Usuario> selectUsuario() {
        String[] colunas = new String[]{"_id", "email", "senha"};

        List<Usuario> lista = new ArrayList<Usuario>();


        Cursor cursor = db.query("usuario", colunas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Usuario usuario = new Usuario(cursor.getInt(0), cursor.getString(1), cursor.getString(2));

                lista.add(usuario);

            } while (cursor.moveToNext());
        }

        return lista;
    }

    public String getNomeDono(int id) {
        String[] colunas = new String[]{"nome"};

        Cursor cursor = db.query("cliente", colunas, "_id = ?", new String[]{id + ""}, null, null, null);

        if (cursor.moveToFirst()) {
            return cursor.getString(0);
        }

        return "erro";
    }


    public boolean emailExists(String email) {
        List<Usuario> usuarios = this.selectUsuario();

        for (Usuario usuario : usuarios) {
            if (usuario.getEmail().equalsIgnoreCase(email)) {
                return true;
            }
        }
        return false;
    }

    public boolean rgExists(String rg) {
        List<Cliente> clientes = this.selectCliente();

        for (Cliente cliente : clientes) {
            if (cliente.getRg().equalsIgnoreCase(rg)) {
                return true;
            }
        }
        return false;
    }

    public boolean cpfExists(String cpf) {
        List<Cliente> clientes = this.selectCliente();

        for (Cliente cliente : clientes) {
            if (cliente.getCpf().equalsIgnoreCase(cpf)) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String email, String senha) {
        String[] colunas = new String[]{"_id", "email", "senha"};

        email = email.toLowerCase();

        List<Usuario> lista = new ArrayList<Usuario>();

        Cursor cursor = db.query("usuario", colunas, "email = ? AND senha = ?", new String[]{email, senha}, null, null, null);

        return cursor.moveToFirst();

    }


    public void insertCliente(Cliente cliente) {

        ContentValues values = new ContentValues();

        values.put("nome", cliente.getNome());
        values.put("idade", cliente.getIdade());
        values.put("telefone", cliente.getTelefone());
        values.put("email", cliente.getEmail());
        values.put("rg", cliente.getRg());
        values.put("cpf", cliente.getCpf());

        db.insert("cliente", null, values);

    }

    public List<Cliente> selectCliente() {
        String[] colunas = new String[]{"_id", "nome", "idade", "telefone", "email", "rg", "cpf"};

        List<Cliente> lista = new ArrayList<Cliente>();


        Cursor cursor = db.query("cliente", colunas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Cliente cliente = new Cliente(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );

                lista.add(cliente);

            } while (cursor.moveToNext());
        }

        return lista;
    }

    public void updateCliente(Cliente cliente) {

        ContentValues values = new ContentValues();
        values.put("telefone", cliente.getTelefone());
        values.put("email", cliente.getEmail());

        int rows = db.update("cliente", values, "_id = ?", new String[]{"" + cliente.getId()});
        System.out.println("@@@@@@@@@@ROWS: " + rows);


    }

    public void deleteCliente(int id) {
        db.delete("cliente", "_id = ?", new String[]{"" + id});
    }

    // CREATE TABLE cliente (

    // _id INTEGER PRIMARY KEY AUTOINCREMENT,
    // nome TEXT NOT NULL,
    // idade INTEGER NOT NULL,
    // telefone TEXT NOT NULL,
    // email TEXT NOT NULL,
    // rg TEXT NOT NULL,
    // cpf TEXT NOT NULL

    // );


    public void insertPet(Pet pet) {

        ContentValues values = new ContentValues();

        values.put("nome", pet.getNome());
        values.put("idade", pet.getIdade());
        values.put("id_dono", pet.getIdDono());
        values.put("especie", pet.getEspecie());
        values.put("raca", pet.getRaca());

        db.insert("pet", null, values);

    }

    public List<Pet> selectPet() {
        String[] colunas = new String[]{"_id", "nome", "idade", "id_dono", "especie", "raca"};

        List<Pet> lista = new ArrayList<Pet>();


        Cursor cursor = db.query("pet", colunas, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Pet pet = new Pet(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getInt(2),
                        cursor.getInt(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );

                lista.add(pet);

            } while (cursor.moveToNext());
        }

        return lista;
    }

    // sem update de pet

    public void deletePet(int id) {
        db.delete("pet", "_id = ?", new String[]{"" + id});
    }

}
