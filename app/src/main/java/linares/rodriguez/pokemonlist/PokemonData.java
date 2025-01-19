package linares.rodriguez.pokemonlist;

public class PokemonData {

    public PokemonData(String name, int index, int imageURL, String[] tipo, float peso, float altura) {
        this.name = name;
        this.index = index;
        //this.imageURL = imageURL;
        this.tipo = tipo;
        this.peso = peso;
        this.altura = altura;
    }

    public PokemonData(String name, int index, String[] tipo, float peso, float altura) {
        this.name = name;
        this.index = index;
        //this.imageURL = imageURL;
        this.tipo = tipo;
        this.peso = peso;
        this.altura = altura;
    }

    private String name;
    private int index;
    private String imageURL;
    private String[] tipo;
    private float peso;
    private float altura;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String[] getTipo() {
        return tipo;
    }

    public void setTipo(String[] tipo) {
        this.tipo = tipo;
    }

    public float getPeso() {
        return peso;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getAltura() {
        return altura;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }



}
