package fr.isen.touret.androiderestaurant.basket

import android.content.Context
import com.google.gson.GsonBuilder
import fr.isen.touret.androiderestaurant.network.Plat


class Basket {
    var items: MutableList<BasketItem> = mutableListOf()

    fun add(plat: Plat, count: Int, context: Context) {
        val existingItem = items.firstOrNull { it.plat == plat }
        existingItem?.let {
            it.count = it.count + count
        } ?: run {
            items.add(BasketItem(count, plat))
        }
        save(context)
    }
    fun addOne(item: BasketItem, context: Context) {
        val existingItem = items.firstOrNull { it.plat == item.plat }
        existingItem?.let {
            it.count++
        } ?: run {
            items.add(BasketItem(1, item.plat))
        }
        save(context)
    }

    fun getTotalItemCount(): Int {
        var totalCount = 0
        for (item in items) {
            totalCount += item.count
        }
        return totalCount
    }




    fun delete(item: BasketItem, context: Context) {
        items.removeAll { item.plat.name == it.plat.name }
        save(context)
    }

    fun save(context: Context) {
        val json = GsonBuilder().create().toJson(this)

        val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(BASKET_PREFERENCES_KEY, json)
        editor.apply()
    }
    companion object {
        fun current(context: Context): Basket {
            val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(BASKET_PREFERENCES_KEY, null)
            if(json != null) {
                return GsonBuilder().create().fromJson(json, Basket::class.java)
            }
            return Basket()
        }

        val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
        val BASKET_PREFERENCES_KEY = "BASKET_PREFERENCES_KEY"
    }

    fun removeOne(item: BasketItem, context: Context) {
        val index = items.indexOfFirst { it.plat == item.plat }
        if (index != -1) {
            items[index].count--
            if (items[index].count == 0) {
                items.removeAt(index)
            }
            save(context)
        }
    }
}