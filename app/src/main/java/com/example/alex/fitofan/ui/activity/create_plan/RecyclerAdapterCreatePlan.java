package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.ExerciseModel;
import com.example.alex.fitofan.models.TrainingModel;
import com.example.alex.fitofan.utils.CustomDialog;
import com.example.alex.fitofan.utils.FormatTime;
import com.google.gson.Gson;

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
        mTrainingModel.getExercises().add(mTrainingModel.getExercises().size(), new ExerciseModel());
        this.notifyItemInserted(mTrainingModel.getExercises().size() + 1);
    }

    void delItem() {
        mTrainingModel.getExercises().remove(getItemCount() - 3);
        this.notifyItemRemoved(getItemCount() - 1);
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
        LinearLayout linerNameTraining = linear.findViewById(R.id.liner_name_training);
        LinearLayout linerDescriptionTraining = linear.findViewById(R.id.liner_description_training);
        TextView nameTraining = linear.findViewById(R.id.et_training_name_create);
        TextView nameTrainingDescription = linear.findViewById(R.id.ed_description_training);
        Button btAddImageTraining = linear.findViewById(R.id.bt_add_image_training);
        ImageView imageTraining = linear.findViewById(R.id.image_training_plan_create);
        CardView cvImageTraining = linear.findViewById(R.id.image_training_plan_card);

        //body view
        LinearLayout linerNameExercise = linear.findViewById(R.id.liner_name_exercise);
        LinearLayout linerDescriptionExercise = linear.findViewById(R.id.liner_description_exercise);
        LinearLayout linerTimeBetweenExercise = linear.findViewById(R.id.liner_time_between_exercise);
        LinearLayout linerRecoveryTime = linear.findViewById(R.id.liner_recovery_time);
        LinearLayout linerExerciseTime = linear.findViewById(R.id.liner_exercise_time);
        LinearLayout linerNumberApproaches = linear.findViewById(R.id.liner_number_approaches);
        TextView exerciseNumber = linear.findViewById(R.id.exercise_number);
        EditText etNameExercise = linear.findViewById(R.id.et_exercise_name);
        EditText etDescription = linear.findViewById(R.id.et_description_exercise);
        Button btAddImageExercise = linear.findViewById(R.id.bt_add_image_exercise);
        Button btAddAudio = linear.findViewById(R.id.bt_add_audio_exercise);
        EditText etNumberRepetition = linear.findViewById(R.id.et_number_approaches);
        EditText etTimeExercise = linear.findViewById(R.id.et_exercise_time);
        EditText etTimeBetweenExercise = linear.findViewById(R.id.et_time_between_exercise);
        EditText etRelaxTime = linear.findViewById(R.id.et_recovery_time);
        ImageView imageExercise = linear.findViewById(R.id.image_exercise_create);
        CardView cvImageExercise = linear.findViewById(R.id.image_exercise_card);

        //footer
        Button btAddExercise = linear.findViewById(R.id.bt_add_exercise);
        Button btDelExercise = linear.findViewById(R.id.bt_del_exercise);
        Button btSaveTraining = linear.findViewById(R.id.bt_save_plan);
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

            linerNameTraining.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_class),
                        mCreatePlanActivity.getResources().getString(R.string.class_description),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                //set text if !null
                setDataEt(dialog, nameTraining);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    nameTraining.setText(et.getText());
                    mTrainingModel.setName(String.valueOf(et.getText()));
                    dialog.dismiss();
                });
            });

            linerDescriptionTraining.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_description),
                        mCreatePlanActivity.getResources().getString(R.string.description_description_plan),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                //set text if !null
                setDataEt(dialog, nameTrainingDescription);

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

            etNumberRepetition.setText("1");

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

            btAddAudio.setOnClickListener(v ->
                    mCreatePlanActivity.chooseAudioExercise(position)
            );

            exerciseNumber.setText(mCreatePlanActivity.getResources().getString(R.string.exercise) + " #" + position);

            linerRecoveryTime.setOnClickListener(v -> {
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

            linerTimeBetweenExercise.setOnClickListener(v -> {
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

            linerExerciseTime.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialogSpinner(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.duration_exercise),
                        mCreatePlanActivity.getResources().getString(R.string.time_exercise_description),
                        mCreatePlanActivity.getResources().getString(R.string.save), 2);

                LinearLayout time = dialog.findViewById(R.id.liner_time);
                LinearLayout text = dialog.findViewById(R.id.liner_text);
                Spinner sp = dialog.findViewById(R.id.spinner_type);

                String[] data = {"Time", "Distance", "Weight", "Count"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(mCreatePlanActivity.getContext(), R.layout.spinner_item, data);
                adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
                sp.setAdapter(adapter);
                sp.setSelection(0);
                sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int p, long id) {
                        switch (p) {
                            case 0:
                                time.setVisibility(View.VISIBLE);
                                text.setVisibility(View.GONE);
                                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(viev -> {
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
                                    mTrainingModel.getExercises().get(position - 1).setTime(temp_time * 10 + p);
                                    etTimeExercise.setText(FormatTime.formatTime(temp_time));
                                    RecyclerAdapterCreatePlan.this.notifyItemChanged(getItemCount() - 1);
                                    Log.e("onItemSelected: ", String.valueOf(mTrainingModel.getExercises().get(position).getTime()));
                                    dialog.dismiss();
                                });
                                break;
                            default:
                                time.setVisibility(View.GONE);
                                text.setVisibility(View.VISIBLE);
                                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                                    long temp = Integer.valueOf(String.valueOf(et.getText()));
                                    mTrainingModel.getExercises().get(position - 1).setTime(temp * 10 + p);
                                    etTimeExercise.setText(et.getText());
                                    RecyclerAdapterCreatePlan.this.notifyItemChanged(getItemCount() - 1);
                                    Log.e("onItemSelected: ", String.valueOf(mTrainingModel.getExercises().get(position).getTime()));
                                    dialog.dismiss();
                                });
                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            });

            linerNameExercise.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.exercise),
                        mCreatePlanActivity.getResources().getString(R.string.exercise_name_description),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                //set text if !null
                setDataEt(dialog, etNameExercise);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    etNameExercise.setText(et.getText());
                    mTrainingModel.getExercises().get(position - 1).setName(String.valueOf(et.getText()));
                    dialog.dismiss();
                });
            });

            linerDescriptionExercise.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_description),
                        mCreatePlanActivity.getResources().getString(R.string.description_description_plan),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                //set text if !null
                setDataEt(dialog, etDescription);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    etDescription.setText(et.getText());
                    mTrainingModel.getExercises().get(position - 1).setDescription(String.valueOf(et.getText()));
                    dialog.dismiss();
                });
            });

            linerNumberApproaches.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.number_approaches),
                        mCreatePlanActivity.getResources().getString(R.string.number_repetitions_description),
                        mCreatePlanActivity.getResources().getString(R.string.save), 2);

                //set text if !null
                setDataEt(dialog, etNumberRepetition);

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
            btAddExercise.setOnClickListener(v -> {
                mCreatePlanActivity.addItemExercise();
            });

            btSaveTraining.setOnClickListener(v -> {
                Log.e("onBindViewHolder: ", new Gson().toJson(mTrainingModel));
                mCreatePlanActivity.setPlans(mTrainingModel);
            });

            totalTimeTraining.setText(setAllTime());

            btDelExercise.setOnClickListener(v -> {
                if (getItemCount() > 3) {
                    delItem();
                }
            });
        }
    }

    private void setDataEt(Dialog dialog, TextView nameTraining) {
        if (nameTraining.getText().length() > 0) {
            EditText et = dialog.findViewById(R.id.et_add_field_dialog);
            et.setText(nameTraining.getText());
        }
    }

    private void setDataEditTraining(ImageView imageTraining, TextView nameTraining, TextView nameTrainingDescription, CardView cvImageTraining) {
        nameTraining.setText(mTrainingModel.getName());
        nameTrainingDescription.setText(mTrainingModel.getDescription());
        if (mTrainingModel.getImage() != null) {
            cvImageTraining.setVisibility(View.VISIBLE);
            Glide.with(mCreatePlanActivity.getContext())
                    .load(Uri.parse(mTrainingModel.getImage()))
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
                    .into(image);
        }
    }

    void setAudio(Uri uriExercise, int position) {
        mTrainingModel.getExercises().get(position - 1).setAudio(uriExercise.toString());
    }

    void setImage(Uri uriExercise, ImageView imageExercise, CardView cvExercise) {
        cvExercise.setVisibility(View.VISIBLE);
        Glide.with(mCreatePlanActivity.getContext()).load(uriExercise).into(imageExercise);
    }

    @Override
    public int getItemCount() {
        return mTrainingModel.getExercises() == null ? 0 : mTrainingModel.getExercises().size() + 2;
    }
}