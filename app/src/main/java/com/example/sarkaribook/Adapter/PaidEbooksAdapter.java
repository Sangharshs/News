package com.example.sarkaribook.Adapter;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.sarkaribook.AllActivities.PDFViewActivity;
import com.example.sarkaribook.AllActivities.SubscriptionsActivity;
import com.example.sarkaribook.Model.Ebook;
import com.example.sarkaribook.R;
import com.example.sarkaribook.TinyDB;
import com.example.sarkaribook.ui.home.BottomSheetSubscriptionFragment;

import java.util.List;

public class PaidEbooksAdapter extends RecyclerView.Adapter<PaidEbooksAdapter.viewHolder> {
        List<Ebook> ebookList;
        Context context;
        TinyDB tinyDB;

public PaidEbooksAdapter(List<Ebook> ebookList, Context context) {
        this.ebookList = ebookList;
        this.context = context;
        tinyDB = new TinyDB(context);
        }

@NonNull
@Override
public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ebook,parent,false);
        return new viewHolder(v);
        }

@Override
public void onBindViewHolder(@NonNull viewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.textView.setText(ebookList.get(position).title);
        holder.pdfDesc.setText(ebookList.get(position).description);
holder.indexNumText.setText(String.valueOf(String.valueOf(position)));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tinyDB.getBoolean("isSubscribed")){
//
//                    Intent intent = new Intent(view.getContext(), PDFViewActivity.class);
//                    intent.putExtra("path",ebookList.get(position).getFile_url());
//                    view.getContext().startActivity(intent);

                }else{

//                    BottomSheetSubscriptionFragment bottomSheet = new BottomSheetSubscriptionFragment();
//                    bottomSheet.show(context.getApplicationContext().getFragmentManager(),
//                            "ModalBottomSheet");

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                        builder1.setMessage("Please Buy Subscription First");
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                "Buy Subscription",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent intent = new Intent(context, SubscriptionsActivity.class);
                                        context.startActivity(intent);
                                    }
                                });

                        builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.dismiss();
                            }
                        });

                        AlertDialog alert11 = builder1.create();
                        alert11.setCancelable(false);
                        alert11.show();
                        Toast.makeText(context, "Buy Subscription", Toast.LENGTH_SHORT).show();
                }
           }
        });


        holder.imageView.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View view) {

   if( tinyDB.getBoolean("isSubscribed") ){
        Toast.makeText(context, "Download Start", Toast.LENGTH_SHORT).show();
        String getUrl = ebookList.get(position).file_url;

        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(getUrl));
        request.setTitle("SL_" + ebookList.get(position).title);
        request.setMimeType("application/pdf");
        request.setDescription("Description");
        request.setAllowedOverMetered(true);
        request.allowScanningByMediaScanner();
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOCUMENTS, "SL_" + ebookList.get(position).title);
        DownloadManager dm = (DownloadManager) view.getContext().getSystemService(DOWNLOAD_SERVICE);
        dm.enqueue(request);
    }else{
       AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
       builder1.setMessage("Please Buy Subscription First");
       builder1.setCancelable(true);

       builder1.setPositiveButton(
               "Buy Subscription",
               new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       Intent intent = new Intent(context, SubscriptionsActivity.class);
                       context.startActivity(intent);

                   }
               });
       builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
           }
       });

       AlertDialog alert11 = builder1.create();
       alert11.setCancelable(false);
       alert11.show();
       Toast.makeText(context, "Buy Subscription", Toast.LENGTH_SHORT).show();
       Toast.makeText(context, "Buy Subscription Plan For This", Toast.LENGTH_SHORT).show();
   }
}
        });
        }

@Override
public int getItemCount() {
        return ebookList.size();
        }

public class viewHolder extends RecyclerView.ViewHolder {
    TextView textView,pdfDesc,indexNumText;
    ImageView imageView;
    public viewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.pdfTitle);
        pdfDesc = itemView.findViewById(R.id.pdfDescription);
        imageView = itemView.findViewById(R.id.downloadBtn);
        indexNumText = itemView.findViewById(R.id.indexNumTextView);

    }

}


}
