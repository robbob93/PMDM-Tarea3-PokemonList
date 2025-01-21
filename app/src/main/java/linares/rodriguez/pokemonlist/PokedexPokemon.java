package linares.rodriguez.pokemonlist;

import java.util.List;

public class PokedexPokemon {
    private String name;
    private int id;
    private String imageUrl;
    private List<String> types;
    private int weight;
    private int height;

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

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
