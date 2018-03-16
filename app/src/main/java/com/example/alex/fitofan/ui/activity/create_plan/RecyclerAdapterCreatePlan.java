package com.example.alex.fitofan.ui.activity.create_plan;

import android.app.Dialog;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.FormatTime;

import java.util.ArrayList;
import java.util.Objects;

public class RecyclerAdapterCreatePlan extends RecyclerView.Adapter<RecyclerAdapterCreatePlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private TrainingModel mTrainingModel;
    private CreatePlanActivity mCreatePlanActivity;
    private int id;
    private final long MILISEC_MIN = 60000L;
    private final long MILISEC_SEC = 1000L;
    private int lastPosition = -1;


    RecyclerAdapterCreatePlan(CreatePlanActivity mCreatePlanActivity, TrainingModel mTrainingModel, int id) {
        this.mCreatePlanActivity = mCreatePlanActivity;
        this.mTrainingModel = mTrainingModel;
        this.id = id;
    }

    ArrayList<ExerciseModel> getExerciseModels() {
        return mTrainingModel.getExercises();
    }

    void addItem() {
        mTrainingModel.getExercises().add(mTrainingModel.getExercises().size() - 1, new ExerciseModel());
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                RecyclerAdapterCreatePlan.this.notifyDataSetChanged();
                RecyclerAdapterCreatePlan.this.notifyItemInserted(mTrainingModel.getExercises().size() - 1);
            }
        });

    }

    void delItem(int position) {
        mTrainingModel.getExercises().remove(position);
        lastPosition--;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            public void run() {
                RecyclerAdapterCreatePlan.this.notifyDataSetChanged();
            }
        });
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
                .inflate(R.layout.item_create_plan, parent, false);
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        setAnimation(holder.itemView, position);
        Log.e("onBindViewHolder: ", String.valueOf(getItemCount()));

        TextView delItemPlan = linear.findViewById(R.id.tv_cancel_item_create_plan);
        View delimiter = linear.findViewById(R.id.delimiter_exercise);
        EditText etNameExercise = linear.findViewById(R.id.et_exercise_name);
        EditText etDescription = linear.findViewById(R.id.et_description_exercise);
        MaterialRippleLayout btAddImage = linear.findViewById(R.id.bt_add_image_exercise);
        MaterialRippleLayout btAddAudio = linear.findViewById(R.id.bt_add_audio_exercise);
        EditText etNumberRepetition = linear.findViewById(R.id.et_number_repetitions);
        EditText etTimeExercise = linear.findViewById(R.id.et_exercise_time);
        EditText etTimeBetweenExercise = linear.findViewById(R.id.et_time_between_exercise);
        EditText etRelaxTime = linear.findViewById(R.id.et_recovery_time);
        ImageView image = linear.findViewById(R.id.image_exercise_create);
        CardView cvImage = linear.findViewById(R.id.image_exercise_card);

        //разделитель и кнопка удаления упражнения
        if (position < mTrainingModel.getExercises().size() - 1) {
            delItemPlan.setVisibility(View.GONE);
            delimiter.setVisibility(View.VISIBLE);
        }

        if (position == 0) {
            delItemPlan.setVisibility(View.GONE);
            delimiter.setVisibility(View.GONE);
            if (mTrainingModel.getExercises().size() > 1) {
                delimiter.setVisibility(View.VISIBLE);
            }
        } else if (position == mTrainingModel.getExercises().size() - 1) {
            delItemPlan.setVisibility(View.VISIBLE);
            delimiter.setVisibility(View.GONE);
        }

        if (id > 0) {
            setDataEdit(position,
                    etNameExercise,
                    etDescription,
                    etNumberRepetition,
                    etTimeExercise,
                    etTimeBetweenExercise,
                    etRelaxTime,
                    image, cvImage);
        }

        delItemPlan.setOnClickListener(v -> {
            mCreatePlanActivity.delItemExercise(position);
            mCreatePlanActivity.setAllTime();
        });

        btAddAudio.setOnClickListener(v -> {
            mCreatePlanActivity.chooseAudioExercise(position);
        });

        etRelaxTime.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.relax_time),
                    mCreatePlanActivity.getResources().getString(R.string.relax_time_description),
                    mCreatePlanActivity.getResources().getString(R.string.save));

            dialog.findViewById(R.id.bt_save_time).setOnClickListener(viev -> {
                EditText min = dialog.findViewById(R.id.select_time_min);
                EditText sec = dialog.findViewById(R.id.select_time_sec);
                if (Objects.equals(String.valueOf(min.getText()), "")) {
                    min.setText("0");
                }
                if (Objects.equals(String.valueOf(sec.getText()), "")) {
                    sec.setText("0");
                }
                long temp_time = Integer.valueOf(String.valueOf(min.getText())) * MILISEC_MIN;
                temp_time += Long.valueOf(String.valueOf(sec.getText())) * MILISEC_SEC;
                mTrainingModel.getExercises().get(position).setRecoveryTime(temp_time);
                String temp_text = FormatTime.formatTime(temp_time);
                etRelaxTime.setText(temp_text);
                dialog.dismiss();
            });

        });

        etTimeBetweenExercise.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.time_between_exercise),
                    mCreatePlanActivity.getResources().getString(R.string.time_between_exercise_description),
                    mCreatePlanActivity.getResources().getString(R.string.save));
            dialog.findViewById(R.id.bt_save_time).setOnClickListener(viev -> {
                EditText min = dialog.findViewById(R.id.select_time_min);
                EditText sec = dialog.findViewById(R.id.select_time_sec);
                if (Objects.equals(String.valueOf(min.getText()), "")) {
                    min.setText("0");
                }
                if (Objects.equals(String.valueOf(sec.getText()), "")) {
                    sec.setText("0");
                }
                long temp_time = Integer.valueOf(String.valueOf(min.getText())) * MILISEC_MIN;
                temp_time += Long.valueOf(String.valueOf(sec.getText())) * MILISEC_SEC;
                mTrainingModel.getExercises().get(position).setTimeBetween(temp_time);
                String temp_text = FormatTime.formatTime(temp_time);
                etTimeBetweenExercise.setText(temp_text);
                mCreatePlanActivity.setAllTime();
                dialog.dismiss();
            });

        });

        etTimeExercise.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.time_exercise),
                    mCreatePlanActivity.getResources().getString(R.string.time_exercise_description),
                    mCreatePlanActivity.getResources().getString(R.string.save));

            dialog.findViewById(R.id.bt_save_time).setOnClickListener(viev -> {
                EditText min = dialog.findViewById(R.id.select_time_min);
                EditText sec = dialog.findViewById(R.id.select_time_sec);
                if (Objects.equals(String.valueOf(min.getText()), "")) {
                    min.setText("0");
                }
                if (Objects.equals(String.valueOf(sec.getText()), "")) {
                    sec.setText("0");
                }
                long temp_time = Integer.valueOf(String.valueOf(min.getText())) * MILISEC_MIN;
                temp_time += Long.valueOf(String.valueOf(sec.getText())) * MILISEC_SEC;
                mTrainingModel.getExercises().get(position).setTime(temp_time);
                String temp_text = FormatTime.formatTime(temp_time);
                etTimeExercise.setText(temp_text);
                mCreatePlanActivity.setAllTime();
                dialog.dismiss();
            });

        });

        etNameExercise.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.exercise),
                    mCreatePlanActivity.getResources().getString(R.string.exercise_name_description),
                    mCreatePlanActivity.getResources().getString(R.string.save), 1);

            dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                etNameExercise.setText(et.getText());
                mTrainingModel.getExercises().get(position).setName(String.valueOf(et.getText()));
                dialog.dismiss();
            });
        });

        etDescription.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.training_description),
                    mCreatePlanActivity.getResources().getString(R.string.description_description_plan),
                    mCreatePlanActivity.getResources().getString(R.string.save), 1);

            dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                etDescription.setText(et.getText());
                mTrainingModel.getExercises().get(position).setDescription(String.valueOf(et.getText()));
                dialog.dismiss();
            });
        });

        etNumberRepetition.setOnClickListener(v -> {
            Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                    mCreatePlanActivity.getResources().getString(R.string.number_repetitions),
                    mCreatePlanActivity.getResources().getString(R.string.number_repetitions_description),
                    mCreatePlanActivity.getResources().getString(R.string.save), 2);

            dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                etNumberRepetition.setText(et.getText());
                mTrainingModel.getExercises().get(position).setCountRepetitions(Integer.valueOf(String.valueOf(et.getText())));
                dialog.dismiss();
            });
        });

        btAddImage.setOnClickListener(v -> {
            mCreatePlanActivity.choosePictureExercise(position, image, cvImage);
        });


    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mCreatePlanActivity.getContext(), android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    private void setDataEdit(int position, EditText etNameExercise, EditText etDescription, EditText etNumberRepetition, EditText etTimeExercise, EditText etTimeBetweenExercise, EditText etRelaxTime, ImageView image, CardView cvImage) {
        etNameExercise.setText(mTrainingModel.getExercises().get(position).getName());
        etDescription.setText(mTrainingModel.getExercises().get(position).getDescription());
        etNumberRepetition.setText(String.valueOf(mTrainingModel.getExercises().get(position).getCountRepetitions()));
        etTimeExercise.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position).getTime()));
        etTimeBetweenExercise.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position).getTimeBetween()));
        etRelaxTime.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position).getRecoveryTime()));
        if (mTrainingModel.getExercises().get(position).getImage() != null) {
            cvImage.setVisibility(View.VISIBLE);
            Glide.with(mCreatePlanActivity.getContext())
                    .load(Uri.parse(mTrainingModel.getExercises().get(position).getImage()))
                    .placeholder(R.mipmap.icon)
                    .fitCenter()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(image);
        }
    }

    void setAudio(Uri uriExercise, int position) {
        mTrainingModel.getExercises().get(position).setAudio(uriExercise.toString());
    }

    void setImage(Uri uriExercise, ImageView imageExercise, CardView cvExercise, int position) {
        cvExercise.setVisibility(View.VISIBLE);
        Glide.with(mCreatePlanActivity.getContext()).load(uriExercise).into(imageExercise);
        mTrainingModel.getExercises().get(position).setImage(uriExercise.toString());
    }

    @Override
    public int getItemCount() {
        return mTrainingModel.getExercises() == null ? 0 : mTrainingModel.getExercises().size();
    }

}
