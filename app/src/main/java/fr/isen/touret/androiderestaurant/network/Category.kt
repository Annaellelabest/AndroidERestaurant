package fr.isen.touret.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Category(
    @SerializedName("name_fr") val name: String,
    @SerializedName("name_en") val nameEn: String,
    @SerializedName("items") val items: List<Plat>)
: Serializable