package com.walhalla.ttvloader.activity.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.walhalla.ttvloader.R;
import com.walhalla.ttvloader.databinding.FragmentNumberInputBinding;
import com.walhalla.utils.ApkInstaller;

import java.util.HashMap;
import java.util.Map;


public class NumberInputFragment extends Fragment {

    private FragmentNumberInputBinding binding;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String NUMBER_KEY = "number_key";
    private static final String KEY_SELECTED_INTENT = "selectedIntent";

    int selectedIntent = 0;

    private final Map<Integer, Integer> intentRadioMap = new HashMap<Integer, Integer>() {{
        put(0, R.id.radioYoutube);
        put(1, R.id.radioInstagram);
        put(2, R.id.radioOkru);
        put(3, R.id.radioTiktok);
        put(4, R.id.radioLikee);
    }};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNumberInputBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        int savedNumber = sharedPreferences.getInt(NUMBER_KEY, 0);
        binding.numberInput.setText(String.valueOf(savedNumber));

        binding.okButton.setOnClickListener(v -> {
            String numberStr = binding.numberInput.getText().toString();
            if (!numberStr.isEmpty()) {
                int number = Integer.parseInt(numberStr);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(NUMBER_KEY, number);
                editor.apply();
                ((ToolsActivity) getActivity()).clickOk(number, selectedIntent);
            } else {
                Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show();
            }
        });

        binding.fdroid.setOnClickListener(v -> {
            ApkInstaller.downloadAndInstallApk(getContext(),
                    "https://f-droid.org/F-Droid.apk"
            );
        });


        selectedIntent = sharedPreferences.getInt(KEY_SELECTED_INTENT, 0);
        binding.radioGroupIntents.check(getCheckedIdForIntent(selectedIntent));


        binding.radioGroupIntents.setOnCheckedChangeListener((group, checkedId) -> {
            selectedIntent = getIntentNumberByCheckedId(checkedId);

            // Сохранение выбранного значения
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(KEY_SELECTED_INTENT, selectedIntent);
            editor.apply();
        });
    }

    private int getCheckedIdForIntent(int selectedIntent) {
        if (intentRadioMap.containsKey(selectedIntent)) {
            Integer r = intentRadioMap.get(selectedIntent);
            return (r == null) ? -1 : r;
        } else {
            return R.id.radioYoutube;  // По умолчанию YoutubeIntent
        }
    }


    private int getIntentNumberByCheckedId(int checkedId) {
        for (Map.Entry<Integer, Integer> entry : intentRadioMap.entrySet()) {
            if (entry.getValue() == checkedId) {
                return entry.getKey();
            }
        }
        return 0;  // По умолчанию YoutubeIntent
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}