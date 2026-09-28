package com.cognito.scanner.focus

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScannerFocusService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName != "com.cognitoiq.ciqmobile.byod.hermes") return
        if (event.eventType == AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED) {
            focusOnBarcodeField()
        }
    }

    private fun focusOnBarcodeField() {
        val rootNode = rootInActiveWindow ?: return
        findAndFocusEditText(rootNode)
    }

    private fun findAndFocusEditText(node: AccessibilityNodeInfo) {
        if (node.className?.contains("EditText") == true) {
            node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
            return
        }
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { findAndFocusEditText(it) }
        }
    }

    override fun onInterrupt() {}
}
