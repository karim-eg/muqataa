/*
 * Copyright (c) 2023-2024. Kotect Company, https://kotect.com
 * Main Programmer: Karim Abdallah
 * contact: support@kotect.com
 */

package co.encept.muqataa.ui

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.encept.muqataa.R
import co.encept.muqataa.databinding.FragmentBlacklistBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn


class BlacklistFragment : Fragment() {
    private lateinit var bind: FragmentBlacklistBinding


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentBlacklistBinding.inflate(layoutInflater)

        // Coming soon
        // Encept Ltd Company 24/10/2023

        return bind.root
    }
}



