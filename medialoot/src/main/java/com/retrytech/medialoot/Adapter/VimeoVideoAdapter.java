package com.retrytech.medialoot.Adapter;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.retrytech.medialoot.Activity.Constant;
import com.retrytech.medialoot.Activity.FileSizes;
import com.retrytech.medialoot.Activity.SessionManager;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.model.VimeoModel;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.FileDetailLayBinding;
import com.retrytech.medialoot.databinding.VimeoDownloadLayoutBinding;
import com.walhalla.ui.DLog;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.CLIPBOARD_SERVICE;

public class VimeoVideoAdapter extends RecyclerView.Adapter<VimeoVideoAdapter.RecyclerViewHolder> {
    private static int viewtypeinit;
    Context context;
    List<VimeoModel> vimeoModelList;


    public VimeoVideoAdapter(Context context, List<VimeoModel> vimeoModelList) {
        this.context = context;
        this.vimeoModelList = vimeoModelList;
    }


    public int getItemViewType(int i) {
        return super.getItemViewType(i);
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecyclerViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vimeo_download_layout, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, final int position) {
        holder.bindView(position);

        VimeoModel vimeoModel = vimeoModelList.get(position);

        holder.binding.txtQuality.setText(vimeoModel.getFormat());
        holder.binding.progressBar.setVisibility(View.VISIBLE);
        holder.binding.downloadBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                FileDetailLayBinding fileDetailLayBinding = FileDetailLayBinding.inflate(LayoutInflater.from(context), null, false);
                fileDetailLayBinding.etFileName.setText(String.valueOf(System.currentTimeMillis()));
                fileDetailLayBinding.etVideoUrl.setText(vimeoModel.getDownurl());
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                //give YourVideoUrl below
                retriever.setDataSource(vimeoModel.getDownurl(), new HashMap<String, String>());
// this gets frame at 2nd second
                Bitmap image = retriever.getFrameAtTime(2000000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                fileDetailLayBinding.imgThumb.setImageBitmap(image);
                fileDetailLayBinding.tvCopy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("label", fileDetailLayBinding.etVideoUrl.getText().toString().trim());
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(clip);
                            Toast.makeText(context, "copied text", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                final String[] filename = {fileDetailLayBinding.etFileName.getText().toString().trim() + ".mp4"};
                final String[] videoPath = {"file://" + SessionManager.getFolderPath(context) + filename[0]};
                final String[] savePath = {SessionManager.getFolderPath(context) + "/" + filename[0]};
                fileDetailLayBinding.savePath.setText(savePath[0]);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
                alertDialog.setView(fileDetailLayBinding.getRoot());
                alertDialog.setPositiveButton("Download", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(Objects.requireNonNull(context));
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Constant.download);
//            builder.setContentTitle("Video Download")
//                    .setContentText("Download in progress")
//                    .setSmallIcon(R.drawable.video)
//                    .setPriority(NotificationCompat.PRIORITY_HIGH);
                        int PROGRESS_MAX = 100;
                        filename[0] = fileDetailLayBinding.etFileName.getText().toString().trim() + ".mp4";
                        videoPath[0] = "file://" + SessionManager.getFolderPath(context) + filename[0];
                        savePath[0] = SessionManager.getFolderPath(context) + "/" + filename[0];
                        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(vimeoModel.getDownurl()));
                        request.setTitle("Video Download");
                        request.allowScanningByMediaScanner();
                        request.setDestinationUri(Uri.parse(videoPath[0]));
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                        final DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);


                        final long downloadId = manager.enqueue(request);
                        Toast.makeText(context, "Download Start", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent("string");

                        intent.putExtra("isdownload", true);
                        // put your all data using put extra

                        LocalBroadcastManager.getInstance(Objects.requireNonNull(context)).sendBroadcast(intent);

                        new Thread(new Runnable() {

                            @Override
                            public void run() {

                                boolean downloading = true;

                                while (downloading) {

                                    DownloadManager.Query q = new DownloadManager.Query();
                                    q.setFilterById(downloadId);

                                    Cursor cursor = manager.query(q);
                                    cursor.moveToFirst();
                                    if (cursor.getCount() != 0) {
                                        int bytes_downloaded = cursor.getInt(cursor
                                                .getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                                        int bytes_total = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));

                                        if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)) == DownloadManager.STATUS_SUCCESSFUL) {
                                            downloading = false;
                                        }

                                        final int dl_progress = (int) ((bytes_downloaded * 100l) / bytes_total);

                                        long fileSizeInKB = bytes_total / 1024;
                                        float totalizeInMB = fileSizeInKB / 1024;
                                        float totalizeInGB = totalizeInMB / 1024;
                                        long SizeInKB = bytes_downloaded / 1024;
                                        long downloadSizeInMB = SizeInKB / 1024;

                                        String totalSize = "";
                                        String sizeDownload = "";
                                        if (downloadSizeInMB == 0) {
                                            sizeDownload = SizeInKB + "KB";
                                        } else {
                                            sizeDownload = downloadSizeInMB + " MB";
                                        }
                                        if (totalizeInGB > 1) {
                                            totalSize = String.format("%.2f", totalizeInGB).replace(".00", "") + " GB";
                                        } else if (totalizeInMB > 1) {
                                            totalSize = String.format("%.2f", totalizeInMB).replace(".00", "") + " MB";
                                        } else {
                                            totalSize = fileSizeInKB + " KB";
                                        }

                                        if (dl_progress >= 0 && dl_progress <= 100) {
                                            Intent intent = new Intent("filter_string");
                                            DownloadFile downloadFile = new DownloadFile();
                                            downloadFile.setId(downloadId);
                                            downloadFile.setFilename(filename[0]);
                                            downloadFile.setTotalSize(totalSize);
                                            downloadFile.setCompleteSize(sizeDownload);
                                            downloadFile.setProgress((int) dl_progress);
                                            downloadFile.setPath(savePath[0]);
                                            intent.putExtra("progress", downloadFile);
                                            // put your all data using put extra

                                            LocalBroadcastManager.getInstance(Objects.requireNonNull(context)).sendBroadcast(intent);
                                        }
                                    } else {
                                        Intent intent = new Intent("filter_string");
                                        DownloadFile downloadFile = new DownloadFile();
                                        downloadFile.setId(downloadId);
                                        downloadFile.setFilename(filename[0]);
                                        downloadFile.setProgress(101);
                                        downloadFile.setPath(savePath[0]);
                                        intent.putExtra("progress", downloadFile);
                                        LocalBroadcastManager.getInstance(Objects.requireNonNull(context)).sendBroadcast(intent);
                                    }
                                    cursor.close();
                                }

                            }
                        }).start();
                    }
                }).setNegativeButton("Cancel", null);
                alertDialog.show();
            }
        });
    }


    public long getItemId(int i) {
        return super.getItemId(i);
    }

    @Override
    public int getItemCount() {
        return this.vimeoModelList.size();
    }

