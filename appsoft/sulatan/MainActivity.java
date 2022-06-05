// Sulatan

package balikbayan.box.sulatan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.mnuNew:       onFileNew();        break;
            case R.id.mnuOpen:      onFileOpen();       break;
            case R.id.mnuSave:      onFileSave();       break;
            case R.id.mnuSaveAs:    onFileSaveAs();     break;
            case R.id.mnuDelete:    onToolsDelete();    break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void onFileNew() {

    }

    private void onFileOpen() {

    }

    private void onFileSave() {

    }

    private void onFileSaveAs() {

    }

    private void onToolsDelete() {

    }
}
