package com.pushdeer.os.ui.compose.componment

import androidx.compose.material.AlertDialog
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pushdeer.os.R
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
                    Text(text = stringResource(id = R.string.global_alert_ok))
                }

            },
            dismissButton = {
                TextButton(onClick = {
                    alertRequest.onCancelAction.invoke()
                    alertRequest.show.value = false
                }) {
                    Text(text = stringResource(id = R.string.global_alert_cancel))
                }

            },
            title = { Text(text = alertRequest.title) },
            text = alertRequest.content
        )
    }
}