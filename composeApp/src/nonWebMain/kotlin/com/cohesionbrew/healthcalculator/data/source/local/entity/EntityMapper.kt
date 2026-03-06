package com.cohesionbrew.healthcalculator.data.source.local.entity

interface EntityMapper<Entity, Model> {
    fun toEntity(model: Model): Entity
    fun toModel(entity: Entity): Model
}
