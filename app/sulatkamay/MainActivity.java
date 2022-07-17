
package balikbayan.box.sulatkamay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private CustomView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = new CustomView(getApplicationContext());
        setContentView(view);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.mnuOpen: onFileOpen(); break;
            case R.id.mnuSave: onFileSave(); break;
            case R.id.mnuSaveAs: onFileSaveAs(); break;
            case R.id.mnuUndo:  onEditUndo();  break;
            case R.id.mnuClear:  onEditClear();  break;
            case R.id.mnuLight:  onToolsLightPen();  break;
            case R.id.mnuHeavy:  onToolsHeavyPen();  break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    private void onFileOpen() {

    }

    private void onFileSave() {

    }

    private void onFileSaveAs() {

    }

    private void onEditUndo() {

    }

    private void onEditClear() {

    }

    private void onToolsLightPen() {

    }

    private void onToolsHeavyPen() {

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    }

}
