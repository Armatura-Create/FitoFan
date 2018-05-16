package com.example.alex.fitofan.utils;

import android.content.Context;
import android.content.Intent;

import com.example.alex.fitofan.ui.activity.comments.CommentsActivity;
import com.example.alex.fitofan.ui.activity.preview_plan.PreviewPlanActivity;
import com.example.alex.fitofan.ui.activity.user_profile.UserProfileActivity;

public class ActionPlanCard {

    public ActionPlanCard() {
    }

    public static void goComments(Context context, String planId, String userId) {
        Intent intent = new Intent(context, CommentsActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    public static void goUserProfile(Context context, String uid) {
        Intent intent = new Intent(context, UserProfileActivity.class);
        intent.putExtra("uid", uid);
        context.startActivity(intent);
    }

    public static void goPreviewPlan(Context context, String planId, String userId, boolean isWall) {
        Intent intent = new Intent(context, PreviewPlanActivity.class);
        intent.putExtra("planId", planId);
        intent.putExtra("isWall", isWall);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

}
