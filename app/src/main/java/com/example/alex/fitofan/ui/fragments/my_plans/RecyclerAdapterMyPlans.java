package com.example.alex.fitofan.ui.fragments.my_plans;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.client.Request;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.GetUserModel;
import com.example.alex.fitofan.models.SendExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.settings.MSharedPreferences;
import com.example.alex.fitofan.utils.ActionPlanCard;
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
import static com.bumptech.glide.request.RequestOptions.diskCacheStrategyOf;

public class RecyclerAdapterMyPlans extends RecyclerView.Adapter<RecyclerAdapterMyPlans.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView

    private MyPlansFragment mMyPlansFragment;
    private ArrayList<TrainingModel> mTrainings;
    private ProgressDialog mProgressDialog;
    private boolean isMy;

    int numberOfClicks = 0;
    boolean threadStarted = false;
    final int DELAY_BETWEEN_CLICKS_IN_MILLISECONDS = 250;

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

    public boolean getMy() {
        return isMy;
    }

    public void setMy(boolean my) {
        isMy = my;
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
        TextView countLike = linear.findViewById(R.id.count_like);
        ImageView imageTraining = linear.findViewById(R.id.image_training);

        imageTraining.setImageDrawable(null);

        ImageView sharePlan = linear.findViewById(R.id.icon_send_plan);
        ImageView save = linear.findViewById(R.id.icon_save);
        ImageView like = linear.findViewById(R.id.icon_like);
        ImageView comments = linear.findViewById(R.id.icon_comments);
        LinearLayout planLinear = linear.findViewById(R.id.plan_liner);

        name.setText(mTrainings.get(position).getName());
        description.setText(mTrainings.get(position).getDescription());
        time.setText(FormatTime.formatTime(mTrainings.get(position).getTime()));

        if (!isMy) {
            like.setVisibility(View.VISIBLE);
            comments.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            sharePlan.setVisibility(View.GONE);
        } else {
            sharePlan.setVisibility(View.VISIBLE);
            like.setVisibility(View.GONE);
            comments.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
        }

        if (mTrainings.get(position).getImage() != null) {
            Glide.with(mMyPlansFragment.getContext())
                    .load(Uri.parse(mTrainings.get(position).getImage()))
                    .apply(centerCropTransform())
                    .apply(diskCacheStrategyOf(DiskCacheStrategy.AUTOMATIC))
                    .transition(withCrossFade())
                    .into(imageTraining);
        }

        sharePlan.setOnClickListener(view -> {
            mProgressDialog.show();
            Bitmap imageBitmap = null;
            if (mTrainings.get(position).getImage() != null) {
                try {
                    mMyPlansFragment.requestMultiplePermissions();
                    imageBitmap = MediaStore.Images.Media.getBitmap(mMyPlansFragment.getActivity().getContentResolver(),
                            Uri.parse(mTrainings.get(position).getImage()));
                    imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                    assert imageBitmap != null;
                    try {
                        ArrayList<SendExerciseModel> exercise = new ArrayList<>();
                        HashMap<String, String> training = new HashMap<>();
                        training.put("signature", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getSignature());
                        training.put("uid", new Gson().fromJson(MSharedPreferences.getInstance().getUserInfo(), GetUserModel.class).getUser().getUid());
                        training.put("plan_time", String.valueOf(mTrainings.get(position).getTime()));
                        training.put("name", String.valueOf(mTrainings.get(position).getName()));
                        training.put("description", String.valueOf(mTrainings.get(position).getDescription()));
                        training.put("image_path", URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
                        for (int i = 0; i < mTrainings.get(position).getExercises().size(); i++) {
                            SendExerciseModel model = new SendExerciseModel();
                            model.setCountRepetitions(mTrainings.get(position).getExercises().get(i).getCountRepetitions());
                            model.setDescription(mTrainings.get(position).getExercises().get(i).getDescription());
                            model.setName(mTrainings.get(position).getExercises().get(i).getName());
                            model.setRecoveryTime(mTrainings.get(position).getExercises().get(i).getRecoveryTime());
                            model.setTime(mTrainings.get(position).getExercises().get(i).getTime());
                            model.setTimeBetween(mTrainings.get(position).getExercises().get(i).getTimeBetween());
                            Bitmap exercisesBitmap = null;
                            if (mTrainings.get(position).getImage() != null) {
                                try {
                                    exercisesBitmap = MediaStore.Images.Media.getBitmap(mMyPlansFragment.getActivity().getContentResolver(),
                                            Uri.parse(mTrainings.get(position).getExercises().get(i).getImage()));
                                    imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            assert exercisesBitmap != null;
                            model.setImagePath(CompressImage.getBase64FromBitmap(exercisesBitmap));
                            exercise.add(model);
                        }
                        training.put("exercises", URLEncoder.encode(new Gson().toJson(exercise), "UTF-8"));
                        Request.getInstance().sendPlan(training, mMyPlansFragment);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Toast.makeText(mMyPlansFragment.getContext(), "Сбой при отрпавке", Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        planLinear.setOnClickListener(v -> {
            ++numberOfClicks;
            if (!threadStarted) {
                new Thread(() -> {
                    threadStarted = true;
                    try {
                        Thread.sleep(DELAY_BETWEEN_CLICKS_IN_MILLISECONDS);
                        if (numberOfClicks == 1) {
                            mMyPlansFragment.goToPreview(
                                    mTrainings.get(position).getId(),
                                    mTrainings.get(position).getUserId());
                        } else if (numberOfClicks == 2) {
                            mMyPlansFragment.likePlan(String.valueOf(mTrainings.get(position).getId()),
                                    like, countLike, false, position);
                        }
                        numberOfClicks = 0;
                        threadStarted = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        like.setOnClickListener(view -> {
            mMyPlansFragment.likePlan(String.valueOf(mTrainings.get(position).getId()), like, countLike, true, position);
        });

        save.setOnClickListener(view -> {
            mMyPlansFragment.savePlan(String.valueOf(mTrainings.get(position).getId()), save, position);
        });

        comments.setOnClickListener(view -> {
            ActionPlanCard.goComments(mMyPlansFragment.getContext(),
                    String.valueOf(mTrainings.get(position).getId()), mTrainings.get(position).getUserId());
        });
    }

    @Override
    public int getItemCount() {
        return mTrainings == null ? 0 : mTrainings.size();
    }
}
