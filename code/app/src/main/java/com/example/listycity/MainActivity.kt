package com.example.listycity

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity.ui.theme.ListyCityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val cityRepository = CityRepository()
        cityRepository.makeRepo(listOf("Edmonton", "Vancouver", "Moscow",
            "Sydney", "Berlin", "Vienna",
            "Tokyo", "Beijing", "Osaka",
            "New Delhi"))
        setContent {
            ListyCityTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    CityListScreen(
                        cities = cityRepository.cities,
                        onAddCity = { cityRepository.addCity(it) },
                        onDeleteCity = { cityRepository.deleteCity(it) },
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }
}

class CityRepository{
    private val _cities = mutableStateListOf<City>()
    // Get a read-only list for the UI to display
    val cities: List<City>
        get() = _cities

    fun addCity(city: String){
        val temp = City(city)
        _cities.add((temp))
    }

    fun deleteCity(city: String){
        _cities.remove(City(city))
    }
    fun makeRepo(cities: List<String>){
        for (city in cities){
            addCity(city)
        }
    }
}

data class City(
    private val name: String
){
    val cityName: String
        get() = name
}
@Composable
fun CityListScreen(
    // cities: List<String> is the list of city names that this screen receives from Main Activity
    cities: List<City>,
    onAddCity: (String) -> Unit,
    onDeleteCity: (String) -> Unit,
    // modifier: Modifier = Modifier allows layout information such as padding, to be passed into this screen
    modifier: Modifier = Modifier
) {
    var newCityName by remember { mutableStateOf("") }
    var selected  by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier = Modifier.padding(16.dp)){
            OutlinedTextField(
                value = newCityName,
                onValueChange = { newCityName = it},
                label = { Text("City name") },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (newCityName.isNotBlank()) {
                        onAddCity(newCityName)
                        newCityName = ""
                    }
                }
            ) {
                Text("Add City")
            }

            Button(
                onClick = {
                    if (selected.isNotBlank()) {
                        onDeleteCity(selected)
                        selected = ""
                    }
                }
            ) {
                Text("Delete City")
            }
        }

        LazyColumn(modifier = modifier.fillMaxSize()){
            // items(cities) loops through the city list and creates one UI row for each city
            items(cities) {city ->
                CityRow(city = city,
                    selected,
                    onCityClick = { clickedCity ->
                        selected = if (selected == clickedCity) "" else clickedCity
                    })
            }
        }
    }

}

@Composable
fun CityRow(
    city: City,
    selectedCity: String,
    onCityClick: (String) -> Unit
) {
    val isSelected = selectedCity == city.cityName
        Text (
            text = city.cityName,
            fontSize = 28.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 14.dp)
                .background(if (isSelected) Color.LightGray else Color.Transparent)
                .clickable{
                    onCityClick(city.cityName)
                }
        )
}
