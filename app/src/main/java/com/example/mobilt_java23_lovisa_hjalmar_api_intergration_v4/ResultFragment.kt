package com.example.mobilt_java23_lovisa_hjalmar_api_intergration_v4

import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.mobilt_java23_lovisa_hjalmar_api_intergration_v4.databinding.FragmentResultBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.URL

class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!
    private val apiUrl = "https://www.thecolorapi.com/id?hex="

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val colorCode = ResultFragmentArgs.fromBundle(requireArguments()).colorCode
        fetchColorData(colorCode)
    }

    private fun fetchColorData(colorCode: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = URL("$apiUrl$colorCode").readText()
                val jsonObject = JSONObject(response)
                val name = jsonObject.getJSONObject("name").getString("value")
                val hex = jsonObject.getString("hex")
                val contrast = jsonObject.getJSONObject("contrast").getDouble("value")

                withContext(Dispatchers.Main) {
                    binding.tvColorName.text = "Name: $name"
                    binding.tvHexCode.text = "HEX: $hex"
                    if (contrast < 4.5) {
                        sendLowContrastNotification(name)
                    }
                }
            } catch (e: Exception) {
                Log.e("ResultFragment", "Error fetching color data", e)
            }
        }
    }

    private fun sendLowContrastNotification(colorName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "lowContrast",
                "Low Contrast Alert",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = requireContext().getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(requireContext(), "lowContrast")
            .setContentTitle("Low Contrast Warning")
            .setContentText("The color $colorName has low contrast!")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        NotificationManagerCompat.from(requireContext()).notify(1, notification)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
