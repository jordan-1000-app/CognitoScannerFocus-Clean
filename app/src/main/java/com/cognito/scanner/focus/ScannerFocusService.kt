package com.cognito.scanner.focus

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo

class ScannerFocusService : AccessibilityService() {
    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return
        if (event.packageName != "com.cognitoiq.ciqmobile.byod.hermes") return
        
        focusOnBarcodeField()
    }

    private fun focusOnBarcodeField() {
        val rootNode = rootInActiveWindow ?: return
        findAndFocusBarcodeField(rootNode)
    }

    private fun findAndFocusBarcodeField(node: AccessibilityNodeInfo) {
        // Cerca EditText con hint "Scan or enter a barcode"
        if (node.className?.contains("EditText") == true) {
            val hint = node.hintText?.toString() ?: ""
            if (hint.contains("Scan") || hint.contains("barcode")) {
                node.performAction(AccessibilityNodeInfo.ACTION_FOCUS)
                return
            }
        }
        
        for (i in 0 until node.childCount) {
            node.getChild(i)?.let { findAndFocusBarcodeField(it) }
        }
    }

    override fun onInterrupt() {}
}
