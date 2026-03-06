package com.cohesionbrew.healthcalculator.data.source.local.impl

import com.cohesionbrew.healthcalculator.data.source.local.UserProfileLocalDataSource
import com.cohesionbrew.healthcalculator.data.source.local.dao.UserProfileDao
import com.cohesionbrew.healthcalculator.data.source.local.entity.UserProfileEntity
import com.cohesionbrew.healthcalculator.data.source.local.entity.UserProfileEntityMapper
import com.cohesionbrew.healthcalculator.domain.model.UserProfile

class UserProfileLocalDataSourceImpl(
    userProfileDao: UserProfileDao,
    userProfileEntityMapper: UserProfileEntityMapper = UserProfileEntityMapper()
) : BaseRoomLocalDataSource<String, UserProfileEntity, UserProfile>(
    dao = userProfileDao,
    mapper = userProfileEntityMapper
), UserProfileLocalDataSource
