package charlie.pokedex;

/**
 * Created by charlie on 12/9/14.
 */
public class PokedexListItem {


    private String pokemonName;
    private String pokedexID;
    private String pokedexLabel;

    public PokedexListItem(String pokemonName, String pokedexID) {
        this.pokemonName = pokemonName;
        this.pokedexID = pokedexID;
        this.pokedexLabel = parseID(pokedexID);
    }

    private String parseID(String id) {
        String result = "#";
        if (id.length() == 1) {
            result += "00" + id;
        } else if (id.length() == 2) {
            result += "0" + id;
        } else {
            result += id;
        }
        return result;
    }

    public String getPokemonName() {
        return pokemonName;
    }

    public void setPokemonName(String pokemonName) {
        this.pokemonName = pokemonName;
    }

    public String getPokedexID() {
        return pokedexID;
    }

    public void setPokedexID(String pokedexID) {
        this.pokedexID = pokedexID;
    }

    public String getPokedexLabel() {
        return pokedexLabel;
    }

    public void setPokedexLabel(String pokedexLabel) {
        this.pokedexLabel = pokedexLabel;
    }
}
