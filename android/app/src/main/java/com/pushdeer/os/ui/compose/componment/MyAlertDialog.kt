package com.pushdeer.os.ui.compose.componment

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.pushdeer.os.holder.RequestHolder

@Composable
fun MyAlertDialog(alertRequest: RequestHolder.AlertRequest) {
    if (alertRequest.show.value) {
        AlertDialog(
            onDismissRequest = { alertRequest.show.value = false },
            confirmButton = {
                TextButton(onClick = {
                    alertRequest.onOKAction.invoke()
                    alertRequest.show.value = false
                }) {
                    Text(text = "Ok")
                }

            },
            dismissButton = {
                TextButton(onClick = {
                    alertRequest.onCancelAction.invoke()
                    alertRequest.show.value = false
                }) {
                    Text(text = "Cancel")
                }

            },
            title = { Text(text = alertRequest.title) },
            text = alertRequest.content
        )
    }
}