package com.example.sarkaribook.ui.home;

import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.sarkaribook.Adapter.DownloadedItemsAdapter;
import com.example.sarkaribook.Model.Category;
import com.example.sarkaribook.R;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DownloadFragment extends Fragment {


    public DownloadFragment() {
        // Required empty public constructor
    }

    RecyclerView downloadedItemsRecyclerView;
    View v;
    List<File> pdfFilesList;
    DownloadedItemsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_download, container, false);
        downloadedItemsRecyclerView = v.findViewById(R.id.downloadedItemsRecyclerView);



        displaydownloadedPdfList();



        // Inflate the layout for this fragment
        return v;
    }

    public ArrayList<File> findPdf(File file){
        ArrayList<File> arrayList = new ArrayList<>();
        File[] files = file.listFiles();

        for(File singleFile: files){

            if(singleFile.isDirectory() && !singleFile.isHidden()){
                arrayList.addAll(findPdf(singleFile));
            }else{
                Log.e("FILENAMES",String.valueOf(singleFile.getName().endsWith("pdf")));
               // if( singleFile.getName().startsWith("SL_")){
                    arrayList.add(singleFile);
               // }
            }
        }
        return arrayList;
    }

    private void displaydownloadedPdfList() {

        downloadedItemsRecyclerView = v.findViewById(R.id.downloadedItemsRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(v.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        downloadedItemsRecyclerView.setLayoutManager(linearLayoutManager);
        pdfFilesList = new ArrayList<>();
//      getExternalStorageDirectory().getAbsolutePath(), folder_main
        String folder_main = "SarkariLibrary";
       // pdfFilesList.addAll(findPdf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)));
       //                               request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "SarakariLibrary/" + ebookList.get(position).title);
        pdfFilesList.addAll(findPdf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/SarakariLibrary/")));
        Log.e("FILES",String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS+"/SarakariLibrary/")));
        adapter = new DownloadedItemsAdapter(pdfFilesList,getContext());
        downloadedItemsRecyclerView.setAdapter(adapter);
    }




}