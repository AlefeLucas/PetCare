package br.com.mobile.petcare.petcare.modelo;

/**
 * Created by TI01N-2 on 17/11/2015.
 */
public class Cliente {

    private int id;
    private String nome;
    private int idade;
    private String telefone;
    private String email;
    private String rg;
    private String cpf;

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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Cliente(int id, String nome, int idade, String telefone, String email, String rg, String cpf) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.email = email;
        this.rg = rg;
        this.cpf = cpf;
    }


    public Cliente() {

    }

    public Cliente(String nome, int idade, String telefone, String email, String rg, String cpf) {
        this.nome = nome;
        this.idade = idade;
        this.telefone = telefone;
        this.email = email;
        this.rg = rg;
        this.cpf = cpf;
    }
}


