package com.walhalla.ttvloader.activity.tools

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import com.walhalla.ttvloader.R
import com.walhalla.ttvloader.databinding.FragmentNumberInputBinding
import com.walhalla.utils.ApkInstaller
import java.util.HashMap
import java.util.Map

class NumberInputFragment : Fragment() {
    private var binding: FragmentNumberInputBinding? = null
    var selectedIntent: Int = 0
    private val intentRadioMap = object : HashMap<Int, Int>() {
            init {
                put(0, R.id.radioYoutube)
                put(1, R.id.radioInstagram)
                put(2, R.id.radioOkru)
                put(3, R.id.radioTiktok)
                put(4, R.id.radioLikee)
            }
        }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNumberInputBinding.inflate(inflater, container!!, false)
        return binding!!.getRoot()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState!!)
        var sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var savedNumber: Int = sharedPreferences.getInt(NUMBER_KEY, 0)
        binding!!.numberInput.setText(savedNumber.toString())
        binding!!.okButton.setOnClickListener({ v -> 
                var numberStr: String = binding!!.numberInput.getText().toString()
                if (!numberStr.isEmpty()) {
                    var number: Int = Integer.parseInt(numberStr)
                    var editor: SharedPreferences.Editor = sharedPreferences.edit()
                    editor.putInt(NUMBER_KEY, number)
                    editor.apply()
                    (getActivity()!! as ToolsActivity)
                } else {
                    Toast.makeText(requireContext(), "Please enter a number", Toast.LENGTH_SHORT).show()
                }
                })
        binding!!.fdroid.setOnClickListener({ v -> 
                ApkInstaller.downloadAndInstallApk(getContext()!!, "https://f-droid.org/F-Droid.apk")
                })
        selectedIntent = sharedPreferences.getInt(KEY_SELECTED_INTENT, 0)
        binding!!.radioGroupIntents.check(getCheckedIdForIntent(selectedIntent))
        binding!!.radioGroupIntents.setOnCheckedChangeListener({ group, checkedId -> 
                selectedIntent = getIntentNumberByCheckedId(checkedId)
                // Сохранение выбранного значения
                var editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putInt(KEY_SELECTED_INTENT, selectedIntent)
                editor.apply()
                })
    }
    private fun getCheckedIdForIntent(selectedIntent: Int): Int {
        if (intentRadioMap.containsKey(selectedIntent)) {
            var r: Int? = intentRadioMap.`get`(selectedIntent)
            return if (r == null) 1 else r
        } else {
            return R.id.radioYoutube
        }
    }
    // По умолчанию YoutubeIntent
    private fun getIntentNumberByCheckedId(checkedId: Int): Int {
        for (entry in intentRadioMap.entries) {
            if (entry.value == checkedId) {
                return entry.key
            }
        }
        return 0
    }
    // По умолчанию YoutubeIntent
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
    companion object {
        private const val PREFS_NAME = "MyPrefs"
        private const val NUMBER_KEY = "number_key"
        private const val KEY_SELECTED_INTENT = "selectedIntent"
    }
}
