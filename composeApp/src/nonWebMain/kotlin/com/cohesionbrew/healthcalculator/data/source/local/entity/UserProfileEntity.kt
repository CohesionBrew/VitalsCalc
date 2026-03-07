@file:OptIn(ExperimentalUuidApi::class)

package com.cohesionbrew.healthcalculator.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cohesionbrew.healthcalculator.domain.model.UserProfile
import kotlin.uuid.ExperimentalUuidApi

@Entity(tableName = "user_profile")
data class UserProfileEntity(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = "default",
    @ColumnInfo(name = "name") val name: String? = null,
    @ColumnInfo(name = "dob") val dob: String? = null,
    @ColumnInfo(name = "gender") val gender: String = "male",
    @ColumnInfo(name = "height_cm") val heightCm: Double = 0.0,
    @ColumnInfo(name = "weight_kg") val weightKg: Double = 0.0,
    @ColumnInfo(name = "body_fat_pct") val bodyFatPct: Double? = null,
    @ColumnInfo(name = "waist_cm") val waistCm: Double = 0.0,
    @ColumnInfo(name = "neck_cm") val neckCm: Double = 0.0,
    @ColumnInfo(name = "hip_cm") val hipCm: Double = 0.0,
    @ColumnInfo(name = "resting_hr") val restingHr: Int = 0,
    @ColumnInfo(name = "use_metric") val useMetric: Boolean = true,
    @ColumnInfo(name = "activity_level") val activityLevel: String = "sedentary",
    @ColumnInfo(name = "calorie_goal") val calorieGoal: String = "maintenance",
    @ColumnInfo(name = "bmr_formula") val bmrFormula: String = "mifflin",
    @ColumnInfo(name = "advanced_mode") val advancedMode: Boolean = false,
    @ColumnInfo(name = "language") val language: String = "en"
)

class UserProfileEntityMapper : EntityMapper<UserProfileEntity, UserProfile> {
    override fun toEntity(model: UserProfile): UserProfileEntity {
        return UserProfileEntity(
            id = model.id,
            name = model.name,
            dob = model.dob,
            gender = model.gender,
            heightCm = model.heightCm,
            weightKg = model.weightKg,
            bodyFatPct = model.bodyFatPct,
            waistCm = model.waistCm,
            neckCm = model.neckCm,
            hipCm = model.hipCm,
            restingHr = model.restingHr,
            useMetric = model.useMetric,
            activityLevel = model.activityLevel,
            calorieGoal = model.calorieGoal,
            bmrFormula = model.bmrFormula,
            advancedMode = model.advancedMode,
            language = model.language
        )
    }

    override fun toModel(entity: UserProfileEntity): UserProfile {
        return UserProfile(
            id = entity.id,
            name = entity.name,
            dob = entity.dob,
            gender = entity.gender,
            heightCm = entity.heightCm,
            weightKg = entity.weightKg,
            bodyFatPct = entity.bodyFatPct,
            waistCm = entity.waistCm,
            neckCm = entity.neckCm,
            hipCm = entity.hipCm,
            restingHr = entity.restingHr,
            useMetric = entity.useMetric,
            activityLevel = entity.activityLevel,
            calorieGoal = entity.calorieGoal,
            bmrFormula = entity.bmrFormula,
            advancedMode = entity.advancedMode,
            language = entity.language
        )
    }
}
