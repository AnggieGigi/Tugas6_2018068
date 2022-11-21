package com.example.pertemuan4_actionbar;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.example.pertemuan4_actionbar.databinding.FragmentAlarmBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import java.util.Calendar;

public class AlarmFragment extends Fragment {
    private FragmentAlarmBinding binding;
    private MaterialTimePicker picker;
    private Calendar calendar;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding =
                FragmentAlarmBinding.inflate(getLayoutInflater());
        getActivity().setContentView(binding.getRoot());
        createNotificationChannel();
        binding.selectedTimeBtn.setOnClickListener(new
                                                           View.OnClickListener() {
                                                               @Override
                                                               public void onClick(View view) {
                                                                   showTimePicker();
                                                               }
                                                           });
        binding.setAlarmBtn.setOnClickListener(new
                                                       View.OnClickListener() {
                                                           @Override
                                                           public void onClick(View view) {
                                                               setAlarm();
                                                           }
                                                       });
        binding.cancelAlarmBtn.setOnClickListener(new
                                                          View.OnClickListener() {
                                                              @Override
                                                              public void onClick(View view) {
                                                                  cancelAlarm();
                                                              }
                                                          });
    }

    private void cancelAlarm() {
        //untuk menggagalkan alarm yang sudah disetel
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);
        if (alarmManager == null) {
            alarmManager = (AlarmManager)
                    getActivity().getSystemService(Context.ALARM_SERVICE);
        }
        alarmManager.cancel(pendingIntent);
        Toast.makeText(getActivity(), "Alarm Cancelled",
                Toast.LENGTH_SHORT).show();
    }

    private void setAlarm() {
        //untuk menjalankan alarm yang sudah disetel
        alarmManager = (AlarmManager)
                getActivity().getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(getActivity(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getActivity(), 0,
                intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(getActivity(), "Alarm Set Successfully",
                Toast.LENGTH_SHORT).show();
    }

    private void showTimePicker() {
        //memunculkan dialog timepicker menggunakan library dari android
        picker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Alarm Time")
                .build();
        picker.show(getActivity().getSupportFragmentManager(), "AlarmManager");
        //mengeset waktu didalam view
        picker.addOnPositiveButtonClickListener(new
                                                        View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                if (picker.getHour() > 12) {
                                                                    binding.selectedTime.setText(
                                                                            String.format("%02d : %02d",
                                                                                    picker.getHour(), picker.getMinute())
                                                                    );
                                                                } else {
                                                                    binding.selectedTime.setText(picker.getHour()
                                                                            + " : " + picker.getMinute() + " ");
                                                                }
                                                                //menangkap inputan jam kalian lalu memulai alarm
                                                                calendar = Calendar.getInstance();
                                                                calendar.set(Calendar.HOUR_OF_DAY,
                                                                        picker.getHour());
                                                                calendar.set(Calendar.MINUTE,
                                                                        picker.getMinute());
                                                                calendar.set(Calendar.SECOND, 0);
                                                                calendar.set(Calendar.MILLISECOND, 0);
                                                            }
                                                        });
    }

    private void createNotificationChannel() {
        //mendeskripsikan channel notifikasi yang akan dibangun
        CharSequence name = "INI ALARM MANAGER";
        String description = "PRAKTIKUM BAB5 TENTANG ALARM MANAGER";
        //tingkat importance = high ( penting sekali )
        int importance = NotificationManager.IMPORTANCE_HIGH;
        NotificationChannel channel = new
                NotificationChannel("AlarmManager", name, importance);
        channel.setDescription(description);
        //membuka izin pengaturan dari aplikasi untuk memulai ervice notifikasi
        NotificationManager notificationManager =
                getActivity().getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle
                                     savedInstanceState) {
        return
                inflater.inflate(R.layout.fragment_alarm, container, false);
    }


}
