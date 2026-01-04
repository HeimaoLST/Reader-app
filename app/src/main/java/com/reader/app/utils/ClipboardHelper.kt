package com.reader.app.utils

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context
import android.util.Patterns

class ClipboardHelper(private val context: Context) {
    
    fun getClipboardUrl(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        
        if (!clipboard.hasPrimaryClip()) return null
        
        if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == true ||
            clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == true) {
            
            val item = clipboard.primaryClip?.getItemAt(0) ?: return null
            val text = item.text?.toString() ?: return null
            
            if (Patterns.WEB_URL.matcher(text).matches()) {
                return text
            }
        }
        return null
    }
}
