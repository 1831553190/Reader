package com.example.myapplication;

import android.Manifest;
import android.app.AlertDialog;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.RecyclerAdapter;
import com.example.myapplication.bean.AppData;
import com.example.myapplication.database.UserDataBase;
import com.example.myapplication.activity.ImportDataActivity;
import com.example.myapplication.model.Data;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment implements TextWatcher, RecyclerAdapter.OnItemLongClickListener, View.OnClickListener {
	RecyclerView recyclerView;
	RecyclerAdapter recyclerAdapter;
	List<Data> list;
	Button mainImpBtn;
	LinearLayout linearLayout;
	View view;
	UserDataBase userDataBase;
	String id;
	SharedPreferences preferences;
	EditText bookSearch;
	private boolean isLogin=false;
	
	FloatingActionButton fab;
	final String[] permission={Manifest.permission.WRITE_EXTERNAL_STORAGE};
	
	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		init();
		return view;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getContext());
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("android.intent.action.CAST_BROADCAST");
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				int code = intent.getIntExtra("code", 0);
				if (code == 2) {
					addAllBook();
				}
			}
			
		};
		broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}
	
	private void init() {
		view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_main, null);
		userDataBase = new UserDataBase(getContext());
		recyclerView = view.findViewById(R.id.mainRecycleViewer);
		linearLayout = view.findViewById(R.id.main_import_layout);
		mainImpBtn = view.findViewById(R.id.main_impBtn);
		bookSearch = view.findViewById(R.id.main_bookSearch);
		fab=view.findViewById(R.id.fab);
		fab.setOnClickListener(this);
//		bookSearch.clearFocus();
		list = new ArrayList<>();
		preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
		id = preferences.getString(AppData.USER_ID, "");
		list.addAll(userDataBase.queryBookSelf(id));
		recyclerAdapter = new RecyclerAdapter(list, getContext());
		recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
		recyclerView.setAdapter(recyclerAdapter);
		addAllBook();
		recyclerAdapter.setOnItemLongClickListener(this);
		bookSearch.addTextChangedListener(this);
		recyclerAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(View view, int position) {
				Intent intent = new Intent(getContext(), ReadTxtActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("book", list.get(position));
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});
		mainImpBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivityForResult(new Intent(getContext(), ImportDataActivity.class), 2);
			}
		});
		linearLayout.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
		initPermission();
		
	}
	
	private void initPermission() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
			if(PackageManager.PERMISSION_GRANTED !=getContext().checkSelfPermission(permission[0]))
			requestPermissions(permission,0);
		}
	}
	
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		super.onRequestPermissionsResult(requestCode, permissions, grantResults);
		if (grantResults[0]==-1){
			new AlertDialog.Builder(getContext())
					.setTitle("权限不足")
					.setMessage("应用需要获取读取存储空间权限以保证软件能够正常运行")
					.setPositiveButton("重新授权", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							initPermission();
						}
					})
					.setNegativeButton("退出", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							getActivity().finish();
						}
					})
					.setCancelable(false)
					.show();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == 2) {
			addAllBook();
		}
	}
	
	public void addAllBook() {
		isLogin=preferences.getBoolean(AppData.IS_LOGIN,false);
		id = isLogin? preferences.getString(AppData.USER_ID, "0"):"0";
		list.clear();
		list.addAll(userDataBase.queryBookSelf(id));
		System.out.println("---"+id);
		linearLayout.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
		bookSearch.setVisibility(list.size() == 0 ? View.GONE : View.VISIBLE);
		recyclerAdapter.notifyDataSetChanged();
	}
	
	@Override
	public void onResume() {
		addAllBook();
		super.onResume();
	}
	
	@Override
	public void onItemLongClick(View view, final int position) {
		AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			File file = new File(list.get(position).getFilePath());
			View v = LayoutInflater.from(getContext()).inflate(R.layout.bookdatail_dialog, null);
			alert.setView(v);
			TextView dialogTitle = v.findViewById(R.id.dialigTitle);
			dialogTitle.setText(list.get(position).getTitle());
			TextView dialogFilePath = v.findViewById(R.id.dialog_bookPath);
			dialogFilePath.setText("文件路径：" + file.getPath());
			alert.setPositiveButton("开始阅读", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(getContext(), ReadTxtActivity.class);
					Bundle bundle = new Bundle();
					bundle.putSerializable("book", list.get(position));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			})
					.setNegativeButton("移出书架", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							userDataBase.deleteBook(list.get(position));
							list.remove(position);
							recyclerAdapter.notifyDataSetChanged();
							linearLayout.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
							Snackbar.make(getView(),"已移出书架！",Snackbar.LENGTH_SHORT).show();
							
//							Toast.makeText(getContext(), "已移出书架！", Toast.LENGTH_SHORT).show();
						}
					})
					.create()
					.show();
			
		} else {
			alert.setTitle(list.get(position).getTitle())
					.setMessage("路径：" + list.get(position).getFilePath())
					.setPositiveButton("开始阅读", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent(getContext(), ReadTxtActivity.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("book", list.get(position));
							intent.putExtras(bundle);
							startActivity(intent);
						}
					})
					.setNegativeButton("移出书架", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							userDataBase.deleteBook(list.get(position));
							list.remove(position);
							recyclerAdapter.notifyDataSetChanged();
							linearLayout.setVisibility(list.size() == 0 ? View.VISIBLE : View.GONE);
							Toast.makeText(getContext(), "已移出书架！", Toast.LENGTH_SHORT).show();
						}
					}).create().show();
		}
	}
	
	
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {
	
	}
	
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		recyclerAdapter.getFilter().filter(s);
	}
	
	@Override
	public void afterTextChanged(Editable s) {
	
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fab:
				startActivityForResult(new Intent(getContext(), ImportDataActivity.class), 2);
				break;
		}
	}
}
