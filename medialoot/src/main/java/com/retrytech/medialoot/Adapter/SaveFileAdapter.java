package com.retrytech.medialoot.Adapter;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.retrytech.medialoot.MediaTools;
import com.retrytech.medialoot.model.DownloadFile;
import com.retrytech.medialoot.R;
import com.retrytech.medialoot.databinding.FileItemBinding;
import com.walhalla.ui.DLog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SaveFileAdapter extends RecyclerView.Adapter<SaveFileAdapter.SaveFileViewHolder> {
    private File[] allFiles;
    private ArrayList<DownloadFile> downloadFileArrayList;
    private Context context;
    OnItemClickListener listener;
    private ArrayList<String> selectDeleted = new ArrayList<>();
    public boolean isMultiselect = false;


    public SaveFileAdapter(File[] allFiles, ArrayList<DownloadFile> downloadFileArrayList, OnItemClickListener listener) {
        this.allFiles = allFiles;
        this.downloadFileArrayList = downloadFileArrayList;
        this.listener = listener;

    }

    public interface OnItemClickListener {
        void onItemClick(SaveFileViewHolder holder, int position, DownloadFile downloadFile);

        void onMultidelete(boolean ischeck);

        void onItemRename();
    }

    public void remove(int position) {
        this.downloadFileArrayList.remove(position);
        notifyItemRemoved(position);
    }

    public void update(List<DownloadFile> data) {
        this.downloadFileArrayList.clear();
        this.downloadFileArrayList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SaveFileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        FileItemBinding binding = FileItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        context = parent.getContext();
        return new SaveFileViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SaveFileViewHolder holder, int position) {
        DownloadFile file = downloadFileArrayList.get(position);
        holder.binding.tvTitle.setText(file.getFilename());


        if (file.getAddedDate() != null&&file.getDuration()!=null) {

            holder.binding.duration.setText(getDate(Long.parseLong(file.getAddedDate())) + " " + convertMillieToHMmSs(Long.parseLong(file.getDuration())));
        }
        Glide.with(context)
                .asBitmap()
                .load(Uri.fromFile(new File(file.getPath())))
                .placeholder(R.drawable.placce_holder_video)
                .error(R.drawable.placce_holder_video)
                .into(holder.binding.img);

        long fileSize = (Long.parseLong(file.getTotalSize()) / 1024);
        float mbSize = fileSize / 1024;
        float gbSize = mbSize / 1024;
        if (gbSize > 1) {
            holder.binding.tvSize.setText(String.format("%.2f", gbSize).replace(".00", "") + " GB");
        } else if (mbSize > 1)
            holder.binding.tvSize.setText(String.format("%.2f", mbSize).replace(".00", "") + " MB");
        else
            holder.binding.tvSize.setText(fileSize + " KB");

        holder.binding.multiSelect.setChecked(file.isSelected());

        holder.binding.multiSelect.setTag(file);
        if (isMultiselect) {
            holder.binding.multiSelect.setVisibility(View.VISIBLE);
        } else {
            holder.binding.multiSelect.setVisibility(View.GONE);
        }

        if (checkFileisInArray(file) == -1) {
            holder.binding.multiSelect.setChecked(false);
        } else {
            holder.binding.multiSelect.setChecked(true);
        }

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                isMultiselect = true;
                listener.onMultidelete(true);
                selectDeleted.add(file.getPath());

                notifyDataSetChanged();

//                if (isChecked) {
//                    groupUsers.setUserid(mapFriend.getId());
//                    groupUsers.setuserName(mapFriend.getUsername());
//                    userList.add(groupUsers);

//                    addedMem.setVisibility(View.VISIBLE);
//
//
//                }else {
//                    groupUsers.setUserid(mapFriend.getId());
//                    groupUsers.setuserName(mapFriend.getUsername());
//                    userList.remove(groupUsers);
//                    members.remove(mapFriend.getUsername());
//                }


                return false;
            }
        });

        holder.binding.actionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.binding.actionBtn);
                popupMenu.inflate(R.menu.actions);
                popupMenu.setOnMenuItemClickListener(menuItem -> {
                    int itemId = menuItem.getItemId();
                    if (itemId == R.id.play_action_btn) {
                        try {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file.getPath()));
                            intent.setDataAndType(Uri.parse(file.getPath()), "video/mp4");
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else if (itemId == R.id.remove_action_btn) {
                        File files = new File(file.getPath());
                        onRequestRemove(files, holder.getAdapterPosition());
                    } else if (itemId == R.id.rename_action_btn) {
                        View editTextContainer = LayoutInflater.from(context).inflate(R.layout.dialog_edit_text, null, false);
                        final EditText editText = (EditText) editTextContainer.findViewById(R.id.edit_text);
                        editText.setText(file.getFilename().substring(0, file.getFilename().lastIndexOf(".")));
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                .setView(editTextContainer)
                                .setTitle("Rename Video")
                                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        File parent = new File(file.getPath());
                                        File to = new File(parent.getParent(), editText.getText().toString() + ".mp4");
                                        if (parent.renameTo(to)) {
                                            scanFile(to);
                                            scanFile(parent);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    listener.onItemRename();
                                                }
                                            }, 700);
                                        }
