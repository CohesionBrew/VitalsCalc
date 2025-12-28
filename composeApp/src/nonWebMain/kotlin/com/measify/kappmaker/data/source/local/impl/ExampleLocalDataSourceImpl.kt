package com.measify.kappmaker.data.source.local.impl

import com.measify.kappmaker.data.source.local.ExampleLocalDataSource
import com.measify.kappmaker.data.source.local.dao.ExampleDao
import com.measify.kappmaker.data.source.local.entity.ExampleEntity
import com.measify.kappmaker.data.source.local.entity.ExampleEntityMapper
import com.measify.kappmaker.domain.model.ExampleModel

class ExampleLocalDataSourceImpl(
    exampleDao: ExampleDao,
    exampleEntityMapper: ExampleEntityMapper = ExampleEntityMapper()
) : BaseRoomLocalDataSource<String, ExampleEntity, ExampleModel>(
    dao = exampleDao,
    mapper = exampleEntityMapper
), ExampleLocalDataSource