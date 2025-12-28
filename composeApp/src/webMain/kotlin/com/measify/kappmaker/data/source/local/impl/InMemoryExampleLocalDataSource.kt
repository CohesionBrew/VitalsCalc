package com.measify.kappmaker.data.source.local.impl

import com.measify.kappmaker.data.source.local.ExampleLocalDataSource
import com.measify.kappmaker.domain.model.ExampleModel

class InMemoryExampleLocalDataSource :
    BaseInMemoryLocalDataSource<String, ExampleModel>({ it.id }), ExampleLocalDataSource
