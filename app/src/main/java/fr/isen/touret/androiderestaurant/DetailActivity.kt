package fr.isen.touret.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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


var price = 0.0
class DetailActivity : ComponentActivity() {
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


@Composable
fun DetailView(plat: Plat) {
    var quantity by remember { mutableStateOf(1) }
    val text = plat.prices.firstOrNull()?.price ?: ""
    val priceFloat = text.toFloatOrNull() ?: 0f

    val newPrice = priceFloat * quantity



    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AsyncImage(
                model = plat.images.firstOrNull() ?: "",
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
                    .padding(8.dp)
                    .clip(RoundedCornerShape(10.dp)),
                placeholder = painterResource(id = R.drawable.aba),
                error = painterResource(id = R.drawable.aba),
            )

            Text(
                text = plat.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Row {
                plat.ingredients.forEach { ingredient ->
                    Text(
                        text = ingredient.name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = ", ",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 16.dp)
            ) {

                Button(
                    onClick = { if (quantity > 1) quantity-- },
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "-",
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
                        text = "+",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = colorResource(id = R.color.white)
                    )
                }
            }
            Row {
                Text(
                    text = newPrice.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 1.dp)
                )
                Text(
                    text = "€",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

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

