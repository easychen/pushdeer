package com.pushdeer.os.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorPalette = darkColors(
    primary = Purple200,
    primaryVariant = Purple700,
    secondary = Teal200
)

private val LightColorPalette = lightColors(
    primary = Purple500,
    primaryVariant = Purple700,
    secondary = Teal200

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

val Colors.MBlue: Color
    @Composable get() = if (isLight) MainBlue else MainBlue

val Colors.MBottomBtn: Color
    @Composable get() = MainBottomBtn

val Colors.MBottomBarBgc: Color
    @Composable get() = if (isLight) Color.White else Color.White

//val Colors.thingNormal: Color
//    @Composable get() = if (isLight) Green400 else Green700
//
//val Colors.thingLost: Color
//    @Composable get() = if (isLight) Red400 else Red700
//
//val Colors.thingEnd: Color
//    @Composable get() = if (isLight) Color.LightGray else Color.DarkGray

@Composable
fun Colors.mainBottomBtn(selected: Boolean): Color {
    return if (selected) MBlue else MBottomBtn
}

@Composable
fun PushDeerTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}