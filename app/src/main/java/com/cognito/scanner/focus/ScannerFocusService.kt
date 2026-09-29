package com.cognito.scanner.focus

import android.accessibilityservice.AccessibilityService
import android.os.Handler
import android.os.Looper
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScannerFocusService : AccessibilityService() {
    private val handler = Handler(Looper.getMainLooper())
    private var focusRunnable: Runnable? = null

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName == "com.cognitoiq.ciqmobile.byod.hermes") {
            if (focusRunnable == null) startAutoFocus()
        }
    }

    private fun startAutoFocus() {
        focusRunnable = object : Runnable {
            override fun run() {
                focusOnBarcodeField()
                handler.postDelayed(this, 300)
            }
        }
        handler.post(focusRunnable!!)
    }

    private fun focusOnBarcodeField() {
        val rootNode = rootInActiveWindow ?: return
        findAndFocusEditText(rootNode)
    }

    private fun findAndFocusEditText(node: AccessibilityNodeInfo) {
        if (node.className?.contains("EditText") == true) {
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
        }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { findAndFocusEditText(it) }
        }
    }

    override fun onInterrupt() {
        focusRunnable?.let { handler.removeCallbacks(it) }
    }
}
