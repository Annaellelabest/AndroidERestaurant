package fr.isen.touret.androiderestaurant.network

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Ingredient (
    @SerializedName("name_fr") val name: String,
    @SerializedName("name_en") val nameEn: String)
    : Serializable