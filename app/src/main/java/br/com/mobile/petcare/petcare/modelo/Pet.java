package br.com.mobile.petcare.petcare.modelo;

/**
 * Created by Alefe Lucas on 27/11/2015.
 */
public class Pet {

    private int id;
    private String nome;
    private int idade;
    private int idDono;
    private String especie;
    private String raca;

    public Pet() {
    }

    public Pet(String nome, int idade, int idDono, String especie, String raca) {
        this.nome = nome;
        this.idade = idade;
        this.idDono = idDono;
        this.especie = especie;
        this.raca = raca;
    }

    public Pet(int id, String nome, int idade, int idDono, String especie, String raca) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.idDono = idDono;
        this.especie = especie;
        this.raca = raca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public int getIdDono() {
        return idDono;
    }

    public void setIdDono(int idDono) {
        this.idDono = idDono;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaca() {
        return raca;
    }

    public void setRaca(String raca) {
        this.raca = raca;
    }
}
