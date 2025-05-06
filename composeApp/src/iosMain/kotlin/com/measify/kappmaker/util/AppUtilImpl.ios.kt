package com.measify.kappmaker.util

import platform.Foundation.NSBundle
import platform.Foundation.NSURL
import platform.MessageUI.MFMailComposeViewController
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

class AppUtilImpl : AppUtil {
    override fun shareApp() {
        val shareMessage = "\n\n"
        val items = listOf("$shareMessage ${getAppStoreLink()}")
        val activityViewController = UIActivityViewController(items, null)
        val uiController = UIApplication.sharedApplication.keyWindow?.rootViewController
        uiController?.let { it.presentViewController(activityViewController, true, {}) }
    }


    override fun openFeedbackMail() {

        try {
            val appName = NSBundle.mainBundle.objectForInfoDictionaryKey("CFBundleName") as? String
                ?: "App Name"
            val mailComposeViewController = MFMailComposeViewController()
            mailComposeViewController.setSubject("$appName Feedback/Bug Report")
            mailComposeViewController.setToRecipients(listOf(Constants.CONTACT_EMAIL))
            val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
            rootViewController?.presentViewController(mailComposeViewController, true, {})
        } catch (e: Exception) {
            val mailUrl = NSURL(string = "mailto:${Constants.CONTACT_EMAIL}")
            UIApplication.sharedApplication.openURL(mailUrl)
        }

    }

    private fun getAppStoreLink(): String {
        return "https://apps.apple.com/app/id${Constants.APPSTORE_APP_ID}"
    }

}