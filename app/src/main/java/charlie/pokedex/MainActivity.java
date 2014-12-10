package charlie.pokedex;

import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    RelativeLayout loadingScreen;
    CustomAdapter adapter;
    Resources resources;
    public MainActivity activity = null;
    public ArrayList<PokedexListItem> pokedex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingScreen = (RelativeLayout)findViewById(R.id.mainLoadingScreen);
        activity = this;
        getPokedex();

        resources = getResources();
        listView = (ListView)findViewById(R.id.pokedexListView);

    }

    private void getPokedex() {
        loadingScreen.setVisibility(View.VISIBLE);
        new PokedexRequest().execute();
    }

    private void setFakeData() {
        for (int i = 1; i <= 10; i++) {
            final PokedexListItem item = new PokedexListItem(i, "Pokemon " + String.valueOf(i), String.valueOf(i));
            pokedex.add(item);
        }
    }

    public void onItemClick(int mPosition) {
        PokedexListItem temp = (PokedexListItem)pokedex.get(mPosition);

        Intent intent = new Intent(this, PokemonActivity.class);
        intent.putExtra("name", temp.getPokemonName());
        intent.putExtra("id", temp.getPokedexID());
        startActivity(intent);
    }

    private class PokedexRequest extends AsyncTask<Void, Integer, String> {

        @Override
        protected String doInBackground(Void... params) {
            String result = "";
            try {
                URL url = new URL("http://pokeapi.co/api/v1/pokedex/1/");
                HttpURLConnection connection;
                connection = (HttpURLConnection)url.openConnection();
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);

                final int statusCode = connection.getResponseCode();
                if (statusCode != HttpURLConnection.HTTP_OK) {
                    Log.d("WEB", "The Request failed with status code " + statusCode + ".");
                } else {
                    InputStream inputStream = new BufferedInputStream(connection.getInputStream());
                    result = getResponseText(inputStream);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... params) {

        }

        @Override
        protected void onPostExecute(String result) {
            loadingScreen.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                JSONArray pokemonArray = object.getJSONArray("pokemon");

                for(int i = 0; i < pokemonArray.length(); i++) {
                    JSONObject pokemon = pokemonArray.getJSONObject(i);
                    String url = pokemon.getString("resource_uri");
                    String[] tokens = url.split("/");
                    String id = tokens[3];
                    String name = pokemon.getString("name");
                    name = name.substring(0,1).toUpperCase() + name.substring(1);
                    int val = Integer.parseInt(id);
                    if (val > 0 && val < 152) {
                        PokedexListItem newItem = new PokedexListItem(val, name, id);
                        pokedex.add(newItem);
                    }
                }

                Collections.sort(pokedex, new Comparator<PokedexListItem>() {
                    @Override
                    public int compare(PokedexListItem lhs, PokedexListItem rhs) {
                        return (rhs.getID() <= lhs.getID() ? 1 : -1);
                    }
                });

                adapter = new CustomAdapter(activity, pokedex, resources);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private String getResponseText(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line = reader.readLine();
        while (line != null) {
            sb.append(line + "\n");
            line = reader.readLine();
        }
        return sb.toString();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
