// Sulatan

package balikbayan.box.sulatan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_ALL_FILES_ACCESS_CODE =  9901;
    private final int REQUEST_EXTERNAL_STORAGE_CODE = 9902;

    private View view1 = null;
    private String pathname, filename;

    private EditText editText1;

    private boolean newfile, enable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // remove underline      attributes background     @android:color/transparent
        // move to first line    attributes gravity        top
        editText1 = findViewById(R.id.editText1);

        // tanungin ang user kung iaallow na maaccess ang storage
        // ito ay para lang sa Allow access to media only
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE_CODE);

            Log.d("KLGYN", "REQUEST PERMISSION");

            return;
        }

        // aalamin nito kung nakaenable ang Allow management of all files
        // kung hindi ioopen nito ang dialog
        // ito ay hindi mabablock kaya ang line kasunod nito ay maeexecute
        if (Environment.isExternalStorageManager()) {

            Log.d("KLGYN", "PERMISSION GRANTED");

            openDefaultFile();

        } else {
            Log.d("KLGYN", "PERMISSION DENIED");

            openDialog();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_ALL_FILES_ACCESS_CODE) {

            // ang value ng resultCode ay laging RESULT_CANCEL kaya ito ang ginamit ko
            if (Environment.isExternalStorageManager()) {
                Log.d("KLGYN", "ACTIVITY RESULT OK");

                openDefaultFile();

            } else {
                Log.d("KLGYN", "ACTIVITY RESULT CANCEL");

                enable = false;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_EXTERNAL_STORAGE_CODE) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("KLGYN", "REQUEST GRANTED");
                Log.d("KLGYN", "OPEN ACTIVITY");

                openDialog();

            } else {
                Log.d("KLGYN", "REQUEST DENIED");

                enable = false;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        menu.findItem(R.id.mnuNew).setEnabled(enable);
        menu.findItem(R.id.mnuOpen).setEnabled(enable);
        menu.findItem(R.id.mnuSave).setEnabled(enable);
        menu.findItem(R.id.mnuSaveAs).setEnabled(enable);
        menu.findItem(R.id.mnuDelete).setEnabled(enable);

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

    // gumawa ng bagong file
    private void onFileNew() {
        newfile = true;
        editText1.setText("");
        filename = "";

        // ilagay ang app name sa title bar
        String app_name =  getResources().getString(R.string.app_name);
        setTitle(app_name);
    }

    // ilagay sa edit text ang inopeng file
    private void onFileOpen() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Open File");

        ListView listView1 = new ListView(this);
        CustomArrayAdapter adapter = new CustomArrayAdapter(this, R.layout.adapter_view_layout);
        enumFiles(pathname, adapter);
        listView1.setAdapter(adapter);
        builder.setView(listView1);

        // open button
        builder.setPositiveButton("open", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // iopen ang file at ilagay sa edit text
                newfile = false;
                String[] str = new String[1];
                open(pathname, filename, str);
                editText1.setText(str[0]);

                // ilagay ang filename sa title bar
                String app_name =  getResources().getString(R.string.app_name);
                setTitle(app_name + " - " + filename);
            }
        });

        // cancel button
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        // dito to ilagay pagkatapos ng dialog.show kasi mag-eerror
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

        // ang long click ay para i-highlight ang selected item
        listView1.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                // alisin ang backgound color ng dating highligthed item
                if(view1 != null) view1.setBackgroundColor(Color.TRANSPARENT);

                // i-highlight ang napiling item at i save ang view na 'to
                view.setBackgroundColor(Color.LTGRAY);
                view1 = view;

                // kunin ang client na naka highlight
                CustomArrayAdapter adapter = (CustomArrayAdapter) parent.getAdapter();
                filename = adapter.getItem(position).getStr1();

                // ang item ay selected
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);

                // true - ang event na 'to ay di na ipapasa sa ibang event listener
                return true;
            }
        });

        // ang click ay para alisin ang highlight
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // alisin ang backgound color ng highligthed item
                if(view1 != null) view1.setBackgroundColor(Color.TRANSPARENT);

                // ang item ay hindi selected
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });
    }

    // isave ang string na nasa edit text
    private void onFileSave() {

        String str = editText1.getText().toString();

        // kumuha ng bagong pangalan para sa bagong file
        if(newfile) {

            filename = getUniqueName();

            // ilagay ang filename sa title bar
            String app_name =  getResources().getString(R.string.app_name);
            setTitle(app_name + " - " + filename);
        }

        //
        save(pathname, filename, str);
    }

    private void onFileSaveAs() {

    }

    private void onToolsDelete() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to permanently delete " + filename + " file?");

        // yes button
        builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                // idelete ang current file
                File file = new File(pathname, filename);

                if(file.delete()) {
                    onFileNew();
                    Toast.makeText(getApplicationContext(), "NA DELETE NA", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "DI KO MADELETE", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // no button
        builder.setNegativeButton("no", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }

    // allow all files access dialog
    private void openDialog() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, REQUEST_ALL_FILES_ACCESS_CODE);
    }

    //
    private void openDefaultFile() {

        newfile = false;
        enable = true;

        pathname = getExternalDrive() + "/mga_data/sulatan";
        File file = getLatestFile(pathname);
        filename = file.getName();
        String[] str = new String[1];
        open(file, str);

        editText1.setText(str[0]);

        // ilagay ang filename sa title bar
        String app_name =  getResources().getString(R.string.app_name);
        setTitle(app_name + " - " + filename);

    }

    // kunin ang pathname ng external sd card
    private String getExternalDrive() {
        File[] files;
        String strg = "storage";
        String str1, str2;
        int i, k;

        files = getExternalMediaDirs();
        str1 =  files[1].getParent();
        k = str1.indexOf(strg) + strg.length() + 1;
        i = str1.indexOf("/", k);
        str2 = str1.substring(0, i);

        return str2;
    }

    //
    private String getUniqueName() {
        Calendar c;
        int YYYY, MM, DD, hh, mm, ss;
        String str;

        c = Calendar.getInstance();
        YYYY = c.get(Calendar.YEAR);
        MM = c.get(Calendar.MONTH);
        DD  = c.get(Calendar.DAY_OF_MONTH);
        hh  = c.get(Calendar.HOUR_OF_DAY);
        mm  = c.get(Calendar.MINUTE);
        ss  = c.get(Calendar.SECOND);
        str = String.format("%d%02d%02d_%02d%02d%02d.txt",YYYY,MM+1,DD,hh,mm,ss);

        return str;
    }

    // kunin ang mga file na nasa directory pathname
    // ilagay ang mga ito sa array
    private void enumFiles(String pathname, CustomArrayAdapter array) {
        File path;
        File[] files;
        Calendar c;
        int YYYY, MM, DD, hh, mm, ss;
        String str1, str2, str3;
        CustomElement element;

        path = new File(pathname);
        files = path.listFiles();

        for (File file : files) {

            str1 = file.getName();

            c = Calendar.getInstance();
            c.setTimeInMillis(file.lastModified());
            YYYY = c.get(Calendar.YEAR);
            MM = c.get(Calendar.MONTH);
            DD  = c.get(Calendar.DAY_OF_MONTH);
            hh  = c.get(Calendar.HOUR_OF_DAY);
            mm  = c.get(Calendar.MINUTE);
            ss  = c.get(Calendar.SECOND);
            str2 = String.format("%02d-%02d-%04d %02d:%02d:%02d",MM,DD,YYYY,hh,mm,ss);

            str3 = String.format("%d",file.length());

            if(file.isFile()) {
                element = new CustomElement(str1, str2, str3);
                array.add(element);
            }
        }
    }

    // kunin ang pinakabagong file na nasa directory pathname
    private File getLatestFile(String pathname) {

        File file = new File(pathname);
        File files[] = file.listFiles();

        int i, i1, i2, n, r;
        String str1, str2;

        n = files.length;
        i1 = 0;
        str1 = files[0].getName();
        for(i=0; i<n; i++) {

            i2 = i;
            str2 = files[i].getName();

            r = str1.compareToIgnoreCase(str2);

            if(r > 0)
                i1 = i2;
        }

        return  files[i1];
    }

    // isave ang string str sa file
    private void save(String pathname, String filename, String str) {

        File file;
        FileOutputStream fos;
        byte[] b;

        try {
            file = new File(pathname, filename);
            fos = new FileOutputStream(file);
            b = str.getBytes();
            fos.write(b);
            fos.close();

            Toast.makeText(this, "NA SAVE NA", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // iopen ang file, ilagay ito sa string str
    private void open(String pathname, String filename, String[] str) {

        File file;
        FileInputStream fis;
        int n;
        byte[] b;

        try {
            file = new File(pathname, filename);
            fis = new FileInputStream(file);
            n = fis.available();
            b = new byte[n];
            fis.read(b);
            fis.close();

            str[0] = new String(b);

        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    // iopen ang file, ilagay ito sa string str
    private void open(File file, String[] str) {

        FileInputStream fis;
        int n;
        byte[] b;

        try {
            fis = new FileInputStream(file);
            n = fis.available();
            b = new byte[n];
            fis.read(b);
            fis.close();

            str[0] = new String(b);

        } catch (IOException e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
