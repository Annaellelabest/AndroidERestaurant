package fr.isen.touret.androiderestaurant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box

import androidx.compose.foundation.layout.Column


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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.isen.touret.androiderestaurant.network.Plat
import fr.isen.touret.androiderestaurant.ui.theme.AndroidERestaurantTheme
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import fr.isen.touret.androiderestaurant.basket.Basket
import fr.isen.touret.androiderestaurant.basket.BasketActivity


var price = 0.0
class DetailActivity : ComponentActivity() {
    override fun onDestroy() {
        Log.d("on destroyed called", "life cycle Starter destroyed ")
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Log.d("on resume called", "life cycle resumed ")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val plat = intent.getSerializableExtra(CATEGORY_EXTRA_PLAT) as? Plat

        setContent {
            var ingredient = plat?.ingredients?.map { it.name }?.joinToString(", ") ?: ""
            var price = plat?.prices?.firstOrNull()?.price ?: 0.0
        }

        setContent {
            AndroidERestaurantTheme {
                plat?.let { DetailView(it) }
            }
        }
    }

    companion object {
        val CATEGORY_EXTRA_PLAT = "CATEGORY_EXTRA_PLAT"
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun DetailView(plat: Plat) {
    var quantity by remember { mutableStateOf(1) }
    val text = plat.prices.firstOrNull()?.price ?: ""
    val priceFloat = text.toFloatOrNull() ?: 0f
    val newPrice = priceFloat * quantity
    val pagerState = rememberPagerState(pageCount = { plat.images.size })
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn(
            modifier = Modifier.padding(1.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .padding(end = 16.dp, top = 16.dp),
                    contentAlignment = Alignment.TopEnd
                ) {
                    val basket = Basket.current(context)
                    val totalItemCount = basket.getTotalItemCount()

                    if (totalItemCount > 0) {
                        Box(
                            modifier = Modifier
                                .background(Color.Red, shape = CircleShape)
                                .size(30.dp)
                                .clip(CircleShape)
                                .align(Alignment.TopEnd)

                        ) {
                            Text(
                                text = totalItemCount.toString(),
                                color = Color.White,
                                style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                    Button(
                        onClick = {
                            val intent = Intent(context, BasketActivity::class.java)
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.transparent))

                    ) {
                        Box {

                            Image(
                                painter = painterResource(id = R.drawable.iconba),
                                contentDescription = "picture basket",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier
                                    .size(40.dp),
                            )

                        }
                    }
                }
            }
            item {
                HorizontalPager(state = pagerState) { page ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        AsyncImage(
                            model = plat.images.getOrNull(page)
                                ?: "",
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(16.dp)),
                            placeholder = painterResource(id = R.drawable.aba),
                            error = painterResource(id = R.drawable.aba),
                        )
                    }
                }
            }
            item {
                Text(
                    text = plat.name,
                    modifier = Modifier
                        .padding(bottom = 16.dp),
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Serif
                    ),
                )
            }
            item {
                Row {
                    plat.ingredients.forEach { ingredient ->
                        Text(
                            text = ingredient.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,

                            )
                        Text(
                            text = ", ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,

                            )
                    }
                }
            }
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {

                    Button(
                        onClick = { if (quantity > 1) quantity-- },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id =R.string.title_activity_less),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.white),
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = quantity.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { quantity++ },
                        modifier = Modifier.padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = stringResource(id =R.string.title_activity_add),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.white)
                        )
                    }
                }
            }
            item {
                Row {
                    Text(
                        text = newPrice.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    Text(
                        text = "€",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                }
            }
            item {
                Button(
                    onClick = {
                        val toastText = "Vous avez ajouté  $quantity x ${plat.name} au panier pour  $newPrice €"
                        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()
                        Basket.current(context).add(plat, quantity, context)
                    },
                    modifier = Modifier
                        .padding(top = 30.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.submit),
                        fontSize = 20.sp,
                    )
                }
            }
        }
    }
}


@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    AndroidERestaurantTheme {
        Greeting3("Android")
    }
}
