package vn.com.grooo.mediacreator.common.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

import vn.com.grooo.mediacreator.R;

/**
 * Created by NgocThai on 07/01/2017.
 */

public class FragmentUtils {

    public static boolean checkExistStack(String stack, AppCompatActivity context) {
        boolean result = false;
        ArrayList<String> ls = getAllStackName(context);
        for (int i = 0; i < ls.size(); i++) {
            if (stack.equalsIgnoreCase(ls.get(i))) {
                result = true;
            }
        }
        return result;
    }

    public static ArrayList<String> getAllStackName(AppCompatActivity context) {
        ArrayList<String> ls = new ArrayList<>();
        FragmentManager fm = context.getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 1) {
            for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
                ls.add(fm.getBackStackEntryAt(i).getName());
            }
        }
        return ls;
    }

    public static String checkLatestFm(AppCompatActivity context) {
        ArrayList<String> lsStack = getAllStackName(context);
        if (countStackFm(context) > 1 && lsStack.size() > 1) {
            int index = countStackFm(context) - 1;
            return lsStack.get(index);
        }
        return "";
    }

    public static int countStackFm(AppCompatActivity context) {
        FragmentManager fm = context.getSupportFragmentManager();
        return fm.getBackStackEntryCount();
    }

    public static void replaceFragment(Fragment fm, int idFrameLayout, String stack, AppCompatActivity context) {
        FragmentTransaction ft;
        ft = context.getSupportFragmentManager().beginTransaction();
        ft.replace(idFrameLayout, fm);
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.addToBackStack(stack);
        ft.commit();
    }

    public static void replaceFragment(Fragment fm, int idFrameLayout, AppCompatActivity context) {
        FragmentTransaction ft;
        ft = context.getSupportFragmentManager().beginTransaction();
        ft.replace(idFrameLayout, fm);
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void addFragment(Fragment fm, int idFrameLayout, String stack, AppCompatActivity context) {
        FragmentTransaction ft;
        ft = context.getSupportFragmentManager().beginTransaction();
        ft.add(idFrameLayout, fm);
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
        ft.addToBackStack(stack);
        ft.commit();
    }

    public static void addFragment(Fragment fm, int idFrameLayout, AppCompatActivity context) {
        FragmentTransaction ft;
        ft = context.getSupportFragmentManager().beginTransaction();
        ft.add(idFrameLayout, fm);
        ft.setCustomAnimations(R.anim.anim_in, R.anim.anim_out);
        ft.addToBackStack(null);
        ft.commit();
    }

    public static void popStackFm(String stack, AppCompatActivity context) {
        FragmentManager fm = context.getSupportFragmentManager();
        fm.popBackStack(stack, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void popStackFm(AppCompatActivity context) {
        FragmentManager fm = context.getSupportFragmentManager();
        fm.popBackStack();
    }
}
