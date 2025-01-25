package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pokemon implements Serializable {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("sprites")
    private Sprites sprites;

    @SerializedName("types")
    private List<TypeEntry> types; // Usamos List<String> para simplificar el manejo de tipos

    @SerializedName("weight")
    private float weight;

    @SerializedName("height")
    private float height;

    private List<String> typesNames;

    // Constructor vacío (necesario para Firestore)
    public Pokemon() {
    }

    // Constructor Para la API
/*
    public Pokemon(int id, String name, String imageUrl, float weight, List<TypeEntry> types, float height) {
        this.id = id;
        this.name = name;
        this.sprites = new Sprites(imageUrl); // Creamos la instancia de Sprites
        this.types = types; // Guardamos la lista de TypeEntry
        this.weight = weight;
        this.height = height;
    }

 */

    public Pokemon(int id, String name, String imageUrl, List<String> types, float weight,  float height) {
        this.id = id;
        this.name = name;
        this.sprites = new Sprites(imageUrl); // Creamos la instancia de Sprites
        this.typesNames = types; // Guardamos la lista de TypeEntry
        this.weight = weight;
        this.height = height;
    }



    public Pokemon(String name){
        this.name = name;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    public void setImageUrl(String imageUrl) {
        if (sprites == null) {
            sprites = new Sprites(imageUrl);
        } else {
            sprites.setFrontDefault(imageUrl);
        }
    }

    public void setTypes(List<TypeEntry> types){
        this.types = types;
    }

    public List<TypeEntry> getTypes() {
        return types;
    }



    public List<String> getTypesNames() {
        return transformToListTypeNames();
    }

    public void setTypesNames(List<String> types) {
        this.typesNames = types;
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

    // Clase interna Sprites
    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault;

        public Sprites(String frontDefault) {
            this.frontDefault = frontDefault;
        }

        public String getFrontDefault() {
            return frontDefault;
        }

        public void setFrontDefault(String frontDefault) {
            this.frontDefault = frontDefault;
        }
    }

    // Clase interna TypeEntry (similar a la que ya tenías en `PokemonDetails`)
    public static class TypeEntry {
        @SerializedName("type")
        private Type type;

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        public static class Type {
            @SerializedName("name")
            private String name;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    // Método auxiliar para extraer los nombres de los tipos
    public List<String> transformToListTypeNames() {
        List<String> typeNames = new ArrayList<>();
        if (types != null) {
            for (TypeEntry typeEntry : types) {
                if (typeEntry.getType() != null && typeEntry.getType().getName() != null) {
                    typeNames.add(typeEntry.getType().getName());
                }
            }
        }
        return typeNames;
    }

    public static List<TypeEntry> convertTypeNamesToEntries(List<String> typeNames) {
        List<TypeEntry> typeEntries = new ArrayList<>();
        for (String name : typeNames) {
            TypeEntry.Type type = new TypeEntry.Type();
            type.setName(name); // Necesitas agregar un setter en la clase Type
            TypeEntry entry = new TypeEntry();
            entry.setType(type); // Necesitas agregar un setter en la clase TypeEntry
            typeEntries.add(entry);
        }
        return typeEntries;
    }
}
