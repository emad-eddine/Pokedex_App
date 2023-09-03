package com.kichou.imad.pokdexapp.pokemonList

import androidx.compose.animation.fadeIn
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter

import com.kichou.imad.pokdexapp.R
import com.kichou.imad.pokdexapp.data.models.PokedexListEntry
import com.kichou.imad.pokdexapp.ui.theme.Roboto
import com.kichou.imad.pokdexapp.ui.theme.RobotoCondensed
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun PokemonListScreen(

    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()){

    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()){

        Column (
            modifier = Modifier.fillMaxWidth()
        ){


            Spacer(modifier = Modifier.height(20.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_international_pok_mon_logo),
                contentDescription ="pokemon_logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .align(CenterHorizontally))

            SearchBar(
                hint = "Search",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                viewModel.searchPokemonList(it)
            }


            Spacer(modifier = Modifier.height(20.dp))

            PokemonList(navController = navController)



        }



    }



}


@Composable
fun SearchBar(

    modifier: Modifier = Modifier,
    hint: String ="",
    onSearch : (String) -> Unit = {}){


    var text by remember {
        mutableStateOf("")
    }

    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier){

        BasicTextField(
            value = text,
            onValueChange = {
            text = it
            onSearch(it) },
            maxLines = 1,
            singleLine = true,
            textStyle = TextStyle(
                color = Color.Black
            ),
            modifier = Modifier
                .fillMaxWidth()
                .shadow(5.dp, shape = CircleShape)
                .background(color = Color.White, shape = CircleShape)
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .onFocusChanged {
                    isHintDisplayed = !it.isFocused
                }
        )

        if(isHintDisplayed){
            Text(text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp))
        }

    }





}



@Composable
fun PokemonList(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()) {


    val pokemonList by remember {
        viewModel.pokemonList
    }

    val endReached by remember {
        viewModel.endReached
    }

    val isLoading by remember {
        viewModel.isLoading
    }

    val loadError by remember {
        viewModel.loadError
    }

    var isSearching by remember {
        viewModel.isSearching
    }

    LazyColumn(contentPadding = PaddingValues( 16.dp)){

        val itemCount = if(pokemonList.size %2 == 0){
            pokemonList.size / 2
        }else{
            pokemonList.size / 2 +1
        }

        items(itemCount){

            if(it>= itemCount - 1 && !endReached &&!isLoading && !isSearching){
                viewModel.loadPokemonPagination()
            }

            PokemonRow(rowIndex = it,
                entries = pokemonList,
                navController = navController)
        }



    }

    Box(modifier = Modifier.fillMaxSize(),
        contentAlignment = Center){
            if(isLoading){
                CircularProgressIndicator(color = Color.LightGray)
            }
            if(loadError.isNotEmpty()){
                ShowError(errorMsg = loadError) {
                    viewModel.loadPokemonPagination()
                }
            }
    }

}




@Composable
fun PokedexEntry(
    entry: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: PokemonListViewModel = hiltViewModel()){


    val defaultColor = Color.White

    var dominantColor by remember {
        mutableStateOf(defaultColor)
    }

    var isProgressBarVisible by remember {
        mutableStateOf(false)
    }


    Box (
        contentAlignment = Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColor,
                        defaultColor
                    )
                )
            )
            .clickable {

                navController.navigate(
                    "pokemon_detail_screen/${dominantColor.toArgb()}/${entry.pokemonName}"
                )
            }){


        Column {


            AsyncImage(
                model = entry.pokemonImgUrl,
                contentDescription =  entry.pokemonName,
                onLoading = {
                    isProgressBarVisible = true
                },
                onSuccess = { success ->
                    isProgressBarVisible = false
                    val drawable = success.result.drawable
                    viewModel.calcDominantColor(drawable){color->
                        dominantColor = color
                    }

                },
                modifier = Modifier
                    .size(120.dp)
                    .align(CenterHorizontally)

            )




            Text(text = entry.pokemonName,
                fontFamily = RobotoCondensed,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth())


        }

        if(isProgressBarVisible){
            CircularProgressIndicator(
                modifier = Modifier
                    .size(80.dp)
                    .align(Center),
                color = Color.LightGray
            )
        }

    }


}


@Composable
fun PokemonRow(
    rowIndex : Int,
    entries : List<PokedexListEntry>,
    navController: NavController){

    Column {
        Row {
            PokedexEntry(entry = entries[rowIndex *2 ],
                navController = navController,
                modifier = Modifier
                    .weight(1f))

            Spacer(modifier = Modifier.width(15.dp))

            if(entries.size >= rowIndex *2 +2){
                PokedexEntry(entry = entries[rowIndex *2 +1 ],
                    navController = navController,
                    modifier = Modifier
                        .weight(1f))
            }
            else{
                Spacer(modifier = Modifier.weight(1f))
            }

        }

        Spacer(modifier = Modifier.height(16.dp))
    }


}


@Composable
fun ShowError(
    errorMsg : String,
    onError : () -> Unit){

    Column {
        Text(text = errorMsg,
            style = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Red
            )
        )
        Spacer(modifier = Modifier.height(6.dp))

        Button(onClick = {onError()}) {
            Text(text = "Reload")
        }
    }

}