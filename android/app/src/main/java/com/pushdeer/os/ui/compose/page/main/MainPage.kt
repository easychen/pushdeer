package com.pushdeer.os.ui.compose.page.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pushdeer.os.R
import com.pushdeer.os.holder.RequestHolder
import com.pushdeer.os.ui.navigation.Page
import com.pushdeer.os.ui.navigation.pageList
import com.pushdeer.os.ui.theme.mainBottomBtn
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun MainPage(requestHolder: RequestHolder) {
    LaunchedEffect(Unit) {
        requestHolder.coroutineScope.launch {
            requestHolder.pushDeerViewModel.userInfo()
        }
    }

    var titleStringId by remember {
        mutableStateOf(Page.Messages.labelStringId)
    }
    val navController = rememberNavController()
    Scaffold(
        scaffoldState = rememberScaffoldState(),
        bottomBar = {
            BottomNavigation(backgroundColor = MaterialTheme.colors.surface) {
                val navBackStackEntry by requestHolder.globalNavController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                pageList.forEach { page ->
                    val selected = page.labelStringId == titleStringId
                    BottomNavigationItem(
                        icon = {
                            Icon(
                                painter = painterResource(id = page.id),
                                contentDescription = stringResource(id = titleStringId),
                                modifier = Modifier.size(23.dp),
                                tint = MaterialTheme.colors.mainBottomBtn(selected = selected)
                            )
                        },
                        label = {
                            Text(
                                stringResource(id = page.labelStringId),
                                color = MaterialTheme.colors.mainBottomBtn(selected = selected)
                            )
                        },
                        selected = currentDestination?.hierarchy?.any { it.route == page.route } == true,
                        onClick = {
                            navController.navigate(page.route) {
                                titleStringId = page.labelStringId
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        },
        snackbarHost = { },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(id = R.drawable.ic_logo_svg_1),
                    contentDescription = "",
                    alpha = 0.05F, colorFilter = ColorFilter.tint(color = Color.Gray),
                    modifier = Modifier
                        .align(alignment = Alignment.BottomCenter)
                        .size(500.dp)
                        .offset(x = (-180).dp, y = 140.dp),
                )
                NavHost(
                    navController = navController,
                    startDestination = Page.Messages.route,
                ) {
                    composable(Page.Devices.route) {
                        DeviceListPage(requestHolder = requestHolder)
                    }
                    composable(Page.Keys.route) {
                        KeyListPage(requestHolder = requestHolder)
                    }
                    composable(Page.Messages.route) {
                        MessageListPage(requestHolder = requestHolder)
                    }
                    composable(Page.Settings.route) {
                        SettingPage(requestHolder = requestHolder)
                    }
                }
            }
        },
    )
}