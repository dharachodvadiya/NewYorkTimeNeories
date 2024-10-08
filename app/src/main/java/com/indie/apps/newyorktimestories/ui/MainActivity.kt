package com.indie.apps.newyorktimestories.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.indie.apps.newyorktimestories.ui.story_detail.StoryDetailScreen
import com.indie.apps.newyorktimestories.ui.story_list.StoryListScreen
import com.indie.apps.newyorktimestories.ui.story_list.StoryListViewModel
import com.indie.apps.newyorktimestories.ui.theme.NewYorkTimeStoriesTheme
import com.indie.apps.newyorktimestories.ui.web_view.WebViewScreen
import com.indie.apps.newyorktimestories.util.Constant

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NewYorkTimeStoriesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.StoryListScreen.route,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(
                            route = Screen.StoryListScreen.route
                        ) {
                            StoryListScreen(
                                onItemClick = {
                                    navController.navigate(Screen.StoryDetailScreen.route + "/${it}")
                                }
                            )
                        }
                        composable(
                            route = Screen.StoryDetailScreen.route + "/{${Constant.PARAM_ARTICLE_ID}}",
                        ) {
                            StoryDetailScreen(
                                onNavigationBack = {
                                    navController.navigateUp()
                                },
                                onSeeMoreClick = {
                                    navController.navigate(Screen.WebViewScreen.route + "/${it}")
                                }
                            )
                        }
                        composable(
                            route = Screen.WebViewScreen.route + "/{${Constant.PARAM_ARTICLE_ID}}",
                        ) {
                           WebViewScreen()
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NewYorkTimeStoriesTheme {
        Greeting("Android")
    }
}