package com.example.newsapp.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.newsapp.models.getAllCategories
import com.example.newsapp.network.models.NewsManager

@Composable
fun CategoriesTab(
    onFetch: (String) -> Unit = {},
    newsManager: NewsManager
){
    val categories = getAllCategories()

    Column(
        modifier = Modifier
            .padding(10.dp)
    ){
        LazyRow{
            items(categories.size){
                val category = categories[it]

                CategoryItem(
                    category = category.categoryName,
                    onFetch = onFetch,
                    isSelected = newsManager.selectedCategory.value == category
                )
            }
        }
    }
}

@Composable
fun CategoryItem(
    category: String,
    isSelected: Boolean = false,
    onFetch: (String) -> Unit
){
    val backgroundColor =
        if(isSelected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.primaryContainer
    val textColor =
        if(isSelected) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onPrimaryContainer

    Surface(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .clickable {
                onFetch(category)
            },
        shape = MaterialTheme.shapes.small,
        color = backgroundColor
    ) {
        Text(
            text = category,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CategoryTabPreview(){
    CategoryItem(category = "Sport", onFetch = {}, isSelected = true)
}