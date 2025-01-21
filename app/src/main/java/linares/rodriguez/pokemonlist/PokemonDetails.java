package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PokemonDetails {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("sprites")
    private Sprites sprites;

    @SerializedName("types")
    private List<TypeEntry> types;

    @SerializedName("weight")
    private int weight;

    @SerializedName("height")
    private int height;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl() {
        return sprites.getFrontDefault();
    }

    public List<String> getTypeNames() {
        List<String> typeNames = new ArrayList<>();
        for (TypeEntry typeEntry : types) {
            typeNames.add(typeEntry.getType().getName());
        }
        return typeNames;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault;

        public String getFrontDefault() {
            return frontDefault;
        }
    }

    public static class TypeEntry {
        @SerializedName("type")
        private Type type;

        public Type getType() {
            return type;
        }

        public static class Type {
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }
        }
    }
}
