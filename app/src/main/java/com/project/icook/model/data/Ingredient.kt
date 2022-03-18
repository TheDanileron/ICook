package com.project.icook.model.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


data class Ingredient(
    val id: Long,
    val name: String,
    val unit: String,
    val amount: Double,
) {
    override fun toString(): String {
        return " - $name - $amount $unit"
    }
}
