package com.example.newsapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.newsapp.components.BottomMenu
import com.example.newsapp.data.models.BottomMenuScreen
import com.example.newsapp.data.models.repository.TopNewsArticle
import com.example.newsapp.network.models.NewsManager
import com.example.newsapp.ui.screen.DetailScreen
import com.example.newsapp.ui.screen.Sources
import com.example.newsapp.ui.screen.TopNews

@Composable
fun NewsApp(){
    val navController = rememberNavController()
    val scrollState = rememberScrollState()

    MainScreen(navController, scrollState)
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navHostController: NavHostController, scrollState: ScrollState){
    Scaffold(bottomBar = { BottomMenu(navController = navHostController)}) {
        Navigation(navHostController, it)
    }
}

@Composable
fun Navigation(navHostController: NavHostController, paddingValues: PaddingValues){
    val newsManager = remember {
        NewsManager()
    }

    val articles = mutableListOf<TopNewsArticle>()
    articles.addAll(newsManager.getArticlesByCategory.value.articles ?: listOf())

    NavHost(navController = navHostController, startDestination = BottomMenuScreen.TopNews.route){
        bottomNavigation(
            navController = navHostController,
            paddingValues = paddingValues,
            articles = articles,
            newsManager
        )
        composable(
            "Detail/{index}",
            arguments = listOf(navArgument("index") { type = NavType.IntType })
        ){
            val index = it.arguments?.getInt("index")
            index?.let {
                if(newsManager.query.value != ""){
                    articles.clear()
                    articles.addAll(newsManager.searchNewsResponse.value.articles ?: listOf())
                } else{
                    articles.clear()
                    articles.addAll(newsManager.getArticlesByCategory.value.articles ?: listOf())
                }
                val article = articles[index]

                DetailScreen(navController = navHostController, article, paddingValues)
            }
        }
    }
}

fun NavGraphBuilder.bottomNavigation(
    navController: NavController,
    paddingValues: PaddingValues,
    articles: List<TopNewsArticle>,
    newsManager: NewsManager
){
    composable(BottomMenuScreen.TopNews.route){
        TopNews(
            navController = navController,
            paddingValues = paddingValues,
            articles = articles,
            newsManager = newsManager,
            query = newsManager.query
        )
    }

    composable(BottomMenuScreen.Sources.route){
        Sources(newsManager, paddingValues)
    }
}