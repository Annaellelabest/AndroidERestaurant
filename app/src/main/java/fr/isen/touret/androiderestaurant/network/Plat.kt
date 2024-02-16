package fr.isen.touret.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Plat (
    @SerializedName("name_fr") val name: String,
    @SerializedName("name_en") val nameEn: String,
    @SerializedName("ingredients") val ingredients: List<Ingredient>,
    @SerializedName("images") val images: List<String>,
    @SerializedName("prices") val prices: List<Price>,
  val pricesInt: Float,
   ):Serializable