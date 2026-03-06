package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.UserProfileLocalDataSource
import com.cohesionbrew.healthcalculator.domain.model.UserProfile

class InMemoryUserProfileLocalDataSource :
    BaseInMemoryLocalDataSource<String, UserProfile>({ it.id }),
    UserProfileLocalDataSource
