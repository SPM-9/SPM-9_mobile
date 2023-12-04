package com.fxxkywcx.nostudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fxxkywcx.nostudy.activities.AboutInfoActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.fxxkywcx.nostudy.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

    public void JumpToWarning(View view) {
    }

    public void JumpToScore(View view) {
    }

    public void JumpToHomework(View view) {
    }

    public void JumpToExam(View view) {
    }

    public void JumpToChat(View view) {
    }

    public void JumpToResourceDownload(View view) {
    }

    public void JumpToSign(View view) {
    }

    public void JumpToChooseCourse(View view) {
    }

    public void modifypassword(View view) {}

    public void exit(View view) {}

    public void about(View view) {
        Intent intent = new Intent(this, AboutInfoActivity.class);
        startActivity(intent);
    }
}