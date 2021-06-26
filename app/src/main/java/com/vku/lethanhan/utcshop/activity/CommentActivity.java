package com.vku.lethanhan.utcshop.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vku.lethanhan.utcshop.R;
import com.vku.lethanhan.utcshop.adapter.CommentAdapter;
import com.vku.lethanhan.utcshop.api.ApiService;
import com.vku.lethanhan.utcshop.data_local.AccessTokenManager;
import com.vku.lethanhan.utcshop.model.ApiResponse;
import com.vku.lethanhan.utcshop.model.Comment;
import com.vku.lethanhan.utcshop.model.Product;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener {

    EditText edt_comment_box;
    ImageView img_send;
    Toolbar toolbar;
    BottomNavigationView layout_add_comment;
    Product product;

    ArrayList<Comment> comments = new ArrayList<>();
    CommentAdapter commentAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        mapping();

        toolbar.setTitle("Bình luận");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        product = (Product) intent.getSerializableExtra("product");

        recyclerView = findViewById(R.id.recycler_view);
        commentAdapter = new CommentAdapter(this, comments);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        getData();


    }

    private void getData() {
        ApiService.apiService.getComment(product.getId()).enqueue(new Callback<ArrayList<Comment>>() {
            @Override
            public void onResponse(Call<ArrayList<Comment>> call, Response<ArrayList<Comment>> response) {
                if (response.isSuccessful()){
                    for (Comment comment: response.body()){
                        comments.add(comment);
                        commentAdapter.notifyDataSetChanged();
                        toolbar.setTitle("Bình luận(" +commentAdapter.getItemCount()+ ")");
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Comment>> call, Throwable t) {
                Log.e("CommentError", t.toString());
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void mapping() {
        recyclerView = findViewById(R.id.recycler_view);
        layout_add_comment = findViewById(R.id.layout_add_comment);

        if (!MainActivity.isLogin())
            layout_add_comment.setVisibility(View.INVISIBLE);

        edt_comment_box = findViewById(R.id.edt_comment_box);
        img_send = findViewById(R.id.img_send);
        img_send.setOnClickListener(this);
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_send:
                sendComment();
                break;
        }
    }

    private void sendComment() {
        if (!edt_comment_box.getText().toString().isEmpty()){
            ApiService.apiService.addComment(AccessTokenManager.getAccess_token(),
                    product.getId(), edt_comment_box.getText().toString()).enqueue(new Callback<ApiResponse>() {
                @Override
                public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                    Toast.makeText(CommentActivity.this, String.valueOf(response.code()), Toast.LENGTH_SHORT).show();
                    if (response.isSuccessful()){
                        getData();
                        commentAdapter.notifyDataSetChanged();

                    }else{
                        Toast.makeText(CommentActivity.this, "Chua vao", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ApiResponse> call, Throwable t) {
                    Log.e("CommentError", t.toString());
                }
            });
        }
    }


}