package RMI;

import java.io.Serializable;

public class Livro implements Serializable {

    private static final long serialVersionUID = 1L;

    private String titulo;
    private String autor;
    private String isbn;
    private int quantidade;

    public Livro(String titulo, String autor, String isbn, int quantidade) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.quantidade = quantidade;
    }

    public String getInfo() {
        return "Livro: " + this.titulo +
                "\nAutor: " + this.autor +
                "\nISBN: " + this.isbn +
                "\nQuantidade: " + this.quantidade;
    }

    public void devolver() {
        this.quantidade++;
    }

    public boolean emprestar() {
        if (this.quantidade > 0) {
            this.quantidade--;
            return true;
        }
        return false;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getQuantidade() {
        return quantidade;
    }
}
