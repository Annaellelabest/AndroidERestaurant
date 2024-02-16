package fr.isen.touret.androiderestaurant


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fr.isen.touret.androiderestaurant.basket.BasketActivity
import fr.isen.touret.androiderestaurant.ui.theme.AndroidERestaurantTheme
import kotlin.math.log

enum class DishType {
    STARTER,
    DISH,
    DESSERT;

    fun title(): String {
        return when(this) {
            STARTER -> "Entrées"
            DISH -> "Plats"
            DESSERT -> "Desserts"
        }
    }
}

interface MenuInteface {
    fun dishPressed(dish: DishType)
}
class MainActivity : ComponentActivity(), MenuInteface{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(this)
                }
            }
        }
    }



    override fun dishPressed(dish: DishType) {
        val titleResourceId: Int = when(dish) {
            DishType.STARTER -> R.string.starter
            DishType.DISH -> R.string.dish
            DishType.DESSERT -> R.string.dessert
        }
        val title: String = getString(titleResourceId)

        val intent = when(dish) {
            DishType.STARTER -> Intent(this, MenuActivity::class.java)
            DishType.DISH -> Intent(this, MenuActivity::class.java)
            DishType.DESSERT -> Intent(this, MenuActivity::class.java)
        }
        intent.putExtra(MenuActivity.CATEGORY_EXTRA_KEY,dish)
        intent.putExtra("activity_title", title)

        Toast.makeText(this, "You click on $title", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }


    override fun onPause() {
        super.onPause()
        Log.d("on pause called", "life cycle paused ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("on resume called", "life cycle resumed ")
    }


}

@Composable
fun Greeting(menu : MenuInteface) {
val context= LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp, top = 16.dp)

    ) {
        Button(
            onClick = {
                val intent = Intent(context, BasketActivity::class.java)
                context.startActivity(intent)
            },
        ) {
            Text("Voir mon panier")
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Image(
           painterResource(R.drawable.aba),
            contentDescription = "picture sushi",
        )

        ElevatedButton(onClick = {
            menu.dishPressed(DishType.STARTER)
        },
            modifier = Modifier
                .padding(top = 8.dp)
                .height(100.dp)
                .fillMaxWidth(2f),

        ) {
            Text(
                text = stringResource(id = R.string.starter),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(25.dp)
                    .offset(x = 40.dp),
                color = colorResource(id = R.color.colorAba)
            )
            Image(
                painter = painterResource(R.drawable.starter),
                contentDescription = "picturte starter",
                modifier = Modifier
                    .size(300.dp)

            )
        }

        ElevatedButton(onClick = { menu.dishPressed(DishType.DISH) },
                modifier = Modifier
                    .padding(top = 8.dp)
                    .height(100.dp)
                    .fillMaxWidth(2f)
        ) {
            Text(stringResource(id = R.string.dish),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(25.dp)
                    .offset(x = 40.dp),
                color = colorResource(id = R.color.colorAba)
            )
            Image(
                painter = painterResource(R.drawable.dish),
                contentDescription = "picturte dish",
                modifier = Modifier
                    .size(300.dp)
                    .offset(x = 14.dp)

            )

        }
        ElevatedButton(onClick = { menu.dishPressed(DishType.DESSERT)},
            modifier = Modifier
                .padding(top = 8.dp)
                .height(100.dp)
                .fillMaxWidth(2f)

        ) {
            Text(stringResource(id = R.string.dessert),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(25.dp)
                    .offset(x = 40.dp),
                color = colorResource(id = R.color.colorAba)

            )
            Image(
                painter = painterResource(R.drawable.dessert),
                contentDescription = "picturte dessert",
                modifier = Modifier
                    .size(300.dp)


            )
        }
    }



}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        Greeting(object : MenuInteface {
            override fun dishPressed(dish: DishType) {
                // Nothing to do
            }
        })
    }
}