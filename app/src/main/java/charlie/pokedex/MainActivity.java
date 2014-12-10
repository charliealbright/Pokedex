package charlie.pokedex;

import android.content.res.Resources;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    ListView listView;
    CustomAdapter adapter;
    public MainActivity activity = null;
    public ArrayList<PokedexListItem> pokedex = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        setFakeData();

        Resources resources = getResources();
        listView = (ListView)findViewById(R.id.pokedexListView);

        adapter = new CustomAdapter(this, pokedex, resources);
        listView.setAdapter(adapter);
    }

    private void setFakeData() {
        for (int i = 1; i <= 10; i++) {
            final PokedexListItem item = new PokedexListItem("Pokemon " + String.valueOf(i), String.valueOf(i));
            pokedex.add(item);
        }
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
