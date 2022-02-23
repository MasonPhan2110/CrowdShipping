package com.Duong.crowdshipping.adapter;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.Duong.crowdshipping.Activity.DetailPostActivity;
import com.Duong.crowdshipping.Activity.MapActivity;
import com.Duong.crowdshipping.R;
import com.Duong.crowdshipping.model.Post;
import com.Duong.crowdshipping.model.SliderData;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.geojson.Point;
import com.mapbox.search.MapboxSearchSdk;
import com.mapbox.search.ResponseInfo;
import com.mapbox.search.SearchEngine;
import com.mapbox.search.SearchOptions;
import com.mapbox.search.SearchRequestTask;
import com.mapbox.search.SearchSelectionCallback;
import com.mapbox.search.result.SearchResult;
import com.mapbox.search.result.SearchSuggestion;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SentPostAdapter extends   RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context mcontext;
    private List<Post> mPost;
    private String Type;
    private SearchEngine searchEngine;
    private SearchRequestTask searchRequestTask;
    private List<Point> targetPoint = new ArrayList<>();
    public SentPostAdapter(Context mcontext, List<Post> mPost,String type){
        this.mcontext = mcontext;
        this.mPost = mPost;
        this.Type = type;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.item_post_home, parent, false);
        return  new SentPostAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ViewHolder itemHolder = (ViewHolder) holder;
        if(mPost.size()==0){
            if(Type.equals("Receive")){
                itemHolder.text_no_post.setText("Bạn chưa nhận tin nào");
            }else if(Type.equals("Sent")){
                itemHolder.text_no_post.setText("Bạn chưa đăng tin nào");
            }else if(Type.equals("Complete")){
                itemHolder.text_no_post.setText("Bạn chưa hoàn thành tin nào");
            }

            itemHolder.text_no_post.setVisibility(View.VISIBLE);
            itemHolder.linear1.setVisibility(View.GONE);
        }else{
            itemHolder.text_no_post.setVisibility(View.GONE);
            itemHolder.linear1.setVisibility(View.VISIBLE);
            Post post = mPost.get(position);
            String img = post.getLinkImage().get("Image0").toString();
            holder.setIsRecyclable(false);
            String[] type = post.getType().split("-");
            String[] time = post.getTime().split("_");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
            String currentDateandTime = sdf.format(new Date());
            String[] currentTime = currentDateandTime.split("_");
            String timeSubtract = " ";
            if(time[0].equals(currentTime[0])){
                SimpleDateFormat dateFormat = new SimpleDateFormat("HHmmss");
                try {
                    Date datePost = dateFormat.parse(time[1]);
                    Date datenow = dateFormat.parse(currentTime[1]);
                    long diff = datenow.getTime() - datePost.getTime();
                    int timeinSecond = (int) (diff/1000);
                    int hours, minutes, seconds;
                    hours = timeinSecond / 3600;
                    timeinSecond = timeinSecond - (hours * 3600);
                    minutes = timeinSecond / 60;
                    timeinSecond = timeinSecond - (minutes * 60);
                    seconds = timeinSecond;
                    if(hours == 0){
                        if(minutes == 0){
                            timeSubtract="Vừa xong";
                        }else{
                            timeSubtract = minutes + " m";
                        }
                    }else{
                        timeSubtract = hours + " h";
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }else{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                try{
                    Date datePost = dateFormat.parse(time[0]);
                    Date datenow = dateFormat.parse(currentTime[0]);
                    Log.d("Timenow", datenow+"-"+datePost);
                    long diff = datenow.getTime() - datePost.getTime();
                    int timeinSecond = (int) (diff/1000);
                    int hours, day;
                    hours = timeinSecond / 3600;
                    day = hours/24;
                    if(day<=1){
                        timeSubtract = day+" day";
                    }
                    else{
                        timeSubtract = day+" days";
                    }
                }catch(ParseException e) {
                    e.printStackTrace();
                }
            }
            Glide.with(mcontext).load(img).into(itemHolder.image);
            itemHolder.from.setText("Từ: "+post.getAddressFrom().get("Wards")+", "+post.getAddressFrom().get("District")+", "+post.getAddressFrom().get("City"));
            itemHolder.to.setText("Đến: " +post.getAddressTo().get("Wards")+", "+post.getAddressTo().get("District")+", "+post.getAddressTo().get("City"));
            itemHolder.type.setText("Loại hàng hóa: "+type[1]);
            itemHolder.time.setText(timeSubtract);
            if(Type.equals("Receive")){
                try {
                    MapboxSearchSdk.initialize((Application) mcontext.getApplicationContext(),
                            mcontext.getString(R.string.mapbox_access_token),
                            LocationEngineProvider.getBestLocationEngine(mcontext));

                }catch (Exception e){
                    Log.d("Already initialized", e.toString());
                }
                searchEngine = MapboxSearchSdk.getSearchEngine();

                final SearchOptions options = new SearchOptions.Builder()
                        .limit(5)
                        .build();
                searchRequestTask = searchEngine.search(post.getAddressTo().get("Address")+", đường "+post.getAddressTo().get("Streets")
                        +", phường " +post.getAddressTo().get("Wards")+", quận "+post.getAddressTo().get("District")+
                        ", "+post.getAddressTo().get("City"), options, searchCallback);
                searchRequestTask = searchEngine.search(post.getAddressFrom().get("Address")+", đường "+post.getAddressFrom().get("Streets")
                        +", phường " +post.getAddressFrom().get("Wards")+", quận "+post.getAddressFrom().get("District")+
                        ", "+post.getAddressFrom().get("City"), options, searchCallback);
                itemHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mcontext, MapActivity.class);
                        intent.putExtra("Post", (Serializable) post);
                        intent.putExtra("TargetTo", targetPoint.get(1));
                        intent.putExtra("TargetFrom", targetPoint.get(0));
                        mcontext.startActivity(intent);
                    }
                });
            }else{
                itemHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mcontext, DetailPostActivity.class);
                        intent.putExtra("Post", (Serializable) post);
                        intent.putExtra("Type", Type);
                        mcontext.startActivity(intent);
                    }
                });
            }

        }
    }

    @Override
    public int getItemCount() {
        if(mPost.size() == 0){
            return 1;
        }
        return mPost.size();
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView type,from,to,time, text_no_post;
        LinearLayout linearLayout,linear1;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            type = itemView.findViewById(R.id.type);
            from = itemView.findViewById(R.id.from);
            to = itemView.findViewById(R.id.to);
            time = itemView.findViewById(R.id.time);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            text_no_post = itemView.findViewById(R.id.text_no_post);
            linear1 = itemView.findViewById(R.id.linear1);
        }
    }
    private final SearchSelectionCallback searchCallback = new SearchSelectionCallback() {
        @Override
        public void onSuggestions(@NonNull List<? extends SearchSuggestion> suggestions, @NonNull ResponseInfo responseInfo) {
            if (suggestions.isEmpty()) {
                Log.i("SearchApiExample", "No suggestions found");
            } else {
                Log.i("SearchApiExample", "Search suggestions: " + suggestions + "\nSelecting first...");
                searchRequestTask = searchEngine.select(suggestions.get(0), this);
            }
        }
        @Override
        public void onCategoryResult(@NonNull SearchSuggestion searchSuggestion, @NonNull List<? extends SearchResult> list, @NonNull ResponseInfo responseInfo) {
            Log.i("SearchApiExample", "Category search results: " + list);
        }

        @Override
        public void onResult(@NonNull SearchSuggestion suggestion, @NonNull SearchResult result, @NonNull ResponseInfo info) {
            targetPoint.add(result.getCoordinate());
            Log.i("SearchApiExample", "Search result: " + targetPoint);
        }

        @Override
        public void onError(@NonNull Exception e) {
            Log.i("SearchApiExample", "Search error: ", e);
        }
    };
}