//                    update(downloadFileArrayList);
                                    }
                                })
                                .setNegativeButton(android.R.string.cancel, null)
                                .create();
                        alertDialog.show();
                    } else if (itemId == R.id.share_action_btn) {
                        context.startActivity(MediaTools.buildSharedIntent(context, new File(file.getPath())));
                    } else if (itemId == R.id.moveFile) {
                        listener.onItemClick(holder, position, downloadFileArrayList.get(position));
//    moveVideo(file);
                    }

                    return true;
                });
                popupMenu.show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMultiselect) {
                    int temp = checkFileisInArray(file);
                    if (temp == -1) {
                        selectDeleted.add(file.getPath());
                    } else {
                        selectDeleted.remove(file.getPath());
                    }
                    if (selectDeleted.isEmpty()) {
                        isMultiselect = false;
                        listener.onMultidelete(false);
                    }
                    notifyDataSetChanged();
                } else {
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(file.getPath()));
                        intent.setDataAndType(Uri.parse(file.getPath()), "video/mp4");
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    public static String convertMillieToHMmSs(long millie) {
        long seconds = (millie / 1000);
        long second = seconds % 60;
        long minute = (seconds / 60) % 60;
        long hour = (seconds / (60 * 60)) % 24;

        String result = "";
        if (hour > 0) {
            return String.format("%02d:%02d:%02d", hour, minute, second);
        } else {
            return String.format("%02d:%02d", minute, second);
        }

    }

    private String getDate(long time) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(time * 1000);
        String date = DateFormat.format("dd-MM-yyyy", cal).toString();
        return date;
    }

    private void moveVideo(DownloadFile file) {
//        InputStream in = null;
//                OutputStream out = null;
//                try {
//
//                    //create output directory if it doesn't exist
//                    File dir = new File (Environment.DIRECTORY_MOVIES);
//                    if (!dir.exists())
//                    {
//                        dir.mkdirs();
//                    }
//
//
//                    in = new FileInputStream(file.getPath());
//                    out = new FileOutputStream(Environment.DIRECTORY_MOVIES);
//
//                    byte[] buffer = new byte[1024];
//                    int read;
//                    while ((read = in.read(buffer)) != -1) {
//                        out.write(buffer, 0, read);
//                    }
//                    in.close();
//                    in = null;
//
//                    // write the output file
//                    out.flush();
//                    out.close();
//                    out = null;
//
//                    // delete the original file
//                    new File(file.getPath()).delete();
//
//
//                }
//
//                catch (FileNotFoundException fnfe1) {
//                    Log.e("tag", fnfe1.getMessage());
//                }
//                catch (Exception e) {
//                    Log.e("tag", e.getMessage());
//                }

        String downloadPath = android.os.Environment
                .getExternalStorageDirectory().getAbsolutePath()
                + File.separator
                + "data" + File.separator;

        File newFile = new File(new File(downloadPath), file.getFilename());
        FileChannel outputChannel = null;
        FileChannel inputChannel = null;
        try {
            outputChannel = new FileOutputStream(newFile).getChannel();
            inputChannel = new FileInputStream(file.getPath()).getChannel();
            inputChannel.transferTo(0, inputChannel.size(), outputChannel);
            inputChannel.close();

            new File(file.getPath()).delete();

        } catch (FileNotFoundException e) {
            DLog.handleException(e);
        } catch (IOException e) {
            DLog.handleException(e);
        } finally {
            if (inputChannel != null) {
                try {
                    inputChannel.close();
                } catch (IOException e) {
                    DLog.handleException(e);
                }
            }
            if (outputChannel != null) {
                try {
                    outputChannel.close();
                } catch (IOException e) {
                    DLog.handleException(e);
                }
            }
        }

    }


    public void scanFile(File file) {
        Uri uri = Uri.fromFile(file);
        Intent scanFileIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
        context.sendBroadcast(scanFileIntent);
    }

    private void removeMedia(File f) {
        ContentResolver resolver = context.getContentResolver();
        resolver.delete(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, MediaStore.Video.Media.DATA + "=?", new String[]{f.getAbsolutePath()});
    }

    @Override
    public void unregisterAdapterDataObserver(@NonNull RecyclerView.AdapterDataObserver
                                                      observer) {
        super.unregisterAdapterDataObserver(observer);
    }

    @Override
    public int getItemCount() {
        return downloadFileArrayList.size();
    }

    private int checkFileisInArray(DownloadFile file) {
        for (int i = 0; i < selectDeleted.size(); i++) {
            if (selectDeleted.get(i).equalsIgnoreCase(file.getPath())) {
                return i;
            }
        }
        return -1;
    }

    public class SaveFileViewHolder extends RecyclerView.ViewHolder {
        private FileItemBinding binding;

        public SaveFileViewHolder(@NonNull FileItemBinding itemBinding) {
            super(itemBinding.getRoot());
            binding = itemBinding;
        }
    }

    public ArrayList<String> getDeletebleArray() {
        return selectDeleted;
    }

    private void onRequestRemove(File file, int adapterPosition) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.remove_action_btn)
                .setMessage("Remove Video")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        File files = new File(file.getPath());
                        boolean deleted = files.delete();
                        if (deleted) {
                            scanFile(files);
                            Toast.makeText(context, "Video Deleted", Toast.LENGTH_SHORT).show();
                            remove(adapterPosition);
                        }
                        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(files)));

                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();


    }
}
