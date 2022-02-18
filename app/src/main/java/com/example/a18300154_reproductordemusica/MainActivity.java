package com.example.a18300154_reproductordemusica;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView lv_musica;
    String[] items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv_musica = (ListView) findViewById(R.id.lv_musica);

        Permisos();
    }

    public void Permisos(){
        Dexter.withContext(this).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        anotarCanciones();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public ArrayList<File> encontrarCancion(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singlefile: files){
            if(singlefile.isDirectory() && !singlefile.isHidden()){
                arrayList.addAll(encontrarCancion(singlefile));
            }
            else{
                if(singlefile.getName().endsWith(".mp3") || singlefile.getName().endsWith(".wav")){
                    arrayList.add(singlefile);
                }
            }
        }
        return arrayList;
    }

    void anotarCanciones(){
        final ArrayList<File> cancion = encontrarCancion(Environment.getExternalStorageDirectory());

        items = new String[cancion.size()];

        for(int i = 0; i < cancion.size(); i++){
            items[i] = cancion.get(i).getName().toString().replace(".mp3", "").replace("wav", "");
        }
        //ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        //lv_musica.setAdapter(adaptador);

        customAdaptador customAdaptador = new customAdaptador();
        lv_musica.setAdapter(customAdaptador);
    }

    class customAdaptador extends BaseAdapter{

        @Override
        public int getCount() {
            return items.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View myView = getLayoutInflater().inflate(R.layout.list_item, null);
            TextView text = myView.findViewById(R.id.nombre);
            text.setSelected(true);
            text.setText(items[i]);

            return myView;
        }
    }
}