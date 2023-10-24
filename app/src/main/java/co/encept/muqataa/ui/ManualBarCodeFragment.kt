/*
 * Copyright (c) 2023-2023. Encept Ltd Company, https://encept.co
 * contact: support@encept.co
 */

package co.encept.muqataa.ui

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import co.encept.muqataa.HandleBarCode
import co.encept.muqataa.R
import co.encept.muqataa.databinding.FragmentManualBarCodeBinding
import co.encept.muqataa.ui.models.ValidateBarcodeModel
import com.google.android.gms.ads.AdRequest


class ManualBarCodeFragment : Fragment() {
    private lateinit var bind: FragmentManualBarCodeBinding

    private val validateModel: ValidateBarcodeModel by viewModels()

    private lateinit var vb: Vibrator


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentManualBarCodeBinding.inflate(layoutInflater)

        bind.ad.loadAd(AdRequest.Builder().build())
        val handleBarCode = HandleBarCode()

        vb = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vm = activity?.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vm.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            activity?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        bind.apply {
            edtManualCode.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(char: CharSequence, start: Int, before: Int, count: Int) {
                    if (char.isNotEmpty() || char.toString().trim() != "") {
                        activity?.runOnUiThread {
                            if (!txtResult.isVisible || !cardResult.isVisible) {
                                txtResult.visibility = View.VISIBLE
                                cardResult.visibility = View.VISIBLE
                            }

                            if (char.toString().trim().startsWith("729", true) || char.toString().trim().startsWith("871", true)) {
                                txtResult.text = getString(R.string.product_banned)
                                txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                txtCountry.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                vibrate(700)

                            } else if (char.toString().trim().length >= 5) {
                                val check = validateModel.checkMuqataa(char.toString().trim())

                                if (check) {
                                    txtResult.text = getString(R.string.product_banned)
                                    txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.red))
                                } else {
                                    txtResult.text = getString(R.string.product_okay)
                                    txtResult.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                                }

                                txtCountry.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

                            } else {
                                txtResult.visibility = View.GONE
                                cardResult.visibility = View.GONE
                            }


                            txtCountry.text = handleBarCode.getCountry(requireContext(), char.toString().trim())
                        }
                    }
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

                override fun afterTextChanged(s: Editable?) { }
            })
        }

        return bind.root
    }


    private fun vibrate(duration: Long) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vb.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            @Suppress("DEPRECATION")
            vb.vibrate(duration)
        }
    }
}