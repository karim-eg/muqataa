/*
 * Copyright (c) 2023-2024. Kotect Company, https://kotect.com
 * Main Programmer: Karim Abdallah
 * contact: support@kotect.com
 */
package co.encept.muqataa.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import co.encept.muqataa.Consts
import co.encept.muqataa.R
import co.encept.muqataa.databinding.FragmentAboutBinding
import com.google.android.gms.ads.AdRequest


class AboutFragment : Fragment() {
    private lateinit var bind: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentAboutBinding.inflate(layoutInflater)

        bind.apply {
            ad.loadAd(AdRequest.Builder().build())


            imgKotWeb.setOnClickListener {
                openBrowser(Consts.KOTECT_LINK, requireContext())
            }

            imgKotGithub.setOnClickListener {
                openBrowser(Consts.KOTECT_GITHUB, requireContext())
            }

            imgKotFb.setOnClickListener {
                openBrowser(Consts.KOTECT_FACEBOOK, requireContext())
            }

            imgKotLinkedin.setOnClickListener {
                openBrowser(Consts.KOTECT_LINKEDIN, requireContext())
            }


            imgKotEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:${Consts.KOTECT_EMAIL}")
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_kotect_company))
                startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
            }


            imgGithub.setOnClickListener {
                openBrowser(Consts.KARIM_GITHUB, requireContext())
            }

            imgFb.setOnClickListener {
                openBrowser(Consts.KARIM_FACEBOOK, requireContext())
            }

            imgLinkedin.setOnClickListener {
                openBrowser(Consts.KARIM_LINKEDIN, requireContext())
            }

            imgEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:${Consts.KARIM_EMAIL}")
                intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_muqataa_developer))
                startActivity(Intent.createChooser(intent, getString(R.string.send_email)))
            }


            btnPrivacy.setOnClickListener {
                openBrowser(Consts.PRIVACY_POLICY, requireContext())
            }

            btnMoreApps.setOnClickListener {
                openBrowser(Consts.KOTECT_GOOGLE_PLAY, requireContext())
            }
        }

        return bind.root
    }

    private fun openBrowser(url: String, c: Context) {
        CustomTabsIntent.Builder().build().launchUrl(c, Uri.parse(url))
    }
}