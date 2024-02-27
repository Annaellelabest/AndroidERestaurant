package fr.isen.touret.androiderestaurant


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import fr.isen.amiot.androiderestaurant.network.NetworkConstants
import fr.isen.touret.androiderestaurant.ui.theme.AndroidERestaurantTheme
import org.json.JSONObject
import com.android.volley.Request
import com.google.gson.GsonBuilder
import fr.isen.touret.androiderestaurant.basket.Basket
import fr.isen.touret.androiderestaurant.basket.BasketActivity
import fr.isen.touret.androiderestaurant.network.Category
import fr.isen.touret.androiderestaurant.network.MenuResults
import fr.isen.touret.androiderestaurant.network.Plat

private val selectedItems = mutableListOf<Plat>()

class MenuActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = (intent.getSerializableExtra(CATEGORY_EXTRA_KEY)as? DishType) ?: DishType.STARTER
        val title = intent.getStringExtra("activity_title")
        Log.d("StarterActivity", "Title received: $title")

        setContent {
            AndroidERestaurantTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2(title ?: "")
                    MenuView(type, title ?: "", selectedItems)
                }
            }
        }
    }

    override fun onDestroy() {
        Log.d("on destroyed called", "life cycle Starter destroyed ")
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        Log.d("on resume called", "life cycle resumed ")
    }

    companion object {
        val CATEGORY_EXTRA_KEY = "CATEGORY_EXTRA_KEY"
    }



}

@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    AndroidERestaurantTheme {
        Greeting2("Android")
    }
}

@Composable
fun MenuView(type: DishType, title: String, basketItems: List<Plat>) {

    val category = remember {
        mutableStateOf<Category?>(null)
    }
    val context = LocalContext.current


    val selectedDish = remember { mutableStateOf<Plat?>(null) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally

    ) {

        Text(
            title,
            fontSize = 40.sp,
            color = colorResource(id = R.color.colorAba),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(end = 16.dp, top = 16.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            val basket = Basket.current(context)
            val totalItemCount = basket.getTotalItemCount()
            Log.d("2BasketItems", "2Total number of items in the basket: $totalItemCount")

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
        Box(modifier = Modifier.padding(bottom = 15.dp)) {
            LazyColumn {
                category.value?.items?.forEach { plat ->
                    item {
                        Surface(
                            onClick = {
                                val intent = Intent(context, DetailActivity::class.java)
                                intent.putExtra(DetailActivity.CATEGORY_EXTRA_PLAT, plat)
                                context.startActivity(intent)
                            },
                            modifier = Modifier
                                .padding(top = 15.dp)
                                .height(150.dp)
                                .fillMaxWidth(0.9f),
                            shape = RoundedCornerShape(10.dp),
                            color = colorResource(id = R.color.button)
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(
                                            text = plat.name,
                                            color = colorResource(id = R.color.colorAba),
                                            modifier = Modifier
                                                .padding(4.dp)
                                                .offset(x = 8.dp),
                                            style = TextStyle(
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                fontFamily = FontFamily.Serif
                                            ),
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = plat.prices.firstOrNull()?.price ?: "",
                                                color = colorResource(id = R.color.colorAba),
                                                fontSize = 20.sp,
                                                modifier = Modifier.offset(x = 12.dp),
                                            )
                                            Text(
                                                text = "€",
                                                color = colorResource(id = R.color.colorAba),
                                                fontSize = 20.sp,
                                                modifier = Modifier.offset(x = 12.dp),
                                            )
                                        }
                                    }
                                    val imageUrl = remember {
                                        if (plat.images.isNotEmpty()) {
                                            plat.images[0]
                                        } else {
                                            null
                                        }
                                    }
                                    AsyncImage(
                                        model = imageUrl ?: plat.images.getOrNull(1) ?: "",
                                        contentDescription = null,
                                        modifier = Modifier
                                            .size(150.dp)
                                            .padding(8.dp)
                                            .clip(RoundedCornerShape(10.dp)),
                                        placeholder = painterResource(id = R.drawable.aba),
                                        error = painterResource(id = R.drawable.aba),
                                    )
                                }
                            }
                        }

                    }
                }
            }
        }
        postData(type, type.title(), category)
    }


}


@Composable
fun postData( type: DishType, title: String, category: MutableState<Category?>) {
    val currentCategory = title
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(NetworkConstants.ID_SHOP, "1")

    val request = JsonObjectRequest(Request.Method.POST,
        NetworkConstants.URL,
        params,
        {
            Log.d("request", it.toString(2))
            val result = GsonBuilder().create().fromJson(it.toString(), MenuResults::class.java)

            Log.d("parse", "")
            result.data.first{
                it.name == currentCategory
            }

            val filteredResults = result.data.first { category -> category.name == currentCategory }
            category.value = filteredResults
            Log.d("result","")
        },
        {
            Log.e("request", it.toString())
        })
    queue.add(request)
}





