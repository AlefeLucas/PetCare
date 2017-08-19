package br.com.mobile.petcare.petcare.modelo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class BdConnect extends SQLiteOpenHelper {

    private static final String NOME_BASEDADOS = "petcare";
    private static final int VERSAO_BD = 3;

    public BdConnect(Context context) {
        super(context, NOME_BASEDADOS, null, VERSAO_BD);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE cliente ( _id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, idade INTEGER NOT NULL, telefone TEXT NOT NULL, email TEXT NOT NULL, rg TEXT UNIQUE NOT NULL, cpf TEXT UNIQUE NOT NULL);");
        db.execSQL("CREATE TABLE pet ( _id INTEGER PRIMARY KEY AUTOINCREMENT, nome TEXT NOT NULL, idade INTEGER NOT NULL, id_dono INTEGER NOT NULL, especie TEXT NOT NULL, raca TEXT NULL, FOREIGN KEY (id_dono) REFERENCES cliente (_id) ON DELETE CASCADE);");
        /*db.execSQL("CREATE TRIGGER delete_pet_with_cliente BEFORE DELETE ON cliente "
                + "FOR EACH ROW BEGIN"
                + " DELETE FROM pet WHERE cliente._id = id_dono"
                + " END;");*/
        db.execSQL("CREATE TABLE usuario (_id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT UNIQUE NOT NULL, senha TEXT NOT NULL);");
    }

    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            // Enable foreign key constraints
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE cliente");
        db.execSQL("DROP TABLE pet");
        db.execSQL("DROP TABLE usuario");
        onCreate(db);
    }
}