package com.example.myapplication.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.appcompat.widget.SearchView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.selection.OnDragInitiatedListener;
import androidx.recyclerview.selection.OnItemActivatedListener;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.selection.StorageStrategy;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.IAdapter;
import com.example.myapplication.file.ItemIsLookup;
import com.example.myapplication.file.MyItemKeyProvide;
import com.example.myapplication.model.Data;
import com.example.myapplication.R;
import com.example.myapplication.ReadTxtActivity;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.unit.ToolsUtil;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.*;

public class ImportDataActivity extends AppCompatActivity implements ActionMode.Callback {

    RecyclerView recyclerView;
    IAdapter adapter;
    ActionMode actionMode = null;
    private static final String currPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//    List<Data> dataList;
    String extention = ".txt";
    UserDataBase userDataBase;
    SelectionTracker tracker;
    Thread thread;
    private List<Data> mFilterList;
    String id,title;

    volatile boolean flag=true;
    boolean isLogin=false;
    SharedPreferences preferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_data);
        userDataBase = new UserDataBase(this);
        recyclerView = findViewById(R.id.data_recyclerview);
//        dataList = new ArrayList<>();
        mFilterList=new ArrayList<>();
        preferences= PreferenceManager.getDefaultSharedPreferences(ImportDataActivity.this);
        isLogin=preferences.getBoolean(AppData.IS_LOGIN,false);
        id =isLogin?preferences.getString(AppData.USER_ID, "0"):"0";
        adapter = new IAdapter(mFilterList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        initTracker();
        adapter.setOnItemClickListener(new IAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, final int position) {
                String ins = "加入书架";
                final boolean isInBookself = userDataBase.queryABook(mFilterList.get(position).getFileMd5(),id);
                if (isInBookself) {
                    ins = null;
                }
                new AlertDialog.Builder(ImportDataActivity.this)
                        .setTitle(mFilterList.get(position).getTitle())
                        .setMessage("路径：" + mFilterList.get(position).getFilePath())
                        .setPositiveButton("开始阅读", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(ImportDataActivity.this, ReadTxtActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putSerializable("book",mFilterList.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(ins, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (!isInBookself) {
                                    Data data = mFilterList.get(position);
                                    data.setUserId(id);
                                    userDataBase.insertToBookself(data);
                                    Intent intent = new Intent("android.intent.action.CAST_BROADCAST");
                                    intent.putExtra("code", 2);
                                    LocalBroadcastManager.getInstance(ImportDataActivity.this).sendBroadcast(intent);
                                    sendBroadcast(intent);
                                    Snackbar.make(findViewById(android.R.id.content),"已添加！",Snackbar.LENGTH_SHORT).show();
//                                    Toast.makeText(ImportDataActivity.this, "已添加！", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }).create().show();
            }
        });
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if (!thread.isInterrupted()){
                    getAllFiles(currPath);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setAddAll();
                            findViewById(R.id.layoutProcess).setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        });
        thread.start();
    }
//
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        tracker.onSaveInstanceState(outState);
//    }

    private void initTracker() {
//        mFilterList=adapter.getFilterList();
        tracker = new SelectionTracker.Builder<>(
                "select_item",
                recyclerView,
                new MyItemKeyProvide(1,mFilterList),
                new ItemIsLookup(recyclerView),
                StorageStrategy.createLongStorage()).
                withOnItemActivatedListener(new OnItemActivatedListener<Long>() {
                    @Override
                    public boolean onItemActivated(@NonNull ItemDetailsLookup.ItemDetails<Long> item, @NonNull MotionEvent e) {
                        return false;
                    }
                }).withOnDragInitiatedListener(new OnDragInitiatedListener() {
            @Override
            public boolean onDragInitiated(@NonNull MotionEvent e) {
                return false;
            }
        })
                .build();

        adapter.setTracker(tracker);
        tracker.addObserver(new SelectionTracker.SelectionObserver() {
            @Override
            public void onItemStateChanged(@NonNull Object key, boolean selected) {
                super.onItemStateChanged(key, selected);
//                actionMode.setTitle("已选"+tracker.getSelection().size());
            }

            @Override
            public void onSelectionRefresh() {
                super.onSelectionRefresh();
            }


            @Override
            public void onSelectionChanged() {
                super.onSelectionChanged();
                if (actionMode == null && tracker.hasSelection()) {
                    actionMode = startSupportActionMode(ImportDataActivity.this);
                    actionMode.getMenuInflater().inflate(R.menu.imp_actionmenu, actionMode.getMenu());
                    actionMode.setTitle("已选" + tracker.getSelection().size() + "项");
                } else if (!tracker.hasSelection() && actionMode != null) {
                    actionMode.finish();
                    adapter.notifyDataSetChanged();
                } else {
                    if (actionMode != null) {
                        actionMode.setTitle("已选" + tracker.getSelection().size() + "项");
                    }
                }
            }

            @Override
            public void onSelectionRestored() {
                super.onSelectionRestored();

            }
        });
    }
    private void getAllFiles(String path) {
        File file = new File(path);                 //获取根目录路径

        if (file.isDirectory()) {                   //判断文件是不是目录

            File[] files = file.listFiles();        //如果是目录，则获取目录下的文件
            for (File file1 : files) {              //遍历文件
                if(!flag)                           //用来判断是否结束递归
                    return;
                if (file1.isFile()) {               //如果遍历出来的这一项是文件
                    if (file1.getPath().substring(file1.getPath().length() - extention.length()).equals(extention) && file1.getTotalSpace() >= 1024) {
                                                    //判断是不是txt文件
                        title=file1.getName();
                    	Data data = new Data();
                        data.setFilePath(file1.getPath());
                        data.setTitle(title.substring(0,title.length()-4));
                        data.setFileMd5(ToolsUtil.getFileMD5(file1));
                        mFilterList.add(data);      //添加数据到列表
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                                //在ui线程更新
                            }
                        });
                    }
                } else if (file1.isDirectory() && !file1.getPath().contains("./")) {
                            //如果目录是文件夹，再次获取这个目录的路径，然后用递归，重复上面步骤
                    getAllFiles(file1.getPath());
                }
            }
        }

    }


    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        adapter.setActionMode(true);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.import_book:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Iterator<Data> iterator = tracker.getSelection().iterator();
                        while (iterator.hasNext()) {
                            Data i = iterator.next();
                            if (!userDataBase.queryABook(i.getFileMd5(),id)) {
//                                Data data = dataList.get((int) i);
                                i.setUserId(id);
                                userDataBase.insertToBookself(i);
                            }
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ImportDataActivity.this, "导入完成", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent("android.intent.action.CAST_BROADCAST");//
                                intent.putExtra("code", 2);
                                LocalBroadcastManager.getInstance(ImportDataActivity.this).sendBroadcast(intent);
                                sendBroadcast(intent);
                                finish();
                            }
                        });
                    }
                }).start();
        }
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        tracker.clearSelection();
        adapter.setActionMode(false);
        actionMode = null;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.imp_searchmenu,menu);
        SearchView searchView= (SearchView) menu.findItem(R.id.imp_search_action).getActionView();
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                flag=false;
                adapter.getFilter().filter(newText);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }


}
