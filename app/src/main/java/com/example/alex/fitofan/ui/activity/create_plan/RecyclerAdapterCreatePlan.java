package com.example.alex.fitofan.ui.activity.create_plan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.example.alex.fitofan.R;
import com.example.alex.fitofan.models.GetExerciseModel;
import com.example.alex.fitofan.models.GetPlanModel;
import com.example.alex.fitofan.models.GetTrainingModel;
import com.example.alex.fitofan.models.PhotoModel;
import com.example.alex.fitofan.utils.CompressImage;
import com.example.alex.fitofan.utils.CountData;
import com.example.alex.fitofan.utils.CustomDialog.CustomDialog;
import com.example.alex.fitofan.utils.CustomDialog.RecyclerAdapterCard;
import com.example.alex.fitofan.utils.FormatTime;
import com.example.alex.fitofan.utils.ItemClickSupport;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Objects;

public class RecyclerAdapterCreatePlan extends RecyclerView.Adapter<RecyclerAdapterCreatePlan.ViewHolder> {

    //Предоставляет ссылку на представления, используемые в RecyclerView
    private GetPlanModel mPlanModel;
    private CreatePlanActivity mCreatePlanActivity;
    private ArrayList<RecyclerGridAdapterPhotos> adapters;
    private boolean isEdit;
    private final long MILISEC_MIN = 60000L;
    private final long MILISEC_SEC = 1000L;
    private final String levelZero = "#d1d3d1";
    private final String levelOne = "#83ca9c";
    private final String levelTwo = "#fece0b";
    private final String levelThree = "#f05448";

    RecyclerAdapterCreatePlan(CreatePlanActivity mCreatePlanActivity, GetPlanModel mPlanModel, boolean isEdit) {
        this.isEdit = isEdit;
        this.mCreatePlanActivity = mCreatePlanActivity;
        this.mPlanModel = mPlanModel;
        adapters = new ArrayList<>();
        if (!isEdit) {
            this.mPlanModel.getExercises().get(0).setPhotos(new ArrayList<>());
            this.mPlanModel.setTraining(new GetTrainingModel());

            adapters.add(new RecyclerGridAdapterPhotos(mCreatePlanActivity));
        } else {
            for (int i = 0; i < (mPlanModel.getExercises() != null ? mPlanModel.getExercises().size() : 0); i++) {
                adapters.add(new RecyclerGridAdapterPhotos(mCreatePlanActivity, mPlanModel.getExercises().get(i).getImage(), mPlanModel.getExercises().get(i).getPhotos(), this));
                adapters.get(i).notifyDataSetChanged();
            }
        }
    }

    void addItem() {
        mPlanModel.getExercises().add(mPlanModel.getExercises().size(), new GetExerciseModel());
        mPlanModel.getExercises().get(mPlanModel.getExercises().size() - 1).setPhotos(new ArrayList<>());
        adapters.add(mPlanModel.getExercises().size() - 1, new RecyclerGridAdapterPhotos(mCreatePlanActivity));
        if (isEdit)
            mPlanModel.getExercises().get(mPlanModel.getExercises().size() - 1).setNew(isEdit);
        this.notifyItemInserted(mPlanModel.getExercises().size() + 1);
    }

