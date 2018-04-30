package com.example.alex.fitofan.ui.fragments.my_plans;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.FormatTime;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

import static android.util.Base64.encodeToString;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class RecyclerAdapterMyPlans extends RecyclerView.Adapter<RecyclerAdapterMyPlans.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private MyPlansFragment mMyPlansFragment;
    private ArrayList<TrainingModel> mTrainings;
    private ProgressDialog mProgressDialog;

    public RecyclerAdapterMyPlans(ArrayList<TrainingModel> trainings, ProgressDialog mProgressDialog, MyPlansFragment mMyPlansFragment) {
        mTrainings = trainings;
        this.mMyPlansFragment = mMyPlansFragment;
        this.mProgressDialog = mProgressDialog;
    }

    public ArrayList<TrainingModel> getTrainings() {
        return mTrainings;
    }

    public void setTrainings(ArrayList<TrainingModel> trainings) {
        mTrainings = trainings;
        super.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        //Определение класса ViewHolder
        private LinearLayout mLinearLayout;

        ViewHolder(LinearLayout v) {
            super(v);
            mLinearLayout = v;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_plan, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        TextView name = linear.findViewById(R.id.tv_training_name);
        TextView description = linear.findViewById(R.id.tv_description);
        TextView time = linear.findViewById(R.id.tv_total_time);
        ImageView imageTraining = linear.findViewById(R.id.image_training);
        LinearLayout sharePlan = linear.findViewById(R.id.send_my_plan);

        name.setText(mTrainings.get(position).getName());
        description.setText(mTrainings.get(position).getDescription());
        time.setText(FormatTime.formatTime(mTrainings.get(position).getTime()));

        if (mTrainings.get(position).getImage() != null) {
            Glide.with(mMyPlansFragment.getContext())
                    .load(Uri.parse(mTrainings.get(position).getImage()))
                    .apply(centerCropTransform())
                    .transition(withCrossFade())
                    .into(imageTraining);
        }

        sharePlan.setOnClickListener(view -> {
            mProgressDialog.show();
            Bitmap imageBitmap = null;
            if (mTrainings.get(position).getImage() != null) {
                try {
                    imageBitmap = MediaStore.Images.Media.getBitmap(mMyPlansFragment.getActivity().getContentResolver(),
                            Uri.parse(mTrainings.get(position).getImage()));
                    imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            HashMap<String, String> training = new HashMap<>();
            training.put("plan_time", String.valueOf(mTrainings.get(position).getTime()));
            training.put("name", String.valueOf(mTrainings.get(position).getName()));
            training.put("description", String.valueOf(mTrainings.get(position).getDescription()));
            training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
            assert imageBitmap != null;
            try {
                training.put("image_path", URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            training.put("exercises", new Gson().toJson(mTrainings.get(position).getExercises()));
            training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());

            Request.getInstance().sendPlan(training, mMyPlansFragment);
        });
    }

    @Override
    public int getItemCount() {
        assert mTrainings != null;
        return mTrainings.size();
    }
}
