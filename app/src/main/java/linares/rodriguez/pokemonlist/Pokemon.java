package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Pokemon implements Serializable {

    

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("sprites")
    private Sprites sprites;

    @SerializedName("types")
    private List<String> types;

    @SerializedName("weight")
    private float weight;

    @SerializedName("height")
    private float height;


    public Pokemon(String name, int id, String imageUrl, List<String> types, int weight, int height) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }


    // Constructor sin argumentos (requerido por Firestore)
    public Pokemon() {
    }


    private String imageUrl;

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

    public List<String> getTypes() {
        return types;
    }
    public void setTypes(List<String> types){
        this.types = types;
    }


    public float getWeight() {
        return weight;
    }

    public void setWeight(float peso) {
        this.peso = peso;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float altura) {
        this.altura = altura;
    }

    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }
    }


}
