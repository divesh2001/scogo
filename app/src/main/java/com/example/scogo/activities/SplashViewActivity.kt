package com.example.scogo.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import com.example.scogo.R
import com.example.scogo.ui.theme.ScogoTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.delay

class SplashViewActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ScogoTheme {
                val context = LocalContext.current
                SplashScreen()
            }
        }
    }

    @Composable
    private fun SplashScreen() {

        val context = LocalContext.current

        LaunchedEffect(Unit) {
            delay(2000)
            val intent = Intent(context, CurrencyActivity::class.java)
            context.startActivity(intent)
            finish()
        }

        SplashIcon()
    }

    @Composable
    private fun SplashIcon() {

        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(Color.White, darkIcons = true)

        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(1000)),
            exit = fadeOut(tween(0)),
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(id = R.color.colorWhite))
            ) {
                Image(
                    painterResource(id = R.drawable.icon_scogo),
                    contentDescription = "splashIcon",
                    Modifier.align(Alignment.Center)
                )
            }
        }

    }
}



