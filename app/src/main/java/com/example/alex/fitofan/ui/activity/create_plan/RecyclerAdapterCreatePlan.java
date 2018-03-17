package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


    RecyclerAdapterCreatePlan(CreatePlanActivity mCreatePlanActivity, TrainingModel mTrainingModel, int id) {
        this.mCreatePlanActivity = mCreatePlanActivity;
        this.mTrainingModel = mTrainingModel;
        this.id = id;
    }

    void addItem() {
        mTrainingModel.getExercises().add(mTrainingModel.getExercises().size() - 1, new ExerciseModel());
        new Handler(Looper.getMainLooper()).post(RecyclerAdapterCreatePlan.this::notifyDataSetChanged);

    }

    void delItem(int position) {
        mTrainingModel.getExercises().remove(position - 1);
        new Handler(Looper.getMainLooper()).post(RecyclerAdapterCreatePlan.this::notifyDataSetChanged);
    }

    ArrayList<ExerciseModel> getExerciseModels() {
        return mTrainingModel.getExercises();
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
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else if (position != getItemCount() - 1) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Создание нового представления
        LinearLayout linear = null;
        switch (viewType) {
            case 0:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_create_plan_header, parent, false);
                break;
            case 1:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_create_plan_body, parent, false);
                break;
            case 2:
                linear = (LinearLayout) LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_create_plan_footer, parent, false);
                break;
        }
        return new ViewHolder(linear);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        Log.e("onBindViewHolder: ", String.valueOf(getItemCount()));

        //header view
        TextView nameTraining = linear.findViewById(R.id.et_training_name_create);
        TextView nameTrainingDescription = linear.findViewById(R.id.ed_description_training);
        MaterialRippleLayout btAddImageTraining = linear.findViewById(R.id.bt_add_image_training);
        ImageView imageTraining = linear.findViewById(R.id.image_training_plan_create);
        CardView cvImageTraining = linear.findViewById(R.id.image_training_plan_card);

        //body view
        TextView delItemPlan = linear.findViewById(R.id.tv_cancel_item_create_plan);
        View delimiter = linear.findViewById(R.id.delimiter_exercise);
        EditText etNameExercise = linear.findViewById(R.id.et_exercise_name);
        EditText etDescription = linear.findViewById(R.id.et_description_exercise);
        MaterialRippleLayout btAddImageExercise = linear.findViewById(R.id.bt_add_image_exercise);
        MaterialRippleLayout btAddAudio = linear.findViewById(R.id.bt_add_audio_exercise);
        EditText etNumberRepetition = linear.findViewById(R.id.et_number_repetitions);
        EditText etTimeExercise = linear.findViewById(R.id.et_exercise_time);
        EditText etTimeBetweenExercise = linear.findViewById(R.id.et_time_between_exercise);
        EditText etRelaxTime = linear.findViewById(R.id.et_recovery_time);
        ImageView imageExercise = linear.findViewById(R.id.image_exercise_create);
        CardView cvImageExercise = linear.findViewById(R.id.image_exercise_card);

        //footer
        MaterialRippleLayout btAddEcercise = linear.findViewById(R.id.bt_add_item_exercise);
        MaterialRippleLayout btSaveTraining = linear.findViewById(R.id.bt_save_all_plan);
        TextView totalTimeTraining = linear.findViewById(R.id.tv_total_time);

        //header methods
        if (position == 0) {
            if (id > 0) {
                setDataEditTraining(imageTraining,
                        nameTraining,
                        nameTrainingDescription,
                        cvImageTraining);
            }

            btAddImageTraining.setOnClickListener(v -> {
                mCreatePlanActivity.requestMultiplePermissions();
                if (ContextCompat.checkSelfPermission(mCreatePlanActivity.getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    mCreatePlanActivity.choosePicturePlan(position, imageTraining, cvImageTraining);
            });

            nameTraining.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_class),
                        mCreatePlanActivity.getResources().getString(R.string.class_description),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    nameTraining.setText(et.getText());
                    mTrainingModel.setName(String.valueOf(et.getText()));
                    dialog.dismiss();
                });
            });

            nameTrainingDescription.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_description),
                        mCreatePlanActivity.getResources().getString(R.string.description_description_plan),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    nameTrainingDescription.setText(et.getText());
                    mTrainingModel.setDescription(String.valueOf(et.getText()));
                    dialog.dismiss();
                });
            });
        }
        //body methods
        if (position > 0 && position != getItemCount() - 1) {

            //разделитель и кнопка удаления упражнения
            if (position < mTrainingModel.getExercises().size() + 1) {
                delItemPlan.setVisibility(View.GONE);
                delimiter.setVisibility(View.VISIBLE);
            }

            if (position == 1) {
                delItemPlan.setVisibility(View.GONE);
                delimiter.setVisibility(View.GONE);
                if (mTrainingModel.getExercises().size() > 1) {
                    delimiter.setVisibility(View.VISIBLE);
                }

            } else if (position == mTrainingModel.getExercises().size()) {
                delItemPlan.setVisibility(View.VISIBLE);
                delimiter.setVisibility(View.GONE);
            }

            if (id > 0) {
                setDataEditExercise(position,
                        etNameExercise,
                        etDescription,
                        etNumberRepetition,
                        etTimeExercise,
                        etTimeBetweenExercise,
                        etRelaxTime,
                        imageExercise, cvImageExercise);
            }

            delItemPlan.setOnClickListener(v -> {
                mCreatePlanActivity.delItemExercise(position);
            });

            btAddAudio.setOnClickListener(v ->
                    mCreatePlanActivity.chooseAudioExercise(position)
            );

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
                    mTrainingModel.getExercises().get(position - 1).setRecoveryTime(temp_time);
                    etRelaxTime.setText(FormatTime.formatTime(temp_time));
                    dialog.dismiss();
                });

            });

            etTimeBetweenExercise.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.time_between_exercise),
                        mCreatePlanActivity.getResources().getString(R.string.time_between_exercise_description),
                        mCreatePlanActivity.getResources().getString(R.string.save));
                dialog.findViewById(R.id.bt_save_time).setOnClickListener(viev -> {
                    EditText min = dialog.findViewById(R.id.select_time_min),
                            sec = dialog.findViewById(R.id.select_time_sec);
                    if (Objects.equals(String.valueOf(min.getText()), "")) {
                        min.setText("0");
                    }
                    if (Objects.equals(String.valueOf(sec.getText()), "")) {
                        sec.setText("0");
                    }
                    long temp_time = Integer.valueOf(String.valueOf(min.getText())) * MILISEC_MIN;
                    temp_time += Long.valueOf(String.valueOf(sec.getText())) * MILISEC_SEC;
                    mTrainingModel.getExercises().get(position - 1).setTimeBetween(temp_time);
                    etTimeBetweenExercise.setText(FormatTime.formatTime(temp_time));
                    this.notifyItemChanged(getItemCount() - 1);
                    dialog.dismiss();
                });

            });

            etTimeExercise.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialogTime(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.time_exercise),
                        mCreatePlanActivity.getResources().getString(R.string.time_exercise_description),
                        mCreatePlanActivity.getResources().getString(R.string.save));

                dialog.findViewById(R.id.bt_save_time).setOnClickListener(viev -> {
                    EditText min = dialog.findViewById(R.id.select_time_min),
                            sec = dialog.findViewById(R.id.select_time_sec);
                    if (Objects.equals(String.valueOf(min.getText()), "")) {
                        min.setText("0");
                    }
                    if (Objects.equals(String.valueOf(sec.getText()), "")) {
                        sec.setText("0");
                    }
                    long temp_time = Integer.valueOf(String.valueOf(min.getText())) * MILISEC_MIN;
                    temp_time += Long.valueOf(String.valueOf(sec.getText())) * MILISEC_SEC;
                    mTrainingModel.getExercises().get(position - 1).setTime(temp_time);
                    etTimeExercise.setText(FormatTime.formatTime(temp_time));
                    this.notifyItemChanged(getItemCount() - 1);
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
                    mTrainingModel.getExercises().get(position - 1).setName(String.valueOf(et.getText()));
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
                    mTrainingModel.getExercises().get(position - 1).setDescription(String.valueOf(et.getText()));
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
                    mTrainingModel.getExercises().get(position - 1).setCountRepetitions(Integer.valueOf(String.valueOf(et.getText())));
                    dialog.dismiss();
                });
            });

            btAddImageExercise.setOnClickListener(v ->
                    mCreatePlanActivity.choosePictureExercise(position, imageExercise, cvImageExercise)
            );


        }

        //footer methods
        if (position == getItemCount() - 1) {
            btAddEcercise.setOnClickListener(v -> {
                mCreatePlanActivity.addItemExercise();
            });

            btSaveTraining.setOnClickListener(v -> {
                mCreatePlanActivity.setPlans(mTrainingModel);
            });

            totalTimeTraining.setText(setAllTime());
        }
    }

    private void setDataEditTraining(ImageView imageTraining, TextView nameTraining, TextView nameTrainingDescription, CardView cvImageTraining) {
        nameTraining.setText(mTrainingModel.getName());
        nameTrainingDescription.setText(mTrainingModel.getDescription());
        if (mTrainingModel.getImage() != null) {
            cvImageTraining.setVisibility(View.VISIBLE);
            Glide.with(mCreatePlanActivity.getContext())
                    .load(Uri.parse(mTrainingModel.getImage()))
                    .placeholder(R.mipmap.icon)
                    .fitCenter()
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(imageTraining);
        }
    }

    private String setAllTime() {
        long allTime = 0L;

        for (int i = 0; i < mTrainingModel.getExercises().size(); i++) {
            allTime += mTrainingModel.getExercises().get(i).getTime();
            allTime += mTrainingModel.getExercises().get(i).getTimeBetween();
        }
        mTrainingModel.setTime(allTime);
        return FormatTime.formatTime(allTime);
    }

    private void setDataEditExercise(int position, EditText etNameExercise, EditText etDescription, EditText etNumberRepetition, EditText etTimeExercise, EditText etTimeBetweenExercise, EditText etRelaxTime, ImageView image, CardView cvImage) {
        etNameExercise.setText(mTrainingModel.getExercises().get(position - 1).getName());
        etDescription.setText(mTrainingModel.getExercises().get(position - 1).getDescription());
        etNumberRepetition.setText(String.valueOf(mTrainingModel.getExercises().get(position - 1).getCountRepetitions()));
        etTimeExercise.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position - 1).getTime()));
        etTimeBetweenExercise.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position - 1).getTimeBetween()));
        etRelaxTime.setText(FormatTime.formatTime(mTrainingModel.getExercises().get(position - 1).getRecoveryTime()));
        if (mTrainingModel.getExercises().get(position - 1).getImage() != null) {
            cvImage.setVisibility(View.VISIBLE);
            Glide.with(mCreatePlanActivity.getContext())
                    .load(Uri.parse(mTrainingModel.getExercises().get(position - 1).getImage()))
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

    void setImage(Uri uriExercise, ImageView imageExercise, CardView cvExercise, int position, int type) {
        cvExercise.setVisibility(View.VISIBLE);
        Glide.with(mCreatePlanActivity.getContext()).load(uriExercise).into(imageExercise);
        if (type == 1)
            mTrainingModel.getExercises().get(position).setImage(uriExercise.toString());
        else
            mTrainingModel.setImage(uriExercise.toString());

    }

    @Override
    public int getItemCount() {
        return mTrainingModel.getExercises() == null ? 0 : mTrainingModel.getExercises().size() + 2;
    }
}
