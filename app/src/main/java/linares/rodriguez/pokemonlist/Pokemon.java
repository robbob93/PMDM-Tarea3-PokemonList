package linares.rodriguez.pokemonlist;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un Pokémon.
 * Esta clase contiene información sobre el Pokémon, como su ID, nombre, tipos, peso, altura y su imagen.
 * Además, maneja la conversión de tipos de Pokémon a una lista de nombres.
 */
public class Pokemon implements Serializable {

    //Mapea la clave "id" en el JSON al campo "id" en la clase.
    @SerializedName("id")
    private int id; // ID del Pokémon
    //Mapea la clave "name" en el JSON al campo "name" en la clase.
    @SerializedName("name")
    private String name; // Nombre del Pokémon
    //Mapea la clave "sprites" en el JSON al campo "sprites" en la clase.
    @SerializedName("sprites")
    private Sprites sprites; // Información sobre la imagen del Pokémon
    //Mapea la clave "types" en el JSON al campo "types" en la clase.
    @SerializedName("types")
    private List<TypeEntry> types; // Tipos del Pokémon, representados por una lista de objetos TypeEntry
    //Mapea la clave "weight" en el JSON al campo "weight" en la clase.
    @SerializedName("weight")
    private float weight; // Peso del Pokémon
    //Mapea la clave "height" en el JSON al campo "height" en la clase.
    @SerializedName("height")
    private float height; // Altura del Pokémon

    private List<String> typesNames; // Lista de los nombres de los tipos de Pokémon

    /**
     * Constructor vacío necesario para Firestore y otras operaciones de deserialización.
     */
    public Pokemon() {
    }

    /**
     * Constructor para crear un objeto Pokémon con los datos completos.
     *
     * @param id El ID del Pokémon.
     * @param name El nombre del Pokémon.
     * @param imageUrl La URL de la imagen del Pokémon.
     * @param types La lista de tipos del Pokémon.
     * @param weight El peso del Pokémon.
     * @param height La altura del Pokémon.
     */
    public Pokemon(int id, String name, String imageUrl, List<String> types, float weight,  float height) {
        this.id = id;
        this.name = name;
        this.sprites = new Sprites(imageUrl); // Instanciar Sprites con la URL de la imagen
        this.typesNames = types; // Guardar los nombres de los tipos
        this.weight = weight;
        this.height = height;
    }

    /**
     * Constructor con solo el nombre para la lista de Pokedex.
     *
     * @param name El nombre del Pokémon.
     */
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

    /**
     * Obtiene la URL de la imagen del Pokémon.
     *
     * @return La URL de la imagen, o null si no hay imagen disponible.
     */
    public String getImageUrl() {
        return sprites != null ? sprites.getFrontDefault() : null;
    }

    public void setImageUrl(String imageUrl) {
        if (sprites == null) {
            sprites = new Sprites(imageUrl); // Si no hay un objeto Sprites, lo creamos con la URL
        } else {
            sprites.setFrontDefault(imageUrl); // Si ya existe, solo actualizamos la URL de la imagen
        }
    }

    public void setTypes(List<TypeEntry> types){
        this.types = types;
    }

    public List<TypeEntry> getTypes() {
        return types;
    }

    /**
     * Obtiene los nombres de los tipos de Pokémon como una lista de Strings.
     *
     * @return Una lista con los nombres de los tipos.
     */
    public List<String> getTypesNames() {
        return transformToListTypeNames(); // Convierte los tipos a una lista de nombres
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

    /**
     * Clase interna que representa la información sobre las imágenes del Pokémon.
     */
    public static class Sprites {
        @SerializedName("front_default")
        private String frontDefault; // URL de la imagen del frente del Pokémon

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

    /**
     * Clase interna que representa la información sobre los tipos de Pokémon.
     */
    public static class TypeEntry {
        @SerializedName("type")
        private Type type; // Información sobre el tipo del Pokémon

        public Type getType() {
            return type;
        }

        /**
         * Clase interna que representa un tipo específico de Pokémon.
         */
        public static class Type {
            @SerializedName("name")
            private String name; // Nombre del tipo (e.g., "fire", "water")

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }
        }
    }

    /**
     * Método auxiliar para convertir la lista de objetos TypeEntry a una lista de nombres de tipos.
     *
     * @return Una lista de nombres de los tipos del Pokémon.
     */
    public List<String> transformToListTypeNames() {
        List<String> typeNames = new ArrayList<>();
        if (types != null) {
            for (TypeEntry typeEntry : types) {
                if (typeEntry.getType() != null && typeEntry.getType().getName() != null) {
                    typeNames.add(typeEntry.getType().getName()); // Añade el nombre del tipo a la lista
                }
            }
        }
        return typeNames;
    }
}