//    public static Activity scanForActivity(Context context) {
//        if (context == null) {
//            return null;
//        }
//        if (context instanceof Activity) {
//            return (Activity) context;
//        }
//        if (context instanceof ContextWrapper) {
//            return scanForActivity(((ContextWrapper) context).getBaseContext());
//        }
//        return null;
//    }

//    public static String videoSizeCalculate(long j) {
//        DecimalFormat decimalFormat = new DecimalFormat("0.00");
//        float f = (float) j;
//        if (f < 1048576.0f) {
//            StringBuilder sb = new StringBuilder();
//            sb.append(decimalFormat.format((double) (f / 1024.0f)));
//            sb.append(" Kb");
//            return sb.toString();
//        } else if (f < 1.07374182E9f) {
//            StringBuilder sb2 = new StringBuilder();
//            sb2.append(decimalFormat.format((double) (f / 1048576.0f)));
//            sb2.append(" Mb");
//            return sb2.toString();
//        } else if (f >= 1.09951163E12f) {
//            return "";
//        } else {
//            StringBuilder sb3 = new StringBuilder();
//            sb3.append(decimalFormat.format((double) (f / 1.07374182E9f)));
//            sb3.append(" Gb");
//            return sb3.toString();
//        }
//    }

//    public static long getFolderSize(File file) {
//        if (!file.isDirectory()) {
//            return file.length();
//        }
//        long j = 0;
//        for (File folderSize : file.listFiles()) {
//            j += getFolderSize(folderSize);
//        }
//        return j;
//    }

//    private boolean hasImage(@NonNull ImageView imageView) {
//        Drawable drawable = imageView.getDrawable();
//        boolean z = drawable != null;
//        if (!z || !(drawable instanceof BitmapDrawable)) {
//            return z;
//        }
//        return ((BitmapDrawable) drawable).getBitmap() != null;
//    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder {
        VimeoDownloadLayoutBinding binding;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = VimeoDownloadLayoutBinding.bind(itemView);
        }

        @SuppressLint({"StaticFieldLeak"})
        public void bindView(final int i) {
            try {
                new AsyncTask<String, String, String>() {

                    public String doInBackground(String... strings) {
                        if (i < vimeoModelList.size()) {
                            try {
                                int urlLenth = new URL(((VimeoModel) vimeoModelList.get(i)).getDownurl()).openConnection().getContentLength();
                                return "" + FileSizes.getHumanReadableSize(urlLenth, context).toString();
                            } catch (IOException e) {
                                DLog.handleException(e);
                            }

                        }
                        return null;
                    }

                    public void onPostExecute(String str) {
                        super.onPostExecute(str);
                        if (str != null) {
                            try {
                                binding.progressBar.setVisibility(View.GONE);
                                binding.videoSize.setText(str);
                            } catch (Exception e) {
                                DLog.handleException(e);
                            }
                        }
                    }
                }.execute(new String[]{((VimeoModel) vimeoModelList.get(i)).getDownurl()});
            } catch (Exception e) {
                DLog.handleException(e);
            }
        }
    }
}
