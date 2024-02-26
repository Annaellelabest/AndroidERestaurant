package fr.isen.touret.androiderestaurant.basket

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import fr.isen.touret.androiderestaurant.MainActivity
import fr.isen.touret.androiderestaurant.R
import fr.isen.touret.androiderestaurant.ui.theme.AndroidERestaurantTheme
import java.math.BigDecimal


class BasketActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AndroidERestaurantTheme {
                BasketView()
            }
        }
    }


}

fun calculateTotalPrice(basketItems: List<BasketItem>): BigDecimal {
    var totalPrice = BigDecimal.ZERO
    for (item in basketItems) {
        val itemPrice = BigDecimal(item.plat.prices.first().price)
        totalPrice += itemPrice * BigDecimal(item.count)
    }
    return totalPrice
}




@Composable
fun BasketView() {
    val context = LocalContext.current
    val basketItems = remember {
        mutableStateListOf<BasketItem>()
    }
    LazyColumn {
        item {
            Text(
                text = "Votre panier",
                modifier = Modifier.padding(8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 40.sp,
                color = colorResource(id = R.color.colorAba)
            )
        }
        items(basketItems) { item ->
            BasketItemView(item, basketItems)
        }
        item {
            val totalPrice = calculateTotalPrice(basketItems)
            Text(
                text = "Prix total: $totalPrice €",
                modifier = Modifier.padding(8.dp),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                ),
                color = Color.Black
            ) }
        item {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = {
                        if (basketItems.isNotEmpty()) {

                            Toast.makeText(context, "Merci pour votre commande chez aba restaurat", Toast.LENGTH_SHORT).show()

                            val basket = Basket.current(context)
                            basket.items.clear()
                            basket.save(context)

                            val intent = Intent(context, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        } else {
                            Toast.makeText(context, "Votre panier est vide", Toast.LENGTH_SHORT).show()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = basketItems.isNotEmpty()
                ) {
                    Text(
                        text = "Commander",
                        color = Color.White
                    )
                }
            }
        }
    }
    basketItems.addAll(Basket.current(context).items)


}

@Composable
fun BasketItemView(item: BasketItem, basketItems: MutableList<BasketItem>) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .background(Color.White)
    ) {
        Row(Modifier.padding(8.dp)) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(item.plat.images.first())
                    .build(),
                null,
                placeholder = painterResource(R.drawable.aba),
                error = painterResource(R.drawable.aba),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .width(80.dp)
                    .height(80.dp)
                    .clip(RoundedCornerShape(10))
                    .padding(8.dp)
            )
            Column(
                Modifier
                    .align(alignment = Alignment.CenterVertically)
                    .padding(8.dp)
            ) {
                Text(
                    text = item.plat.name,
                    modifier = Modifier.widthIn(max = 200.dp),
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif
                )
                Text("${item.plat.prices.first().price} €")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(
                        onClick = {
                            if (item.count > 1) {
                                Basket.current(context).removeOne(item, context)
                                basketItems.clear()
                                basketItems.addAll(Basket.current(context).items)
                            }
                        }
                    ) {
                     Text(
                         text ="-",
                         fontSize = 35.sp,
                         modifier = Modifier.padding(top = 0.dp))
                    }
                    Text(item.count.toString(), Modifier.padding(horizontal = 8.dp))
                    IconButton(
                        onClick = {
                            Basket.current(context).addOne(item, context)
                            basketItems.clear()
                            basketItems.addAll(Basket.current(context).items)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add")
                    }
                }
            }

            Spacer(Modifier.weight(1f))


            IconButton(
                onClick = {
                    Basket.current(context).delete(item, context)
                    basketItems.clear()
                    basketItems.addAll(Basket.current(context).items)
                },
                modifier = Modifier.size(60.dp)
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Supprimer")
            }
        }
    }
}
