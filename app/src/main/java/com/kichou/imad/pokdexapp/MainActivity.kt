package com.kichou.imad.pokdexapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.kichou.imad.pokdexapp.pokemonDetailScreen.PokemonDetailScreen
import com.kichou.imad.pokdexapp.pokemonList.PokemonListScreen
import com.kichou.imad.pokdexapp.ui.theme.PokedexAppTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokedexAppTheme{

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "pokemon_list_screen"){

                    composable(route = "pokemon_list_screen"){
                        PokemonListScreen(navController)
                    }

                    composable(route = "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                        arguments = listOf(navArgument(
                            name = "dominantColor"){
                            type = NavType.IntType
                        },
                            navArgument("pokemonName"){
                                type = NavType.StringType
                            }
                        )){

                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White

                        }

                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }

                        PokemonDetailScreen(dominantColor = dominantColor,
                            pokemonName = pokemonName?.toLowerCase(Locale.ROOT) ?: "",
                            navController = navController)

                    }


                }


            }
        }
    }
}
