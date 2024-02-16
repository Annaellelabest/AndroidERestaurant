package fr.isen.touret.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PriceInt(@SerializedName("price") val priceInt: Int): Serializable