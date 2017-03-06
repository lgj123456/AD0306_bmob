package com.example.yhdj.ad0306bmob;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private Button btn_add;
    private List<Person> mPersons = new ArrayList<>();
    private MyAdapter mMyAdapter;
    private Button btn_reg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        //申请权限
        applyWritePermission();
        getAllPerson();
    }

    private void applyWritePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            applyPermission();
        }
    }

    private void applyPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
    }

    private void getAllPerson() {
        BmobQuery<Person> personBmobQuery = new BmobQuery<Person>();
        personBmobQuery.findObjects(new FindListener<Person>() {
            @Override
            public void done(List<Person> list, BmobException e) {
                if (e == null) {
                    mPersons = list;

                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                    mMyAdapter = new MyAdapter(mPersons);
                    mRecyclerView.setLayoutManager(linearLayoutManager);
                    mRecyclerView.setAdapter(mMyAdapter);
                    Toast.makeText(MainActivity.this, "点击头像修改数据！！！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initViews() {
        Bmob.initialize(this, "88550df99426c362c26d3ee1151a6bc6");
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        btn_reg = (Button) findViewById(R.id.btn_reg);
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addPerson.class);
                startActivity(intent);
                finish();
            }
        });

    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<Person> mPersons;
        private List<ImageBean> mImageBeen = new ArrayList<>();

        public MyAdapter(List<Person> mPersons) {
            this.mPersons = mPersons;
        }

        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.person_item, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
            final Person p = mPersons.get(position);
            holder.tv_name.setText(p.getName());
            holder.tv_age.setText(String.valueOf(p.getAge()));
            holder.tv_address.setText(p.getAddress());

            //更新数据
            holder.headImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = holder.getAdapterPosition();
                    Intent intent = new Intent(MainActivity.this, addPerson.class);
                    intent.putExtra("name", p.getName());
                    intent.putExtra("age", p.getAge());
                    intent.putExtra("address", p.getAddress());
                    intent.putExtra("url", mImageBeen.get(index).getUrl());
                    intent.putExtra("perId", p.getObjectId());
                    intent.putExtra("imgId", mImageBeen.get(index).getObjectId());
                    startActivity(intent);
                }
            });


            final BmobQuery<ImageBean> imageBeanBmobQuery = new BmobQuery<>();
            imageBeanBmobQuery.findObjects(new FindListener<ImageBean>() {
                @Override
                public void done(List<ImageBean> list, BmobException e) {
                    if (e == null) {
                        mImageBeen = list;
                        Glide.with(MainActivity.this).load(mImageBeen.get(position).getUrl()).into(holder.headImg);
                    } else {
                        Toast.makeText(MainActivity.this, "error", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            holder.img_del.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = holder.getAdapterPosition();
                    String perId = p.getObjectId();
                    String imgId = mImageBeen.get(position).getObjectId();


                    //删除图片
                    mImageBeen.get(index).delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            //删除人
                            p.delete(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(MainActivity.this, "删除成功！！！", Toast.LENGTH_SHORT).show();
                                        getAllPerson();
                                    } else {
                                        Toast.makeText(MainActivity.this, "删除失败！！！", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Toast.makeText(MainActivity.this, "删除成功+1！！！", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            });
        }


        @Override
        public int getItemCount() {
            return mPersons.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private ImageView headImg;
            private TextView tv_name;
            private TextView tv_age;
            private TextView tv_address;
            private ImageView img_del;

            public ViewHolder(View itemView) {
                super(itemView);
                headImg = (ImageView) itemView.findViewById(R.id.img_head);
                tv_name = (TextView) itemView.findViewById(R.id.iv_name);
                tv_age = (TextView) itemView.findViewById(R.id.iv_age);
                tv_address = (TextView) itemView.findViewById(R.id.iv_address);
                img_del = (ImageView) itemView.findViewById(R.id.img_del);
            }
        }
    }
//申请权限回调


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1001: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "申请权限成功！！！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "申请权限失败，可能会导致应用异常！！！", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
