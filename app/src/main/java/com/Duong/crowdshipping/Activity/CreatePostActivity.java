package com.Duong.crowdshipping.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.Duong.crowdshipping.R;

public class CreatePostActivity extends AppCompatActivity {
    String type, item;
    LayoutInflater inflater;
    LinearLayout linear_layout;
    TextView post;
    EditText addressFrom, addressTo, phoneFrom, phoneTo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.green));

        post = findViewById(R.id.post);
        linear_layout = findViewById(R.id.linear_layout);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            type = extras.getString("type"," ");
            item = extras.getString("item"," ");
        }
        inflater = LayoutInflater.from(CreatePostActivity.this);
        String[] payshipType = {"Người chuyển trả", "Người nhận trả"};
        if(type.equals("clothes")){
           switch (item){
               case "Quần áo":
                   View view = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] clothesType = {"Đồng hồ cơ", "Đồng hồ điện tử","Đồng hồ thông minh"};
                   Spinner spinner = view.findViewById((R.id.drop_down_watch));
                   Spinner spinner_ship_cost_clothes = view.findViewById(R.id.drop_down_ship_cost);
                   ArrayAdapter clothesAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,clothesType);
                   ArrayAdapter shipCostAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,clothesType);
                   spinner.setAdapter(clothesAdapter);
                   spinner_ship_cost_clothes.setAdapter(shipCostAdapter);
                   linear_layout.addView(view);
                   break;
               case "Đồng hồ":
                   View view1 = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] watchType = {"Đồng hồ cơ", "Đồng hồ điện tử","Đồng hồ thông minh"};
                   Spinner spinner_watch = view1.findViewById((R.id.drop_down_watch));
                   Spinner spinner_ship_cost_watch = view1.findViewById(R.id.drop_down_ship_cost);
                   addressFrom = view1.findViewById(R.id.addressFrom);
                   addressTo = view1.findViewById(R.id.addressTo);
                   phoneFrom = view1.findViewById(R.id.phoneFrom);
                   phoneTo = view1.findViewById(R.id.phoneTo);
                   ArrayAdapter watchAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,watchType);
                   ArrayAdapter shipCostAdapter1 = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,payshipType);
                   spinner_watch.setAdapter(watchAdapter);
                   spinner_ship_cost_watch.setAdapter(shipCostAdapter1);
                   post.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View view) {
                           if(addressFrom.getText().toString().matches(" ")
                                   || addressTo.getText().toString().matches(" ")
                                   || phoneFrom.getText().toString().matches(" ")
                                   || phoneTo.getText().toString().matches(" "))
                           {
                               AlertDialog.Builder builder = new AlertDialog.Builder(CreatePostActivity.this);
                               builder.setMessage("Bạn cần điền đầy đủ các thông tin!").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {

                                   }
                               }).show();
                           }else {
                               postClick(type, item);
                           }
                       }
                   });
                   linear_layout.addView(view1);
                   break;
               case "Giày dép":
                   Log.d("Testtttttttttttttt", item);
                   View view2 = inflater.inflate(R.layout.layout_clothes_watch,linear_layout,false);
                   String[] shoesType = {"Giày nam", "Giày nữ","Giày cao gót"};
                   Spinner spinner_ship_cost_shoes = view2.findViewById(R.id.drop_down_ship_cost);
                   Spinner spinner2 = view2.findViewById((R.id.drop_down_watch));
                   ArrayAdapter shoesAdapter = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,shoesType);
                   ArrayAdapter shipCostAdapter2 = new ArrayAdapter(CreatePostActivity.this,android.R.layout.simple_spinner_item,payshipType);
                   spinner2.setAdapter(shoesAdapter);
                   spinner_ship_cost_shoes.setAdapter(shipCostAdapter2);
                   linear_layout.addView(view2);
                   break;
               case "Túi xách":
                   Log.d("Testtttttttttttttt", item);
                   break;
               default:
                   break;
           }
        }
    }

    private void postClick(String type, String item) {
        Toast.makeText(CreatePostActivity.this,type+item,Toast.LENGTH_SHORT).show();

    }
}