package com.example.papka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.example.papka.ui.navigation.AppNavigation
import com.example.papka.ui.theme.PapkaTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PapkaTheme {
                Surface {
                    AppNavigation()
                }
            }
        }
    }
}