    private void delItem() {
        mPlanModel.getExercises().remove(getItemCount() - 3);
        adapters.remove(getItemCount() - 3);
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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        //Заполнение заданного представления данными
        final LinearLayout linear = holder.mLinearLayout;

        //header view
        LinearLayout linerNameTraining = linear.findViewById(R.id.liner_name_training);
        LinearLayout linerDescriptionTraining = linear.findViewById(R.id.liner_description_training);
        LinearLayout linerInventoryTraining = linear.findViewById(R.id.liner_inventory_training);
        TextView nameTraining = linear.findViewById(R.id.et_training_name_create);
        TextView nameTrainingDescription = linear.findViewById(R.id.ed_description_training);
        TextView inventory = linear.findViewById(R.id.ed_inventory_training);
        Button btAddImageTraining = linear.findViewById(R.id.bt_add_image_training);
        ImageView imageTraining = linear.findViewById(R.id.image_training_plan_create);
        CardView cvImageTraining = linear.findViewById(R.id.image_training_plan_card);
        TextView textDescription = linear.findViewById(R.id.text_description_plan);
        TextView textName = linear.findViewById(R.id.text_name_plan);
        TextView difficultyLevel = linear.findViewById(R.id.difficulty_level_text);
        SeekBar levelTraining = linear.findViewById(R.id.levelTraining);

        //body view
        RecyclerView rv_images = linear.findViewById(R.id.image_exercise_rv);
        LinearLayout linerNameExercise = linear.findViewById(R.id.liner_name_exercise);
        LinearLayout linerTimeBetweenExercise = linear.findViewById(R.id.liner_time_between_exercise);
        LinearLayout linerRecoveryTime = linear.findViewById(R.id.liner_recovery_time);
        LinearLayout linerExerciseTime = linear.findViewById(R.id.liner_exercise_time);
        LinearLayout linerNumberApproaches = linear.findViewById(R.id.liner_number_approaches);
        LinearLayout borderDescription = linear.findViewById(R.id.background_border_description);
        LinearLayout borderAudio = linear.findViewById(R.id.background_border_audio);
        TextView exerciseNumber = linear.findViewById(R.id.exercise_number);
        TextView etNameExercise = linear.findViewById(R.id.et_exercise_name);
        ImageButton btAddImageExercise = linear.findViewById(R.id.bt_add_image_exercise);
        ImageButton btAddAudio = linear.findViewById(R.id.bt_add_audio_exercise);
        ImageButton btAddDescriptionExercise = linear.findViewById(R.id.bt_add_description);
        TextView etNumberRepetition = linear.findViewById(R.id.et_number_approaches);
        TextView etTimeExercise = linear.findViewById(R.id.et_exercise_time);
        TextView etTimeBetweenExercise = linear.findViewById(R.id.et_time_between_exercise);
        TextView etRelaxTime = linear.findViewById(R.id.et_recovery_time);
        TextView textNameExrcise = linear.findViewById(R.id.text_name_exercises);
        VideoView videoView = linear.findViewById(R.id.video_view);

        //footer
        Button btAddExercise = linear.findViewById(R.id.bt_add_exercise);
        Button btDelExercise = linear.findViewById(R.id.bt_del_exercise);
        Button btSaveTraining = linear.findViewById(R.id.bt_save_plan);
        TextView totalTimeTraining = linear.findViewById(R.id.tv_total_time);

        //header methods
        if (position == 0) {
            if (isEdit) {
                setDataEditTraining(imageTraining,
                        nameTraining,
                        nameTrainingDescription,
                        cvImageTraining);
            }

            textDescription.setText(textDescription.getText() + "*");
            textName.setText(textName.getText() + "*");
            btAddImageTraining.setText(btAddImageTraining.getText() + "*");

            difficultyLevel.setText(mCreatePlanActivity.getResources().getString(R.string.difficulty_level) + " : " + 1);
            levelTraining.getProgressDrawable().setColorFilter(Color.parseColor(levelZero), PorterDuff.Mode.SRC_ATOP); // полоска
            levelTraining.getThumb().setColorFilter(Color.parseColor(levelZero), PorterDuff.Mode.SRC_ATOP); // кругляшок

            btAddImageTraining.setOnClickListener(v -> {
                mCreatePlanActivity.requestMultiplePermissions();
                if (ContextCompat.checkSelfPermission(mCreatePlanActivity.getContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                    mCreatePlanActivity.choosePicture(position, imageTraining, cvImageTraining);
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
                    mPlanModel.getTraining().setName(String.valueOf(et.getText()));

                    clearFocus(v);

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
                    mPlanModel.getTraining().setDescription(String.valueOf(et.getText()));

                    clearFocus(v);

                    dialog.dismiss();
                });
            });

            linerInventoryTraining.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_inventory),
                        mCreatePlanActivity.getResources().getString(R.string.inventory_plan),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                //set text if !null
                setDataEt(dialog, inventory);

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                    inventory.setText(et.getText());
                    mPlanModel.getTraining().setInventory(String.valueOf(et.getText()));

                    clearFocus(v);

                    dialog.dismiss();
                });
            });

            levelTraining.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    switch (progress){
                        case 0:
                            seekBar.getProgressDrawable().setColorFilter(Color.parseColor(levelZero), PorterDuff.Mode.SRC_ATOP); // полоска
                            seekBar.getThumb().setColorFilter(Color.parseColor(levelZero), PorterDuff.Mode.SRC_ATOP); // кругляшок
                            break;
                        case 1:
                            seekBar.getProgressDrawable().setColorFilter(Color.parseColor(levelOne), PorterDuff.Mode.SRC_ATOP); // полоска
                            seekBar.getThumb().setColorFilter(Color.parseColor(levelOne), PorterDuff.Mode.SRC_ATOP); // кругляшок
                            break;
                        case 2:
                            seekBar.getProgressDrawable().setColorFilter(Color.parseColor(levelTwo), PorterDuff.Mode.SRC_ATOP); // полоска
                            seekBar.getThumb().setColorFilter(Color.parseColor(levelTwo), PorterDuff.Mode.SRC_ATOP); // кругляшок
                            break;
                        case 3:
                            seekBar.getProgressDrawable().setColorFilter(Color.parseColor(levelThree), PorterDuff.Mode.SRC_ATOP); // полоска
                            seekBar.getThumb().setColorFilter(Color.parseColor(levelThree), PorterDuff.Mode.SRC_ATOP); // кругляшок
                            break;
                    }

                    difficultyLevel.setText(mCreatePlanActivity.getResources().getString(R.string.difficulty_level) + " : " + progress);
                    mPlanModel.getTraining().setPlanLevel(String.valueOf(progress));
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }

        //body methods
        if (position > 0 && position != getItemCount() - 1) {

            etRelaxTime.setText(FormatTime.formatTime(0));
            etTimeBetweenExercise.setText(FormatTime.formatTime(0));
            etTimeExercise.setText(FormatTime.formatTime(0));
            etNameExercise.setText("");
            etNumberRepetition.setText("1");

            textNameExrcise.setText(textNameExrcise.getText() + "*");

            if (isEdit) {
                setDataEditExercise(position,
                        etNameExercise,
                        etNumberRepetition,
                        etTimeExercise,
                        etTimeBetweenExercise,
                        etRelaxTime,
                        rv_images);
            }


            GridLayoutManager layoutManager = new GridLayoutManager(mCreatePlanActivity.getContext(), 3);
            rv_images.setLayoutManager(layoutManager);
            if (!isEdit)
                rv_images.setAdapter(adapters.get(position - 1));

            btAddAudio.setOnClickListener(v ->
                    mCreatePlanActivity.chooseAudioExercise(position, borderAudio)
            );

            exerciseNumber.setText("#" + position);

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
                    mPlanModel.getExercises().get(position - 1).setRecoveryTime(String.valueOf(temp_time));
                    etRelaxTime.setText(FormatTime.formatTime(temp_time));
                    this.notifyItemChanged(getItemCount() - 1);

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

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
                    mPlanModel.getExercises().get(position - 1).setTimeBetween(String.valueOf(temp_time));
                    etTimeBetweenExercise.setText(FormatTime.formatTime(temp_time));
                    this.notifyItemChanged(getItemCount() - 1);

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

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
                RecyclerView rv = dialog.findViewById(R.id.rv);

                LinearLayoutManager linearLayoutManager =
                        new LinearLayoutManager(mCreatePlanActivity.getContext(),
                                LinearLayoutManager.HORIZONTAL, false);
                rv.setLayoutManager(linearLayoutManager);
                final RecyclerAdapterCard adapter = new RecyclerAdapterCard();
                rv.setAdapter(adapter);

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
                    mPlanModel.getExercises().get(position - 1).setTime(String.valueOf(temp_time * 10));
                    etTimeExercise.setText(FormatTime.formatTime(temp_time));
                    RecyclerAdapterCreatePlan.this.notifyItemChanged(getItemCount() - 1);

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

                    dialog.dismiss();
                });

                ItemClickSupport.addTo(rv).setOnItemClickListener((recyclerView, position1, v1) -> {
                    adapter.getHolder().changeState(position1);
                    switch (position1) {
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
                                mPlanModel.getExercises().get(position - 1).setTime(String.valueOf(temp_time * 10 + position1));
                                etTimeExercise.setText(FormatTime.formatTime(temp_time));
                                RecyclerAdapterCreatePlan.this.notifyItemChanged(getItemCount() - 1);
                                dialog.dismiss();
                            });
                            break;
                        default:
                            time.setVisibility(View.GONE);
                            text.setVisibility(View.VISIBLE);
                            dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(viev -> {
                                EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                                long temp = et.getText().length() <= 0 ? 0 : Integer.valueOf(String.valueOf(et.getText()));
                                mPlanModel.getExercises().get(position - 1).setTime(String.valueOf(temp * 10 + position1));
                                etTimeExercise.setText(FormatTime.formatCountWithDimension(Long.valueOf(mPlanModel.getExercises().get(position - 1).getTime())));
                                RecyclerAdapterCreatePlan.this.notifyItemChanged(getItemCount() - 1);
                                dialog.dismiss();
                            });
                            break;
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
                    mPlanModel.getExercises().get(position - 1).setName(String.valueOf(et.getText()));

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

                    dialog.dismiss();
                });
            });

            btAddDescriptionExercise.setOnClickListener(v -> {
                Dialog dialog = CustomDialog.dialog(mCreatePlanActivity.getContext(),
                        mCreatePlanActivity.getResources().getString(R.string.training_description),
                        mCreatePlanActivity.getResources().getString(R.string.description_description_plan),
                        mCreatePlanActivity.getResources().getString(R.string.save), 1);

                EditText et = dialog.findViewById(R.id.et_add_field_dialog);
                if (mPlanModel.getExercises().get(position - 1).getDescription() != null)
                    et.setText(mPlanModel.getExercises().get(position - 1).getDescription());

                dialog.findViewById(R.id.bt_dialog_add).setOnClickListener(v1 -> {
                    mPlanModel.getExercises().get(position - 1).setDescription(String.valueOf(et.getText()));
                    if (et.getText().length() > 0)
                        borderDescription.setVisibility(View.VISIBLE);
                    else
                        borderDescription.setVisibility(View.INVISIBLE);

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

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
                    mPlanModel.getExercises().get(position - 1).setCountRepetitions(String.valueOf(et.getText()));

                    if (isEdit)
                        mPlanModel.getExercises().get(position - 1).setEdit(isEdit);

                    clearFocus(v);

                    dialog.dismiss();
                });
            });

            btAddImageExercise.setOnClickListener(v -> {
                if (adapters.get(position - 1).getItemCount() < 1) {
                    Dialog dialog = CustomDialog.dialogSimple(mCreatePlanActivity.getContext(), "Select an action", "", "Video", "Image");
                    dialog.findViewById(R.id.bt_positive).setOnClickListener(v1 -> {
                        mCreatePlanActivity.recordingVideo(position, videoView);
                        dialog.cancel();
                    });
                    dialog.findViewById(R.id.bt_negative).setOnClickListener(v1 -> {
                        if (adapters.get(position - 1).getItemCount() < 5) {
                            mCreatePlanActivity.choosePicture(position);
                            setVideoRealPath("", position, videoView);
                        } else
                            Toast.makeText(mCreatePlanActivity.getContext(), "Max 5 images", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    });
                } else if (adapters.get(position - 1).getItemCount() < 5)
                    mCreatePlanActivity.choosePicture(position);
                else
                    Toast.makeText(mCreatePlanActivity.getContext(), "Max 5 images", Toast.LENGTH_SHORT).show();
            });
        }

        //footer methods
        if (position == getItemCount() - 1) {
            btAddExercise.setOnClickListener(v ->
                    mCreatePlanActivity.addItemExercise());

            btSaveTraining.setOnClickListener(v -> {
                for (int i = 0; i < mPlanModel.getExercises().size(); i++) {
                    mPlanModel.getExercises().get(i).setPhotos(adapters.get(i).getImagesString());
                }
                mCreatePlanActivity.sendPlan(mPlanModel);
            });

            totalTimeTraining.setText(setAllTime());

            btDelExercise.setOnClickListener(v -> {
                if (getItemCount() > 3) {
                    if (isEdit)
                        mCreatePlanActivity.getDeleteExercises().add(mPlanModel.getExercises().get(holder.getAdapterPosition() - 2).getId());
                    delItem();
                }
            });
        }
    }

    private void clearFocus(View v) {
        v.clearFocus();
        InputMethodManager imm = (InputMethodManager) mCreatePlanActivity.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    private void setDataEt(Dialog dialog, TextView nameTraining) {
        if (nameTraining.getText().length() > 0) {
            EditText et = dialog.findViewById(R.id.et_add_field_dialog);
            et.setText("");
            et.append(nameTraining.getText());
        }
    }

    private void setDataEditTraining(ImageView imageTraining, TextView nameTraining, TextView nameTrainingDescription, CardView cvImageTraining) {
        nameTraining.setText(mPlanModel.getTraining().getName());
        nameTrainingDescription.setText(mPlanModel.getTraining().getDescription());
        if (mPlanModel.getTraining().getImage() != null) {
            cvImageTraining.setVisibility(View.VISIBLE);
            Glide.with(mCreatePlanActivity.getContext())
                    .load(Uri.parse(mPlanModel.getTraining().getImage()))
                    .into(imageTraining);
        }
    }

    private String setAllTime() {

        long allTime = CountData.mathData(mPlanModel).getTimeLong();

        mPlanModel.getTraining().setPlan_time(String.valueOf(allTime));
        return CountData.mathData(mPlanModel).getTime();
    }

    private void setDataEditExercise(int position, TextView etNameExercise, TextView etNumberRepetition, TextView etTimeExercise, TextView etTimeBetweenExercise, TextView etRelaxTime, RecyclerView rv) {
        etNameExercise.setText(mPlanModel.getExercises().get(position - 1).getName());
        etNumberRepetition.setText(String.valueOf(mPlanModel.getExercises().get(position - 1).getCountRepetitions()));
        etTimeExercise.setText(FormatTime.formatCountWithDimension(Long.valueOf(mPlanModel.getExercises().get(position - 1).getTime())));
        etTimeBetweenExercise.setText(FormatTime.formatTime(Long.valueOf(mPlanModel.getExercises().get(position - 1).getTimeBetween())));
        etRelaxTime.setText(FormatTime.formatTime(Long.valueOf(mPlanModel.getExercises().get(position - 1).getRecoveryTime())));
        rv.setAdapter(adapters.get(position - 1));
    }

    void setAudio(Uri uriExercise, int position, LinearLayout border) {
        if (uriExercise != null)
            border.setVisibility(View.VISIBLE);
        else
            border.setVisibility(View.INVISIBLE);
        mPlanModel.getExercises().get(position - 1).setMusicUrls(String.valueOf(uriExercise));
        if (isEdit)
            mPlanModel.getExercises().get(position - 1).setEdit(isEdit);
    }

    void setImage(Uri uri, ImageView imageExercise, CardView cvExercise) {
        cvExercise.setVisibility(View.VISIBLE);
        if (uri != null)
            Glide.with(mCreatePlanActivity.getContext())
                    .load(uri)
                    .into(imageExercise);
        if (isEdit) {
            mPlanModel.getTraining().setEditPhoto(isEdit);
        }
    }

    void setImageExercise(Uri uri, int position) {
        try {
            Bitmap imageBitmap = MediaStore.Images.Media.getBitmap(mCreatePlanActivity.getContentResolver(), uri);
            imageBitmap = CompressImage.compressImageFromBitmap(imageBitmap);
            PhotoModel tempPhotoModel = new PhotoModel();
            tempPhotoModel.setImagePath(URLEncoder.encode(CompressImage.getBase64FromBitmap(imageBitmap), "UTF-8"));
            adapters.get(position - 1).addImage(uri, tempPhotoModel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void setVideoRealPath(String uri, int position, VideoView video) {
        mPlanModel.getExercises().get(position - 1).setVideoUrl(uri);
        video.setVisibility(View.VISIBLE);

        video.setVideoURI(Uri.parse(uri));
        video.setOnPreparedListener(mp -> {
            Log.d("START VIDEO", "start Uri");
            video.start();
            mp.setVolume(0, 0);
            mp.setLooping(true);
        });

        video.requestFocus();
    }

    @Override
    public int getItemCount() {
        return mPlanModel.getExercises() == null ? 0 : mPlanModel.getExercises().size() + 2;
    }
}