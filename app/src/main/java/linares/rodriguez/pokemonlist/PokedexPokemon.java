package linares.rodriguez.pokemonlist;

import java.util.List;

public class PokedexPokemon {
    private String name;
    private int id;
    private String imageUrl;
    private List<String> types;
    private float weight;
    private float height;

    public PokedexPokemon(String name, int id, String imageUrl, List<String> types, int weight, int height) {
        this.name = name;
        this.id = id;
        this.imageUrl = imageUrl;
        this.types = types;
        this.weight = weight;
        this.height = height;
    }

    public String getName() {
        return name;
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

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
