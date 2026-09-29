package com.cognito.scanner.focus

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScannerFocusService : AccessibilityService() {
    private var lastBarcode = ""

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event?.packageName != "com.cognitoiq.ciqmobile.byod.hermes") return
        
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            val newText = event.text?.getOrNull(0)?.toString() ?: ""
            
            if (newText != lastBarcode && newText.isNotEmpty()) {
                lastBarcode = newText
                showKeyboard()
            }
        }
    }

    private fun showKeyboard() {
        val rootNode = rootInActiveWindow ?: return
        findAndActivateBarcodeField(rootNode)
    }

    private fun findAndActivateBarcodeField(node: AccessibilityNodeInfo) {
        if (node.className?.contains("EditText") == true) {
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            node.performAction(AccessibilityNodeInfo.ACTION_CLICK)
            return
        }
        
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { findAndActivateBarcodeField(it) }
        }
    }

    override fun onInterrupt() {}
}
