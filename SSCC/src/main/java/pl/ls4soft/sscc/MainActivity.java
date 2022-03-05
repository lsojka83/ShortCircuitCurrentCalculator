package pl.ls4soft.sscc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements Serializable{

    private File currentDir;
    private FileArrayAdapter adapter;

    private ListView listViewFiles;

    Bundle bundleOut = new Bundle();
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        intent = new Intent(getApplicationContext(), CalcActivity.class);

        listViewFiles = (ListView) findViewById(R.id.listViewFiles);
        currentDir = new File(Environment.getExternalStorageDirectory().toString()+"/SCCurrent");
        fill(currentDir);


        //create new diagram
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentA = new Intent(getApplicationContext(), CalcActivity.class);
                startActivity(intentA);
            }
        });

    listViewFiles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            onListItemClick(view,position,id);

       }
    });

        listViewFiles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                return onLongListItemClick(view,position,id);
//                return false;
            }
        });
    }

    protected void onListItemClick(View view, int position, long id) {

//        try {

            bundleOut.putString("path", adapter.getItem(position).getPath());
            bundleOut.putString("name", adapter.getItem(position).getName());
            intent.putExtras(bundleOut);
            startActivity(intent);
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this, R.string.old_file, Toast.LENGTH_SHORT).show();
//        }
    }

    int auxPosition;
    protected boolean onLongListItemClick(View view, int position, long id) {

        auxPosition = position;

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        File file = new File(files.get(auxPosition).getPath());
                        file.delete();
                        fill(currentDir);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(getString(R.string.delete_file)).setPositiveButton(getString(R.string.yes_answer), dialogClickListener)
                .setNegativeButton(getString(R.string.no_answer), dialogClickListener).show();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_about) {

            Intent intentAbout = new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intentAbout);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    List<Option> dir ;
    static List<Option> files;

    private void fill(File f) {
        File[] dirs = f.listFiles();

       dir = new ArrayList<>();
        files = new ArrayList<>();
        try {
            for (File ff : dirs) {
                if (ff.isDirectory())
                    dir.add(new Option(ff.getName(), "Folder", ff.getAbsolutePath()));
                else {
                    files.add(new Option(ff.getName(), getString(R.string.main_creation_date) + checkFileLastModificationDate(ff), ff.getAbsolutePath()));
                }
            }
        } catch (Exception e) {

        }
        Collections.sort(dir);
        Collections.sort(files);
        dir.addAll(files);
        adapter = new FileArrayAdapter(MainActivity.this, R.layout.file_view, dir);
        listViewFiles.setAdapter(adapter);
    }

    private String checkFileLastModificationDate(File file)
    {
        Date lastModDate = new Date(file.lastModified());
        return lastModDate.toString();
    }


    public static int checkLastFileNumberName()
    {
        int actualNubmer = 0, bigerNumer = 0;
        int dotPosition = 0;

        String name, numberInName;
    try {
    if (files.size() > 0) {
        for (int i = 0; i < files.size(); i++) {
            name = files.get(i).getName();
            dotPosition = name.indexOf('.');
            numberInName = name.substring(dotPosition + 1, name.length());
//            System.out.println("!!!!!!!!!!!" + numberInName);
            actualNubmer = Integer.valueOf(numberInName);
            if (actualNubmer > bigerNumer)
                bigerNumer = actualNubmer;
        }
    }
    }
    catch (Exception e)
    {}
        return bigerNumer;
    }

    @Override
    protected void onStart() {
        Log.d("aktywność", "onStart");
        super.onStart();
    }

    @Override
    protected void onResume() {
        fill(currentDir);

        Log.d("aktywność", "onResume");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d("aktywność", "onPause");
        super.onPause();
    }

    @Override
    protected void onRestart() {
        Log.d("aktywność", "onRestart");

        super.onRestart();
    }

    @Override
    protected void onStop() {
        Log.d("aktywność", "onStop");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        Log.d("aktywność", "onDestroy");
        super.onDestroy();
    }
}
