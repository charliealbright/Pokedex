package charlie.pokedex;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class PokemonActivity extends ActionBarActivity {

    private PokemonActivity activity;

    //HEADER LAYOUT
    private TextView nameBig;
    private TextView idBig;
    private ImageView type1;
    private ImageView type2;

    //BASE STATS LAYOUT
    private ProgressBar hpBar;
    private ProgressBar attackBar;
    private ProgressBar defenseBar;
    private ProgressBar specialBar;
    private ProgressBar speedBar;
    private TextView hpVal;
    private TextView attackVal;
    private TextView defenseVal;
    private TextView specialVal;
    private TextView speedVal;

    //OTHER
    private String id;
    private ArrayList<String> effective;
    private ArrayList<String> weak;
    private ImageView pokemonImage;
    private RelativeLayout loadingScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon);

        activity = this;

        nameBig = (TextView)findViewById(R.id.nameBig);
        idBig = (TextView)findViewById(R.id.idBig);
        type1 = (ImageView)findViewById(R.id.type1);
        type2 = (ImageView)findViewById(R.id.type2);
        pokemonImage = (ImageView)findViewById(R.id.pokemonImage);
        loadingScreen = (RelativeLayout)findViewById(R.id.pokemonLoadingScreen);

        hpBar = (ProgressBar)findViewById(R.id.hpBar);
        attackBar = (ProgressBar)findViewById(R.id.attackBar);
        defenseBar = (ProgressBar)findViewById(R.id.defenseBar);
        specialBar = (ProgressBar)findViewById(R.id.specialBar);
        speedBar = (ProgressBar)findViewById(R.id.speedBar);
        hpVal = (TextView)findViewById(R.id.hpVal);
        attackVal = (TextView)findViewById(R.id.attackVal);
        defenseVal = (TextView)findViewById(R.id.defenseVal);
        specialVal = (TextView)findViewById(R.id.specialVal);
        speedVal = (TextView)findViewById(R.id.speedVal);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        String label = intent.getStringExtra("label");

        effective = new ArrayList<>();
        weak = new ArrayList<>();

        getPokemonData(id);
        nameBig.setText(name);
        idBig.setText(label);
    }

    private void getPokemonData(String id) {
        loadingScreen.setVisibility(View.VISIBLE);
        new PokemonRequest().execute(id);
    }

    private class PokemonRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String id = params[0];

            try {
                URL url = new URL("http://pokeapi.co/api/v1/pokemon/" + id + "/");
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
        protected void onPostExecute(String result) {
            ArrayList<String> typeArray = new ArrayList<>();
            pokemonImage.setImageResource(activity.getResources().getIdentifier("big_" + id, "drawable", "charlie.pokedex"));
            try {
                JSONObject object = new JSONObject(result);
                JSONArray types = object.getJSONArray("types");
                if (types.length() == 1) {
                    JSONObject type = types.getJSONObject(0);
                    String typeName = type.getString("name");
                    String uri = type.getString("resource_uri");
                    String[] tokens = uri.split("/");
                    typeArray.add(tokens[3]);
                    type1.setImageResource(activity.getResources().getIdentifier(typeName, "drawable", "charlie.pokedex"));
                } else if (types.length() == 2) {
                    JSONObject typeOne = types.getJSONObject(0);
                    JSONObject typeTwo = types.getJSONObject(1);
                    String nameOne = typeOne.getString("name");
                    String nameTwo = typeTwo.getString("name");
                    String uriOne = typeOne.getString("resource_uri");
                    String uriTwo = typeTwo.getString("resource_uri");
                    String[] tokensOne = uriOne.split("/");
                    String[] tokensTwo = uriTwo.split("/");
                    typeArray.add(tokensOne[4]);
                    typeArray.add(tokensTwo[4]);
                    type1.setImageResource(activity.getResources().getIdentifier(nameOne, "drawable", "charlie.pokedex"));
                    type2.setImageResource(activity.getResources().getIdentifier(nameTwo, "drawable", "charlie.pokedex"));
                }

                int hp = object.getInt("hp");
                hpBar.setProgress(hp);
                hpVal.setText(String.valueOf(hp));
                int attack = object.getInt("attack");
                attackBar.setProgress(attack);
                attackVal.setText(String.valueOf(attack));
                int defense = object.getInt("defense");
                defenseBar.setProgress(defense);
                defenseVal.setText(String.valueOf(defense));
                int special = object.getInt("sp_atk");
                specialBar.setProgress(special);
                specialVal.setText(String.valueOf(special));
                int speed = object.getInt("speed");
                speedBar.setProgress(speed);
                speedVal.setText(String.valueOf(speed));

                for (int i = 0; i < typeArray.size(); i++) {
                    new TypeRequest().execute(typeArray.get(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class TypeRequest extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String id = params[0];

            try {
                URL url = new URL("http://pokeapi.co/api/v1/type/" + id + "/");
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
        protected void onPostExecute(String result) {
            loadingScreen.setVisibility(View.GONE);
            try {
                JSONObject object = new JSONObject(result);
                JSONArray weak = object.getJSONArray("weakness");
                JSONArray effective = object.getJSONArray("super_effective");

                for (int i = 0; i < weak.length(); i++) {
                    JSONObject temp = weak.getJSONObject(i);

                }
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
        getMenuInflater().inflate(R.menu.menu_pokemon, menu);
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
