package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

public class Pokemon {


    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;
/*
    @SerializedName("sprites")
    private Sprites sprites;

    @SerializedName("types")
    private List<TypeEntry> types;
 */
    @SerializedName("weight")
    private float weight;

    @SerializedName("height")
    private float height;


    public Pokemon(String name, int index, String imageURL, String[] tipo, float peso, float altura) {
        this.name = name;
        this.id = index;
        this.imageUrl = imageURL;
        this.tipo = tipo;
        this.peso = peso;
        this.altura = altura;
    }

    public Pokemon(String name, int index, String[] tipo, float peso, float altura) {
        this.name = name;
        this.id = index;
        //this.imageURL = imageURL;
        this.tipo = tipo;
        this.peso = peso;
        this.altura = altura;
    }
    // Constructor sin argumentos (requerido por Firestore)
    public Pokemon() {
    }


    private String imageUrl;
    private String[] tipo;
    private float peso;
    private float altura;

    public Pokemon(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String[] getTipo() {
        return tipo;
    }

    public void setTipo(String[] tipo) {
        this.tipo = tipo;
    }

    public float getWeight() {
        return weight;
    }

    public void setPeso(float peso) {
        this.peso = peso;
    }

    public float getHeight() {
        return height;
    }

    public void setAltura(float altura) {
        this.altura = altura;
    }



}
