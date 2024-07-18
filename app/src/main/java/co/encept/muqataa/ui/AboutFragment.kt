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
import co.encept.muqataa.databinding.FragmentAboutBinding
import com.google.android.gms.ads.AdRequest


class AboutFragment : Fragment() {
    private lateinit var bind: FragmentAboutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        bind = FragmentAboutBinding.inflate(layoutInflater)

        bind.apply {
            ad.loadAd(AdRequest.Builder().build())


            imgGithub.setOnClickListener {
                openBrowser("https://github.com/kimoandroid", requireContext())
            }

            imgFb.setOnClickListener {
                openBrowser("https://www.facebook.com/karim.abdallah.dev", requireContext())
            }

            imgLinkedin.setOnClickListener {
                openBrowser("https://www.linkedin.com/in/karim-abdallah-dev", requireContext())
            }

            imgWeb.setOnClickListener {
                openBrowser("https://encept.co", requireContext())
            }

            imgEmail.setOnClickListener {
                val intent = Intent(Intent.ACTION_SENDTO)
                intent.data = Uri.parse("mailto:karim@encept.co")
                intent.putExtra(Intent.EXTRA_SUBJECT, "Contact Muqataa Developer")
                startActivity(Intent.createChooser(intent, "Send Email"))
            }

            btnPrivacy.setOnClickListener {
                openBrowser("https://github.com/kimoandroid/muqataa/blob/main/privacy-policy.md", requireContext())
            }

            btnMoreApps.setOnClickListener {
                openBrowser("https://play.google.com/store/apps/dev?id=6033125290854254520", requireContext())
            }
        }

        return bind.root
    }

    private fun openBrowser(url: String, c: Context) {
        CustomTabsIntent.Builder().build().launchUrl(c, Uri.parse(url))
    }
}