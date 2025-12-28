package com.measify.kappmaker.data.source.local

import com.measify.kappmaker.domain.model.ExampleModel
import kotlinx.coroutines.flow.Flow

interface ExampleLocalDataSource: LocalDataSource<String, ExampleModel>